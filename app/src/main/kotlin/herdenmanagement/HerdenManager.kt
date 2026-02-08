package herdenmanagement

import herdenmanagement.model.DekoElement
import herdenmanagement.model.*

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

class HerdenManager {
    val elemente = ArrayList<DekoElement>()
    fun manageHerde(mainActivity: MainActivity) {
        elemente.add(mainActivity.acker.lassGrasWachsen(Position(1, 0)))
        elemente.add(mainActivity.acker.lassGrasWachsen(Position(1, 1)))
        elemente.add(mainActivity.acker.lassKalbWeiden(Position(0, 3)))
        elemente.add(mainActivity.acker.lassGrasWachsen(Position(2, 0)))
        elemente.add(mainActivity.acker.lassKalbWeiden(Position(2, 2)))
        val bladeRunner = mainActivity.acker.lassRindWeiden("Blade Runner")
        for (dekoElement in elemente) {
            if (dekoElement is Kalb) {
                bladeRunner.position = dekoElement.position
                bladeRunner.gibMilch()
            }
            if (dekoElement is Gras) {
                bladeRunner.position = dekoElement.position
                bladeRunner.frissGras()
            }
        }
    }
}