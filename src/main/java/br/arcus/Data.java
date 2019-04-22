package br.arcus;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.arcus.ability.*;

public class Data {
    public static Map<String, PlayerData> PlayerDatas = new HashMap<>();

    public static Map<String, Ability> registeredAbility = new HashMap<>();
    public static Map<String, Potential> registeredPotential = new HashMap<>();
    public static List<String> EnableWorlds = new ArrayList<>();

    public static void loadConfig() {
        YamlConfiguration config = checkConfigFile();
        if (!config.contains("EnableWorlds")) {
            config.set("EnableWorlds", Arrays.asList("world"));
            try {
                config.save(new File(Main.Plugin.getDataFolder(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EnableWorlds.clear();
        EnableWorlds.addAll(config.getStringList("EnableWorlds"));
    }

    static YamlConfiguration checkConfigFile() {
        File f = new File(Main.Plugin.getDataFolder(), "config.yml");
        if (!f.exists()) {
            Main.Plugin.saveDefaultConfig();
        }
        return YamlConfiguration.loadConfiguration(f);
    }

    public static PlayerData getData(Entity e) {
        Player p = null;
        if (e instanceof Projectile) {
            Projectile pr = (Projectile) e;
            if (pr.getShooter() instanceof Player) {
                p = (Player) pr.getShooter();
            }
        }
        if (e instanceof Player) {
            p = (Player) e;
        }
        if (p != null) {
            PlayerData pd = PlayerDatas.get(p.getName());
            if (pd == null) {
                pd = SQLManager.loadData(p.getName());
                if (pd != null) {
                    PlayerDatas.put(p.getName(), pd);
                    SQLManager.sync(pd);
                }
            }
            return pd;
        }
        return null;
    }
}
