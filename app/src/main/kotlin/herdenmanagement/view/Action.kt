package herdenmanagement.view

import android.os.SystemClock

/**
 * Repräsentiert eine abstrakte Aktion, die als [Runnable] ausgeführt werden kann.
 *
 * Jede Action verfügt über eine definierte Wartezeit (in Millisekunden), die nach ihrer Ausführung
 * eingehalten wird. Diese Wartezeit ist standardmäßig durch [Animator.WARTEZEIT] festgelegt und kann
 * beim Erzeugen der Action überschrieben werden.
 *
 * Didaktische Hinweise:
 * - **Vererbung und Abstraktion:**
 *   Die Klasse [Action] ist abstrakt und implementiert [Runnable]. Dies ermöglicht es, konkrete
 *   Aktionen zu definieren, die als Threads oder Aufgaben im UI-Thread ausgeführt werden können.
 * - **Wartezeiten in Animationen:**
 *   Die Wartezeit steuert, wie lange der Ausführungsfluss nach der Durchführung einer Action pausiert.
 *   Dies ist besonders nützlich, um Animationen oder sequentielle Aktionen sichtbar und nachvollziehbar zu machen.
 * - **Android SystemClock:**
 *   Die Methode [sleep] verwendet [SystemClock.sleep], um den aktuellen Thread für die definierte
 *   Wartezeit anzuhalten. Dies zeigt, wie in Android zeitgesteuerte Abläufe implementiert werden können.
 *
 * @param waitingTime Die Wartezeit (in Millisekunden) nach der Ausführung der Action. Standardmäßig
 *                    wird [Animator.WARTEZEIT] verwendet.
 */
abstract class Action @JvmOverloads constructor(
    /**
     * Die Wartezeit nach Ausführung der Action in Millisekunden.
     */
    private val waitingTime: Int = Animator.WARTEZEIT
) : Runnable {

    /**
     * Pausiert den aufrufenden Thread für die in [waitingTime] definierte Dauer.
     *
     * Diese Methode sorgt dafür, dass nach der Ausführung der Action eine Pause eingelegt wird.
     * Falls [waitingTime] negativ ist, wird keine Pause eingelegt.
     *
     * Didaktischer Hinweis:
     * - Der Einsatz von [SystemClock.sleep] veranschaulicht eine einfache Möglichkeit, Wartezeiten
     *   in Android-Anwendungen zu implementieren, ohne den Hauptthread zu blockieren.
     */
    fun sleep() {
        if (waitingTime < 0) {
            return
        }
        SystemClock.sleep(waitingTime.toLong())
    }
}
