package herdenmanagement.model

/**
 * Definiert die möglichen Richtungen, in die eine Kuh (oder allgemein ein Objekt) schauen oder sich bewegen kann.
 *
 * Diese Aufzählung wird in der Darstellung (z. B. Anzeige von Bildern) und der Bewegung auf dem Acker verwendet.
 * Jede Richtung unterstützt zudem Hilfsoperationen, um Drehungen zu simulieren, wie z. B. eine Drehung nach links,
 * nach rechts oder eine 180°-Drehung (umgekehrt).
 *
 * Didaktische Hinweise:
 * - **Enum-Klassen:** Diese Klasse zeigt, wie in Kotlin Enum-Klassen genutzt werden, um eine fest definierte
 *   Menge von Konstanten zu repräsentieren.
 * - **Erweiterte Eigenschaften:** Die Eigenschaften [links], [rechts] und [umgekehrt] demonstrieren, wie
 *   man durch benutzerdefinierte Getter zusätzliche Logik in Enums einbauen kann.
 *
 * @author Steffen Greiffenberg
 */
enum class Richtung {
    NORD, OST, SUED, WEST;

    /**
     * Gibt die Richtung zurück, in die sich ein Objekt nach einer Drehung nach links bewegt.
     *
     * Didaktischer Hinweis:
     * - Der Einsatz des `when`-Ausdrucks illustriert eine elegante und übersichtliche Möglichkeit,
     *   Logik in Enums zu implementieren.
     */
    val links: Richtung
        get() = when (this) {
            NORD -> WEST
            OST -> NORD
            SUED -> OST
            WEST -> SUED
        }

    /**
     * Gibt die Richtung zurück, in die sich ein Objekt nach einer Drehung nach rechts bewegt.
     *
     * Didaktischer Hinweis:
     * - Diese Eigenschaft demonstriert, wie man durch einfache logische Operationen die nächste
     *   Richtung ermitteln kann, was besonders in grafischen Anwendungen nützlich ist.
     */
    val rechts: Richtung
        get() = when (this) {
            NORD -> OST
            OST -> SUED
            SUED -> WEST
            WEST -> NORD
        }

    /**
     * Gibt die entgegengesetzte Richtung zurück (180°-Drehung).
     *
     * Didaktischer Hinweis:
     * - Die Eigenschaft [umgekehrt] zeigt, wie man mit einem einfachen Mapping von Konstanten
     *   eine praktische Funktionalität implementiert, die in vielen Anwendungsfällen von Bedeutung ist.
     */
    val umgekehrt: Richtung
        get() = when (this) {
            NORD -> SUED
            OST -> WEST
            SUED -> NORD
            WEST -> OST
        }
}
