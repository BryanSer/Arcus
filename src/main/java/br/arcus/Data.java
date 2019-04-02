package br.arcus;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.arcus.ability.*;

public class Data {
    public static Map<String, PlayerData> PlayerDatas = new HashMap<>();

    public static Map<String,Ability> registeredAbility = new HashMap<>();
    public static Map<String,Potential> registeredPotential = new HashMap<>();


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
        if(p != null){
            return PlayerDatas.get(p.getName());
        }
        return null;
    }
}
