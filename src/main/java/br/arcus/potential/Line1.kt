package br.arcus.potential

import Br.API.ItemBuilder
import br.arcus.Potential
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

fun getPotential(): List<GemPotential> = listOf(
        AttackGem,
        BackstabGem,
        ChainLigGem,
        CritGem,
        CurseArrowGem,
        DiffusionGem,
        LightningGem,
        VampireGem,
        //HealthGem,
        ShieldGem,
        SidestepGem,
        HeavyGem,
        ArmorGem
)

abstract class GemPotential(
        name: String,
        displayName: String,
        description: List<String>,
        index: Int,
        displayItemBuilder: ItemBuilder
) : Potential(
        name,
        displayName,
        description,
        index,
        displayItemBuilder
) {
    init {
        config["amount"] = 10.0
    }

    var amount: Double? = null
    fun getAmount(): Double {
        if (amount == null) {
            amount = (config["amount"] as Number).toDouble() / 100.0
        }
        return amount ?: getAmount()
    }

    override fun reload() {
        super.reload()
        amount = null
    }
}

object AttackGem : GemPotential(
        "AttackGem",
        "§aAttackGem",
        listOf(
                "§7攻击时造成额外上海",
                "§7增加量: {amount}%"
        ),
        0,
        ItemBuilder.getBuilder(Material.PAPER)
) , Listener {
    @EventHandler
    fun onDamage(evt:EntityDamageByEntityEvent){
        if(super.isActiving(evt.damager as? Player ?: return)){
            evt.damage *= (1.0 + this.getAmount())
        }
    }
}

object BackstabGem : GemPotential(
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

object ChainLigGem : GemPotential(
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

object CritGem : GemPotential(
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

object CurseArrowGem : GemPotential(
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

object DiffusionGem : GemPotential(
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

object LightningGem : GemPotential(
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

object VampireGem : GemPotential(
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