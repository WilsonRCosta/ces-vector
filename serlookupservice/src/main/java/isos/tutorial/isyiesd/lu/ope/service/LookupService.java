package isos.tutorial.isyiesd.lu.ope.service;

import io.smallrye.mutiny.Uni;
import isos.tutorial.isyiesd.lu.apim.enums.LookupType;
import isos.tutorial.isyiesd.lu.apim.service.ILookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class LookupService implements ILookupService {

    private static final Logger logger = LoggerFactory.getLogger(LookupService.class.getName());
    private final Map<String, List<String>> hostnames = new HashMap<>();
    private final Map<String, AtomicInteger> currTypeIndex = new HashMap<>();

    @Override
    public Uni<Void> register(String type, String hostname) {
        if (checkType(type)) {
            if (!hostnames.containsKey(type)) {
                List<String> hostnameList = new ArrayList<>();
                hostnameList.add(hostname);
                hostnames.put(type, hostnameList);
                logger.info("New type added: " + type + ". With hostname: " + hostname);
            } else {
                hostnames.get(type).add(hostname);
                logger.info("Added hostname " + hostname + " to " + type + " type");
            }
            return Uni.createFrom().voidItem();
        }
        throw new RuntimeException("Invalid type passed: " + type);
    }

    @Override
    public Uni<Void> unRegister(String type, String hostname) {
        if (checkType(type)) {
            if (hostnames.containsKey(type)) {
                if (hostnames.get(type).removeIf(h -> h.equals(hostname))) {
                    logger.info("Hostname " + hostname + " removed in type " + type);
                }
                return Uni.createFrom().voidItem();
            }
            throw new RuntimeException("Hostname " + hostname + " not found");
        }
        throw new RuntimeException("Invalid type passed: " + type);
    }

    @Override
    public Uni<String> lookup(String type) {

        if (!currTypeIndex.containsKey(type)) {
            currTypeIndex.put(type, new AtomicInteger(0));
        }

        final int currIdx = currTypeIndex.get(type).get();

        String hostname;
        if (hostnames.get(type).size() - 1 == currIdx) {
            currTypeIndex.get(type).set(0);
        } else {
            currTypeIndex.get(type).incrementAndGet();
        }
        hostname = hostnames.get(type).get(currIdx);

        return Uni.createFrom().item(hostname);
    }

    private boolean checkType(String type) {
        return Arrays
                .stream(LookupType.values())
                .map(LookupType::getType)
                .anyMatch(lookupType -> lookupType.equals(type));
    }
}
