package br.arcus

import Br.API.GUI.Ex.Item
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.Listener

abstract class Ability(
        val name: String,
        val displayName: String,
        val description: List<String>,
        val level: Int, // 从0开始 最大7
        val default: Boolean = false
) {

    fun getDisplayItem(): Item = Item.getNewInstance { p: Player ->
        null
    }

    fun init() {
        if (this is Listener) {
            Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
        }
    }


    fun isActiving(e: Entity): Boolean {
        var p: Player? = null
        if (e is Projectile) {
            if (e.shooter is Player) {
                p = e.shooter as Player
            }
            return false
        }
        if (e is Player) {
            p = e
        }
        if (p == null) return false
        val pd = Data.PlayerDatas[p.name]
        return pd?.equipAbility?.contains(this.level) ?: false
    }
}