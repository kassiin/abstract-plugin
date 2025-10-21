package net.kassin.abstractPlugin.blocks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import net.kassin.abstractPlugin.DataBaseSource;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SQLBlockContainerRepository implements BlockRepository {

    private static final Gson GSON = new Gson();
    private final DataBaseSource dataSource;

    public SQLBlockContainerRepository(DataBaseSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(BlockContainer container) {
        String blocksJson = GSON.toJson(container.getBlocks());
        final String SQL =
                "INSERT INTO block_container (player, blocks) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE blocks = VALUES(blocks)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, container.getOwner().getUniqueId().toString());
            ps.setString(2, blocksJson);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar BlockContainer para " + container.getOwner().getName());
            e.printStackTrace();
        }
    }

    @Override
    public void remove(BlockContainer container) {
        final String SQL = "DELETE FROM block_container WHERE player = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)) {

            ps.setString(1, container.getOwner().getUniqueId().toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao remover BlockContainer para " + container.getOwner().getName());
            e.printStackTrace();
        }
    }

    @Override
    public List<Block> getContainer(Player player) {
        final String SQL = "SELECT blocks FROM block_container WHERE player = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)) {

            ps.setString(1, player.getUniqueId().toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String blocksJson = rs.getString("blocks");

                    Type type = new TypeToken<Map<String, Integer>>() {
                    }.getType();

                    Map<String, Integer> blocksMap = GSON.fromJson(blocksJson, type);
                    BlockContainer container = new BlockContainer(player);
                    container.getBlocks().putAll(blocksMap);

                    return container.getBlocks().keySet();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar BlockContainer para " + player.getName());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro de parsing JSON ao carregar container.");
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}