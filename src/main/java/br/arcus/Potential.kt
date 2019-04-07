package br.arcus

import Br.API.GUI.Ex.Item
import Br.API.ItemBuilder
import br.arcus.potential.getPotential
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
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
    fun isUnlock(p:Player):Boolean = Data.getData(p).isUnlock(this)
    fun isActiving(p:Player):Boolean = Data.getData(p).isActiving(this)

    fun getDisplayItem(): Item = Item.getNewInstance { p: Player ->
        if (this.isUnlock(p)) {
            val item = displayItemStack.clone()
            if (this.isActiving(p)) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
            }
            return@getNewInstance item
        }
        lockPotentialDisplay
    }.setClick(ClickType.LEFT) { p ->
        if (this.isUnlock(p)) {
            val pd = Data.getData(p)
            pd.equip(this)
        } else {
            p.sendMessage("§c你还没有解锁这个潜能")
        }
    }

    companion object {
        @JvmField
        val lockPotentialDisplay = ItemBuilder.getBuilder(Material.BARRIER).name("§c未解锁潜能").build()

        @JvmStatic
        fun initPotential(){
            for (g in getPotential()){
                g.init()
                Data.registeredPotential[g.name] = g
            }
        }
    }

    open fun reload() {
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
        displayItemStack = displayItemBuilder.name(this.displayName).clearLore().lore(
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