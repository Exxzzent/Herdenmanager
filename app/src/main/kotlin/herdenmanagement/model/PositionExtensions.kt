/**
 * Diese Datei enthält Extension Functions für die Klasse [Position].
 *
 * Die Erweiterungen bieten zusätzliche Funktionalitäten, die den Umgang mit Positionen erleichtern.
 * Dazu gehören:
 * - Berechnung der euklidischen Distanz zwischen zwei Positionen
 * - Operatorüberladungen zum Addieren und Subtrahieren von Positionen
 * - Eine Hilfsmethode zur Verschiebung einer Position um bestimmte Werte
 *
 * Diese Funktionen verbessern die Lesbarkeit des Codes und unterstützen Bewegungsberechnungen im Projekt.
 */

import herdenmanagement.model.Position
import kotlin.math.sqrt

/**
 * Berechnet die euklidische Distanz zwischen dieser Position und [other].
 *
 * @param other Die andere Position, zu der der Abstand berechnet werden soll.
 * @return Der Abstand zwischen den beiden Positionen als Double.
 */
fun Position.distanceTo(other: Position): Double {
    val dx = this.x - other.x
    val dy = this.y - other.y
    return sqrt((dx * dx + dy * dy).toDouble())
}

/**
 * Addiert die X- und Y-Koordinaten dieser Position mit denen von [other] und liefert
 * die resultierende neue Position zurück.
 *
 * @param other Die Position, deren Koordinaten addiert werden.
 * @return Eine neue [Position], die die Summe der beiden Positionen darstellt.
 */
operator fun Position.plus(other: Position): Position {
    return Position(this.x + other.x, this.y + other.y)
}

/**
 * Subtrahiert die X- und Y-Koordinaten von [other] von dieser Position und liefert
 * die resultierende neue Position zurück.
 *
 * @param other Die Position, deren Koordinaten subtrahiert werden.
 * @return Eine neue [Position], die das Ergebnis der Subtraktion darstellt.
 */
operator fun Position.minus(other: Position): Position {
    return Position(this.x - other.x, this.y - other.y)
}

/**
 * Verschiebt diese Position um [deltaX] und [deltaY] und liefert die neue Position zurück.
 *
 * @param deltaX Die Verschiebung entlang der X-Achse.
 * @param deltaY Die Verschiebung entlang der Y-Achse.
 * @return Eine neue [Position], die um die angegebenen Werte verschoben wurde.
 */
fun Position.moveBy(deltaX: Int, deltaY: Int): Position {
    return Position(this.x + deltaX, this.y + deltaY)
}
