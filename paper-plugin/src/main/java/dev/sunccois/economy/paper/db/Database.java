package dev.sunccois.economy.paper.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.sunccois.economy.paper.SunccoisEconomyPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Owns the HikariCP pool and creates the schema on startup.
 * All SQL operations go through {@link #getConnection()}.
 */
public final class Database {

    private final SunccoisEconomyPlugin plugin;
    private HikariDataSource dataSource;

    public Database(SunccoisEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() throws SQLException {
        ConfigurationSection cfg = plugin.getConfig().getConfigurationSection("database");
        if (cfg == null) throw new IllegalStateException("Missing 'database' config section");

        String host = cfg.getString("host", "127.0.0.1");
        int port = cfg.getInt("port", 3306);
        String db = cfg.getString("database", "sunccois");
        String user = cfg.getString("username", "root");
        String pass = cfg.getString("password", "");
        String props = cfg.getString("properties", "useSSL=false&serverTimezone=UTC");

        String jdbc = "jdbc:mysql://" + host + ":" + port + "/" + db + "?" + props;

        HikariConfig hc = new HikariConfig();
        hc.setJdbcUrl(jdbc);
        hc.setUsername(user);
        hc.setPassword(pass);
        hc.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hc.setPoolName("SunccoisEconomy-Hikari");

        ConfigurationSection pool = cfg.getConfigurationSection("pool");
        if (pool != null) {
            hc.setMaximumPoolSize(pool.getInt("maximum-pool-size", 10));
            hc.setMinimumIdle(pool.getInt("minimum-idle", 2));
            hc.setConnectionTimeout(pool.getLong("connection-timeout-ms", 5000));
            hc.setIdleTimeout(pool.getLong("idle-timeout-ms", 600000));
            hc.setMaxLifetime(pool.getLong("max-lifetime-ms", 1800000));
        } else {
            hc.setMaximumPoolSize(10);
        }

        // Durability-friendly settings. InnoDB + autocommit off per-transaction.
        hc.addDataSourceProperty("cachePrepStmts", "true");
        hc.addDataSourceProperty("prepStmtCacheSize", "250");
        hc.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hc.addDataSourceProperty("useServerPrepStmts", "true");

        this.dataSource = new HikariDataSource(hc);

        createSchema();
    }

    private void createSchema() throws SQLException {
        final String accounts = """
                CREATE TABLE IF NOT EXISTS eco_accounts (
                    uuid       CHAR(36)    NOT NULL PRIMARY KEY,
                    username   VARCHAR(32) NOT NULL,
                    balance    BIGINT      NOT NULL DEFAULT 0,
                    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
                                   ON UPDATE CURRENT_TIMESTAMP,
                    KEY idx_username (username),
                    CONSTRAINT chk_balance_nonneg CHECK (balance >= 0)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;

        final String transactions = """
                CREATE TABLE IF NOT EXISTS eco_transactions (
                    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                    account_uuid  CHAR(36)       NOT NULL,
                    type          VARCHAR(16)    NOT NULL,
                    delta         BIGINT         NOT NULL,
                    balance_after BIGINT         NOT NULL,
                    source_server VARCHAR(64)    NOT NULL,
                    actor         VARCHAR(64)    NULL,
                    created_at    TIMESTAMP(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                    KEY idx_account_time (account_uuid, created_at)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;

        try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
            s.execute(accounts);
            s.execute(transactions);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
