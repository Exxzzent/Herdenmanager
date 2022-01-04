package herdenmanagement.model;

import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ein Positionselement kann auf einem {@link Acker} positioniert werden.
 * <p>
 * Änderungen an seinen Eigenschaften kann es PropertyChangeListener senden. Hierzu
 * müssen diese den Objekten dieser Klasse mit {@link #fuegeBeobachterHinzu(PropertyChangeListener)}
 * hinzugefügt werden.
 * <p>
 * Jedes PositionsElement verfügt über eine eindeutige ID.
 * Im Muster Model View Controller sind Objekte erbender Klassen Bestandteil des Model.
 *
 * @author Steffen Greiffenberg
 */
public class PositionsElement extends BeobachtbaresElement {

    /**
     * Schlüssel zur Kommunikation mit einem {@link PropertyChangeListener}.
     * Der Schlüssel wird als property der Methode {@link #informiereBeobachter(String, Object, Object)}
     * übergeben.
     * <p>
     * Der Schlüsel dient für Nachrichten zum Property {@link #nachricht}.
     */
    public final static String PROPERTY_NACHRICHT = "herdenmanagement.model.PositionsElement.nachricht";

    /**
     * Schlüssel zur Kommunikation mit einem {@link PropertyChangeListener}.
     * Der Schlüssel wird als property der Methode {@link #informiereBeobachter(String, Object, Object)}
     * übergeben.
     * <p>
     * Der Schlüssel dient für Nachrichten zum Property {@link #position}.
     */
    public final static String PROPERTY_POSITION = "herdenmanagement.model.PositionsElement.position";

    /**
     * Zahlenwert der automatisch aktualisiert wird. Damit erhält jedes Elememt eine eindeutige ID.
     */
    private static final AtomicInteger nextGeneratedId = new AtomicInteger(1);

    /**
     * Erzeugt eine neue ID. Der Wert in {@link #nextGeneratedId} wird hierfür hochgezählt
     *
     * @return generierte ID
     */
    private static int generateId() {
        for (; ; ) {
            final int result = nextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (nextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * ID des {@link PositionsElement}. Wird während der Initialisierung gesetzt und sollte
     * später nicht mehr verändert werden.
     * <p>
     * Darstellende Views besitzen die selbe ID, um die Suche danach zu beschleunigen und
     * zu vereinfachen.
     */
    private final int id;

    /**
     * Der Acker besitzt mehrere Spalten und Zeilen. In den entstehenden Zellen wird ein
     * #Positionselement platziert.
     */
    private Acker acker;

    /**
     * Position auf dem Acker x = Spalte, y = Zeile
     */
    private Position position;

    /**
     * Bewegungen oder andere Aktionen des Elements können Nachrichten (z.B. Fehlermeldungen)
     * erzeugen. Wenn der PropertyChangeListener dem Schlüssel PROPERTY_NACHRICHT lauscht,
     * wird er über siese Nachrichten informiert.
     */
    private Object nachricht;

    /**
     * Setzt die ID und positioniert das Element bei 0:0. Ein Acker wird noch nicht gesetzt.
     */
    public PositionsElement() {
        id = generateId();
        position = new Position(0, 0);
        nachricht = "";
        acker = null;
    }

    /**
     * @return Eindeutige ID des Elements
     */
    public int gibId() {
        return id;
    }

    /**
     * Setzen der aktuellen Nachricht. Die PropertyChangeListener werden informiert.
     * Die Ressourcen-ID muss in den Strings der App vorhanden sein. Mit dieser Methoden werden
     * keine Integer Werte angezeigt, sondern die Zeichenketten zur Ressourcen-ID!
     *
     * @param resourcenID Ressourcen-ID der Nachricht
     */
    protected void zeigeNachricht(int resourcenID) {
        PositionsElement oldElement = kopiere();
        this.nachricht = resourcenID;
        PositionsElement newElement = kopiere();

        informiereBeobachter(PROPERTY_NACHRICHT, oldElement, newElement);
    }

    /**
     * Setzen der aktuellen Nachricht. Die PropertyChangeListener werden informiert.
     *
     * @param nachricht Letzte Nachricht (Fehler- oder Vollzugsmeldung)
     */
    public void setzeNachricht(String nachricht) {
        PositionsElement oldElement = kopiere();
        this.nachricht = nachricht;
        PositionsElement newElement = kopiere();

        informiereBeobachter(PROPERTY_NACHRICHT, oldElement, newElement);
    }

    /**
     * @return Kopie der aktuelle Position auf dem Acker
     */
    public Position gibPosition() {
        // Kopie zurückgeben, damit diese verändert werden kann
        return new Position(position.x, position.y);
    }

    /**
     * @return Letzte Nachricht
     */
    public Object gibNachricht() {
        return nachricht;
    }

    /**
     * Die PropertyChangeListener werden informiert.
     *
     * @param position Neue Position auf dem Acker
     */
    public void setzePosition(Position position) {
        PositionsElement oldElement = kopiere();
        this.position = position;
        PositionsElement newElement = kopiere();

        informiereBeobachter(PROPERTY_POSITION, oldElement, newElement);
    }

    /**
     * Ruft {@link #setzePosition(Position)} auf
     *
     * @param x X-Koordinate der neuen Position
     * @param y Y-Koordinate der neuen Position
     */
    @Deprecated
    public void setzePosition(int x, int y) {
        setzePosition(new Position(x, y));
    }

    /**
     * @param acker Acker, auf dem das Element positioniert wird.
     */
    protected void setzeAcker(Acker acker) {
        this.acker = acker;
    }

    /**
     * @return Acker, auf dem das Element positioniert wurde.
     */
    public Acker gibAcker() {
        return acker;
    }

    /**
     * @return Name des Positionselements, in der regel der Name der Klasse
     */
    public String gibName() {
        return getClass().getSimpleName();
    }

    /**
     * @return Kopie der Kuh
     */
    public PositionsElement kopiere() {
        // Rindvieh incl. dessen Name kopieren
        PositionsElement kopie = new PositionsElement();
        kopiere(kopie);
        return kopie;
    }

    /**
     * Kopiert die eigenen Attribute in die Kopie.
     *
     * @param kopie Kopie von this
     */
    protected void kopiere(PositionsElement kopie) {
        // eigene Attribute kopieren
        kopie.acker = acker;
        kopie.nachricht = nachricht;
        kopie.position = new Position(position.x, position.y);
    }
}
