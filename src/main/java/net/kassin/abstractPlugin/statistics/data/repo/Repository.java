package net.kassin.abstractPlugin.statistics.data.repo;

import net.kassin.abstractPlugin.utils.DataBaseSource;

import java.util.UUID;

public interface Repository<T> {
    void save(T data);
    T get(UUID id);
    void remove(UUID id);

    default void initTable(DataBaseSource source) {}

}
