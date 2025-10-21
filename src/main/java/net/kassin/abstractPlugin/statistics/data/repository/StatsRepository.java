package net.kassin.abstractPlugin.statistics.data.repository;

import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface StatsRepository {
    CompletableFuture<PlayerStats> getPlayerStats(Player player);

    CompletableFuture<Void> incrementKills(Player player);
    CompletableFuture<Void> incrementDeaths(Player player);
    CompletableFuture<Void>  saveStats(PlayerStats stats);
    CompletableFuture<Void>  removePlayerStats(Player player);
}


