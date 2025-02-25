package herdenmanagement.model

/**
 * Repräsentiert eine Position im zweidimensionalen Raum mit X- und Y-Koordinaten.
 *
 * Objekte der Klasse [PositionsElement] nutzen diese Klasse, um ihre Position innerhalb eines Ackers
 * festzulegen. Während Positionen in anderen Kontexten vielfältige Bedeutungen haben können, dient
 * diese Implementierung primär zur Veranschaulichung grundlegender Konzepte in Kotlin.
 *
 * Didaktische Hinweise:
 * - **Data Class:** Durch die Verwendung einer [data class] werden automatisch sinnvolle Implementierungen
 *   der Methoden [equals], [hashCode] und [toString] generiert, was den Fokus auf die Datenhaltung legt.
 * - **Mutabilität:** Die Eigenschaften [x] und [y] sind mutable, um Zustandsänderungen zu demonstrieren.
 * - **Funktionaler Stil:** Die Methode [naechste] zeigt, wie man durch Kopieren und anschließendes Modifizieren
 *   eines Objekts eine neue Instanz erzeugt.
 *
 * @constructor Erzeugt eine Position mit den angegebenen Koordinaten.
 * @property x Die X-Koordinate.
 * @property y Die Y-Koordinate.
 *
 * @author Steffen Greiffenberg
 */
data class Position(var x: Int, var y: Int) {

    /**
     * Berechnet die nächste Position basierend auf der angegebenen [richtung].
     *
     * Die Methode erstellt eine Kopie der aktuellen Position und modifiziert diese entsprechend:
     * - Bei [Richtung.NORD] wird die Y-Koordinate um 1 dekrementiert.
     * - Bei [Richtung.OST] wird die X-Koordinate um 1 inkrementiert.
     * - Bei [Richtung.SUED] wird die Y-Koordinate um 1 inkrementiert.
     * - Bei [Richtung.WEST] wird die X-Koordinate um 1 dekrementiert.
     *
     * Didaktischer Hinweis:
     * - Der Einsatz des `when`-Ausdrucks demonstriert in Kotlin eine elegante Alternative zu if-else-Konstrukten.
     * - Durch das Kopieren und Modifizieren der Position wird ein funktionaler Programmieransatz unterstützt.
     *
     * @param richtung Die Richtung, in die sich die Position bewegen soll.
     * @return Eine neue [Position], die die veränderte Koordinate repräsentiert.
     */
    fun naechste(richtung: Richtung): Position {
        val position = Position(x, y)
        when (richtung) {
            Richtung.NORD -> position.y -= 1
            Richtung.OST -> position.x += 1
            Richtung.SUED -> position.y += 1
            Richtung.WEST -> position.x -= 1
        }
        return position
    }
}
