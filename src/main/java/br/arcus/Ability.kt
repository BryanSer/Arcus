package br.arcus

import Br.API.GUI.Ex.Item
import Br.API.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.io.File


val lockAbilityDisplay = ItemBuilder.getBuilder(Material.BARRIER).name("§c未解锁能力").build()

abstract class Ability(
        val name: String,
        val displayName: String,
        val description: List<String>,
        val type: AbilityType,
        val level: Int, // 从0开始 最大7
        protected val displayItemBuilder: ItemBuilder
) {

    val config: MutableMap<String, Any> = mutableMapOf()
    val cooldownManager = CDManager.newInstance()
    private lateinit var displayItemStack: ItemStack

    fun getDisplayItem(): Item = Item.getNewInstance { p: Player ->
        if (this.isUnlock(p)) {
            val item = displayItemStack.clone()
            if (this.isActiving(p)) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
            }
            return@getNewInstance item
        }
        lockAbilityDisplay
    }.setClick(ClickType.LEFT) { p ->
        if (this.isUnlock(p)) {
            val pd = Data.getData(p)
            pd.equip(this)
        } else {
            p.sendMessage("§c你还没有解锁这个能力")
        }
    }

    fun init() {
        val file = File(Main.Plugin.dataFolder, "config/ability/$name.yml")
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

    abstract fun onCast(e: Player): Boolean//返回true表示释放成功

    open fun getCooldown(): Double {
        val cd = this.config["cooldown"] as? Number ?: 10.0
        return cd.toDouble()
    }

    fun isActiving(e: Entity): Boolean {
        val pd = Data.getData(e)
        return pd?.equipAbility?.contains(this.level) ?: false
    }

    fun isUnlock(e: Entity): Boolean {
        val pd = Data.getData(e)
        return pd?.isUnlock(this) ?: false
    }
}