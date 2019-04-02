package br.arcus.ability

import Br.API.ItemBuilder
import br.arcus.Ability
import br.arcus.AbilityType
import org.bukkit.Material

fun getProficient():List<Ability> = listOf(
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
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object LiveAndDieTogether : Ability(
        "LiveAndDieTogether",
        "§a同生共死",
        listOf(
                "§7当生命值到达最低周围 §e{range} §7内的玩家吸到身边然后自爆",
                "§7冷却时间§e {cooldown} §7秒",
                "§7按住 §eshift §7触发同生共死"
        ),
        AbilityType.Proficient,
        1,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}