package service.tm.apim.service;

import io.smallrye.mutiny.Uni;

public interface IAxService {

    Uni<Void> axRegister(final Long tid, final String endpoint);

    Uni<Void> axUnregister(final Long tid, final String endpoint);
}
