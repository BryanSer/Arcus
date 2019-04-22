package br.arcus

import Br.API.ItemBuilder
import com.comphenix.protocol.PacketType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.injector.GamePhase
import com.comphenix.protocol.wrappers.EnumWrappers


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
            if(!Data.EnableWorlds.contains(evt.player.world.name)){
                return;
            }
            val pd = Data.getData(evt.player) ?: return
            val ac = pd.getActiving(this) ?: return
            if (!ac.buttonActive) {
                return
            }
            evt.isCancelled = true
            if (ac.cooldownManager.castable(evt.player, ac.getCooldown())) {
                if (ac.onCast(evt.player)) {
                    ac.cooldownManager.cast(evt.player)
                    evt.player.sendMessage("§6§l你发动了${ac.displayName}")
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
        init {
            val t = this
            val pm = ProtocolLibrary.getProtocolManager()
            pm.addPacketListener(object: PacketAdapter(
                    PacketAdapter.params()
                            .clientSide()
                            .gamePhase(GamePhase.PLAYING)
                            .types(PacketType.Play.Client.BLOCK_DIG)
                            .plugin(Main.Plugin)
            ){
                override fun onPacketReceiving(evt: PacketEvent) {
                    if(!Data.EnableWorlds.contains(evt.player.world.name)){
                        return;
                    }
                    val packet = evt.packet
                    val read = packet.playerDigTypes.read(0)
                    if(read == EnumWrappers.PlayerDigType.DROP_ALL_ITEMS
                            || read == EnumWrappers.PlayerDigType.DROP_ITEM){

                        val pd = Data.getData(evt.player) ?: return
                        val ac = pd.getActiving(t) ?: return
                        if (!ac.buttonActive) {
                            return
                        }
                        evt.isCancelled = true
                        if (ac.cooldownManager.castable(evt.player, ac.getCooldown())) {
                            if (ac.onCast(evt.player)) {
                                ac.cooldownManager.cast(evt.player)
                                evt.player.sendMessage("§6§l你发动了${ac.displayName}")
                            }
                        } else {
                            evt.player.sendMessage(
                                    "§c${ac.displayName}冷却时间余剩${ac.cooldownManager.getLeftTime(evt.player, ac.getCooldown())}")
                        }
                    }
                }
            })
        }
    },
    Proficient("§a能力",
            ItemBuilder.getBuilder(Material.ARROW).name("§6能力: §a能力").build(),
            2) {
        @EventHandler
        fun onShift(evt: PlayerToggleSneakEvent) {
            if(!Data.EnableWorlds.contains(evt.player.world.name)){
                return;
            }
            if (!evt.isSneaking) return
            val pd = Data.getData(evt.player) ?: return
            val ac = pd.getActiving(this) ?: return
            if (!ac.buttonActive) {
                return
            }
            if (ac.cooldownManager.castable(evt.player, ac.getCooldown())) {
                if (ac.onCast(evt.player)) {
                    ac.cooldownManager.cast(evt.player)
                    evt.player.sendMessage("§6§l你发动了${ac.displayName}")
                }
            } else {
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
