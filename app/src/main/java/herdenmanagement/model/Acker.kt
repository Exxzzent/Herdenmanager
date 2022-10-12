package herdenmanagement.model

import java.util.ArrayList

/**
 * Der Acker besteht aus einer Matrix an Feldern, auf denen [Kalb], [Gras] und
 * Instanzen von [Rindvieh] platziert werden können. Der Acker beschränkt die Position
 * dieser Objekte mit seiner Größe.
 *
 * Der Acker kann prüfen, ob einer bestimmten Position Gras wächst [istDaGras] oder
 * ein Kalb steht [istDaEinKalb].
 *
 * Wird ein Kalb, Gras oder ein Rindvieh hinzugefügt oder entfernt, informiert der Acker
 * seine Beobachter.
 *
 * Im Muster Model View Controller sind Objekte dieser Klasse Bestandteil des Model. Der Acker
 * kann mit einer [herdenmanagement.view.AckerView] dargestellt werden.
 *
 * @author Steffen Greiffenberg
 *
 * @constructor Erzeugt einen Acker
 * @property spalten Anzahl der Spalten auf dem Acker
 * @property zeilen Anzahl der Zeilen auf dem Acker
 */
class Acker(
    var spalten: Int,
    var zeilen: Int
) : BeobachtbaresElement() {

    /**
     * Objekte der Klasse Rindvieh, die auf dem Acker unterwegs sind
     */
    val viecher: MutableList<Rindvieh> = ArrayList()

    /**
     * Objekte der Klasse Gras, die auf dem Acker wachsen
     */
    val graeser: MutableList<Gras> = ArrayList()

    /**
     * Objekte der Klasse Kalb, die auf dem Acker stehen
     */
    val kaelber: MutableList<Kalb> = ArrayList()

    /**
     * Schaltet die Art der Animation um. Art der Animation der Änderungen auf dem Acker.
     */
    var animation: Threading = Threading.ASYNCHRONOUS_NO_WAIT
        /**
         * @param value SYNCHRONOUS, ASYNCHRONOUS oder ASYNCHRONOUS_NO_WAIT
         */
        set (value) {
            val oldThreading = field
            field = value
            informiereBeobachter(Keys.PROPERTY_THREADING, oldThreading, value)
        }

    /**
     * Erzeugt an der Position eine Instanz von [Gras].
     *
     *
     * Wird ein neues Gras auf dem Acker platziert, werden die Observer des Ackers informiert.
     *
     * @param position Position, an der Gras wachsen soll
     * @return Auf dem Acker eingefügtes Gras
     */
    fun lassGrasWachsen(position: Position): Gras {
        val gras = Gras()
        gras.acker = this
        gras.position = position
        graeser.add(gras)
        informiereBeobachter(Keys.PROPERTY_GRAESER, null, gras)
        return gras
    }

    /**
     * Hier wird ein Rindvieh erzeugt. Dazu braucht es einen Namen.
     * Jedes Rind hat nur einen Acker, ein Acker mehrere
     * Rinder.
     *
     * Wird ein neues Rind auf dem Acker platziert, werden die Observer des Ackers informiert.
     *
     * @param name des Rindes, welches zukünftig hier weidet
     * @return Auf dem Acker platziertes Rind
     */
    fun lassRindWeiden(name: String): Rindvieh {
        val rind = Rindvieh(name)
        rind.acker = this
        viecher.add(rind)
        informiereBeobachter(Keys.PROPERTY_VIECHER, null, rind)
        return rind
    }

    /**
     * Stellt einen Kalb auf den Acker. Rinder können hier zukünftig
     * mit [Rindvieh.gibMilch] Milch geben.
     *
     *
     * Wird ein neues Kalb auf dem Acker platziert, werden die Observer des Ackers informiert.
     *
     * @param position Position des aufzustellenden Kalbes
     * @return Auf dem Acker platziertes Kalb
     */
    fun lassKalbWeiden(position: Position): Kalb {
        val kalb = Kalb()
        kalb.acker = this
        kalb.position = position
        kaelber.add(kalb)
        informiereBeobachter(Keys.PROPERTY_KALB, null, kalb)
        return kalb
    }

    /**
     * @param position Zu prüfende Position
     * @return true, wenn an der Position ein [Kalb] steht
     */
    fun istDaEinKalb(position: Position): Boolean {
        return kaelber.find { it.position == position } != null
    }

    /**
     * @param position Zu prüfende Position
     * @return true, wenn an der Position ein [Gras] wächst
     */
    fun istDaGras(position: Position): Boolean {
        return graeser.find { it.position == position } != null
    }

    /**
     * Kühe können mit [Rindvieh.frissGras] Gras fressen oder
     * mit [Rindvieh.raucheGras] Gras rauchen.
     *
     * @param position Zu prüfende Position
     * @return true, wenn an der Position [Gras] wächst
     */
    fun entferneGras(position: Position): Boolean {
        for (gras in graeser) {
            if (gras.position == position) {
                graeser.remove(gras)
                informiereBeobachter(Keys.PROPERTY_GRAESER, gras, null)
                return true
            }
        }
        return false
    }

    /**
     * Da sich Kühe bewegen können, muss verhindert werden, dass sie den Acker verlassen.
     * Vor einer jeden bewegung prüfen sie deshalb mit dieser Methode, ob die Zielposition
     * gültig ist.
     *
     * @param position Zu prüfende Position
     * @return true, wenn die Position auf dem Acker möglich ist
     */
    fun istGueltig(position: Position): Boolean {
        return position.x > -1 &&
                position.x < spalten &&
                position.y > -1 &&
                position.y < zeilen
    }
}