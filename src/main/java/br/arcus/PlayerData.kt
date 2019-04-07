package br.arcus

import java.util.*


val NULL_EQUIP = 8
fun toArr(bit: Int): Array<Boolean> {
    var bit = bit
    val arr = Array<Boolean>(8) { false }
    for (i in 7 downTo 0) {
        val t = bit and 0b1
        arr[i] = t != 0
        bit = bit shr 1
    }
    return arr
}

fun toBit(arr: Array<Boolean>): Int {
    var r = 0
    for (i in 0..7) {
        r = r shl 1
        r = r or (if (arr[i]) 1 else 0)
    }
    return r
}

class PlayerData(val name: String) {

    private val equipAbility: Array<Int> = arrayOf(NULL_EQUIP, NULL_EQUIP, NULL_EQUIP)
    private val unlockAbility: MutableMap<AbilityType, Array<Boolean>> = EnumMap(AbilityType::class.java)
    private val equipPotential: MutableSet<Int> = mutableSetOf()
    private val unlockPotential: MutableSet<Int> = mutableSetOf()


    fun getActiving(type: AbilityType): Ability? {
        val i = equipAbility[type.index]
        return if (i == NULL_EQUIP) null else type.getAbility(i)
    }

    init {
        unlockAbility[AbilityType.Attack] = Array(8) { false }
        unlockAbility[AbilityType.Defensive] = Array(8) { false }
        unlockAbility[AbilityType.Proficient] = Array(8) { false }
    }

    fun equip(abi: Ability) {
        if (equipAbility[abi.type.index] != abi.level)
            equipAbility[abi.type.index] = abi.level
        else equipAbility[abi.type.index] = NULL_EQUIP
        SQLManager.sync(this)
    }

    fun equip(abi: Potential) {
        if (equipPotential.contains(abi.index)) {
            equipPotential.remove(abi.index)
        } else {
            equipPotential.add(abi.index)
        }
        SQLManager.sync(this)
    }

    fun isUnlock(abi: Ability): Boolean {
        val uc = unlockAbility[abi.type]!!
        return uc[abi.level]
    }

    fun isUnlock(p: Potential): Boolean {
        return unlockPotential.contains(p.index)
    }

    fun unlock(abi: Ability) {
        val uc = unlockAbility[abi.type]!!
        uc[abi.level] = true
        SQLManager.sync(this)
    }

    fun unlock(p: Potential) {
        unlockPotential.add(p.index)
        SQLManager.sync(this)
    }

    fun bitUnlockAbility(): Int {
        var res = 0
        val atk = toBit(unlockAbility[AbilityType.Attack]!!)
        val def = toBit(unlockAbility[AbilityType.Defensive]!!)
        val pro = toBit(unlockAbility[AbilityType.Proficient]!!)
        res = res or atk
        res = res shl 8
        res = res or def
        res = res shl 8
        res = res or pro
        return res
    }

    fun loadUnlockAbility(res: Int) {
        val pro = res and 0b11111111
        unlockAbility[AbilityType.Proficient] = toArr(pro)
        val def = (res shr 8) and 0b11111111
        unlockAbility[AbilityType.Defensive] = toArr(def)
        val atk = (res shr 16) and 0b11111111
        unlockAbility[AbilityType.Attack] = toArr(atk)
    }

    fun bitEquipAbility(): Int {
        var res = 0
        for (i in 0..2) {
            res = res shl 4
            res = res or equipAbility[i]
        }
        return res
    }

    fun loadEquipAbility(res: Int) {
        var v = res
        for (i in 2 downTo 0) {
            val t = v and 0b1111
            equipAbility[i] = t
            v = v shr 4
        }
    }

    fun bitEquipPotential(): Long {
        var res = 0L
        for (v in equipPotential) {
            val e = 1L shl v
            res = res or e
        }
        return res
    }

    fun loadEquipPotential(res: Long) {
        equipPotential.clear()
        for (i in 0..26) {
            val t = ((res shr i) and 0b1).toInt()
            if (t == 1) equipPotential.add(t)
        }
    }

    fun bitUnlockPotential(): Long {
        var res = 0L
        for (v in unlockPotential) {
            val e = 1L shl v
            res = res or e
        }
        return res
    }

    fun loadUnlockPotential(res: Long) {
        unlockPotential.clear()
        for (i in 0..26) {
            val t = ((res shr i) and 0b1).toInt()
            if (t == 1) unlockPotential.add(t)
        }
    }

    fun isActiving(p: Potential): Boolean {
        return this.equipPotential.contains(p.index)
    }

}