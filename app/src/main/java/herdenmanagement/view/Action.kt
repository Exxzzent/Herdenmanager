package herdenmanagement.view

import android.os.SystemClock

/**
 * Erzeugt eine Action. Die Wartezeit nach Ausführung der Action
 * ist [Animator.WARTEZEIT]
 *
 * @param waitingTime Wartezeit nach Ausführung der Action
 */
abstract class Action @JvmOverloads constructor(
    /**
     * Wartezeit nach Ausführung der Action
     */
    private val waitingTime: Int = Animator.WARTEZEIT
) : Runnable {

    /**
     * Schläft die in waitingTime eingestellte Zahl Millisekunden
     */
    fun sleep() {
        if (waitingTime < 0) {
            return
        }
        SystemClock.sleep(waitingTime.toLong())
    }
}