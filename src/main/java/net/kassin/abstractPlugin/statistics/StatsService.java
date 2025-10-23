package net.kassin.abstractPlugin.statistics;

import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import net.kassin.abstractPlugin.statistics.data.repo.AsyncRepository;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public record StatsService(AsyncRepository<PlayerStats> repository) {

    public void saveKill(Player player) {
        repository.getAsync(player.getUniqueId())
                .thenApply(stats -> {
                    if (stats == null)
                        stats = new PlayerStats(player.getUniqueId(), 0, 0);
                    return new PlayerStats(player.getUniqueId(), stats.getKills() + 1, stats.getDeaths());
                }).thenCompose(repository::saveAsync);
    }

    public void saveDeath(Player player) {
        repository.getAsync(player.getUniqueId())
                .thenApply(stats -> {
                    if (stats == null)
                        stats = new PlayerStats(player.getUniqueId(), 0, 0);
                    return new PlayerStats(player.getUniqueId(), stats.getKills(), stats.getKills() + 1);
                });
    }

    public void removePlayerStats(Player player) {
        repository.removeAsync(player.getUniqueId());
    }

    public CompletableFuture<PlayerStats> getPlayerStats(Player player) {
        return repository.getAsync(player.getUniqueId());
    }

}

