package herdenmanagement.model

/**
 * Werden PositionsElemente mit Buttons bewegt, sollte der Animator im Modus
 * ASYNCHRONOUS betrieben werden. Dies stellt die Sichtbarkeit aller Aktionen sicher.
 *
 * Werden Positionselemente auf dem Acker eingerichtet, sollte ASYNCHRONOUS_NO_WAIT
 * verwendet werden. Damit erscheinen alle Aktionen zeitgleich und ohne Verzögerung
 * stattzufinden.
 *
 * Sollen programmierte Aktionen nacheineinander und einzeln sichtbar auf dem Bildschirm
 * nachvollzogen werden, ist SYNCHRONOUS der richtige Modus.
 *
 * SYNCHRONOUS: Direktes Ausführen aller Aktionen im UI-Thread. Bei Ausführung von
 * performAction(Action action) wird der aufrufende Thread WARTEZEIT ms blockiert. Alle
 * Aktionen kommen in eine Warteschlange.
 *
 * ASYNCHRONOUS: Bei Ausführung von performAction(Action action) wird der aufrufende Thread
 * nicht blockiert. Aus der Warteschlange werden die Aktionen nacheinander im UI-Thread ausgeführt.
 * Zwischen den Aktionen wird WARTEZEIT ms gewartet. Alle Aktionen kommen in eine Warteschlange.
 *
 * ASYNCHRONOUS_NO_WAIT: Bei Ausführung von performAction(Action action) wird der aufrufende
 * Thread nicht blockiert. Aus der Warteschlange werden die Aktionen nacheinander im UI-Thread
 * ausgeführt. Zwischen den Aktionen wird nicht gewartet.
 *
 * @author Steffen Greiffenberg
 */
enum class Threading {SYNCHRONOUS, ASYNCHRONOUS, ASYNCHRONOUS_NO_WAIT}