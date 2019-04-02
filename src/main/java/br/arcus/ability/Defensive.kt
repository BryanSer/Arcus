package br.arcus.ability

import Br.API.ItemBuilder
import br.arcus.Ability
import br.arcus.AbilityType
import org.bukkit.Material


fun getDefensive():List<Ability> = listOf(
        Clean,
        SoundOfGoldenLeather
)
object Clean : Ability(
        "Clean",
        "§a净化之术",
        listOf(
                "§7震开周围§e {range} §7格内的玩家并移除自身 §e负面 §7药水效果",
                "§7冷却时间§e {cooldown} §7秒",
                "§7按住 §eF §7移除增益状态"
        ),
        AbilityType.Defensive,
        0,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object SoundOfGoldenLeather : Ability(
        "SoundOfGoldenLeather",
        "§a金革之声",
        listOf(
                "§7当血量低于一半时让对方的攻击力减半§e {time} §7秒",
                "§7冷却时间§e {cooldown} §7秒",
                "§7按住 §eQ §7触发金革之声"
        ),
        AbilityType.Defensive,
        1,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}