package net.kassin.abstractPlugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseSource {

    private String url;
    private String user;
    private String password;

    private HikariDataSource dataSource;

    private DataBaseSource() {
    }

    public static DataBaseSource create(String url, String user, String password) {
        DataBaseSource dataBaseSource = new DataBaseSource();
        dataBaseSource.url = url;
        dataBaseSource.user = user;
        dataBaseSource.password = password;
        dataBaseSource.dataSource = new HikariDataSource(dataBaseSource.getConfig());
        return dataBaseSource;
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    private HikariConfig getConfig(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setConnectionTimeout(3000);
        return hikariConfig;
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("HikariCP: Fonte de dados fechada com sucesso.");
        }
    }

}