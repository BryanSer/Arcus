package br.arcus;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main Plugin;

    @Override
    public void onEnable() {
        Plugin = this;
    }

    @Override
    public void onDisable() {
    }
}
