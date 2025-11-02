package net.kassin.abstractPlugin.statistics.listeners;

import net.kassin.abstractPlugin.AbstractPlugin;
import net.kassin.abstractPlugin.statistics.StatsService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.concurrent.CompletableFuture;

public record StatsListener(StatsService service) implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        CompletableFuture<?>[] futures = new CompletableFuture[1000];
        for (int i = 0; i < 1000; i++) {
            futures[i] = service.saveKill(killer);
        }

        CompletableFuture.allOf(futures)
                .thenCompose(v -> service.getPlayerStats(killer))
                .thenAccept(stats -> {
                    Bukkit.getScheduler().runTask(AbstractPlugin.getInstance(), () ->
                        killer.sendMessage("§aSeu total de abates (1000 atualizações): " + stats.getKills())
                    );
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    Bukkit.getScheduler().runTask(AbstractPlugin.getInstance(), () ->
                        killer.sendMessage("§cErro fatal ao salvar ou carregar estatísticas.")
                    );
                    return null;
                });
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        service.saveDeath(player);
    }

}

