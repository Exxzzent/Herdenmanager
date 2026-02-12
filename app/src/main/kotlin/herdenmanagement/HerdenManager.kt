package herdenmanagement

import herdenmanagement.model.Position
import herdenmanagement.model.Rindvieh

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
class HerdenManager {

    fun manageHerde(mainActivity: MainActivity) {
        val acker = mainActivity.acker
        val vera = acker.lassRindWeiden("Vera")
        mainActivity.toast(euklid(66, 99))
    }

    private fun euklid(z1 : Int, z2 : Int) : Int {
        if (z1 > z2) {
            return euklid(z2, z1 - z2)
        } else if (z2 > z1) {
            return euklid(z1, z2 - z1)
        } else {
            return z1
        }
    }

}

