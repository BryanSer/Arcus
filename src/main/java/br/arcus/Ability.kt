package br.arcus

import Br.API.GUI.Ex.Item
import Br.API.ItemBuilder
import br.arcus.ability.getAttack
import br.arcus.ability.getDefensive
import br.arcus.ability.getProficient
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
        this.type.ability[this.level] = this
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

    private var cooldown: Double? = null

    open fun reload() {
        cooldown = null
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

    open fun getCooldown(): Double {
        if (cooldown == null) cooldown = (this.config["cooldown"] as? Number ?: 10.0).toDouble()
        return cooldown!!
    }

    fun isActiving(e: Entity): Boolean {
        val pd = Data.getData(e)
        return pd?.getActiving(this.type) == this
    }

    fun isUnlock(e: Entity): Boolean {
        val pd = Data.getData(e)
        return pd?.isUnlock(this) ?: false
    }

    companion object {
        @JvmField
        val lockAbilityDisplay = ItemBuilder.getBuilder(Material.BARRIER).name("§c未解锁能力").build()

        @JvmStatic
        fun initAbility() {
            val list = mutableListOf<Ability>()
            list.addAll(getAttack())
            list.addAll(getDefensive())
            list.addAll(getProficient())
            for (ab in list) {
                Data.registeredAbility[ab.name] = ab
            }
        }
    }
}