package service.tplm.ope.service;

import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.tplm.apim.model.LockElement;
import service.tplm.apim.model.LockElementsDto;
import service.tplm.apim.service.ITplmService;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@ApplicationScoped
public class TplmService implements ITplmService {

    private static final Logger logger = LoggerFactory.getLogger(TplmService.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final ConcurrentLinkedQueue<LockElement> lockedRmElems = new ConcurrentLinkedQueue<>();

    @Override
    public Uni<Boolean> verifyLocks(final List<LockElement> lockElements) {
        lock.writeLock().lock();
        boolean canLock;
        try {
            canLock = lockElements.stream().noneMatch(this::isLocked);
            if (canLock) {
                lockedRmElems.addAll(lockElements);
                logger.info("Elements locked for " + lockElements.stream().map(elem -> elem.tid).distinct().collect(Collectors.toList()));
            } else {
                logger.error("Error. Elements could not be all locked.");
            }
        } finally {
            lock.writeLock().unlock();
        }
        return Uni.createFrom().item(canLock);
    }

    @Override
    public Uni<Boolean> releaseLocks(final List<LockElement> lockElements) {
        lock.writeLock().lock();
        boolean elemsRemoved;
        try {
            elemsRemoved = removeLockedElems(lockElements);
            if (elemsRemoved) {
                logger.info("Elements released from " + lockElements.stream().map(elem -> elem.tid).distinct().collect(Collectors.toList()));
            } else {
                logger.error("Error. Could not unlock any lock element. The elements were not acquired yet.");
            }
        } finally {
            lock.writeLock().unlock();
        }
        return Uni.createFrom().item(elemsRemoved);
    }

    @Override
    public Uni<LockElementsDto> listTransactionLocks(Long tid) {
        return Uni.createFrom().item(new LockElementsDto(
                lockedRmElems.stream().filter(elem -> elem.tid == tid).collect(Collectors.toList())
        ));
    }

    private boolean removeLockedElems(List<LockElement> elemsToLock) {
        int initialLockedElemsSize = lockedRmElems.size();

        elemsToLock.forEach(elemToLock -> lockedRmElems.removeIf(lockedElem -> lockComparator(lockedElem, elemToLock)));

        return lockedRmElems.size() < initialLockedElemsSize;
    }

    private boolean isLocked(LockElement lockElement) {
        return lockedRmElems.stream().anyMatch(currLockedElem -> lockComparator(currLockedElem, lockElement));
    }

    private boolean lockComparator(LockElement element1, LockElement element2) {
        return element1.resource.equals(element2.resource) && element1.index == element2.index && element1.tid == element2.tid;
    }

}
