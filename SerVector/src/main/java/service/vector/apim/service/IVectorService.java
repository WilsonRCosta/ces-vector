package service.vector.apim.service;

import io.smallrye.mutiny.Uni;

public interface IVectorService {

    Uni<Void> write(long tid, int index, int value);

    Uni<Integer> read(long tid, int index);
}
