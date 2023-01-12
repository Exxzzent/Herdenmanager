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
data class Position(var x: Int, var y: Int)