package br.arcus

import Br.API.GUI.Ex.Item
import Br.API.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack


val lockAbilityDisplay = ItemBuilder.getBuilder(Material.BARRIER).name("§c未解锁能力").build()

abstract class Ability(
        val name: String,
        val displayName: String,
        val description: List<String>,
        val type: AbilityType,
        val level: Int, // 从0开始 最大7
        val default: Boolean = false
) {

    lateinit var displayItemStack: ItemStack

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

        }
    }

    fun init() {
        if (this is Listener) {
            Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
        }
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