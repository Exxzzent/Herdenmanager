package herdenmanagement.model

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.util.ArrayList

/**
 * Beobachtabere (engl. observable) Objekte haben Beobachter (engl. Obserserver), die über die
 * Änderungen an den Eigenschaften des Objekts informiert werden.
 *
 *
 * Wikipedia beschreibt dies so: Das beobachtete Objekt bietet einen Mechanismus, um
 * Beobachter an- und abzumelden und diese über Änderungen zu informieren. Es kennt alle
 * seine Beobachter nur über die (überschaubare) Schnittstelle Beobachter. Änderungen
 * werden völlig unspezifisch zwischen dem beobachteten Objekt und jedem angemeldeten
 * Beobachter ausgetauscht. Dieses braucht also die weitere Struktur dieser Komponenten
 * nicht zu kennen. Die Beobachter implementieren ihrerseits eine (spezifische) Methode,
 * um auf die Änderung zu reagieren.
 *
 *
 * In Java existiert bereits eine Basiklasse [java.util.Observable], die über eine
 * Lise an [java.util.Observer] Objekten verfügt. Leider werden von diesen Objekten nicht
 * die Art der Änderung propagiert.
 *
 *
 * Für das Herdenmanagement besser geeignet ist deshalb das Interface
 * [java.beans.PropertyChangeListener], die von den Obserser-Objekten implementiert wird.
 * Eine Liste dieser Objekte wird von dieser Klasse verwaltet. An diese werden bei Änderungen
 * Instanzen von [PropertyChangeEvent] gesendet. Diese beinhalten den alten und neuen Wert,
 * den Namen der Eigenschaft und das geänderte Objekt selbst (also stets #this).
 *
 * @author Steffen Greiffenberg
 */
open class BeobachtbaresElement {

    /**
     * Liste mit Objekten der Klasse PropertyChangeListener. Die Listener werden informiert, wenn
     * für die Properties der Klasse neue Eigenschaften gesetzt werden.
     */
    private val listeners: MutableList<PropertyChangeListener> = ArrayList()

    /**
     * Informiert alle Elemente in #listeners
     *
     * @param property Konstante, wie [PositionsElement.PROPERTY_NACHRICHT] oder [PositionsElement.PROPERTY_POSITION]
     * @param oldValue Alter Wert
     * @param newValue Neuer Wert
     */
    protected fun informiereBeobachter(property: String?, oldValue: Any?, newValue: Any?) {
        for (listener in listeners) {
            listener.propertyChange(PropertyChangeEvent(this, property, oldValue, newValue))
        }
    }

    /**
     * @param listener PropertyChangeListener, der über Änderungen am Objekt informiert wird
     */
    fun fuegeBeobachterHinzu(listener: PropertyChangeListener) {
        listeners.add(listener)
    }

    /**
     * @param listener PropertyChangeListener, der zukünftig nicht mehr informiert wird
     */
    fun entferneBeobachter(listener: PropertyChangeListener) {
        listeners.remove(listener)
    }
}