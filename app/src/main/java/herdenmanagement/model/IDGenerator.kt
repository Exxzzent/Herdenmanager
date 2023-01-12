package herdenmanagement.model

import java.util.concurrent.atomic.AtomicInteger

object IDGenerator {

    /**
     * Zahlenwert der automatisch aktualisiert wird. Damit erhält jedes Elememt eine eindeutige ID.
     */
    private val nextGeneratedId = AtomicInteger(1)

    /**
     * Erzeugt eine neue ID. Der Wert in [nextGeneratedId] wird hierfür hochgezählt
     *
     * @return generierte ID
     */
    internal fun generateId(): Int {
        while (true) {
            val result = nextGeneratedId.get()
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
            if (nextGeneratedId.compareAndSet(result, newValue)) {
                return result
            }
        }
    }

}