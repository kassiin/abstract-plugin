package net.kassin.abstractPlugin.blocks;

import net.kassin.abstractPlugin.DataBaseSource;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public interface BlockRepository {

    void save(BlockContainer container);

    void remove(BlockContainer container);

    List<Block> getContainer(Player player);

}
