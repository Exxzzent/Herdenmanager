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
class Acker() : BeobachtbaresElement() {

    constructor(spalten: Int, zeilen: Int) : this() {
        this.spalten = spalten
        this.zeilen = zeilen
    }

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
     * Schaltet die Art der Animation um. Art der Animation der Änderungen auf dem Acker
     * (SYNCHRONOUS, ASYNCHRONOUS oder ASYNCHRONOUS_NO_WAIT).
     */
    var animation: Threading = Threading.ASYNCHRONOUS_NO_WAIT
        set(value) {
            val oldThreading = field
            field = value
            informiereBeobachter(Keys.PROPERTY_THREADING, oldThreading, value)
        }

    /**
     * Anzahl der Spalten des Ackers
     */
    var spalten: Int = 3
        set(value) {
            val oldValue = field
            field = value
            informiereBeobachter(Keys.PROPERTY_SIZE, oldValue, value)
        }

    /**
     * Anzahl der Zeilen des Ackers
     */
    var zeilen: Int = 3
        set(value) {
            val oldValue = field
            field = value
            informiereBeobachter(Keys.PROPERTY_SIZE, oldValue, value)
        }

    /**
     * Erzeugt an der Position eine Instanz von [Gras].
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
     * Der Code demonstriert die Verwendung der apply-Methode.
     *
     * @param name des Rindes, welches zukünftig hier weidet
     * @return Auf dem Acker platziertes Rind
     */
    fun lassRindWeiden(name: String): Rindvieh =
        Rindvieh(name).apply { lassRindWeiden(this) }

    /**
     * Hier wird ein existierendes Rindvieh auf die Weide gestellt.
     *
     * Der Code demonstriert die Verwendung der also-Methode.
     *
     * @param rind Rind, welches zukünftig hier weidet
     */
    fun lassRindWeiden(rind: Rindvieh) = rind.also {
        it.acker = this
        viecher.add(it)
        informiereBeobachter(Keys.PROPERTY_VIECHER, null, it)
    }

    /**
     * Stellt ein Kalb auf den Acker. Rinder können hier zukünftig
     * mit [Rindvieh.gibMilch] Milch geben.
     *
     * Wird ein neues Kalb auf dem Acker platziert, werden die Observer des Ackers informiert.
     *
     * Der Code demonstriert die Verwendung der also-Methode.
     *
     * @param position Position des aufzustellenden Kalbes
     * @return Auf dem Acker platziertes Kalb
     */
    fun lassKalbWeiden(position: Position): Kalb {
        return Kalb().also { kalb ->
            kalb.acker = this
            kalb.position = position
            kaelber.add(kalb)
            informiereBeobachter(Keys.PROPERTY_KALB, null, kalb)
        }
    }

    /**
     * @param position Zu prüfende Position
     * @return true, wenn an der Position ein [Kalb] steht
     */
    fun istDaEinKalb(position: Position): Boolean = kaelber.any { it.position == position }

    /**
     * @param position Zu prüfende Position
     * @return true, wenn an der Position ein [Gras] wächst
     */
    fun istDaGras(position: Position): Boolean = graeser.any { it.position == position }

    /**
     * Entfernt das erste an der Position gefundene Gras.
     *
     * Der Code demonstriert die Verwendung der firstOrNull- und let-Methode.
     *
     * @param position Zu prüfende Position
     */
    fun entferneGras(position: Position) {
        graeser.firstOrNull { it.position == position }?.let {
            graeser.remove(it)
            informiereBeobachter(Keys.PROPERTY_GRAESER, it, null)
        }
    }

    /**
     * Entfernt das Rindvieh, wenn es vorher auf dem Acker stand.
     *
     * Der Code demonstriert die Verwendung der find- und let-Methode.
     *
     * @param rind Zu entfernendes Rindvieh
     * @return true, wenn ein Rind entfernt wurde
     */
    fun entferneRind(rind: Rindvieh) {
        viecher.find { it == rind }?.let {
            viecher.remove(it)
            informiereBeobachter(Keys.PROPERTY_VIECHER, it, null)
        }
    }

    /**
     * Entfernt alle Rinder vom Acker
     *
     * Der Code demonstriert die Verwendung der forEach-Methode und einer Methodenreferenz.
     * Die forEach-Methode gibt Unit zurück, deshalb führt der Zuweisungsoperator nicht dazu,
     * dass entferneRinder einen "echten" Rückgabewert hat.
     */
    fun entferneRinder() = ArrayList(viecher).forEach(::entferneRind)

    /**
     * Da sich Kühe bewegen können, muss verhindert werden, dass sie den Acker verlassen.
     * Vor einer jeden Bewegung prüfen sie deshalb mit dieser Methode, ob die Zielposition
     * gültig ist.
     *
     * @param position Zu prüfende Position
     * @return true, wenn die Position auf dem Acker möglich ist
     */
    fun istGueltig(position: Position): Boolean =
        position.x in 0 until spalten && position.y in 0 until zeilen
}