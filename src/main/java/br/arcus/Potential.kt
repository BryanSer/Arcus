package br.arcus

import Br.API.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import java.io.File

abstract class Potential(
        val name: String,
        val displayName: String,
        val description: List<String>,
        val index: Int,
        protected val displayItemBuilder: ItemBuilder
) {

    val config: MutableMap<String, Any> = mutableMapOf()
    private lateinit var displayItemStack: ItemStack

    fun reload() {
        val file = File(Main.Plugin.dataFolder, "config/potential/$name.yml")
        if (file.exists()) {
            val data = YamlConfiguration.loadConfiguration(file)
            for (key in data.getKeys(false)) {
                config[key] = data[key]
            }
        } else {
            val data = YamlConfiguration()
            for ((k, v) in config) {
                data[k] = v
            }
            data.save(file)
        }
        if (this is Listener) {
            Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
        }
        displayItemStack = displayItemBuilder.name(this.displayName).lore(
                description.map {
                    var str = it
                    for ((k, v) in config) {
                        str = str.replace("{$k}", v.toString())
                    }
                    str
                }.toList()
        ).build()
    }

    fun init() {
        Data.registeredPotential[name] = this
        val file = File(Main.Plugin.dataFolder, "config/potential/$name.yml")
        if (file.exists()) {
            val data = YamlConfiguration.loadConfiguration(file)
            for (key in data.getKeys(false)) {
                config[key] = data[key]
            }
        } else {
            val data = YamlConfiguration()
            for ((k, v) in config) {
                data[k] = v
            }
            data.save(file)
        }
        if (this is Listener) {
            Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
        }
        displayItemStack = displayItemBuilder.name(this.displayName).lore(
                description.map {
                    var str = it
                    for ((k, v) in config) {
                        str = str.replace("{$k}", v.toString())
                    }
                    str
                }.toList()
        ).build()
    }
}