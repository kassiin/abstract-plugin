package net.kassin.abstractPlugin.statistics.data;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
public class PlayerStats {

    private final UUID player;
    private int kills;
    private int deaths;

    public PlayerStats(UUID player, int kills, int deaths) {
        this.player = player;
        this.kills = kills;
        this.deaths = deaths;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

}
