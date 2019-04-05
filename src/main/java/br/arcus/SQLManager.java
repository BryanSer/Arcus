package br.arcus;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.*;

public class SQLManager {
    private static Connection connection;

    private static PreparedStatement select,selectData,insert,update;

    public static void loadConfig() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        File f = new File(Main.Plugin.getDataFolder(), "config.yml");
        if (!f.exists()) {
            Main.Plugin.saveDefaultConfig();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
        ConfigurationSection db = config.getConfigurationSection("Mysql");
        StringBuilder sb = new StringBuilder(String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s",
                db.getString("host"), db.getInt("port"), db.getString("database"), db.getString("user"), db.getString("password")));
        connection = DriverManager.getConnection(sb.toString());
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS ArcusPlayerData(" +
                "Name VARCHAR(80) NOT NULL PRIMARY KEY," +
                "EquipAbility INT NOT NULL," +
                "EquipPotential LONG NOT NULL," +
                "UnlockPotential LONG NOT NULL," +
                "UnlockAbility INT NOT NULL" +
                ");");
        insert = connection.prepareStatement("INSERT INTO MinecraftInfo (Name,EquipAbility,EquipPotential,UnlockPotential,UnlockAbility) VALUES (?, ?, ?, ?, ?)");
        selectData = connection.prepareStatement("SELECT * FROM ArcusPlayerData WHERE Name = ? LIMIT 1");
        update = connection.prepareStatement("UPDATE ArcusPlayerData SET EquipAbility = ?, EquipPotential = ?, UnlockPotential = ?, UnlockAbility = ? WHERE PlayerName = ? LIMIT 1");
    }

    public static PlayerData loadData(String name){
        try {
            selectData.setString(1,name);
            ResultSet rs = selectData.executeQuery();
            if(rs.next()){
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
            }else {
                return new PlayerData(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sync(PlayerData data){
        String name = data.getName();
        int equipAbility = data.bitEquipAbility();
        long equipPotential = data.bitEquipPotential();
        long unlockPotential = data.bitUnlockPotential();
        int unlockAbility = data.bitUnlockAbility();
        try {
            selectData.setString(1,name);
            ResultSet rs = selectData.executeQuery();
            if(rs.next()){
                updateData(name,equipAbility,equipPotential,unlockPotential,unlockAbility);
            }else {
                insertData(name,equipAbility,equipPotential,unlockPotential,unlockAbility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertData(String name, int ea, long ep, long up, int ua){
        try {
            insert.setString(1,name);
            insert.setInt(2,ea);
            insert.setLong(3,ep);
            insert.setLong(4,up);
            insert.setInt(5,ua);
            insert.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateData(String name, int ea, long ep, long up, int ua){
        try {
            update.setInt(1,ea);
            update.setLong(2,ep);
            update.setLong(3,up);
            update.setInt(4,ua);
            update.setString(5,name);
            update.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
