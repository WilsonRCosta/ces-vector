package service.tplm.apim.service;

import io.smallrye.mutiny.Uni;
import service.tplm.apim.model.LockElement;
import service.tplm.apim.model.LockElementsDto;

import java.util.List;

public interface ITplmService {

    Uni<Boolean> verifyLocks(final List<LockElement> lockElements);

    Uni<Boolean> releaseLocks(final List<LockElement> lockElements);

    Uni<LockElementsDto> listTransactionLocks(final Long tid);
}
