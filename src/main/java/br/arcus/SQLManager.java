package br.arcus;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLManager {
    private static Connection connection;

    private static PreparedStatement auto, selectData, insert, update;

    private static boolean isTransform() {
        File f = new File(Main.Plugin.getDataFolder(), ".transform");
        return f.exists();
    }

    private static void transform(){
        try {
            Statement sta = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ArcusPlayerDataFix (Name,EquipAbility,EquipPotential,UnlockPotential,UnlockAbility) VALUES (?, ?, ?, ?, ?)");
            ResultSet rs = sta.executeQuery("SELECT * FROM ArcusPlayerData");
            while(rs.next()){
                String name = rs.getString("Name");
                int ea = rs.getInt("EquipAbility");
                long ep = 0;
                long up = 0;
                int ua = rs.getInt("UnlockAbility");
                ps.setString(1, name);
                ps.setInt(2, ea);
                ps.setLong(3, ep);
                ps.setLong(4, up);
                ps.setInt(5, ua);
                ps.execute();
            }
            sta.execute("DROP TABLE ArcusPlayerData");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setTransform(){
        File f = new File(Main.Plugin.getDataFolder(), ".transform");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        YamlConfiguration config = Data.checkConfigFile();
        ConfigurationSection db = config.getConfigurationSection("Mysql");
        StringBuilder sb = new StringBuilder(String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s",
                db.getString("host"), db.getInt("port"), db.getString("database"), db.getString("user"), db.getString("password")));
        for (String s : db.getStringList("options")) {
            sb.append('&');
            sb.append(s);
        }
        connection = DriverManager.getConnection(sb.toString());
        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS ArcusPlayerDataFix(" +
                        "Name VARCHAR(80) NOT NULL PRIMARY KEY," +
                        "EquipAbility INT NOT NULL," +
                        "EquipPotential BIGINT NOT NULL," +
                        "UnlockPotential BIGINT NOT NULL," +
                        "UnlockAbility INT NOT NULL" +
                        ");");
        auto = connection.prepareStatement("SELECT * FROM ArcusPlayerDataFix WHERE Name = ? LIMIT 1");
        insert = connection.prepareStatement("INSERT INTO ArcusPlayerDataFix (Name,EquipAbility,EquipPotential,UnlockPotential,UnlockAbility) VALUES (?, ?, ?, ?, ?)");
        selectData = connection.prepareStatement("SELECT * FROM ArcusPlayerDataFix WHERE Name = ? LIMIT 1");
        update = connection.prepareStatement("UPDATE ArcusPlayerDataFix SET EquipAbility = ?, EquipPotential = ?, UnlockPotential = ?, UnlockAbility = ? WHERE Name = ? LIMIT 1");
        Bukkit.getScheduler().runTaskTimer(Main.Plugin, () -> {
            try {
                auto.setString(1, "Bryan_lzh");
                auto.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 3L * 60 * 60 * 20, 3L * 60 * 60 * 20);
        if(!isTransform()){
            transform();
            setTransform();
        }
    }

    public static synchronized PlayerData loadData(String name) {
        try {
            selectData.setString(1, name);
            ResultSet rs = selectData.executeQuery();
            if (rs.next()) {
                int equipAbility = rs.getInt("EquipAbility");
                long equipPotential = rs.getLong("EquipPotential");
                long unlockPotential = rs.getLong("UnlockPotential");
                int unlockAbility = rs.getInt("UnlockAbility");
                PlayerData pd = new PlayerData(name);
                pd.loadEquipAbility(equipAbility);
                pd.loadEquipPotential(equipPotential);
                pd.loadUnlockAbility(unlockAbility);
                pd.loadUnlockPotential(unlockPotential);
                return pd;
            } else {
                return new PlayerData(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized void sync(PlayerData data) {
        if (data == null) {
            return;
        }
        String name = data.getName();
        int equipAbility = data.bitEquipAbility();
        long equipPotential = data.bitEquipPotential();
        long unlockPotential = data.bitUnlockPotential();
        int unlockAbility = data.bitUnlockAbility();
        try {
            selectData.setString(1, name);
            ResultSet rs = selectData.executeQuery();
            if (rs.next()) {
                updateData(name, equipAbility, equipPotential, unlockPotential, unlockAbility);
            } else {
                insertData(name, equipAbility, equipPotential, unlockPotential, unlockAbility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void insertData(String name, int ea, long ep, long up, int ua) {
        try {
            insert.setString(1, name);
            insert.setInt(2, ea);
            insert.setLong(3, ep);
            insert.setLong(4, up);
            insert.setInt(5, ua);
            insert.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void updateData(String name, int ea, long ep, long up, int ua) {
        try {
            update.setInt(1, ea);
            update.setLong(2, ep);
            update.setLong(3, up);
            update.setInt(4, ua);
            update.setString(5, name);
            update.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
