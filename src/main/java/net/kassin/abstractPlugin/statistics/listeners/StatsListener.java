package net.kassin.abstractPlugin.statistics.listeners;

import net.kassin.abstractPlugin.statistics.StatsService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class StatsListener implements Listener {

    private final StatsService service;

    public StatsListener(StatsService service) {
        this.service = service;
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        service.saveKill(killer);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        service.saveDeath(player);
    }

}

