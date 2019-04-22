package br.arcus;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

import Br.API.GUI.Ex.UIManager;

public final class Main extends JavaPlugin {

    public static Main Plugin;

    @Override
    public void onEnable() {
        Plugin = this;
        try {
            SQLManager.loadConfig();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Data.loadConfig();
        Bukkit.getPluginManager().registerEvents(CDManager.Companion, this);
        Ability.initAbility();
        Potential.initPotential();
        UIManager.RegisterUI(new AbilityUI());

        Bukkit.getPluginManager().registerEvents(new Listener(){
            @EventHandler
            public void onQuit(PlayerQuitEvent evt){
                PlayerData pd = Data.getData(evt.getPlayer());
                if(pd != null){
                    SQLManager.sync(pd);
                    Data.PlayerDatas.remove(evt.getPlayer().getName());
                }
            }
        }, this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                UIManager.OpenUI(player, "AR_AUI");
            }
            return true;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            for (Ability ability : Data.registeredAbility.values()) {
                ability.reload();
            }
            for (Potential potential : Data.registeredPotential.values()) {
                potential.reload();
            }
            Data.loadConfig();
            sender.sendMessage("§6重载完成");
            return true;
        }
        if (args[0].equalsIgnoreCase("ui")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                UIManager.OpenUI(player, "AR_AUI");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage("§6可用能力: ");
            String str = "";
            for (Ability ability : Data.registeredAbility.values()) {
                str += ability.getName() + ": " + ability.getDisplayName() + "  ";
            }
            sender.sendMessage(str);
            str = "";
            sender.sendMessage("§6可用潜能: ");
            for (Potential ability : Data.registeredPotential.values()) {
                str += ability.getName() + ": " + ability.getDisplayName() + "  ";
            }
            sender.sendMessage(str);
            return true;
        }
        if(args[0].equalsIgnoreCase("unlock") && args.length > 2){
            Player p  = Bukkit.getPlayerExact(args[1]);
            if(p == null){
                sender.sendMessage("§c找不到玩家");
                return true;
            }
            PlayerData pd = Data.getData(p);
            Ability ability = Data.registeredAbility.get(args[2]);
            if(ability != null){
                pd.unlock(ability);
                sender.sendMessage("§6解锁完成");
                return true;
            }
            Potential potential = Data.registeredPotential.get(args[2]);
            if(potential != null){
                pd.unlock(potential);
                sender.sendMessage("§6解锁完成");
                return true;
            }
            sender.sendMessage("§c找不到能力或者潜能");
            return true;
        }
        return false;
    }
}
