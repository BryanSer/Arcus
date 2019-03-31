package br.arcus


val NULL_EQUIP = 8

class PlayerData(val name: String) {

    val equipAbility: Array<Int> = arrayOf(NULL_EQUIP, NULL_EQUIP, NULL_EQUIP)
    val equipPotential: MutableSet<Int> = mutableSetOf()
    val unlockPotential: MutableSet<Int> = mutableSetOf()

    fun bitEquipAbility(): Int {
        var res = 0
        for (i in 0..2) {
            res = res or equipAbility[i]
            res = res shl 4
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

    fun loadUnlockPotential(res:Long){
        unlockPotential.clear()
        for (i in 0..26) {
            val t = ((res shr i) and 0b1).toInt()
            if (t == 1) unlockPotential.add(t)
        }
    }

    init {

    }
}