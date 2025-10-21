package net.kassin.abstractPlugin;

import lombok.Getter;
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

    }

    @Override
    public void onDisable() {
    }

}
