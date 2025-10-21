package net.kassin.abstractPlugin.blocks;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BlockContainer {

    private final Player owner;
    private final Map<String, Integer> blocks;

    public BlockContainer(Player owner) {
        this.owner = owner;
        this.blocks = new HashMap<>();
    }

    public void addBlock(Block block) {
        blocks.put(block.getType().name(), getBlockAmount(block.getType().name()) + 1);
    }

    public void removeBlock(Block block, int amount) {
        blocks.put(block.getType().name(), blocks.get(block.getType().name()) - amount);
        if (blocks.get(block.getType().name()) <= 0) {
            blocks.remove(block.getType().name());
        }
    }

    public int getBlockAmount(String block) {
        return blocks.get(block);
    }

}
