package net.kassin.abstractPlugin.statistics;

import net.kassin.abstractPlugin.DataBaseSource;
import net.kassin.abstractPlugin.statistics.data.repository.AsyncSqlStatsRepository;
import net.kassin.abstractPlugin.statistics.data.repository.CachedStatsRepository;
import net.kassin.abstractPlugin.statistics.data.repository.SqlStatsRepository;
import net.kassin.abstractPlugin.statistics.data.repository.StatsRepository;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsLoader {

    private final JavaPlugin plugin;
    private final StatsService service;

    public StatsLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        DataBaseSource source = DataBaseSource.create("", "", "");
        StatsRepository dbStatsRepository = new SqlStatsRepository(source);
        StatsRepository asyncSqlStatsRepository = new AsyncSqlStatsRepository(dbStatsRepository);
        StatsRepository cacheSqlStatsRepository = new CachedStatsRepository(asyncSqlStatsRepository);
        service = new StatsService(cacheSqlStatsRepository);
    }

    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new StatsListener(service), plugin);
    }

}
