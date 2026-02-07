package herdenmanagement.model

import de.dhsn.herdenmanagement.R

/**
 * Repräsentiert ein tanzendes Rind, das von der Klasse [Rindvieh] erbt.
 *
 * Diese Klasse dient als Template für eine Übungsaufgabe. Die Methoden und Eigenschaften,
 * die hier deklariert sind, sollen mit konkreter Funktionalität gefüllt werden.
 * Dabei können Sie experimentieren und eigene Logik implementieren, um Bewegungen
 * und Tanzschritte zu simulieren.
 *
 * Didaktische Hinweise:
 * - **Erweiterung von Basisklassen:** [TanzRind] erbt von [Rindvieh] und übernimmt damit grundlegende Eigenschaften
 *   wie Position, Blickrichtung und Observer-Benachrichtigungen. Dies zeigt, wie Klassenhierarchien in Kotlin aufgebaut werden.
 * - **Template-Methoden:** Methoden wie [geheSeitwaertsNachLinks] und [geheSeitwaertsNachRechts] sind bewusst leer gelassen,
 *   um den Studenten Raum für eigene Implementierungen zu bieten.
 * - **Sequenzielle Abläufe:** Die Methode [chaChaCha] demonstriert, wie verschiedene Bewegungsfunktionen zu einer Tanzsequenz
 *   kombiniert werden können. Hierbei können die Studenten den Ablauf kreativ erweitern und verfeinern.
 *
 * @constructor Erzeugt ein TanzRind mit dem angegebenen [name]. Der Name wird an die Basisklasse [Rindvieh] übergeben.
 *
 * @author Ihr Name
 */
class TanzRind(name: String) : Rindvieh(name) {

    /**
     * Bewegt das TanzRind seitwärts nach links.
     *
     * Diese Methode ist als Platzhalter gedacht und soll von den Studenten mit eigener Logik befüllt werden.
     */
    var neueRichtung: Richtung = Richtung.NORD
        set(value) {
            val oldRindvieh = clone()
            field = value
            val newRindvieh = clone()
            informiereBeobachter(Keys.PROPERTY_RICHTUNG, oldRindvieh, newRindvieh)
        }
    fun geheSeitwaertsNachLinks() {
        if (gehtsDaWeiterVor) {
            position = position.naechste(neueRichtung)
        } else {
            zeigeNachricht(R.string.rindvieh_vor_mir_kein_acker)
        }
    }

    /**
     * Bewegt das TanzRind seitwärts nach rechts.
     *
     * Auch diese Methode ist ein Template und wartet auf eine konkrete Implementierung durch die Studenten.
     */
    fun geheSeitwaertsNachRechts() {
        // TODO: Implementiere die Logik für die seitliche Bewegung nach rechts.
    }

    /**
     * Prüft, ob eine seitliche Bewegung nach links möglich ist.
     *
     * Der Rückgabewert ist hier vorläufig auf true gesetzt. Studenten sollen diese Logik
     * anpassen, um tatsächliche Randbedingungen zu berücksichtigen.
     *
     * @return true, wenn eine Bewegung nach links möglich ist, andernfalls false.
     */
    val gehtsDaLinksWeiter: Boolean
        get() = true // TODO: Ersetze diese Platzhalter-Logik durch eine echte Prüfung.

    /**
     * Prüft, ob eine seitliche Bewegung nach rechts möglich ist.
     *
     * Der Rückgabewert ist vorläufig auf true gesetzt. Die Studenten sollen diese Methode anpassen,
     * um den Bewegungsraum korrekt zu validieren.
     *
     * @return true, wenn eine Bewegung nach rechts möglich ist, andernfalls false.
     */
    val gehtsDaRechtsWeiter: Boolean
        get() = true // TODO: Ersetze diese Platzhalter-Logik durch eine echte Prüfung.

    /**
     * Führt eine Tanzsequenz namens "Cha-Cha-Cha" aus.
     *
     * Diese Methode kombiniert verschiedene Bewegungen (vorwärts, rückwärts und seitwärts), um
     * eine Tanzabfolge zu simulieren. Die Sequenz kann als Vorlage genutzt werden, um eigene
     * Bewegungsmuster zu entwickeln.
     */
    fun chaChaCha() {
        geheVor()
        geheZurueck()
        geheSeitwaertsNachLinks()
        geheSeitwaertsNachLinks()
        geheZurueck()
        geheVor()
        geheSeitwaertsNachRechts()
        geheSeitwaertsNachRechts()
    }
}
