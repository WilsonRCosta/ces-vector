package isos.tutorial.isyiesd.lu.apim.service;

import io.smallrye.mutiny.Uni;

public interface ILookupService {

    Uni<Void> register(final String type, final String hostname);

    Uni<Void> unRegister(final String type, final String hostname);

    Uni<String> lookup(final String type);
}
