package herdenmanagement.view

import herdenmanagement.model.Threading
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

/**
 * Verantwortlich für die Animation von Statusänderungen bei Views.
 *
 * Der Animator sorgt dafür, dass Änderungen (z. B. Positionsänderungen) von Objekten, die durch die
 * Klassen im Paket [herdenmanagement.model] gesteuert werden, sanft und nachvollziehbar dargestellt werden.
 * Dies wird erreicht, indem die Aktionen, die Animationen auslösen, entweder synchron oder asynchron
 * im UI-Thread ausgeführt werden.
 *
 * **Funktionsweise und Modi:**
 * - **SYNCHRONOUS:**
 *   Aktionen werden direkt im UI-Thread ausgeführt und blockieren diesen für eine festgelegte Wartezeit.
 *   Dies sorgt dafür, dass alle Aktionen nacheinander sichtbar sind. Die Wartezeit ist in [WARTEZEIT] definiert.
 *
 * - **ASYNCHRONOUS:**
 *   Der aufrufende Thread wird nicht blockiert. Aktionen werden in eine Warteschlange eingereiht und im UI-Thread
 *   sequentiell mit einer Wartezeit zwischen den Aktionen abgearbeitet.
 *
 * - **ASYNCHRONOUS_NO_WAIT:**
 *   Auch hier wird der aufrufende Thread nicht blockiert, jedoch werden die Aktionen ohne zusätzliche Wartezeit
 *   im UI-Thread ausgeführt. Somit erscheinen alle Aktionen nahezu gleichzeitig.
 *
 * **Didaktische Hinweise:**
 * - **Trennung von UI-Thread und Arbeits-Threads:**
 *   Änderungen am Layout in Android müssen im Main Thread erfolgen. Der Animator demonstriert, wie mithilfe eines
 *   separaten Threads und Handlern Aktionen im UI-Thread ausgeführt werden können.
 * - **Threading-Strategien:**
 *   Die unterschiedlichen Modi zeigen, wie man blockierende (synchron) und nicht-blockierende (asynchron) Abläufe
 *   implementieren kann, was für die Gestaltung flüssiger Benutzeroberflächen essenziell ist.
 * - **Verwendung von Warteschlangen:**
 *   Durch die Nutzung einer [ArrayBlockingQueue] wird veranschaulicht, wie Aufgaben gesammelt und in einem
 *   kontrollierten Ablauf abgearbeitet werden können.
 *
 * @author Steffen Greiffenberg
 */
class Animator {

    /**
     * Bestimmt den Modus, in dem Aktionen ausgeführt werden.
     *
     * Beim Wechsel in einen asynchronen Modus wird der Animator gestartet, um die in der Warteschlange
     * gespeicherten Aktionen abzuarbeiten.
     *
     * Beim Wechsel zu [Threading.SYNCHRONOUS] werden alle noch anstehenden Aktionen kurz angehalten, sodass
     * der UI-Thread blockiert wird, bis die Aktionen abgearbeitet sind.
     */
    var threading = Threading.SYNCHRONOUS
        set(value) {
            if (field != value) {
                field = value
                when (value) {
                    Threading.SYNCHRONOUS -> {
                        while (running && actions.isNotEmpty()) SystemClock.sleep(10)
                    }
                    Threading.ASYNCHRONOUS, Threading.ASYNCHRONOUS_NO_WAIT -> {
                        running = true
                        start()
                    }
                }
                running = value != Threading.SYNCHRONOUS
            }
        }

    /**
     * Flag, das angibt, ob im asynchronen Modus Aktionen regelmäßig abgearbeitet werden sollen.
     */
    private var running = false

    /**
     * Warteschlange für die auszuführenden Actions.
     *
     * Die Queue speichert alle Aktionen, die im asynchronen Modus nacheinander abgearbeitet werden sollen.
     */
    private val actions: Queue<Action> = ArrayBlockingQueue(1024)

    /**
     * Fügt eine neue [Action] zur Abarbeitung hinzu.
     *
     * Im synchronen Modus wird die Action sofort im UI-Thread ausgeführt und anschließend
     * für die definierte Wartezeit pausiert. Im asynchronen Modus wird die Action in die Warteschlange
     * eingereiht.
     *
     * @param action Die auszuführende Aktion.
     */
    fun performAction(action: Action) {
        if (threading == Threading.ASYNCHRONOUS || threading == Threading.ASYNCHRONOUS_NO_WAIT) {
            actions.add(action)
        } else {
            // Aktion direkt im UI-Thread ausführen
            val handler = Handler(Looper.getMainLooper())
            handler.post(action)

            // Wartezeit einhalten
            action.sleep()
        }
    }

    /**
     * Startet einen separaten Thread, der alle in der Warteschlange gespeicherten Actions abarbeitet.
     *
     * Die Aktionen werden im UI-Thread des Anwendungskontexts mittels eines Handlers ausgeführt.
     * Je nach [threading]-Modus wird zwischen den Aktionen entweder gewartet oder sofort zur nächsten
     * Aktion übergegangen.
     */
    private fun start() {
        Thread {
            while (running) {
                if (!actions.isEmpty()) {
                    val action = actions.poll() ?: continue

                    // Aktion im UI-Thread ausführen
                    val handler = Handler(Looper.getMainLooper())
                    handler.post(action)

                    // Wartezeit einhalten, sofern nicht im NO_WAIT-Modus
                    if (threading != Threading.ASYNCHRONOUS_NO_WAIT) {
                        action.sleep()
                    }
                }
                SystemClock.sleep(10)
            }
        }.start()
    }

    /**
     * Companion Object, das Werte speichert, die für alle Instanzen von [Animator] gelten.
     *
     * In diesem Fall definiert [WARTEZEIT] die Standardwartezeit (in Millisekunden) für alle
     * Animationen. Wird dieser Wert geändert, wirkt sich das auf sämtliche Animator-Instanzen aus.
     */
    companion object {
        /**
         * Die Standardwartezeit in Millisekunden für Bewegungen/Animationen.
         */
        var WARTEZEIT = 1000
    }
}
