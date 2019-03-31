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
        connection.createStatement().execute("CREATE TABLE IF NOT EXISYS ArcusPlayerData(" +
                "Name VARCHAR(80) NOT NULL PRIMARY KEY," +
                "EquipAbility INT NOT NULL," +
                "EquipPotential LONG NOT NULL," +
                "UnlockPotential LONG NOT NULL" +
                ")");
        insert = connection.prepareStatement("INSERT INTO MinecraftInfo (Name,EquipAbility,EquipPotential,UnlockPotential) VALUES (?, ?, ?, ?)");
        selectData = connection.prepareStatement("SELECT * FROM ArcusPlayerData WHERE Name = ? LIMIT 1");
        update = connection.prepareStatement("UPDATE ArcusPlayerData SET EquipAbility = ?, EquipPotential = ?, UnlockPotential = ? WHERE PlayerName = ? LIMIT 1");
    }

    public static PlayerData loadData(String name){
        //TODO 读取 int long long 剩下我写
        try {
            selectData.setString(1,name);
            ResultSet rs = selectData.executeQuery();
            if(rs.next()){

            }else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sync(PlayerData data){
        //TODO 储存int long long

    }
}
