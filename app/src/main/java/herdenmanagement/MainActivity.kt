package herdenmanagement

import herdenmanagement.model.Threading
import android.content.res.Configuration
import de.ba.herdenmanagement.R
import herdenmanagement.view.AckerView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import herdenmanagement.model.Acker

/**
 * Die Klasse wurde vom Android angelegt und sorgt für die Anzeige der App auf dem Handy.
 * Hierzu wird unser [HerdenManager] initialisiert und mit einer [AckerView]
 * verknüpft.
 *
 * @author Steffen Greiffenberg
 */
class MainActivity : AppCompatActivity() {

    /**
     * HerdenManager erzeugen
     */
    private var herdenManager = HerdenManager()

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in [.onSaveInstanceState].  ***Note: Otherwise it is null.***
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Called after [.onCreate].
     */
    override fun onStart() {
        super.onStart()

        // erzeugt einen HerdenManager
        Thread {

            // Acker einrichten
            herdenManager::class.members.firstOrNull {
                // Hat der herdenManager die Methode richteAckerEin?
                it.name == "richteAckerEin"
            }?.call(herdenManager, this@MainActivity)

            // Während manageHerde möchten wir alle Aktionen einzeln nachvollziehen
            acker.animation = Threading.SYNCHRONOUS

            // Hier können vorprogrammierte Aktionen auf dem Acker stattfinden. Zum
            // Beispiel kann ein Rind auf dem Acker bewegt werden.
            herdenManager.manageHerde(this@MainActivity)

            // Alle Aktionen auf dem Acker, die jetzt folgen, werden direkt asynchron
            // ausgeführt. Betroffen sind vor allem Button-Clicks.
            acker.animation = Threading.ASYNCHRONOUS
        }.start()
    }

    /**
     * Called by the system when the device configuration changes while your
     * activity is running.  Note that this will *only* be called if
     * you have selected configurations you would like to handle with the
     * [android.R.attr.configChanges] attribute in your manifest.  If
     * any configuration change occurs that is not selected to be reported
     * by that attribute, then instead of reporting it the system will stop
     * and restart the activity (to have it launched with the new
     * configuration).
     *
     *
     * At the time that this function has been called, your Resources
     * object will have been updated to return resource values matching the
     * new configuration.
     *
     * @param newConfig The new device configuration.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Den Acker der aktuellen AckerView ermitteln
        var ackerView = findViewById<AckerView>(R.id.acker_view)
        val acker = ackerView.acker
        ackerView.acker = Acker(3, 4)

        // Das neue Layout setzen
        // Damit wird auch ein eventuell für das Querformat definiertes Layout verwendet
        setContentView(R.layout.activity_main)

        // Die AckerView dürfte sich durch die vorhergehende Anweisung geändert haben
        // Diese wird zukünftig vom vorhandenen Acker genutzt
        ackerView = findViewById(R.id.acker_view)
        ackerView.acker = acker
        ackerView.requestLayout()
    }

    /**
     * Show a simple message
     */
    fun toast(message: Any) {
        runOnUiThread({ Toast.makeText(this, "" + message, Toast.LENGTH_LONG).show() })
    }

    /**
     * Aktuell angezeigter Acker
     */
    val acker: Acker
        get() {
            val ackerView: AckerView = findViewById(R.id.acker_view)
            return ackerView.acker
        }
}