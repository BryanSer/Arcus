package br.arcus.potential

import Br.API.ItemBuilder
import br.arcus.Potential
import org.bukkit.Material

object AttackGem : Potential(
        "AttackGem",
        "§aAttackGem",
        listOf(
                "§7增加AttackGem宝石额外百分比伤害",
                "§7增加量: {amount}%"
        ),
        0,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object BackstabGem : Potential(
        "BackstabGem",
        "§aBackstabGem",
        listOf(
                "§7增加BackstabGem宝石额外百分比背刺伤害",
                "§7增加量: {amount}%"
        ),
        1,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object ChainLigGem : Potential(
        "ChainLigGem",
        "§aChainLigGem",
        listOf(
                "§7增加ChainLigGem宝石额外连锁次数、跟踪范围",
                "§7增加量: {amount}%"
        ),
        2,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object CritGem : Potential(
        "CritGem",
        "§aCritGem",
        listOf(
                "§7增加CritGem宝石额外百分比暴击伤害",
                "§7增加量: {amount}%"
        ),
        3,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object CurseArrowGem : Potential(
        "CurseArrowGem",
        "§aCurseArrowGem",
        listOf(
                "§7增加CurseArrowGem 宝石额外百分比冻结时间",
                "§7增加量: {amount}%"
        ),
        4,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object DiffusionGem : Potential(
        "DiffusionGem",
        "§aDiffusionGem",
        listOf(
                "§7增加DiffusionGem宝石额外散射数量",
                "§7增加量: {amount}%"
        ),
        5,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object LightningGem : Potential(
        "LightningGem",
        "§aLightningGem",
        listOf(
                "§7增加LightningGem宝石额外触发概率",
                "§7增加量: {amount}%"
        ),
        6,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object VampireGem : Potential(
        "VampireGem",
        "§aVampireGem",
        listOf(
                "§7增加VampireGem宝石额外百分比吸血",
                "§7增加量: {amount}%"
        ),
        7,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}