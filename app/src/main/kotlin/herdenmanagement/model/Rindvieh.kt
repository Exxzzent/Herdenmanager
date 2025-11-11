package herdenmanagement.model

import de.dhsn.herdenmanagement.R

/**
 * Repräsentiert ein Rindvieh, das sich auf einem [Acker] bewegen kann.
 *
 * Diese Klasse erweitert [PositionsElement] und ist Teil des Model im Model-View-Controller (MVC)-Entwurfsmuster.
 * Neben der grundlegenden Eigenschaft einer Position besitzt ein Rindvieh auch eine Blickrichtung, einen Status
 * und einen Milchbestand im Euter. Änderungen an diesen Eigenschaften werden über das Observer-Muster an registrierte
 * Beobachter kommuniziert.
 *
 * Didaktische Hinweise:
 * - **Vererbung und Zustandsänderung:** Durch die Erweiterung von [PositionsElement] werden grundlegende Mechanismen
 *   (wie ID, Position und Observer-Benachrichtigung) übernommen. Zusätzliche Eigenschaften wie [richtung], [status] und
 *   [milchImEuter] zeigen, wie komplexere Zustände modelliert werden können.
 * - **Beobachter-Muster:** Jeder relevante Zustand, der sich ändert (z. B. Richtung, Milchmenge oder Status), löst
 *   eine Benachrichtigung der Observer aus. Dies wird durch das Klonen des Objekts vor und nach der Änderung demonstriert.
 * - **Kotlin-spezifische Idiome:** Methoden wie [also] und der Einsatz von benutzerdefinierten Getter/Setter werden genutzt,
 *   um den Code prägnant und ausdrucksstark zu halten.
 *
 * @constructor Erzeugt ein Rindvieh mit dem angegebenen [name]. Kühe können nur bei ihrer Erzeugung benannt werden,
 * später ist ein Umbenennen nicht möglich.
 * @param name Name des Rindviehs.
 *
 * @author Steffen Greiffenberg
 */
open class Rindvieh(override val name: String) : PositionsElement() {

    /**
     * Die Blickrichtung der Kuh. Standardmäßig schaut sie nach Osten.
     *
     * Beim Ändern der Richtung wird das Objekt vor und nach der Änderung geklont, um
     * den Zustand der Kuh an die Observer zu übermitteln.
     *
     * Beispiel: Um die Kuh nach links zu drehen, kann folgender Code verwendet werden:
     * `rind.richtung = rind.richtung.links`
     */
    var richtung: Richtung = Richtung.OST
        set(value) {
            val oldRindvieh = clone()
            field = value
            val newRindvieh = clone()
            informiereBeobachter(Keys.PROPERTY_RICHTUNG, oldRindvieh, newRindvieh)
        }

    /**
     * Anzahl (in Litern) der Milch im Euter.
     *
     * Die Milchmenge wird durch [frissGras] erhöht und durch [gibMilch] reduziert.
     * Das Melken funktioniert nur, wenn an der aktuellen Position ein [Kalb] vorhanden ist.
     *
     * Die Änderung des Milchbestands wird an die Observer kommuniziert.
     */
    private var milchImEuter: Int = 0
        set(value) {
            val oldRindvieh = clone()
            field = value
            val newRindvieh = clone()
            informiereBeobachter(Keys.PROPERTY_MILCH, oldRindvieh, newRindvieh)
        }

    /**
     * Der aktuelle Status der Kuh. Mögliche Werte sind [Status.WARTET], [Status.FRISST] und [Status.RAUCHT].
     *
     * Änderungen am Status erfolgen ausschließlich über interne Methoden (z. B. [frissGras] oder [gibMilch]),
     * und werden über das Observer-Muster an die registrierten Beobachter kommuniziert.
     */
    var status: Status = Status.WARTET
        private set(value) {
            val oldRindvieh = clone()
            field = value
            val newRindvieh = clone()
            informiereBeobachter(Keys.PROPERTY_STATUS, oldRindvieh, newRindvieh)
        }

    /**
     * Mögliche Zustände der Kuh, die auch für die Darstellung von Bildern relevant sind.
     */
    enum class Status {
        WARTET, FRISST, RAUCHT
    }

    /**
     * Bewegt die Kuh in Blickrichtung ([richtung]).
     *
     * Die Bewegung erfolgt nur, wenn vor der Kuh auf dem [Acker] ein gültiges Feld existiert.
     * Andernfalls wird eine Fehlermeldung über [PositionsElement.zeigeNachricht] angezeigt.
     */
    open fun geheVor() {
        if (gehtsDaWeiterVor) {
            position = position.naechste(richtung)
        } else {
            zeigeNachricht(R.string.rindvieh_vor_mir_kein_acker)
        }
    }

    /**
     * Bewegt die Kuh entgegen ihrer aktuellen Blickrichtung.
     *
     * Diese Methode wird umgangssprachlich auch als "rückwärtige Bewegung" bezeichnet. Auch hier
     * wird sichergestellt, dass die Bewegung innerhalb der Grenzen des Ackers erfolgt.
     * Andernfalls wird eine Fehlermeldung angezeigt.
     */
    fun geheZurueck() {
        if (gehtsDaWeiterZurueck) {
            position = position.naechste(richtung.umgekehrt)
        } else {
            zeigeNachricht(R.string.rindvieh_hinter_mir_kein_acker)
        }
    }

    /**
     * Dreht die Kuh um 90° nach links.
     *
     * Die Methode ändert ausschließlich die [richtung], ohne die [position] der Kuh zu beeinflussen.
     */
    fun dreheDichLinksRum() {
        richtung = richtung.links
    }

    /**
     * Dreht die Kuh um 90° nach rechts.
     *
     * Auch hier ändert sich nur die [richtung], während die [position] unverändert bleibt.
     */
    fun dreheDichRechtsRum() {
        richtung = richtung.rechts
    }

    /**
     * Simuliert das Rauchen von Gras durch die Kuh.
     *
     * Dabei wird geprüft, ob an der aktuellen Position [Gras] vorhanden ist. Ist dies der Fall,
     * ändert die Kuh ihren Status vorübergehend auf [Status.RAUCHT], und das Gras wird vom [Acker] entfernt.
     * Nach dem Rauchen wechselt der Status zurück zu [Status.WARTET].
     */
    fun raucheGras() {
        if (acker.istDaGras(position)) {
            status = Status.RAUCHT
            acker.entferneGras(position)
            status = Status.WARTET
        } else {
            zeigeNachricht(R.string.rindvieh_nix_zu_rauchen)
        }
    }

    /**
     * Simuliert das Fressen von Gras durch die Kuh.
     *
     * Zunächst wird geprüft, ob an der aktuellen [position] [Gras] vorhanden ist. Falls ja,
     * wechselt der Status der Kuh zu [Status.FRISST], der Milchbestand ([milchImEuter]) wird um 1 erhöht,
     * und das Gras wird vom [Acker] entfernt. Anschließend kehrt der Status zu [Status.WARTET] zurück.
     *
     * Ist an der aktuellen Position kein Gras vorhanden, wird eine Fehlermeldung angezeigt.
     */
    fun frissGras() {
        if (acker.istDaGras(position)) {
            status = Status.FRISST
            milchImEuter += 1
            acker.entferneGras(position)
            status = Status.WARTET
        } else {
            zeigeNachricht(R.string.rindvieh_kein_gras)
        }
    }

    /**
     * Melkt die Kuh, falls an der aktuellen Position ein [Kalb] steht.
     *
     * Wird erfolgreich gemolken, wird die Milchmenge ([milchImEuter]) auf 0 gesetzt und der vorherige
     * Milchbestand zurückgegeben. Wird versucht, eine Kuh ohne Milch zu melken, wird eine Fehlermeldung ausgegeben.
     *
     * Die Methode demonstriert den Einsatz der [also]-Funktion, bei der vor der Rückgabe der Milchbestand
     * auf 0 gesetzt wird.
     *
     * @return Die Menge der Milch (in Litern), die vor dem Melken im Euter war.
     */
    fun gibMilch(): Int {
        if (acker.istDaEinKalb(position)) {
            if (istMilchImEuter) {
                return milchImEuter.also { milchImEuter = 0 }
            }
            zeigeNachricht(R.string.rindvieh_erst_fressen)
        } else {
            zeigeNachricht(R.string.rindvieh_kein_kalb)
        }
        return 0
    }

    /**
     * Gibt an, ob aktuell Milch im Euter vorhanden ist.
     *
     * @return true, wenn [milchImEuter] größer als 0 ist, andernfalls false.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val istMilchImEuter: Boolean
        get() = milchImEuter > 0

    /**
     * Prüft, ob die Bewegung nach vorne (in Blickrichtung) innerhalb der Ackergöße möglich ist.
     *
     * Die Prüfung erfolgt über die Methode [Acker.istGueltig] anhand der nächsten Position.
     *
     * @return true, wenn eine Bewegung nach vorne möglich ist.
     */
    val gehtsDaWeiterVor: Boolean
        get() = acker.istGueltig(position.naechste(richtung))

    /**
     * Prüft, ob die Bewegung nach hinten (entgegen der Blickrichtung) innerhalb der Ackergöße möglich ist.
     *
     * Die Prüfung erfolgt über die Methode [Acker.istGueltig] anhand der Position, die sich aus
     * einer Umkehrung der aktuellen Blickrichtung ergibt.
     *
     * @return true, wenn eine Bewegung nach hinten möglich ist.
     */
    val gehtsDaWeiterZurueck: Boolean
        get() = acker.istGueltig(position.naechste(richtung.umgekehrt))
}
