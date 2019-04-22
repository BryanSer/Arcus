package br.arcus.ability

import Br.API.ItemBuilder
import Br.API.Utils
import br.arcus.Ability
import br.arcus.AbilityType
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffectType


fun getDefensive(): List<Ability> = listOf(
        Clean,
        SoundOfGoldenLeather
)

object Clean : Ability(
        "Clean",
        "§a净化之术",
        listOf(
                "§7震开周围§e {range} §7格内的玩家并移除自身 §e负面 §7药水效果",
                "§7冷却时间§e {cooldown} §7秒",
                "§7按下 §eQ §7使用能力"
        ),
        AbilityType.Defensive,
        0,
        ItemBuilder.getBuilder(Material.MILK_BUCKET)
) {
    init {
        config["range"] = 3.0
        config["cooldown"] = 10.0
    }

    override fun onCast(p: Player): Boolean {
        for (pe in PotionEffectType.values()) {
            if (ArtificialScroll.buff.contains(pe)) {
                continue
            }
            if (pe != null)
                p.removePotionEffect(pe)
        }
        val range = (config["range"] as Number).toDouble()
        for (e in p.getNearbyEntities(range, range, range)) {
            if (e !== p) {
                val vec = e.location.toVector().subtract(p.location.toVector()).normalize().multiply(1.5)
                e.velocity = vec
            }
        }
        return true
    }
}

object SoundOfGoldenLeather : Ability(
        "SoundOfGoldenLeather",
        "§a金革之声",
        listOf(
                "§7当血量低于一半时让指向的玩家的攻击力减半§e {time} §7秒",
                "§7冷却时间§e {cooldown} §7秒",
                "§7按下 §eQ §7发动"
        ),
        AbilityType.Defensive,
        1,
        ItemBuilder.getBuilder(Material.GOLD_LEGGINGS)
), Listener {
    init {
        config["time"] = 5.0
        config["cooldown"] = 25.0
    }

    val target = mutableMapOf<Int, Long>()

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onDamage(evt: EntityDamageByEntityEvent) {
        val p = evt.damager
        if (p is Player) {
            val time = target[p.entityId] ?: return
            if (System.currentTimeMillis() < time) {
                evt.damage *= 0.5
            }
        }
    }

    override fun onCast(p: Player): Boolean {
        val attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        if (p.health / attribute.value > 0.5) {
            p.sendMessage("§c你的血量还高于一半")
            return false
        }
        val target = Utils.Coordinate.getLookAtEntity(p, 30.0, 60)
        if (target == null) {
            p.sendMessage("§c你没有指向任何敌人")
            return false
        }
        this.target[target.entityId] = System.currentTimeMillis() + (1000L * (config["time"] as Number).toDouble()).toLong()
        return true
    }
}