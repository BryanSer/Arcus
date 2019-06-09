package br.arcus.ability

import Br.API.ItemBuilder
import br.arcus.Ability
import br.arcus.AbilityType
import br.arcus.Main
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

fun getAttack(): List<Ability> {
    return listOf(
            ArtificialScroll,
            ToFightAnUphillBattle
    )
}

object ArtificialScroll : Ability(
        "ArtificialScroll",
        "§a上古卷轴",
        listOf(
                "§7在{range}格内离自己最近的玩家吸到身边并移除对方 §e增益 §7药水效果",
                "§7冷却时间§e {cooldown} §7秒",
                "§7按下 §eF §7发动能力"
        ),
        AbilityType.Attack,
        0,
        ItemBuilder.getBuilder(Material.PAPER)
) {

    val buff = mutableListOf<PotionEffectType>()

    fun removeBuff(p: Player) {
        for (pe in buff) {
            p.removePotionEffect(pe)
        }
    }

    init {
        config["range"] = 3.0
        config["cooldown"] = 10.0

        buff.add(PotionEffectType.ABSORPTION)
        buff.add(PotionEffectType.DAMAGE_RESISTANCE)
        buff.add(PotionEffectType.FAST_DIGGING)
        buff.add(PotionEffectType.FIRE_RESISTANCE)
        buff.add(PotionEffectType.GLOWING)
        buff.add(PotionEffectType.HEAL)
        buff.add(PotionEffectType.HEALTH_BOOST)
        buff.add(PotionEffectType.INCREASE_DAMAGE)
        buff.add(PotionEffectType.JUMP)
        buff.add(PotionEffectType.INVISIBILITY)
        buff.add(PotionEffectType.SPEED)
        buff.add(PotionEffectType.LUCK)
        buff.add(PotionEffectType.NIGHT_VISION)
        buff.add(PotionEffectType.LEVITATION)
    }

    override fun onCast(p: Player): Boolean {
        val range = (config["range"] as Number).toDouble()
        var target: Player? = null
        var min = Double.MAX_VALUE
        for (e in p.getNearbyEntities(range, range, range)) {
            if (e is Player && e !== p) {
                val d = e.location.distanceSquared(p.location)
                if (d < min) {
                    min = d
                    target = e
                }
            }
        }
        if (target != null) {
            removeBuff(target)
            val vec = p.location.toVector().subtract(target.location.toVector()).normalize().multiply(0.95)
            target.velocity = vec
            return true
        }
        p.sendMessage("§c找不到目标")
        return false
    }
}

object ToFightAnUphillBattle : Ability(
        "ToFightAnUphillBattle",
        "§a背水一战",
        listOf(
                "§7当血量低于一半可发动 §e{time}秒 §7双倍弹跳和移速",
                "§7冷却时间§e {cooldown} §7秒",
                "§7按下 §eF §7发动能力"
        ),
        AbilityType.Attack,
        1,
        ItemBuilder.getBuilder(Material.BUCKET)
) {
    init {
        config["time"] = 15.0
        config["cooldown"] = 45.0
    }

    override fun onCast(p: Player): Boolean {
        val attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        if (p.health / attribute.value > 0.5) {
            p.sendMessage("§c你的血量还高于一半")
            return false
        } else {
            val time = (config["time"] as Number).toDouble()
            val tick = (time * 20).toInt()
            val attr = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
            val it = attr.modifiers.iterator()
            while (it.hasNext()) {
                val next = it.next()

                if (next.name.equals("ToFightAnUphillBattle", true)) {
                    attr.removeModifier(next)
                    break;
                }
            }
            attr.addModifier(AttributeModifier("ToFightAnUphillBattle", 0.5, AttributeModifier.Operation.ADD_SCALAR))
            p.addPotionEffect(PotionEffect(PotionEffectType.JUMP, tick, 1))
            Bukkit.getScheduler().runTaskLater(Main.Plugin, {
                val attr = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                val it = attr.modifiers.iterator()
                while (it.hasNext()) {
                    val next = it.next()
                    if (next.name.equals("ToFightAnUphillBattle", true)) {
                        attr.removeModifier(next)
                        break;
                    }
                }
            }, tick.toLong())
            return true
        }
    }
}