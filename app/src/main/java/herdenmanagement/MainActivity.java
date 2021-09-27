package herdenmanagement;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import de.ba.herdenmanagement.R;
import herdenmanagement.model.Acker;
import herdenmanagement.view.AckerView;
import herdenmanagement.view.Animator;

/**
 * Die Klasse wurde vom Android angelegt und sorgt für die Anzeige der App auf dem Handy.
 * Hierzu wird unser {@link HerdenManager} initialisiert und mit einer {@link AckerView}
 * verknüpft.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Ich schreibe hier mal rein
     */
    private HerdenManager herdenManager;

    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@link #setContentView(int)} to inflate the
     * activity's UI, using {@link #findViewById} to programmatically interact
     * with widgets in the UI, calling
     * {@link #managedQuery(android.net.Uri, String[], String, String[], String)} to retrieve
     * cursors for data being displayed, etc.
     * <p>
     * <p>You can call {@link #finish} from within this function, in
     * which case onDestroy() will be immediately called without any of the rest
     * of the activity lifecycle ({@link #onStart}, {@link #onResume},
     * {@link #onPause}, etc) executing.
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @see #onStart
     * @see #onSaveInstanceState
     * @see #onRestoreInstanceState
     * @see #onPostCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called after {@link #onCreate} &mdash; or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {@link #onResume}.
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onCreate
     * @see #onStop
     * @see #onResume
     */
    protected void onStart() {
        super.onStart();

        // erzeugt einen HerdenManager
        herdenManager = new HerdenManager();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Während manageHerde möchten wir alle Aktionen sehen
                AckerView ackerView = findViewById(R.id.acker_view);

                // Acker einrichten, dies soll in einem "Rutsch" passieren,
                // die einzelnen Aktionen werden nicht animiert
                ackerView.setThreading(Animator.Threading.ASYNCHRONOUS_NO_WAIT);
                herdenManager.richteAckerEin(MainActivity.this);

                // Während manageHerde möchten wir alle Aktionen einzeln nachvollziehen
                ackerView.setThreading(Animator.Threading.SYNCHRONOUS);

                // bewegt ein Rind oder mehrer auf dem Acker
                herdenManager.manageHerde(MainActivity.this);

                // Alle Aktionen auf dem Acker, die jetzt folgen, werden direkt asynchron
                // ausgeführt. Betroffen sind vor allem Button-Clicks
                ackerView.setThreading(Animator.Threading.ASYNCHRONOUS);
            }
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
    public void onConfigurationChanged(Configuration newConfig) {
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

    public void eineMethode(View view) {
    }

    public void zweiteMethode(View view) {
    }
}

