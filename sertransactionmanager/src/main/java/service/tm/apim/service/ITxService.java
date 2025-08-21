package service.tm.apim.service;

import io.smallrye.mutiny.Uni;

public interface ITxService {

    Uni<Long> txBegin();

    Uni<Void> txClose(final Long xid);

    Uni<Void> txCommit(final Long xid);

    Uni<Void> txRollback(final Long xid);

}
