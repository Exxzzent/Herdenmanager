package herdenmanagement;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import de.ba.herdenmanagement.R;
import herdenmanagement.model.Acker;
import herdenmanagement.view.AckerView;
import herdenmanagement.view.Animator;

/**
 * Die Klasse wurde vom Android angelegt und sorgt für die Anzeige der App auf dem Handy.
 * Hierzu wird unser {@link HerdenManager} initialisiert und mit einer {@link AckerView}
 * verknüpft.
 *
 * @author Steffen Greiffenberg
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Ich schreibe hier mal rein
     */
    private HerdenManager herdenManager;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called after {@link #onCreate}.
     */
    protected void onStart() {
        super.onStart();

        // erzeugt einen HerdenManager
        herdenManager = new HerdenManager();

        new Thread(() -> {
            // Während manageHerde möchten wir alle Aktionen sehen
            AckerView ackerView = findViewById(R.id.acker_view);

            // Acker einrichten, dies soll in einem "Rutsch" passieren,
            // die einzelnen Aktionen werden nicht animiert
            ackerView.setThreading(Animator.Threading.ASYNCHRONOUS_NO_WAIT);
            herdenManager.richteAckerEin(MainActivity.this);

            // Während manageHerde möchten wir alle Aktionen einzeln nachvollziehen
            ackerView.setThreading(Animator.Threading.SYNCHRONOUS);

            // Hier können vorprogrammierte Aktionen auf dem Acker stattfinden. Zum
            // Beispiel kann ein Rind auf dem Acker bewegt werden.
            herdenManager.manageHerde(MainActivity.this);

            // Alle Aktionen auf dem Acker, die jetzt folgen, werden direkt asynchron
            // ausgeführt. Betroffen sind vor allem Button-Clicks.
            ackerView.setThreading(Animator.Threading.ASYNCHRONOUS);
        }).start();
    }

    /**
     * Called by the system when the device configuration changes while your
     * activity is running.  Note that this will <em>only</em> be called if
     * you have selected configurations you would like to handle with the
     * {@link android.R.attr#configChanges} attribute in your manifest.  If
     * any configuration change occurs that is not selected to be reported
     * by that attribute, then instead of reporting it the system will stop
     * and restart the activity (to have it launched with the new
     * configuration).
     *
     * <p>At the time that this function has been called, your Resources
     * object will have been updated to return resource values matching the
     * new configuration.
     *
     * @param newConfig The new device configuration.
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Den Acker der aktuellen AckerView ermitteln
        AckerView ackerView = findViewById(R.id.acker_view);
        Acker acker = ackerView.getAcker();

        Animator.Threading currentThreading = ackerView.getThreading();
        ackerView.setThreading(Animator.Threading.ASYNCHRONOUS_NO_WAIT);

        ackerView.setAcker(null);

        // Das neue Layout setzen
        // Damit wird auch ein eventuell für das Querformat definiertes Layout verwendet
        setContentView(R.layout.activity_main);

        // Die AckerView dürfte sich durch die vorhergehende Anweisung geändert haben
        // Diese wird zukünftig vom vorhandenen Acker genutzt
        ackerView = findViewById(R.id.acker_view);
        ackerView.setThreading(Animator.Threading.ASYNCHRONOUS_NO_WAIT);
        ackerView.setAcker(acker);

        // Den Zustand des Threadings wieder herstellen
        ackerView.setThreading(currentThreading);
    }
}

