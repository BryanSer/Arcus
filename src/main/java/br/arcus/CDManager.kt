package br.arcus

import org.bukkit.entity.Entity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import java.text.SimpleDateFormat
import java.util.*

class CDManager {

    private constructor()

    private val lastCast = mutableMapOf<Int, Long>()

    fun cast(e: Entity) {
        lastCast[getID(e)] = System.currentTimeMillis()
    }

    fun castable(e: Entity, cd: Double): Boolean {
        val last = lastCast[getID(e)] ?: return true
        return (System.currentTimeMillis() - last) > cd * 1000L
    }

    fun getLeftTime(e:Entity,cd:Double):String{
        var last = lastCast[getID(e)] ?: 0
        last = System.currentTimeMillis() - last
        last = (cd * 1000L - last).toLong()
        if(last <0){
            return "0.0"
        }else {
            return dateFormat.format(Date(last))
        }
    }

    fun inTime(e: Entity, time: Double): Boolean = !castable(e, time)

    companion object : Listener {
        private val runningCDManager = mutableListOf<CDManager>()
        @JvmField
        val dateFormat = SimpleDateFormat("mm:ss")

        @EventHandler
        fun onDeath(evt: EntityDeathEvent) {
            for (l in runningCDManager) {
                l.lastCast.remove(evt.entity.entityId)
            }
        }

        @JvmStatic
        fun getID(e: Entity): Int {
            var e = e
            if (e is Projectile) {
                if (e.shooter !is Entity) {
                    return -1;
                }
                e = e.shooter as Entity
            }
            return e.entityId
        }

        @JvmStatic
        fun newInstance(): CDManager {
            val cd = CDManager()
            runningCDManager.add(cd)
            return cd
        }
    }
}