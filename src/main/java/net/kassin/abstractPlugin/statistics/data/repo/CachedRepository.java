package net.kassin.abstractPlugin.statistics.data.repo;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;
import java.util.UUID;

public class CachedRepository implements Repository<PlayerStats> {

    private final Repository<PlayerStats> delegate;
    private final Cache<@NotNull UUID, PlayerStats> cache;

    public CachedRepository(Repository<PlayerStats> delegate) {
        this.delegate = delegate;
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(10))
                .maximumSize(10_000)
                .build();
    }

    @Override
    public void save(PlayerStats data) {
        delegate.save(data);
        cache.put(data.getPlayer().getUniqueId(), data);
    }

    @Override
    public PlayerStats get(UUID id) {
        return cache.get(id, delegate::get);
    }

    @Override
    public void remove(UUID id) {
        delegate.remove(id);
        cache.invalidate(id);
    }

}

