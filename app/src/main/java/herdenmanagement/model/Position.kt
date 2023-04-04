package herdenmanagement.model

/**
 * Eine Position speichert eine X- und Y-Koordinate für Objekte der Klasse [PositionsElement].
 * Andere Positionen sind auch wichtig, aber nicht für Rindviecher.
 *
 * @author Steffen Greiffenberg
 *
 * @constructor Erzeugt eine Position
 * @property x X-Koordinate
 * @property y Y-Koordinate
 */
data class Position(var x: Int, var y: Int) {

    /**
     * @rturn Nächste Position in Abhängigkeit von der Richtung
     */
    fun naechste(richtung: Richtung): Position {
        val position = Position(x, y)
        when (richtung) {
            Richtung.NORD -> position.y = position.y - 1
            Richtung.OST -> position.x = position.x + 1
            Richtung.SUED -> position.y = position.y + 1
            Richtung.WEST -> position.x = position.x - 1
        }
        return position
    }
}