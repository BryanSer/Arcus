package br.arcus

import Br.API.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack

val MAX_ABILITY_AMOUNT: Int = 8

enum class AbilityType(
        val displayName: String,
        val displayItem: ItemStack,
        val index: Int
) : Listener {
    Attack("§c攻击",
            ItemBuilder.getBuilder(Material.DIAMOND_SWORD).name("§6能力: §c攻击").build(),
            0) {
        @EventHandler(ignoreCancelled = false)
        fun onF(evt: PlayerSwapHandItemsEvent) {
            val pd = Data.getData(evt.player) ?: return
            val ac = pd.getActiving(this) ?: return
            evt.isCancelled = true
            if (ac.cooldownManager.castable(evt.player, ac.getCooldown())) {
                if (ac.onCast(evt.player)) {
                    ac.cooldownManager.cast(evt.player)
                }
            } else {
                evt.player.sendMessage(
                        "§c${ac.displayName}冷却时间余剩${ac.cooldownManager.getLeftTime(evt.player, ac.getCooldown())}")
            }
        }
    },
    Defensive("§b防御",
            ItemBuilder.getBuilder(Material.SHIELD).name("§6能力: §b防御").build(),
            1) {
        @EventHandler(ignoreCancelled = false)
        fun onQ(evt: PlayerDropItemEvent) {
            val pd = Data.getData(evt.player) ?: return
            val ac = pd.getActiving(this) ?: return
            evt.isCancelled = true
            if (ac.cooldownManager.castable(evt.player, ac.getCooldown())) {
                if (ac.onCast(evt.player)) {
                    ac.cooldownManager.cast(evt.player)
                }
            }else {
                evt.player.sendMessage(
                        "§c${ac.displayName}冷却时间余剩${ac.cooldownManager.getLeftTime(evt.player, ac.getCooldown())}")
            }
        }
    },
    Proficient("§a能力",
            ItemBuilder.getBuilder(Material.ARROW).name("§6能力: §a能力").build(),
            2) {
        @EventHandler
        fun onShift(evt:PlayerToggleSneakEvent){
            if(!evt.isSneaking) return
            val pd = Data.getData(evt.player) ?: return
            val ac = pd.getActiving(this) ?: return
            evt.isCancelled = true
            if (ac.cooldownManager.castable(evt.player, ac.getCooldown())) {
                if (ac.onCast(evt.player)) {
                    ac.cooldownManager.cast(evt.player)
                }
            }else {
                evt.player.sendMessage(
                        "§c${ac.displayName}冷却时间余剩${ac.cooldownManager.getLeftTime(evt.player, ac.getCooldown())}")
            }
        }
    };


    init {
        Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
    }

    val ability: Array<Ability?> = Array(MAX_ABILITY_AMOUNT) { null }

    fun getAbility(index: Int): Ability? = ability[index]
}
