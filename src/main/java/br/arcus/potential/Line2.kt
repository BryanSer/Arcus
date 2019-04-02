package br.arcus.potential

import Br.API.ItemBuilder
import br.arcus.Potential
import org.bukkit.Material

object HealthGem : Potential(
        "HealthGem",
        "§aHealthGem",
        listOf(
                "§7增加HealthGem宝石额外百分比冻结时间",
                "§7增加量: {amount}%"
        ),
        0,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object ShieldGem : Potential(
        "ShieldGem",
        "§aShieldGem",
        listOf(
                "§7增加ShieldGem宝石额外百分比护盾量和护盾持续时间",
                "§7增加量: {amount}%"
        ),
        1,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object SidestepGem : Potential(
        "SidestepGem",
        "§aSidestepGem",
        listOf(
                "§7增加SidestepGem宝石额外闪避概率",
                "§7增加量: {amount}%"
        ),
        2,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object HeavyGem : Potential(
        "HeavyGem",
        "§aHeavyGem",
        listOf(
                "§7增加HeavyGem宝石额外击退概率",
                "§7增加量: {amount}%"
        ),
        3,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object ArmorGem : Potential(
        "ArmorGem",
        "§aArmorGem",
        listOf(
                "§7增加ArmorGem宝石额外承受伤害",
                "§7增加量: {amount}%"
        ),
        4,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}