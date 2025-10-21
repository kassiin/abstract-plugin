package net.kassin.abstractPlugin.statistics.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class PlayerStats {

    private final Player player;
    private int kills;
    private int deaths;

}
