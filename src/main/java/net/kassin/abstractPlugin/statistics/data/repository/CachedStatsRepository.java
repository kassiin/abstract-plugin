package net.kassin.abstractPlugin.statistics.data.repository;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CachedStatsRepository implements StatsRepository {

    private final StatsRepository delegate;
    private final AsyncCache<UUID, PlayerStats> cache;

    public CachedStatsRepository(StatsRepository delegate) {
        this.delegate = delegate;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .buildAsync();
    }

    @Override
    public CompletableFuture<PlayerStats> getPlayerStats(Player player) {
        UUID id = player.getUniqueId();
        return cache.get(id, (uuid, executor) -> delegate.getPlayerStats(player));
    }

    @Override
    public CompletableFuture<Void> incrementKills(Player player) {
        cache.synchronous().invalidate(player.getUniqueId());
        return delegate.incrementKills(player);
    }

    @Override
    public CompletableFuture<Void> incrementDeaths(Player player) {
        cache.synchronous().invalidate(player.getUniqueId());
        return delegate.incrementDeaths(player);
    }

    @Override
    public CompletableFuture<Void> saveStats(PlayerStats stats) {
        cache.put(stats.getPlayer().getUniqueId(), CompletableFuture.completedFuture(stats));
        return delegate.saveStats(stats);
    }

    @Override
    public CompletableFuture<Void> removePlayerStats(Player player) {
        cache.synchronous().invalidate(player.getUniqueId());
        return delegate.removePlayerStats(player);
    }
}
