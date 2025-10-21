package net.kassin.abstractPlugin.blocks;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.kassin.abstractPlugin.DataBaseSource;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CachedBlockContainerRepository implements BlockRepository {

    private final BlockRepository delegate;
    private final Cache<@NotNull UUID, BlockContainer> cache;

    public CachedBlockContainerRepository(BlockRepository delegate) {
        this.delegate = delegate;

        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(5000)
                .build();
    }

    @Override
    public void save(BlockContainer container) {
        delegate.save(container);
        cache.put(container.getOwner().getUniqueId(), container);
    }

    @Override
    public void remove(BlockContainer container) {
        delegate.remove(container);
        cache.invalidate(container.getOwner().getUniqueId());
    }

    @Override
    public List<Block> getContainer(Player player) {
        return List.of();
    }

}
