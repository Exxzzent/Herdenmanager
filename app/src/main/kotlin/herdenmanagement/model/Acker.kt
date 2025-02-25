package herdenmanagement.model

/**
 * Repräsentiert einen Acker, der als Matrix aus Feldern konzipiert ist, auf denen [Kalb],
 * [Gras] und [Rindvieh] positioniert werden können.
 *
 * Der Acker übernimmt mehrere Aufgaben:
 * - Er begrenzt die Position der platzierten Objekte entsprechend seiner definierten Größe.
 * - Er informiert seine Beobachter (über das Observer-Muster) bei Änderungen, wie z. B. beim
 *   Hinzufügen oder Entfernen von Elementen.
 * - Er bietet Methoden zur Überprüfung, ob an bestimmten Positionen beispielsweise Gras wächst
 *   oder ein Kalb steht.
 *
 * Diese Klasse ist Teil des Model im Model-View-Controller (MVC)-Entwurfsmuster. Die visuelle
 * Darstellung eines Ackers erfolgt typischerweise über [herdenmanagement.view.AckerView].
 *
 * Didaktische Hinweise:
 * - **Observer-Muster:** Die Klasse erweitert [BeobachtbaresElement] und zeigt, wie Zustandsänderungen
 *   mittels PropertyChangeListener propagiert werden.
 * - **Kotlin-Syntax:** Die Verwendung von Konstruktorüberladungen, Properties mit benutzerdefinierten
 *   Settern und die Nutzung von Lambda-Ausdrücken (z. B. in [entferneRinder]) werden hier anschaulich dargestellt.
 * - **Markdown in KDoc:** Formatierungen wie **Sichtbarkeiten:** werden gemäß der KDoc-Dokumentation
 *   als fett gedruckter Text dargestellt.
 *
 * @constructor Erzeugt einen Acker ohne initiale Größenangaben. Für die explizite Definition der Größe
 *            sollte der sekundäre Konstruktor genutzt werden.
 * @property spalten Anzahl der Spalten des Ackers (Standard: 3)
 * @property zeilen Anzahl der Zeilen des Ackers (Standard: 3)
 *
 * @author Steffen Greiffenberg
 */
class Acker() : BeobachtbaresElement() {

    /**
     * Sekundärer Konstruktor, der einen Acker mit einer vorgegebenen Anzahl an Spalten und Zeilen erzeugt.
     *
     * @param spalten Anzahl der Spalten auf dem Acker
     * @param zeilen Anzahl der Zeilen auf dem Acker
     */
    constructor(spalten: Int, zeilen: Int) : this() {
        this.spalten = spalten
        this.zeilen = zeilen
    }

    /**
     * Liste der [Rindvieh]-Instanzen, die derzeit auf dem Acker weiden.
     *
     * Dieses Property zeigt den Einsatz von mutable Listen in Kotlin zur Verwaltung von Objekten.
     */
    val viecher = mutableListOf<Rindvieh>()

    /**
     * Liste der [Gras]-Instanzen, die auf dem Acker wachsen.
     *
     * Demonstriert, wie Objekte zur Laufzeit hinzugefügt und entfernt werden können.
     */
    val graeser = mutableListOf<Gras>()

    /**
     * Liste der [Kalb]-Instanzen, die auf dem Acker platziert sind.
     */
    val kaelber = mutableListOf<Kalb>()

    /**
     * Steuert die Art der Animation, mit der Änderungen auf dem Acker visualisiert werden.
     *
     * Mögliche Werte sind [Threading.SYNCHRONOUS], [Threading.ASYNCHRONOUS] und
     * [Threading.ASYNCHRONOUS_NO_WAIT].
     *
     * Beim Ändern dieser Eigenschaft werden die Observer über die Änderung informiert.
     */
    var animation: Threading = Threading.ASYNCHRONOUS_NO_WAIT
        set(value) {
            val oldThreading = field
            field = value
            informiereBeobachter(Keys.PROPERTY_THREADING, oldThreading, value)
        }

    /**
     * Anzahl der Spalten des Ackers.
     *
     * Beim Ändern wird ein Zustandswechsel kommuniziert, sodass die Observer informiert werden.
     */
    var spalten: Int = 3
        set(value) {
            val oldValue = field
            field = value
            informiereBeobachter(Keys.PROPERTY_SIZE, oldValue, value)
        }

    /**
     * Anzahl der Zeilen des Ackers.
     *
     * Wie bei [spalten] wird hier bei einer Änderung der Observer benachrichtigt.
     */
    var zeilen: Int = 3
        set(value) {
            val oldValue = field
            field = value
            informiereBeobachter(Keys.PROPERTY_SIZE, oldValue, value)
        }

    /**
     * Lässt an der angegebenen [position] Gras wachsen.
     *
     * Es wird eine neue [Gras]-Instanz erzeugt, diese auf dem Acker positioniert und in
     * die Liste [graeser] eingefügt. Anschließend werden die Observer über diese Änderung informiert.
     *
     * @param position Die Position, an der das Gras wachsen soll.
     * @return Die erzeugte [Gras]-Instanz, die auf dem Acker eingefügt wurde.
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
     * Erzeugt ein neues [Rindvieh] mit dem angegebenen Namen und platziert es auf dem Acker.
     *
     * Die Methode demonstriert den Einsatz der [apply]-Funktion in Kotlin, um Initialisierungen
     * kompakt durchzuführen.
     *
     * @param name Der Name des Rindviehs, das künftig auf dem Acker weiden soll.
     * @return Das auf dem Acker platzierte [Rindvieh]-Objekt.
     */
    fun lassRindWeiden(name: String): Rindvieh =
        Rindvieh(name).apply { lassRindWeiden(this) }

    /**
     * Platziert ein bereits existierendes [Rindvieh] auf dem Acker.
     *
     * Hier wird die [also]-Methode verwendet, um das Objekt zu konfigurieren und anschließend
     * in die Liste [viecher] aufzunehmen.
     *
     * @param rind Das [Rindvieh], das auf dem Acker weiden soll.
     */
    fun lassRindWeiden(rind: Rindvieh) = rind.also {
        it.acker = this
        viecher.add(it)
        informiereBeobachter(Keys.PROPERTY_VIECHER, null, it)
    }

    /**
     * Platziert ein [Kalb] an der angegebenen [position] auf dem Acker.
     *
     * Die Methode nutzt die [also]-Funktion, um die Initialisierung und Platzierung des
     * Kalbes kompakt darzustellen und die Observer über den Vorgang zu informieren.
     *
     * @param position Die Position, an der das Kalb platziert werden soll.
     * @return Die neu erstellte [Kalb]-Instanz.
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
     * Prüft, ob an der angegebenen [position] ein [Kalb] platziert ist.
     *
     * @param position Die zu prüfende Position.
     * @return true, wenn an der Position ein [Kalb] gefunden wurde, andernfalls false.
     */
    fun istDaEinKalb(position: Position): Boolean = kaelber.any { it.position == position }

    /**
     * Prüft, ob an der angegebenen [position] Gras wächst.
     *
     * @param position Die zu prüfende Position.
     * @return true, wenn an der Position ein [Gras] gefunden wurde, andernfalls false.
     */
    fun istDaGras(position: Position): Boolean = graeser.any { it.position == position }

    /**
     * Entfernt das erste [Gras]-Objekt, das an der angegebenen [position] gefunden wird.
     *
     * Die Implementierung nutzt [firstOrNull] zur Suche und [let], um die Entfernung und
     * Benachrichtigung der Observer zu realisieren.
     *
     * @param position Die zu prüfende Position, an der das Gras entfernt werden soll.
     */
    fun entferneGras(position: Position) {
        graeser.firstOrNull { it.position == position }?.let {
            graeser.remove(it)
            informiereBeobachter(Keys.PROPERTY_GRAESER, it, null)
        }
    }

    /**
     * Entfernt das angegebene [Rindvieh] vom Acker, falls es dort vorhanden ist.
     *
     * Mit der Verwendung von [find] und [let] wird das Rindvieh gesucht und nach erfolgreicher
     * Suche entfernt. Anschließend werden die Observer informiert.
     *
     * @param rind Das [Rindvieh], das entfernt werden soll.
     */
    fun entferneRind(rind: Rindvieh) {
        viecher.find { it == rind }?.let {
            viecher.remove(it)
            informiereBeobachter(Keys.PROPERTY_VIECHER, it, null)
        }
    }

    /**
     * Entfernt alle [Rindvieh]-Objekte vom Acker.
     *
     * Hier wird demonstriert, wie die [forEach]-Methode in Verbindung mit einer Methodenreferenz
     * (in diesem Fall [entferneRind]) verwendet wird, um alle Elemente iterativ zu entfernen.
     *
     * Hinweis: Da [forEach] den Rückgabewert [Unit] liefert, hat diese Methode keinen "echten" Rückgabewert.
     */
    fun entferneRinder() = viecher.toList().forEach(::entferneRind)

    /**
     * Überprüft, ob die angegebene [position] innerhalb der Grenzen des Ackers liegt.
     *
     * Da sich Rinder bewegen können, ist es wichtig, dass sie den Acker nicht verlassen.
     * Diese Methode stellt sicher, dass die Zielposition gültig ist.
     *
     * @param position Die zu prüfende Position.
     * @return true, wenn die Position innerhalb der Ackergöße (basierend auf [spalten] und [zeilen])
     *         liegt, andernfalls false.
     */
    fun istGueltig(position: Position): Boolean =
        position.x in 0 until spalten && position.y in 0 until zeilen
}
