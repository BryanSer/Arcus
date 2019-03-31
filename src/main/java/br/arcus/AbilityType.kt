package br.arcus

import Br.API.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

val MAX_ABILITY_AMOUNT: Int = 8

enum class AbilityType(
        val displayName: String,
        val displayItem: ItemStack,
        val index: Int
) {
    Attack("§c攻击", ItemBuilder.getBuilder(Material.DIAMOND_SWORD).name("§6能力: §c攻击").build(), 0),
    Defensive("§b防御", ItemBuilder.getBuilder(Material.SHIELD).name("§6能力: §b防御").build(), 1),
    Proficient("§a能力", ItemBuilder.getBuilder(Material.ARROW).name("§6能力: §a能力").build(), 2);

    val ability: Array<Ability?> = Array(MAX_ABILITY_AMOUNT) { null }

    fun getAbility(index: Int): Ability? = ability[index]
}
