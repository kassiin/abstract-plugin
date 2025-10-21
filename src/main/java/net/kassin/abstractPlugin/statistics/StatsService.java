package net.kassin.abstractPlugin.statistics;

import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import net.kassin.abstractPlugin.statistics.data.repository.StatsRepository;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class StatsService {

    private final StatsRepository repository;

    public StatsService(StatsRepository repository) {
        this.repository = repository;
    }

    public void saveKill(Player player) {
        repository.getPlayerStats(player)
                .thenCompose(stats -> {
                    stats.setKills(stats.getKills() + 1);
                    return repository.saveStats(stats);
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    public void saveDeath(Player player) {
        repository.getPlayerStats(player)
                .thenCompose(stats -> {
                    stats.setDeaths(stats.getDeaths() + 1);
                    return repository.saveStats(stats);
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    public void removePlayerStats(Player player) {
        repository.removePlayerStats(player)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    public CompletableFuture<PlayerStats> getPlayerStats(Player player) {
        return repository.getPlayerStats(player);
    }
}

