package br.arcus.ability

import Br.API.ItemBuilder
import br.arcus.Ability
import br.arcus.AbilityType
import org.bukkit.Material

fun getAttack():List<Ability>{
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
                "§7按住 §eF §7移除增益状态"
        ),
        AbilityType.Attack,
        0,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

object ToFightAnUphillBattle : Ability(
        "ToFightAnUphillBattle",
        "§a背水一战",
        listOf(
                "§7当血量低于一半获得 §e{time}秒 §7双倍弹跳和移速",
                "§7冷却时间§e {cooldown} §7秒",
                "§7按住 §eF §7移除增益状态"
        ),
        AbilityType.Attack,
        1,
        ItemBuilder.getBuilder(Material.PAPER)
) {
}

