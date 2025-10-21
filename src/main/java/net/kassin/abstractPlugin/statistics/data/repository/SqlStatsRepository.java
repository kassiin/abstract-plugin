package net.kassin.abstractPlugin.statistics.data.repository;

import net.kassin.abstractPlugin.DataBaseSource;
import net.kassin.abstractPlugin.statistics.data.PlayerStats;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SqlStatsRepository implements StatsRepository {

    private final DataBaseSource dataSource;
    private static final ExecutorService DB_EXECUTOR = Executors.newCachedThreadPool();

    public SqlStatsRepository(DataBaseSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CompletableFuture<PlayerStats> getPlayerStats(Player player) {
        UUID playerUUID = player.getUniqueId();

        return CompletableFuture.supplyAsync(() -> {
            String SQL = "SELECT kills, deaths FROM player_stats WHERE player_uuid = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL)) {

                ps.setString(1, playerUUID.toString());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int kills = rs.getInt("kills");
                        int deaths = rs.getInt("deaths");
                        return new PlayerStats(player, kills, deaths);
                    }
                }
                return new PlayerStats(player, 0, 0);

            } catch (SQLException e) {
                return null;
            }
        }, DB_EXECUTOR);
    }

    @Override
    public CompletableFuture<Void> incrementKills(Player player) {
        return CompletableFuture.runAsync(() -> {
            String SQL = "UPDATE player_stats SET kills = kills + 1 WHERE player_uuid = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL)) {

                ps.setString(1, player.getUniqueId().toString());
                ps.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Erro ao incrementar Kills para: " + player.getName());
                e.printStackTrace();
            }
        }, DB_EXECUTOR);
    }

    @Override
    public CompletableFuture<Void> incrementDeaths(Player player) {
        return CompletableFuture.runAsync(() -> {
            String SQL = "UPDATE player_stats SET deaths = deaths + 1 WHERE player_uuid = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL)) {

                ps.setString(1, player.getUniqueId().toString());
                ps.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Erro ao incrementar Deaths para: " + player.getName());
                e.printStackTrace();
            }
        }, DB_EXECUTOR);
    }

    @Override
    public CompletableFuture<Void> saveStats(PlayerStats stats) {
        return CompletableFuture.runAsync(() -> {
            String SQL = "INSERT INTO player_stats (player_uuid , kills, deaths) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE kills = VALUES(kills), deaths = VALUES(deaths)";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL)) {

                ps.setString(1, stats.getPlayer().getUniqueId().toString());
                ps.setInt(2, stats.getKills());
                ps.setInt(3, stats.getDeaths());

                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Erro ao salvar estatísticas para: " + stats.getPlayer().getUniqueId());
                e.printStackTrace();
            }
        }, DB_EXECUTOR);
    }

    @Override
    public CompletableFuture<Void> removePlayerStats(Player player) {
        return CompletableFuture.runAsync(() -> {
            String SQL = "DELETE FROM player_stats WHERE player_uuid = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL)) {

                ps.setString(1, player.getUniqueId().toString());
                ps.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Erro ao remover estatísticas para: " + player.getName());
                e.printStackTrace();
            }
        }, DB_EXECUTOR);
    }

}
