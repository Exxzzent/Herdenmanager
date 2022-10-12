package herdenmanagement.model

import de.ba.herdenmanagement.R

/**
 * Ein Rindvieh kann sich auf einem [Acker] bewegen. Hierzu erbt es die Eigenschaft,
 * eine Position auf einem Acker zu besitzen von [PositionsElement]. Zusätzlich kann
 * eine Kuh diese Position aber auch ändern, zum Beispiel mit [geheVor].
 *
 * Es wird sichergestellt, dass die Kuh nicht über den Rand des Ackers hinaus gehen kann.
 * Ist ein Zielfeld der Bewegung ungültig, zeigt die Kuh eine Nachricht mittels
 * [PositionsElement.zeigeNachricht].
 *
 * Im Muster Model View Controller (MVC) sind Objekte dieser Klasse Bestandteil des Model.
 * Die beobachtete Kuh bietet einen Mechanismus, um Beobachter
 * mit [fuegeBeobachterHinzu] an- und mit [entferneBeobachter] abzumelden und diese
 * über Änderungen mittels [BeobachtbaresElement.informiereBeobachter]
 * zu informieren.
 *
 * Eigenschaften, die beobachtet werden können sind die Nachrichten des PositionsElements
 * sowie [richtung], [status] und [milchImEuter].
 *
 * @author Steffen Greiffenberg
 *
 * @constructor Erzeugt ein Rindvieh
 * @param name des Rindviehs. Kühe können nur bei ihrer Erzeugung benannt werden. Ein späteres Umbenennen ist nicht möglich.
 */
open class Rindvieh(override val name: String) : PositionsElement() {

    /**
     * Richtung der Küh. Rindiecher schauen gern nach Norden. Selten nach Süden, manchmal aber
     * eben auch nach Osten oder Westen - jenachdem, welche Richtung hier abgelegt wird.
     */
    internal var richtung: RichtungsTyp = RichtungsTyp.OST
        /**
         * Setzt die Blickrichtung der Kuh. Dies ist von außen nicht möglich, nur ein Aufruf von
         * [dreheDichLinksRum] oder [dreheDichRechtsRum] ändert die Blickrichtung.
         */
        set(value) {
            val oldRindvieh = clone()
            field = value
            val newRindvieh = clone()
            informiereBeobachter(Keys.PROPERTY_RICHTUNG, oldRindvieh, newRindvieh)
        }

    /**
     * Anzahl (Liter) Milch im Euter. Die Zahl erhöht sich durch [frissGras]. Sie
     * wird reduziert durch [gibMilch]. Das Melken funktioniert jedoch nur,
     * wenn auf dem Acker an dieser Stelle ein Kalb steht.
     */
    private var milchImEuter: Int = 0
        /**
         * Setzt den Status der Kuh. Dies ist von außen nicht möglich, nur ein Aufruf von
         * [frissGras] oder [gibMilch] ändert die Milchmenge.
         *
         * Die Observer werden bei Änderungen des Milchstandes informiert.
         */
        set(value) {
            val oldRindvieh = clone()
            field = value
            val newRindvieh = clone()
            informiereBeobachter(Keys.PROPERTY_MILCH, oldRindvieh, newRindvieh)
        }

    /**
     * Status der Kuh. Sie kann sich bewegen, fressen, rauchen oder warten.
     */
    internal var status: StatusTyp = StatusTyp.WARTET
        /**
         * Setzt den Status der Kuh. Dies ist von außen nicht möglich, nur ein Aufruf von
         * zum Beispiel [frissGras] oder [gibMilch] ändert den Status.
         *
         * Die Observer werden bei Änderungen des Stauts informiert.
         */
        set(value) {
            val oldRindvieh = clone()
            field = value
            val newRindvieh = clone()
            informiereBeobachter(Keys.PROPERTY_STATUS, oldRindvieh, newRindvieh)
        }

    /**
     * Mögliche Richtungen, in die die Kuh schauen kann, wichtig für die Anzeige von Bildern
     */
    enum class RichtungsTyp {
        NORD, OST, SUED, WEST
    }

    /**
     * Möglicher Status der Kuh, wichtig für die Anzeige von Bildern
     */
    enum class StatusTyp {
        WARTET, FRISST, RAUCHT
    }

    /**
     * Auf Basis der aktuellen Blickrichtung und Position der Kuh wird das Feld vor
     * der Kuh berechnet. Hier erfolgt noch keine Prüfung, ob diese Position auf dem [Acker]
     * auch wirklich existiert.
     */
    private val positionDavor: Position
        get() {
            val position = this.position
            when (richtung) {
                RichtungsTyp.NORD -> position.y = position.y - 1
                RichtungsTyp.OST -> position.x = position.x + 1
                RichtungsTyp.SUED -> position.y = position.y + 1
                RichtungsTyp.WEST -> position.x = position.x - 1
            }
            return position
        }

    /**
     * Auf Basis der aktuellen Blickrichtung und Position der Kuh wird das Feld vhinter
     * der Kuh berechnet. Hier erfolgt noch keine Prüfung, ob diese Position auf dem [Acker]
     * auch wirklich existiert.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val positionDahinter: Position
        get() {
            val position = this.position
            when (richtung) {
                RichtungsTyp.NORD -> position.y = position.y + 1
                RichtungsTyp.OST -> position.x = position.x - 1
                RichtungsTyp.SUED -> position.y = position.y - 1
                RichtungsTyp.WEST -> position.x = position.x + 1
            }
            return position
        }

    /**
     * Die Kuh wird in Blickrichtung (siehe [richtung]) bewegt. Die Bewegung ist nur
     * möglich, wenn vor der Kuh auf dem [Acker] noch ein Feld existiert.
     */
    open fun geheVor() {
        if (gehtsDaWeiterVor) {
            this.position = positionDavor
        } else {
            zeigeNachricht(R.string.rindvieh_vor_mir_kein_acker)
        }
    }

    /**
     * Die Kuh wird entgegen der Blickrichtung (siehe [richtung]) bewegt. Experten nennnen
     * dies eine "rückwärtige Bewegung", Kinder sagen gern auch "nach hinten". Die Bewegung ist nur
     * möglich, wenn hinter der Kuh auf dem [Acker] noch ein Feld existiert.
     */
    fun geheZurueck() {
        if (gehtsDaWeiterZurueck) {
            this.position = positionDahinter
        } else {
            zeigeNachricht(R.string.rindvieh_hinter_mir_kein_acker)
        }
    }

    /**
     * Diese Methode ändert die Blickrichtung der Kuh. Um die neue Richtung zu verstehen, muss
     * man sich in die Position der Kuh versetzen. Blickt sie momentan nach
     * [RichtungsTyp.OST], so wird sie nach dem Aufruf dieser Methoden nach
     * [RichtungsTyp.NORD] schauen.
     *
     *
     * Die [position] der Kuh ändert sich durch die Drehbewegung nicht.
     */
    fun dreheDichLinksRum() {
        richtung = when (richtung) {
            RichtungsTyp.OST -> RichtungsTyp.NORD
            RichtungsTyp.NORD -> RichtungsTyp.WEST
            RichtungsTyp.WEST -> RichtungsTyp.SUED
            RichtungsTyp.SUED -> RichtungsTyp.OST
        }
    }

    /**
     * Kühe drehen sich zwar lieber links herum, zur Not aber auch nach rechts. Diese Methode
     * ändert entsprechend die Blickrichtung der Kuh. Um die neue Richtung zu verstehen, muss
     * man sich in die Position der Kuh versetzen. Blickt sie momentan nach
     * [RichtungsTyp.OST], so wird sie nach dem Aufruf dieser Methoden nach
     * [RichtungsTyp.SUED] schauen.
     *
     *
     * Die [position] der Kuh ändert sich durch die Drehbewegung nicht.
     */
    fun dreheDichRechtsRum() {
        richtung = when (richtung) {
            RichtungsTyp.OST -> RichtungsTyp.SUED
            RichtungsTyp.SUED -> RichtungsTyp.WEST
            RichtungsTyp.WEST -> RichtungsTyp.NORD
            RichtungsTyp.NORD -> RichtungsTyp.OST
        }
    }

    /**
     * [Gras] kann man nicht nur fressen, sondern auch rauchen. Leider wird dabei keine Milch
     * erzeugt und ungesund ist es auch. Gerauchtes Gras wird vom [Acker] entfernt.
     * Während des Rauchens ist der Status der Kuh [StatusTyp.RAUCHT].
     */
    fun raucheGras() {
        if (this.acker.istDaGras(position)) {
            status = StatusTyp.RAUCHT
            this.acker.entferneGras(position)
            status = StatusTyp.WARTET
        } else {
            zeigeNachricht(R.string.rindvieh_nix_zu_rauchen)
        }
    }

    /**
     * Der Hauptzweck von [Gras] ist es, von Kühen gefressen zu werden. Die Kuh prüft
     * zunächst, ob an der aktuellen Position (Abfrage mit [position]) Gras wächst.
     * Wenn ja, wird dies gefressen (also auch vom Acker entfernt). Wenn nein, setzt die Kuh
     * eine Fehlermeldung mittels [nachricht].
     */
    fun frissGras() {
        if (this.acker.istDaGras(position)) {
            this.status = StatusTyp.FRISST
            milchImEuter += 1
            this.acker.entferneGras(position)
            this.status = StatusTyp.WARTET
        } else {
            zeigeNachricht(R.string.rindvieh_kein_gras)
        }
    }

    /**
     * Steht an der aktuellen Position auf dem [Acker] auch ein [Kalb], kann die
     * Kuh gemolken werden. Nach dem Melken ist die Anzahl in [milchImEuter] natürlich 0.
     * Soll eine Kuh ohne Milch im Euter gemolken werden, wird auch eine Fehlernachricht mittels
     * [nachricht] gespeichert.
     *
     * Die Oberserver werden über die Reduzierung der Milchmenge informiert. Zusätzlich wird
     * der Erfolg oder Nicht-Erfolg des melkens als Nachricht gespeichert.
     *
     * @return Milch, die sich im Euter befand
     */
    fun gibMilch(): Int {
        var result = milchImEuter
        if (this.acker.istDaEinKalb(position)) {
            if (istMilchImEuter) {
                milchImEuter = 0
            } else {
                zeigeNachricht(R.string.rindvieh_erst_fressen)
            }
        } else {
            result = 0
            zeigeNachricht(R.string.rindvieh_kein_kalb)
        }
        return result
    }

    /**
     * @return true, wenn [milchImEuter] > 0
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val istMilchImEuter: Boolean
        get() {
            return milchImEuter > 0
        }

    /**
     * Bei der Bewegung nach vorn darf die Kuh nicht die Grenzen des Ackers
     * überschreiten. Diese werden hier geprüft. Die eigentliche Prüfung erfolgt durch
     * [Acker.istGueltig].
     *
     * @return true = Bewegung ist möglich
     */
    val gehtsDaWeiterVor: Boolean
        get() {
            return this.acker.istGueltig(positionDavor)
        }

    /**
     * Bei der Rückwärtsbewegung darf die Kuh nicht die Grenzen des Ackers
     * überschreiten. Diese Grenzen werden hier geprüft. Die eigentliche Prüfung erfolgt durch
     * [Acker.istGueltig].
     *
     * @return true = Bewegung ist möglich
     */
    val gehtsDaWeiterZurueck: Boolean
        get() {
            return this.acker.istGueltig(positionDahinter)
        }
}