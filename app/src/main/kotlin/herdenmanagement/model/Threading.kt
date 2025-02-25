package herdenmanagement.model

/**
 * Definiert verschiedene Modi für die Animation von Aktionen auf dem Acker.
 *
 * Die Auswahl des richtigen Modus ist entscheidend für die Darstellung der Bewegungen von [PositionsElement]-Objekten
 * und hat Auswirkungen darauf, wie und wann Änderungen sichtbar werden:
 *
 * - **SYNCHRONOUS:**
 *   Aktionen werden direkt im UI-Thread ausgeführt. Bei der Ausführung von performAction(Action action)
 *   wird der aufrufende Thread für eine definierte Wartezeit blockiert, sodass alle Aktionen nacheinander in einer
 *   Warteschlange verarbeitet werden. Dies ermöglicht es, einzelne Aktionen detailliert zu verfolgen.
 *
 * - **ASYNCHRONOUS:**
 *   Aktionen werden in einer Warteschlange verarbeitet, aber der aufrufende Thread wird nicht blockiert. Zwischen
 *   den Aktionen wird jedoch eine Wartezeit von WARTEZEIT ms eingehalten, wodurch die Aktionen sequentiell im UI-Thread
 *   ablaufen, ohne den Hauptthread zu blockieren.
 *
 * - **ASYNCHRONOUS_NO_WAIT:**
 *   Aktionen werden ebenfalls in einer Warteschlange abgearbeitet, jedoch ohne Wartezeiten zwischen den Aktionen.
 *   Dies führt dazu, dass alle Aktionen nahezu gleichzeitig ausgeführt werden, was eine schnelle und ununterbrochene
 *   Darstellung bewirkt.
 *
 * Didaktische Hinweise:
 * - **Animation und UI-Thread:**
 *   Diese Modi veranschaulichen, wie durch unterschiedliche Threading-Strategien die Benutzererfahrung und die
 *   Darstellung von Animationen beeinflusst werden können.
 * - **Schnittstelle zwischen synchroner und asynchroner Ausführung:**
 *   Der Vergleich der drei Modi zeigt den Unterschied zwischen blockierender (synchroner) und nicht-blockierender
 *   (asynchroner) Verarbeitung und deren Auswirkungen auf die Performance einer Anwendung.
 *
 * @author Steffen Greiffenberg
 */
enum class Threading {
    SYNCHRONOUS,
    ASYNCHRONOUS,
    ASYNCHRONOUS_NO_WAIT
}
