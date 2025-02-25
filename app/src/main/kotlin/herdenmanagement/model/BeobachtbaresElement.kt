package herdenmanagement.model

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

/**
 * Repräsentiert ein beobachtbares Element im Sinne des Observer-Musters.
 *
 * Das Observer-Muster ermöglicht es, dass ein Objekt (das beobachtbare Element) seine
 * interessierten Beobachter (Listener) automatisch benachrichtigt, wenn sich sein Zustand
 * ändert. Dieses Muster fördert die lose Kopplung zwischen Komponenten, da das beobachtete
 * Objekt nicht direkt wissen muss, welche spezifischen Aktionen die Beobachter bei einer
 * Änderung ausführen.
 *
 * In dieser Implementierung werden die Observer mithilfe des Interfaces [PropertyChangeListener]
 * realisiert. Änderungen werden über [PropertyChangeEvent] an die Listener kommuniziert.
 *
 * Didaktische Hinweise:
 * - **Entwurfsmuster:** Diese Klasse dient als Beispiel für das Observer-Muster, welches
 *   in vielen Programmiersprachen und Frameworks zur Anwendung kommt.
 * - **Kotlin-Syntax:** Es wird gezeigt, wie in Kotlin Listen zur Verwaltung von Beobachtern
 *   verwendet werden, und wie Lambda-Ausdrücke (z. B. mit der [forEach]-Methode) eingesetzt werden.
 * - **Sichtbarkeiten:** Der Einsatz von [private] und [protected] illustriert, wie man den
 *   Zugriff auf interne Zustände kontrolliert.
 *
 * @author Steffen Greiffenberg
 */
open class BeobachtbaresElement {

    /**
     * Eine mutable Liste von [PropertyChangeListener], die als Beobachter dieses Objekts fungieren.
     *
     * Diese Liste speichert alle Listener, die über Änderungen der Eigenschaften dieses Objekts
     * benachrichtigt werden sollen. Das Hinzufügen und Entfernen von Listenern erfolgt über
     * die Methoden [fuegeBeobachterHinzu] und [entferneBeobachter].
     */
    private val listeners = mutableListOf<PropertyChangeListener>()

    /**
     * Informiert alle registrierten Beobachter über eine Änderung einer Eigenschaft.
     *
     * Für jeden registrierten Listener wird ein [PropertyChangeEvent] erzeugt, das folgende Informationen enthält:
     * - Den Namen der geänderten Eigenschaft (z. B. [Keys.PROPERTY_NACHRICHT] oder [Keys.PROPERTY_POSITION])
     * - Den alten Wert der Eigenschaft
     * - Den neuen Wert der Eigenschaft
     * - Eine Referenz auf das beobachtete Objekt (this)
     *
     * Didaktischer Hinweis:
     * - Diese Methode demonstriert die idiomatische Verwendung von Kotlin-Funktionen wie [forEach] in Kombination
     *   mit Lambda-Ausdrücken zur Iteration über Sammlungen.
     *
     * @param property Der Name der geänderten Eigenschaft
     * @param oldValue Der alte Wert der Eigenschaft
     * @param newValue Der neue Wert der Eigenschaft
     */
    protected fun informiereBeobachter(property: String?, oldValue: Any?, newValue: Any?) {
        listeners.forEach { listener ->
            listener.propertyChange(PropertyChangeEvent(this, property, oldValue, newValue))
        }
    }

    /**
     * Fügt einen neuen [PropertyChangeListener] als Beobachter hinzu.
     *
     * Nach dem Hinzufügen wird der Listener bei jeder Änderung der Eigenschaften dieses Objekts
     * benachrichtigt.
     *
     * @param listener Der [PropertyChangeListener], der über Änderungen informiert werden soll.
     */
    fun fuegeBeobachterHinzu(listener: PropertyChangeListener) {
        listeners.add(listener)
    }

    /**
     * Entfernt einen existierenden [PropertyChangeListener] aus der Beobachterliste.
     *
     * Nach dem Entfernen wird der Listener nicht mehr über zukünftige Änderungen benachrichtigt.
     *
     * @param listener Der [PropertyChangeListener], der nicht länger informiert werden soll.
     */
    fun entferneBeobachter(listener: PropertyChangeListener) {
        listeners.remove(listener)
    }
}
