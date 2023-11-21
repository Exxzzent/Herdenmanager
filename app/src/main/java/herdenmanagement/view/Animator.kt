package herdenmanagement.view

import herdenmanagement.model.Threading
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

/**
 * Der Animator versieht die Statusänderung von Objekten mit einer Animation. Ändert eine
 * View zum Beispiel ihre Position, sorgt der Animator für einen sanften Übergang. Der Nutzer
 * erhält den Eindruck, dass die View sich tatsächlich langsam zu den neuen Koordianten bewegt.
 *
 * Ein Animator wird folgerichtig den View-Klassen (@see [PositionElementView] im
 * HerdenManagement zugewiesen und von der [AckerView] erzeugt und verwaltet.
 *
 * Die Methoden des Klassen aus dem Paket herdenmanagement.model laufen in einem Thread,
 * der von den Methoden der Klassen aus dem Paket herdenmanagement.view losgelöst ist.
 * Der Grund liegt in der Tatsache begründet, dass Änderungen am Layout nur im
 * Main Thread einer Android App vorgenommen werden können.
 *
 * @author Steffen Greiffenberg
 */
class Animator {

    /**
     * Alle Aktionen werden direkt ausgeführt ohne Wartezeit.
     * [Threading.ASYNCHRONOUS], [Threading.SYNCHRONOUS] oder [Threading.ASYNCHRONOUS_NO_WAIT]
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
     * true, wenn im Modus ASYNCHRONOUS regelmäßig Aktionen ausgeführt werden sollen
     */
    private var running = false

    /**
     * Liste von Actions, die nacheinander abgearbeitet werden
     */
    private val actions: Queue<Action> = ArrayBlockingQueue(1024)

    /**
     * Fügt eine Action hinzu
     *
     * @param action Auszuführende Aktion
     */
    fun performAction(action: Action) {
        if (threading == Threading.ASYNCHRONOUS || threading == Threading.ASYNCHRONOUS_NO_WAIT) {
            actions.add(action)
        } else {
            // perform the action
            val handler = Handler(Looper.getMainLooper())
            handler.post(action)

            // wait the predefined waiting time
            action.sleep()
        }
    }

    /**
     * Startet den Animator. Dieser Thread arbeitet alle Actions in actions ab und führt
     * sie im UI-Thread des Context aus.
     */
    private fun start() {
        Thread {
            while (running) {
                if (!actions.isEmpty()) {
                    val action = actions.poll() ?: continue

                    // perform the action
                    val handler = Handler(Looper.getMainLooper())
                    handler.post(action)

                    // wait the predefined waiting time
                    if (threading != Threading.ASYNCHRONOUS_NO_WAIT) {
                        action.sleep()
                    }
                }
                SystemClock.sleep(10)
            }
        }.start()
    }

    /**
     * Ein companion object wird erzeugt, wenn die Klasse geladen wird. Es speichert also für alle
     * Objekt der Klasse Werte. In diesem Fall haben alle Animatoren im Projekt die selbe WARTEZEIT.
     */
    companion object {
        /**
         * Wartezeit für Bewegungen in ms
         */
        var WARTEZEIT = 1000
    }
}