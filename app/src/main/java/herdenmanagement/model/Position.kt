package herdenmanagement.model

/**
 * Eine Position speichert eine X- und Y-Koordinate für Objekte der Klasse [PositionsElement].
 * Andere Positionen sind auch wichtig, aber nicht für Rindviecher.
 *
 * @author Steffen Greiffenberg
 */
data class Position(var x: Int, var y: Int)