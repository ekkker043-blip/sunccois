package ru.craftorium.craftoriumcustom.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.manager.ConfigManager;

public class DatabaseManager {
    private final CraftoriumCustom plugin;
    private final ConfigManager configManager;
    private static Connection connection;
    private String host;
    private String database;
    private static String username;
    private static String password;
    private static String url;
    private static final Executor databaseExecutor;

    public DatabaseManager(CraftoriumCustom plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.loadDatabase();
        this.initializeDatabase();
    }

    private void initializeDatabase() {
        DatabaseManager.connectToDatabase();
        this.createTableIfNotExists();
    }

    private void loadDatabase() {
        String dbType = this.configManager.getString("database.type", "sqlite");
        if ("sqlite".equalsIgnoreCase(dbType)) {
            String dbPath = this.plugin.getDataFolder().getAbsolutePath() + "/database.db";
            url = "jdbc:sqlite:" + dbPath;
            username = null;
            password = null;
        } else {
            this.host = this.configManager.getString("database.host", " ");
            this.database = this.configManager.getString("database.name", "custom");
            username = this.configManager.getString("database.username", "root");
            password = this.configManager.getString("database.password", " ");
            url = "jdbc:mysql://" + this.host + "/" + this.database + "?useSSL=false&autoReconnect=true";
        }
    }

    private static synchronized void connectToDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            if (url.startsWith("jdbc:sqlite:")) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);
            } else {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS players (name VARCHAR(16) PRIMARY KEY, data TEXT);";
        try (PreparedStatement statement = connection.prepareStatement(sql);){
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                DatabaseManager.connectToDatabase();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CompletableFuture<Void> setPlayerAsync(String name, String base64) {
        return CompletableFuture.runAsync(() -> {
            DatabaseManager.checkConnection();
            String sql = url.startsWith("jdbc:sqlite:") ? "INSERT INTO players (name, data) VALUES (?, ?) ON CONFLICT(name) DO UPDATE SET data = excluded.data" : "REPLACE INTO players (name, data) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql);){
                statement.setString(1, name);
                statement.setString(2, base64);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }, databaseExecutor);
    }

    public static CompletableFuture<String> getPlayerAsync(String name) {
        return CompletableFuture.supplyAsync(() -> {
            DatabaseManager.checkConnection();
            String sql = "SELECT data FROM players WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql);){
                statement.setString(1, name);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String string2 = resultSet.getString("data");
                    return string2;
                }
                String string = null;
                return string;
            }
            catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }, databaseExecutor);
    }

    public static String toBase64(HashMap<String, String> map) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(map);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, String> fromBase64(String base64) {
        if (base64 != null && !base64.isEmpty()) {
            try {
                byte[] data = Base64.getDecoder().decode(base64);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                HashMap map = (HashMap)ois.readObject();
                ois.close();
                return map;
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return new HashMap<String, String>();
            }
        }
        return new HashMap<String, String>();
    }

    static {
        databaseExecutor = Executors.newFixedThreadPool(4);
    }
}

