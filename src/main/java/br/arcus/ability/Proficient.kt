package br.arcus.ability

import Br.API.ItemBuilder
import br.arcus.Ability
import br.arcus.AbilityType
import br.arcus.Main
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitTask
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionEffect
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.event.player.PlayerToggleSneakEvent
import java.util.HashMap


fun getProficient(): List<Ability> = listOf(
        ShortNeedleAttack,
        LiveAndDieTogether
)

object ShortNeedleAttack : Ability(
        "ShortNeedleAttack",
        "§a短针攻疽",
        listOf(
                "§7每秒恢复§e {recover}§7生命值",
                "§7回复时无法移动且失明",
                "§7冷却时间§e {cooldown} §7秒",
                "§7长按 §eshift §7进入治愈状态"
        ),
        AbilityType.Proficient,
        0,
        ItemBuilder.getBuilder(Material.POTION),
        buttonActive = false
), Listener {

    init {
        config["recover"] = 5.0
        config["cooldown"] = 10.0
    }

    private val HealTask = HashMap<String, BukkitTask>()

    @EventHandler(ignoreCancelled = true)
    fun onShift(evt: PlayerToggleSneakEvent) {
        if (evt.isSneaking) {
            if (!super.isActiving(evt.player)) {
                return
            }
            if (!cooldownManager.castable(evt.player, getCooldown())) {
                return
            }
            cooldownManager.cast(evt.player)
            val put = HealTask.put(evt.player.name,
                    object : BukkitRunnable() {
                        internal var time = 1
                        private val p = evt.player

                        override fun run() {
                            if (!p.isOnline) {
                                this.cancel()
                                return
                            }
                            if (time++ % 10 == 0) {
                                var health = p.health + (config["recover"] as Number).toDouble()
                                health = if (health > p.maxHealth) p.maxHealth else health
                                p.health = health
                                p.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 40, 4))
                                p.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 0))
                            }
                        }
                    }.runTaskTimer(Main.Plugin, 2, 2))
            put?.cancel()
        } else {
            val task = HealTask[evt.player.name]
            if (task != null) {
                task.cancel()
                evt.player.removePotionEffect(PotionEffectType.SLOW)
                evt.player.removePotionEffect(PotionEffectType.BLINDNESS)
            }
        }
    }

    @EventHandler
    fun onDamage(evt: EntityDamageEvent) {
        if (super.isActiving(evt.entity)) {
            val task = HealTask[evt.entity.name]
            task?.cancel()
        }
    }

    @EventHandler
    fun onDamage(evt: EntityDamageByEntityEvent) {
        if (evt.damager != null && super.isActiving(evt.damager)) {
            val task = HealTask[evt.damager.name]
            task?.cancel()
        }
    }


    override fun onCast(p: Player): Boolean = true
}

object LiveAndDieTogether : Ability(
        "LiveAndDieTogether",
        "§a同生共死",
        listOf(
                "§7当生命值到达25%以下 可将周围 §e{range} §7内的玩家吸到身边然后自爆",
                "§6共造成{damage}伤害 所有玩家共同分担",
                "§7冷却时间§e {cooldown} §7秒",
                "§7按下 §eshift §7触发同生共死"
        ),
        AbilityType.Proficient,
        1,
        ItemBuilder.getBuilder(Material.PAPER)
) {
    init {
        config["range"] = 5.0
        config["damage"] = 100.0
        config["cooldown"] = 100.0
    }

    override fun onCast(p: Player): Boolean {
        val attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        if (p.health / attribute.value > 0.25) {
            p.sendMessage("§c你的血量还高于25%")
            return false
        }
        val range = (config["range"] as Number).toDouble()
        val list = mutableListOf<Player>()
        for (e in p.getNearbyEntities(range, range, range)) {
            if (e is Player && e != p) {
                var vec = p.location.toVector().subtract(e.location.toVector())
                vec = vec.multiply(0.95)
                val loc = e.location.add(vec)
                e.teleport(loc)
                list.add(e)
            }
        }
        list.add(p)
        var damage = (config["damage"] as Number).toDouble() / list.size
        for (p in list) {
            p.damage(damage)
        }
        p.world.createExplosion(p.location.x, p.location.y, p.location.z, 4f, false,false)
        list.clear()
        return true
    }
}