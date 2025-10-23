package net.kassin.abstractPlugin.statistics.data.repo;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AsyncRepository<T> extends Repository<T> {

    default CompletableFuture<Void> saveAsync(T data) {
        return CompletableFuture.runAsync(() -> save(data));
    }

    default void removeAsync(UUID uuid) {
        CompletableFuture.runAsync(() -> remove(uuid));
    }

    default CompletableFuture<T> getAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> get(uuid));
    }

}
