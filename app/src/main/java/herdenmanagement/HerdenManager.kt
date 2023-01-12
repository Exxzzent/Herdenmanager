package herdenmanagement

/**
 * Die Klasse dient der Organisation von Rinderherden. Hierzu werden auf einem
 * [herdenmanagement.model.Acker] Objekte der Klasse [herdenmanagement.model.Kalb]
 * und [herdenmanagement.model.Gras] positioniert. Objekte der Klasse
 * [herdenmanagement.model.Rindvieh] können sich auf einem Acker bewegen
 * und das Gras fressen oder rauchen. Steht auf der aktuellen Position einer
 * Kuh ein Kalb, kann diese auch gemolken werden.
 *
 * Mit einer [herdenmanagement.view.AckerView] wird ein erzeugter Acker auch grafisch angezeigt.
 * Auf diesem können Instanzen von [herdenmanagement.model.Rindvieh],
 * [herdenmanagement.model.Kalb] und [herdenmanagement.model.Gras] eingefügt werden.
 *
 *
 * Im Muster Model View Controller (MVC) entsprechen Objekte dieser Klasse dem Controller.
 * [herdenmanagement.model.Acker], [herdenmanagement.model.Rindvieh], [herdenmanagement.model.Kalb]
 * und [herdenmanagement.model.Gras] bilden im MVC Muster das Model. Im Muster Observer
 * stellen sie die beobachtbaren Objekte dar. Die eigentliche grafische Darstellung
 * des Models erfolgt in den View-Klassen des MVC Musters (also zum Beispiel in der
 * Klasse [herdenmanagement.view.RindviehView]. Diese View-Klassen sind gleichzeit Beobachter
 * gemäß des Observer Muster. Wenn man also Veränderungen an einer Kuh vornimmt, wird diese
 * Ihre Beaobachter informieren und diese passen ihre grafische Darstellung an.
 *
 *
 * Die Klasse verknüpft im Wesentlichen einen [herdenmanagement.model.Acker]
 * (= Model im MVC Muster) mit seiner [herdenmanagement.view.AckerView] (= View im MVC Muster).
 * Da sie diese und andere Vorgänge (insbesondere Änderungen auf Acker) organisiert, ist sie
 * der Controller im MVC Muster.
 *
 * @author Steffen Greiffenberg
 */
@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
class HerdenManager {

    /**
     * Aufruf zur Erzeugung eines HerdenManagers.
     * Diese Methode lässt zum Beispiel Gras wachsen und stellt Kälber auf.
     * Die Einrichtung des Ackers wird nicht animiert dargestellt.
     * Diese Methode kann man auch löschen und nur mit manageHerde arbeiten
     *
     * @param mainActivity Hauptaktivität der App
     */
    fun richteAckerEin(mainActivity: MainActivity) {
    }

    /**
     * Hier wird eine Herde gemanagt. Und zwar professionell. Das bedeutet vor allem,
     * dass Rinder bewegt und zum Fressen angehalten werden. Natürlich können Sie danach
     * Milch geben!
     *
     *
     * Die Aktionen dieser Methoden werden animiert und nacheinander dargestellt. Man kann in der
     * App also die Reihenfolge der Aktionen sehen (anders als die Aktionen in
     * [richteAckerEin].
     *
     * @param mainActivity Hauptaktivität
     */
    fun manageHerde(mainActivity: MainActivity) {
        val acker = mainActivity.acker
        val vera = acker.lassRindWeiden("Vera")

        vera.geheVor()
        mainActivity.toast("Vera läuft!")
    }
}
