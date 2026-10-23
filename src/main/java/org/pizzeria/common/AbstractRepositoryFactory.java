package org.pizzeria.common;

public abstract class AbstractRepositoryFactory {

    public abstract BaseRepository getRepositoryByType(RepositoryTypes type);
}
