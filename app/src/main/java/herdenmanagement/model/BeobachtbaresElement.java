package herdenmanagement.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Beobachtabere (engl. observable) Objekte haben Beobachter (engl. Obserserver), die über die
 * Änderungen an den Eigenschaften des Objekts informiert werden.
 * <p>
 * Wikipedia beschreibt dies so: Das beobachtete Objekt bietet einen Mechanismus, um
 * Beobachter an- und abzumelden und diese über Änderungen zu informieren. Es kennt alle
 * seine Beobachter nur über die (überschaubare) Schnittstelle Beobachter. Änderungen
 * werden völlig unspezifisch zwischen dem beobachteten Objekt und jedem angemeldeten
 * Beobachter ausgetauscht. Dieses braucht also die weitere Struktur dieser Komponenten
 * nicht zu kennen. Die Beobachter implementieren ihrerseits eine (spezifische) Methode,
 * um auf die Änderung zu reagieren.
 * <p>
 * In Java existiert bereits eine Basiklasse {@link java.util.Observable}, die über eine
 * Lise an {@link java.util.Observer} Objekten verfügt. Leider werden von diesen Objekten nicht
 * die Art der Änderung propagiert.
 * <p>
 * Für das Herdenmanagement besser geeignet ist deshalb das Interface
 * {@link java.beans.PropertyChangeListener}, die von den Obserser-Objekten implementiert wird.
 * Eine Liste dieser Objekte wird von dieser Klasse verwaltet. An diese werden bei Änderungen
 * Instanzen von {@link PropertyChangeEvent} gesendet. Diese beinhalten den alten und neuen Wert,
 * den Namen der Eigenschaft und das geänderte Objekt selbst (also stets #this).
 *
 * @author Steffen Greiffenberg
 */
public class BeobachtbaresElement {

    /**
     * Liste mit Objekten der Klasse PropertyChangeListener. Die Listener werden informiert, wenn
     * für die Properties der Klasse neue Eigenschaften gesetzt werden.
     */
    private final List<PropertyChangeListener> listeners;

    /**
     * Erzeugt die Liste der Beobachter. Diese werden können erst später hinzugefügt werden,
     * die die beobachteten Objekten ihre Beobachter zur Laufzeit noch nicht kennen.
     */
    public BeobachtbaresElement() {
        listeners = new ArrayList<>();
    }

    /**
     * Informiert alle Elemente in #listeners
     *
     * @param property Konstante, wie {@link PositionsElement#PROPERTY_NACHRICHT} oder {@link PositionsElement#PROPERTY_POSITION}
     * @param oldValue Alter Wert
     * @param newValue Neuer Wert
     */
    protected void informiereBeobachter(String property, Object oldValue, Object newValue) {
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
        }
    }

    /**
     * @param newListener PropertyChangeListener, der über Änderungen am Objekt informiert wird
     */
    public void fuegeBeobachterHinzu(PropertyChangeListener newListener) {
        listeners.add(newListener);
    }

    /**
     * @param newListener PropertyChangeListener, der zukünftig nicht mehr informiert wird
     */
    public void entferneBeobachter(PropertyChangeListener newListener) {
        listeners.remove(newListener);
    }
}
