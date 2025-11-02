package net.kassin.abstractPlugin.statistics;

import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import net.kassin.abstractPlugin.repo.AsyncRepository;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StatsService {

    private final AsyncRepository<PlayerStats> repository;
    private final ConcurrentHashMap<UUID, ReentrantReadWriteLock> locks = new ConcurrentHashMap<>();

    public StatsService(AsyncRepository<PlayerStats> repository) {
        this.repository = repository;
    }

    private ReentrantReadWriteLock getLock(UUID uuid) {
        return locks.computeIfAbsent(uuid, k -> new ReentrantReadWriteLock());
    }

    public CompletableFuture<Void> saveKill(Player player) {
        UUID uuid = player.getUniqueId();
        return CompletableFuture.runAsync(() -> {
            ReentrantReadWriteLock lock = getLock(uuid);
            lock.writeLock().lock();
            try {
                PlayerStats stats = repository.get(uuid);
                if (stats == null)
                    stats = new PlayerStats(uuid, 0, 0);

                PlayerStats updated = new PlayerStats(uuid, stats.getKills() + 1, stats.getDeaths());
                repository.save(updated);
            } finally {
                lock.writeLock().unlock();
            }
        });
    }

    public CompletableFuture<Void> saveDeath(Player player) {
        UUID uuid = player.getUniqueId();
        return CompletableFuture.runAsync(() -> {
            ReentrantReadWriteLock lock = getLock(uuid);
            lock.writeLock().lock();
            try {
                PlayerStats stats = repository.get(uuid);
                if (stats == null)
                    stats = new PlayerStats(uuid, 0, 0);

                PlayerStats updated = new PlayerStats(uuid, stats.getKills(), stats.getDeaths() + 1);
                repository.save(updated);
            } finally {
                lock.writeLock().unlock();
            }
        });
    }

    public void removePlayerStats(Player player) {
        repository.removeAsync(player.getUniqueId());
    }

    public CompletableFuture<PlayerStats> getPlayerStats(Player player) {
        return repository.getAsync(player.getUniqueId());
    }

}

