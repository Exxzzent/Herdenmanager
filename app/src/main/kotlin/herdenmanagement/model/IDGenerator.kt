package herdenmanagement.model

import kotlinx.atomicfu.atomic

/**
 * Erzeugt eindeutige IDs für Elemente im System.
 *
 * [IDGenerator] nutzt atomare Operationen (über [kotlinx.atomicfu.atomic]), um in
 * einem multithreaded Umfeld eine sichere und konsistente Zählung zu gewährleisten.
 * Jeder Aufruf von [generateId] liefert eine eindeutige Nummer, wobei der interne Zähler
 * nach Erreichen eines bestimmten Maximums (0x00FFFFFF) wieder von 1 beginnt.
 *
 * Didaktische Hinweise:
 * - **Atomare Operationen:** Dieses Objekt zeigt, wie in Kotlin mit Hilfe der Atomic-Funktionalität
 *   von kotlinx.atomicfu Thread-Sicherheit erreicht wird, ohne auf traditionelle Synchronisierungsmechanismen
 *   zurückgreifen zu müssen.
 * - **Loop-Konstrukt:** Die while-Schleife demonstriert, wie man mit Hilfe von Compare-And-Set (CAS)
 *   sicherstellt, dass der Zähler korrekt hochgezählt wird, auch wenn mehrere Threads gleichzeitig
 *   eine neue ID anfordern.
 * - **Rollover-Logik:** Die Implementierung zeigt, wie ein Überlauf behandelt wird, indem der Zähler zurückgesetzt
 *   wird, wenn er einen definierten Grenzwert überschreitet.
 *
 * @author Steffen Greiffenberg
 */
object IDGenerator {

    /**
     * Atomarer Zähler, der für die Generierung eindeutiger IDs verwendet wird.
     *
     * Der Initialwert ist 1. Die atomare Variable stellt sicher, dass alle Operationen
     * auf diesem Wert thread-sicher durchgeführt werden.
     */
    private val nextGeneratedId = atomic(1)

    /**
     * Erzeugt eine neue, eindeutige ID.
     *
     * Der aktuelle Wert von [nextGeneratedId] wird gelesen und anschließend inkrementiert.
     * Dabei wird mit einer Compare-And-Set (CAS)-Operation sichergestellt, dass der Wert
     * korrekt aktualisiert wird, selbst wenn mehrere Threads gleichzeitig auf diese Methode zugreifen.
     *
     * Besonderheit:
     * - Soll der Wert 0x00FFFFFF überschreiten, wird der Zähler wieder auf 1 gesetzt, um sicherzustellen,
     *   dass die generierten IDs stets einen gültigen Wertebereich einhalten.
     *
     * @return Eine eindeutig generierte ID als [Int].
     */
    fun generateId(): Int {
        while (true) {
            val result = nextGeneratedId.value
            // aapt-generated IDs haben das höchste Byte ungleich 0; daher wird hier in den Bereich darunter geklammert.
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
            if (nextGeneratedId.compareAndSet(result, newValue)) {
                return result
            }
        }
    }
}
