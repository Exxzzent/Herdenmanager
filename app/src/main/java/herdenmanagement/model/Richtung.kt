package herdenmanagement.model

/**
 * Mögliche Richtungen, in die die Kuh schauen kann, wichtig für die Anzeige von Bildern und die
 * Bewegung auf dem Acker.
 */
enum class Richtung {
    NORD, OST, SUED, WEST;

    /**
     * Richtung nach einer Drehung nach links
     */
    val links: Richtung
        get() = when (this) {
            NORD -> WEST
            OST -> NORD
            SUED -> OST
            WEST -> SUED
        }

    /**
     * Richtung nach einer Drehung nach rechts
     */
    val rechts: Richtung
        get() = when (this) {
            NORD -> OST
            OST -> SUED
            SUED -> WEST
            WEST -> NORD
        }

    /**
     * Entgegengesetzte Richtung
     */
    val umgekehrt: Richtung
        get() = when (this) {
            NORD -> SUED
            OST -> WEST
            SUED -> NORD
            WEST -> OST
        }
}