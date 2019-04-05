package br.arcus.potential

import Br.API.ItemBuilder
import br.arcus.Potential
import org.bukkit.Material

object HealthGem : GemPotential(
        "HealthGem",
        "§aHealthGem",
        listOf(
                "§7增加HealthGem宝石额外百分比冻结时间",
                "§7增加量: {amount}%"
        ),
        9,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object ShieldGem : GemPotential(
        "ShieldGem",
        "§aShieldGem",
        listOf(
                "§7增加ShieldGem宝石额外百分比护盾量和护盾持续时间",
                "§7增加量: {amount}%"
        ),
        10,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object SidestepGem : GemPotential(
        "SidestepGem",
        "§aSidestepGem",
        listOf(
                "§7增加SidestepGem宝石额外闪避概率",
                "§7增加量: {amount}%"
        ),
        11,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object HeavyGem : GemPotential(
        "HeavyGem",
        "§aHeavyGem",
        listOf(
                "§7增加HeavyGem宝石额外击退概率",
                "§7增加量: {amount}%"
        ),
        12,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object ArmorGem : GemPotential(
        "ArmorGem",
        "§aArmorGem",
        listOf(
                "§7增加ArmorGem宝石额外承受伤害",
                "§7增加量: {amount}%"
        ),
        13,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}