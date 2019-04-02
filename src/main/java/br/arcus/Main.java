package br.arcus;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main Plugin;

    @Override
    public void onEnable() {
        Plugin = this;
        Bukkit.getPluginManager().registerEvents(CDManager.Companion, this);
        Ability.initAbility();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()) return true;
        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            return false;
        }
        if(args[0].equalsIgnoreCase("reload")){
            for (Ability ability : Data.registeredAbility.values()) {
                ability.reload();
            }
            for (Potential potential : Data.registeredPotential.values()) {
                potential.reload();
            }
            sender.sendMessage("§6重载完成");
            return true;
        }

        return false;
    }
}
