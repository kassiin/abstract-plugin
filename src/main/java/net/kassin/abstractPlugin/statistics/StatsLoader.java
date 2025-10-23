package net.kassin.abstractPlugin.statistics;

import net.kassin.abstractPlugin.utils.DataBaseSource;
import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import net.kassin.abstractPlugin.statistics.data.repo.*;
import net.kassin.abstractPlugin.statistics.listeners.StatsListener;
import net.kassin.abstractPlugin.utils.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsLoader {

    private final JavaPlugin plugin;
    private final StatsService service;

    public StatsLoader(JavaPlugin plugin) {
        this.plugin = plugin;

        Config config = new Config(plugin, "stats_db.yml");

        DataBaseSource statsSource = DataBaseSource.create(config.getConfigurationSection("database"));

        Repository<PlayerStats> dbRepository = new SqlRepository(statsSource);
        Repository<PlayerStats> cacheRepository = new CachedRepository(dbRepository);
        AsyncRepository<PlayerStats> asyncStatsRepository = new AsyncStatsRepository<>(cacheRepository);

        service = new StatsService(asyncStatsRepository);
    }

    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new StatsListener(service), plugin);
    }

}
