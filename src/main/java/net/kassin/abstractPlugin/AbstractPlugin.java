package net.kassin.abstractPlugin;

import lombok.Getter;
import net.kassin.abstractPlugin.statistics.StatsLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class AbstractPlugin extends JavaPlugin {

    @Getter
    private static AbstractPlugin instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        StatsLoader statsLoader = new StatsLoader(this);
        statsLoader.registerEvents();
    }

    @Override
    public void onDisable() {
    }

}
