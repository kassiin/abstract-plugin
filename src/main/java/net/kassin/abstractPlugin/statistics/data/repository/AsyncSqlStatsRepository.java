package net.kassin.abstractPlugin.statistics.data.repository;

import net.kassin.abstractPlugin.AbstractPlugin;
import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class AsyncSqlStatsRepository implements StatsRepository {

    private final StatsRepository delegate;
    private final AbstractPlugin plugin;

    public AsyncSqlStatsRepository(StatsRepository delegate) {
        this.delegate = delegate;
        this.plugin = AbstractPlugin.getInstance();
    }

    @Override
    public CompletableFuture<PlayerStats> getPlayerStats(Player player) {
        return delegate.getPlayerStats(player);
    }

    @Override
    public CompletableFuture<Void> incrementKills(Player player) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                delegate.incrementKills(player);
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> incrementDeaths(Player player) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                delegate.incrementDeaths(player);
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> saveStats(PlayerStats stats) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                delegate.saveStats(stats);
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> removePlayerStats(Player player) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                delegate.removePlayerStats(player);
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
