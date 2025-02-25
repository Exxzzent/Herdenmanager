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
 * MainActivity ist der Einstiegspunkt der App und steuert die Anzeige des Herdenmanagement-Systems.
 *
 * Diese Klasse initialisiert den [HerdenManager] und bindet ihn an eine [AckerView]. Im Rahmen des
 * Model-View-Controller (MVC)-Musters wird hier das Model (Acker, Herden) mit der View (AckerView) verknüpft.
 * Änderungen an der Gerätekonfiguration (z. B. beim Wechsel zwischen Hoch- und Querformat) werden ebenfalls
 * hier abgehandelt, um eine konsistente Darstellung zu gewährleisten.
 *
 * Didaktische Hinweise:
 * - **MVC und Observer-Muster:**
 *   Die MainActivity zeigt, wie das Model (HerdenManager, Acker) mit der View (AckerView) interagiert.
 *   Änderungen im Model werden über Observer (PropertyChangeListener) an die View weitergeleitet.
 * - **Threading und asynchrone Verarbeitung:**
 *   Aktionen im HerdenManager werden in einem separaten Thread ausgeführt, um den UI-Thread nicht zu blockieren.
 *   Dabei wird der [Acker]-Animationsmodus dynamisch zwischen SYNCHRONOUS und ASYNCHRONOUS gewechselt.
 * - **Konfigurationsänderungen:**
 *   In [onConfigurationChanged] wird demonstriert, wie auf Layoutänderungen (z. B. bei Drehung des Geräts)
 *   reagiert wird, sodass die View neu aufgebaut und das Model konsistent dargestellt wird.
 *
 * @author Steffen Greiffenberg
 */
class MainActivity : AppCompatActivity() {

    /**
     * Instanz des HerdenManager, der das Model verwaltet.
     */
    private var herdenManager = HerdenManager()

    /**
     * Wird aufgerufen, wenn die Activity gestartet wird.
     *
     * Hier wird das Layout gesetzt und erste Initialisierungen durchgeführt.
     *
     * @param savedInstanceState Falls die Activity zuvor beendet wurde, enthält dieses Bundle den zuletzt
     *                           gespeicherten Zustand.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Wird nach [onCreate] aufgerufen.
     *
     * In [onStart] wird in einem separaten Thread der Acker eingerichtet und der HerdenManager
     * gestartet, um vorprogrammierte Aktionen auf dem Acker durchzuführen. Dabei wird der Animationsmodus
     * des Ackers zunächst auf SYNCHRONOUS gesetzt, um die Aktionen einzeln nachvollziehen zu können, und später
     * auf ASYNCHRONOUS gewechselt.
     */
    override fun onStart() {
        super.onStart()

        Thread {
            // Initialisiert den Acker im HerdenManager, sofern die Methode "richteAckerEin" vorhanden ist.
            herdenManager::class.members.firstOrNull {
                it.name == "richteAckerEin"
            }?.call(herdenManager, this@MainActivity)

            // Setzt den Animationsmodus auf SYNCHRONOUS, damit Aktionen einzeln und sichtbar ablaufen.
            acker.animation = Threading.SYNCHRONOUS

            // Führt vorprogrammierte Aktionen auf dem Acker aus, z. B. Bewegungen von Rindern.
            herdenManager.manageHerde(this@MainActivity)

            // Wechselt den Animationsmodus auf ASYNCHRONOUS, sodass spätere Aktionen (z. B. Button-Klicks)
            // direkt und ohne Verzögerung ausgeführt werden.
            acker.animation = Threading.ASYNCHRONOUS
        }.start()
    }

    /**
     * Wird aufgerufen, wenn sich die Gerätekonfiguration ändert (z. B. Drehung des Geräts).
     *
     * Hier wird das Layout neu gesetzt, um auf Änderungen wie das Querformat zu reagieren. Dabei wird
     * der aktuell angezeigte Acker (und seine darin enthaltenen Objekte) beibehalten und neu zugeordnet.
     *
     * @param newConfig Die neue Gerätekonfiguration.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Den aktuellen Acker aus der AckerView abrufen
        var ackerView = findViewById<AckerView>(R.id.acker_view)
        val acker = ackerView.acker
        ackerView.acker = Acker(3, 4)

        // Setzt das neue Layout, welches auch für das Querformat angepasst sein kann
        setContentView(R.layout.activity_main)

        // Holt die AckerView neu und stellt die Verbindung zum bisherigen Acker wieder her
        ackerView = findViewById(R.id.acker_view)
        ackerView.acker = acker
        ackerView.requestLayout()
    }

    /**
     * Zeigt eine Toast-Nachricht an.
     *
     * Diese Methode stellt sicher, dass Toasts im UI-Thread angezeigt werden.
     *
     * @param message Die Nachricht, die angezeigt werden soll.
     */
    fun toast(message: Any) {
        runOnUiThread { Toast.makeText(this, "$message", Toast.LENGTH_LONG).show() }
    }

    /**
     * Liefert den aktuell angezeigten Acker.
     *
     * Die AckerView wird anhand ihrer ID ermittelt und der darin enthaltene Acker zurückgegeben.
     *
     * @return Das aktuell angezeigte [Acker]-Objekt.
     */
    val acker: Acker
        get() {
            val ackerView: AckerView = findViewById(R.id.acker_view)
            return ackerView.acker
        }
}
