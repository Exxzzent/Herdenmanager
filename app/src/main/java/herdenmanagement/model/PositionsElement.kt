package herdenmanagement.model

/**
 * Ein Positionselement kann auf einem [Acker] positioniert werden.
 *
 *
 * Änderungen an seinen Eigenschaften kann es PropertyChangeListener senden. Hierzu
 * müssen diese den Objekten dieser Klasse mit [.fuegeBeobachterHinzu]
 * hinzugefügt werden.
 *
 *
 * Jedes PositionsElement verfügt über eine eindeutige ID.
 * Im Muster Model View Controller sind Objekte erbender Klassen Bestandteil des Model.
 *
 * @author Steffen Greiffenberg
 */
open class PositionsElement : BeobachtbaresElement(), Cloneable {

    /**
     * ID des [PositionsElement]. Wird während der Initialisierung gesetzt und sollte
     * später nicht mehr verändert werden.
     *
     *
     * Darstellende Views besitzen die selbe ID, um die Suche danach zu beschleunigen und
     * zu vereinfachen.
     */
    internal val id: Int = IDGenerator.generateId()

    /**
     * Der Acker besitzt mehrere Spalten und Zeilen. In den entstehenden Zellen wird ein
     * #Positionselement platziert.
     */
    var acker: Acker? = null

    /**
     * Position auf dem Acker x = Spalte, y = Zeile
     */
    internal var position: Position = Position(0, 0)
        /**
         * Liefert eine Kopie, damit diese manipuliert kann ohne die
         * Position von this zu ändern
         */
        get() {
            return Position(field.x, field.y)
        }
        /**
         * Die PropertyChangeListener werden informiert.
         *
         * @param position Neue Position auf dem Acker
         */
        set(value) {
            val oldElement = clone()
            field = value
            val newElement = clone()
            informiereBeobachter(Keys.PROPERTY_POSITION, oldElement, newElement)
        }

    /**
     * Bewegungen oder andere Aktionen des Elements können Nachrichten (z.B. Fehlermeldungen)
     * erzeugen. Wenn der PropertyChangeListener dem Schlüssel PROPERTY_NACHRICHT lauscht,
     * wird er über siese Nachrichten informiert.
     */
    private var nachricht: Any = ""

    /**
     * Setzen der aktuellen Nachricht. Die PropertyChangeListener werden informiert.
     * Die Ressourcen-ID muss in den Strings der App vorhanden sein. Mit dieser Methoden werden
     * keine Integer Werte angezeigt, sondern die Zeichenketten zur Ressourcen-ID!
     *
     * @param resourcenID Ressourcen-ID der Nachricht
     */
    protected fun zeigeNachricht(resourcenID: Int) {
        val oldElement = clone()
        nachricht = resourcenID
        val newElement = clone()
        informiereBeobachter(Keys.PROPERTY_NACHRICHT, oldElement, newElement)
    }

    /**
     * Setzen der aktuellen Nachricht. Die PropertyChangeListener werden informiert.
     *
     * @param nachricht Letzte Nachricht (Fehler- oder Vollzugsmeldung)
     */
    fun setzeNachricht(nachricht: String) {
        val oldElement = clone()
        this.nachricht = nachricht
        val newElement = clone()
        informiereBeobachter(Keys.PROPERTY_NACHRICHT, oldElement, newElement)
    }

    /**
     * @return Letzte Nachricht
     */
    fun gibNachricht(): Any {
        return nachricht
    }

    /**
     * @return Name des Positionselements, in der regel der Name der Klasse
     */
    open val name: String
        get() {
            return javaClass.simpleName
        }

    fun copy(): PositionsElement {
        return clone() as PositionsElement
    }
}