package herdenmanagement.model

/**
 * Repräsentiert ein Element, das auf einem [Acker] positioniert werden kann.
 *
 * Diese Klasse ist Bestandteil des Model im Model-View-Controller (MVC)-Entwurfsmuster und
 * demonstriert das Zusammenspiel von Observer-Muster und Cloning zur Verwaltung und Propagation
 * von Zustandsänderungen. Jeder Instanz wird eine eindeutige ID zugewiesen, die auch zur Identifikation
 * in zugehörigen Views genutzt wird.
 *
 * Didaktische Hinweise:
 * - **Observer-Muster:** Durch die Vererbung von [BeobachtbaresElement] wird gezeigt, wie Änderungen an
 *   den Eigenschaften mittels [PropertyChangeListener] an interessierte Komponenten kommuniziert werden.
 * - **Cloning:** Die Verwendung von [Cloneable] in Kombination mit dem Klonen des Objekts vor und nach
 *   einer Änderung demonstriert, wie Zustände vor und nach einem Update nachvollzogen werden können.
 * - **Kotlin-Syntax:** Besonderheiten wie `lateinit`-Variablen, benutzerdefinierte Getter/Setter und
 *   Erweiterungsmethoden werden hier praxisnah veranschaulicht.
 *
 * @author
 */
open class PositionsElement : BeobachtbaresElement(), Cloneable {

    /**
     * Eindeutige Identifikationsnummer dieses [PositionsElement].
     *
     * Die ID wird während der Initialisierung gesetzt und bleibt danach unverändert.
     * Dies erleichtert die Zuordnung zwischen Model und View, da beide dieselbe ID verwenden.
     */
    val id: Int = IDGenerator.generateId()

    /**
     * Der [Acker], auf dem dieses Element positioniert wird.
     *
     * Dieses Property wird mit [lateinit] deklariert, um anzuzeigen, dass es nach der
     * Instanziierung initialisiert werden muss. Dies ist ein Beispiel für den Umgang mit
     * nicht sofort verfügbaren Werten in Kotlin.
     */
    lateinit var acker: Acker

    /**
     * Position des Elements auf dem [Acker].
     *
     * Das Property repräsentiert die aktuelle Position als [Position] (x = Spalte, y = Zeile).
     * Der benutzerdefinierte Getter liefert eine Kopie der internen Position, um zu verhindern,
     * dass externe Änderungen den internen Zustand direkt beeinflussen.
     *
     * Der Setter klont vor und nach der Änderung das Objekt, um den Zustand vor und nach
     * der Veränderung an die PropertyChangeListener zu übermitteln.
     *
     * Didaktischer Hinweis:
     * - Zeigt die Verwendung von benutzerdefinierten Getter/Setter in Kotlin.
     * - Demonstriert, wie durch Klonen ein nachvollziehbarer Zustandswechsel dokumentiert werden kann.
     */
    var position: Position = Position(0, 0)
        get() = Position(field.x, field.y)
        set(value) {
            val oldElement = clone()
            field = value
            val newElement = clone()
            informiereBeobachter(Keys.PROPERTY_POSITION, oldElement, newElement)
        }

    /**
     * Aktuelle Nachricht, die mit diesem Element verknüpft ist.
     *
     * Aktionen wie Bewegungen oder andere Ereignisse können Nachrichten generieren,
     * die als Fehlermeldung oder Informationshinweis dienen. Beim Setzen der Nachricht werden
     * die PropertyChangeListener benachrichtigt.
     *
     * Hinweis:
     * - Der Getter liefert den aktuellen Nachrichtenwert. Das Setzen erfolgt über
     *   die Methoden [zeigeNachricht], um einen kontrollierten Änderungsprozess zu gewährleisten.
     */
    var nachricht: Any = ""
        get() = field

    /**
     * Setzt die aktuelle Nachricht anhand einer Ressourcen-ID.
     *
     * Es wird erwartet, dass die übergebene Ressourcen-ID in den App-Strings vorhanden ist.
     * Anstatt die Zahl direkt anzuzeigen, wird der zugehörige String in der Benutzeroberfläche
     * dargestellt.
     *
     * Vor und nach der Änderung wird das Objekt geklont, um den alten und neuen Zustand
     * an die PropertyChangeListener zu übermitteln.
     *
     * @param resourcenID Die Ressourcen-ID der anzuzeigenden Nachricht.
     */
    protected fun zeigeNachricht(resourcenID: Int) {
        val oldElement = clone()
        nachricht = resourcenID
        val newElement = clone()
        informiereBeobachter(Keys.PROPERTY_NACHRICHT, oldElement, newElement)
    }

    /**
     * Setzt die aktuelle Nachricht direkt als String.
     *
     * Diese Methode aktualisiert die Nachricht und informiert alle PropertyChangeListener
     * über die Zustandsänderung. Auch hier wird der alte und der neue Zustand mittels Klonen
     * dokumentiert.
     *
     * @param nachricht Die neue Nachricht, die beispielsweise als Fehler- oder Vollzugsmeldung dienen kann.
     */
    fun zeigeNachricht(nachricht: String) {
        val oldElement = clone()
        this.nachricht = nachricht
        val newElement = clone()
        informiereBeobachter(Keys.PROPERTY_NACHRICHT, oldElement, newElement)
    }

    /**
     * Liefert den Namen des PositionsElements.
     *
     * Standardmäßig entspricht dies dem einfachen Namen der Klasse, kann jedoch in abgeleiteten
     * Klassen überschrieben werden, um eine spezifischere Bezeichnung bereitzustellen.
     */
    open val name: String
        get() = javaClass.simpleName

    /**
     * Erstellt eine Kopie dieses [PositionsElement].
     *
     * Die Methode nutzt [clone] zur Erzeugung eines neuen Objekts, das den aktuellen Zustand
     * widerspiegelt. Dies kann nützlich sein, um einen "Schnappschuss" des Objekts vor einer Änderung
     * zu erstellen.
     *
     * @return Eine neue Instanz von [PositionsElement] mit identischem Zustand.
     */
    fun copy(): PositionsElement = clone() as PositionsElement
}
