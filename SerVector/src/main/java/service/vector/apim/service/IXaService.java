package service.vector.apim.service;

import io.smallrye.mutiny.Uni;

public interface IXaService {
    Uni<Integer> xaPrepare(final long tid);

    Uni<Void> xaCommit(final long tid);

    Uni<Void> xaRollback(final long tid);

    Uni<Void> xaClose(final long tid);
}

