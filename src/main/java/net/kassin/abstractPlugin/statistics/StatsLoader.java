package net.kassin.abstractPlugin.statistics;

import net.kassin.abstractPlugin.repo.AsyncRepository;
import net.kassin.abstractPlugin.repo.Repository;
import net.kassin.abstractPlugin.statistics.commands.StatsCommand;
import net.kassin.abstractPlugin.utils.DataBaseSource;
import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import net.kassin.abstractPlugin.statistics.data.repo.*;
import net.kassin.abstractPlugin.statistics.listeners.StatsListener;
import net.kassin.abstractPlugin.utils.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsLoader {

    private final JavaPlugin plugin;
    private final StatsService service;
    private final DataBaseSource statsSource;

    public StatsLoader(JavaPlugin plugin) {
        this.plugin = plugin;

        Config config = new Config(plugin, "stats_db.yml");

        statsSource = DataBaseSource.create(config.getConfigurationSection("database"));
        Repository<PlayerStats> dbRepository = new SqlRepository(statsSource);
        Repository<PlayerStats> cacheRepository = new CachedRepository(dbRepository);
        AsyncRepository<PlayerStats> asyncStatsRepository = new AsyncStatsRepository<>(cacheRepository);

        service = new StatsService(asyncStatsRepository);
    }

    public void enable() {
        plugin.getServer().getPluginManager().registerEvents(new StatsListener(service), plugin);
        plugin.getCommand("stats").setExecutor(new StatsCommand(service));
    }

    public void disable() {
        statsSource.close();
    }

}
