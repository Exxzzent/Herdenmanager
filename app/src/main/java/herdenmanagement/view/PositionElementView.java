package herdenmanagement.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import herdenmanagement.model.Acker;
import herdenmanagement.model.Position;
import herdenmanagement.model.PositionsElement;

/**
 * Basisklasse für die Darstellung von Eimer, Kühen, etc. Diese View kann auf Änderungen
 * an den Properties ihres PositionsElements reagieren, in der Regel durch Anpassung der Anzeige.
 * <p>
 * Im Muster Model View Controller (MVC) sind Objekte dieser Klasse Bestandteil des Model.
 * <p>
 * Im Muster Obersever ist die Klasse ein ConcreteObserer, der PropertyChangeListener
 * ist das implementierte Observer Interface.
 *
 * @author Steffen Greiffenberg
 */
public class PositionElementView extends AppCompatImageView implements PropertyChangeListener {

    /**
     * Animator für die Aktualisierung der GUI
     */
    private final Animator animator;

    /**
     * {@link PositionsElement}, welches hier dargestellt wird. Das {@link PositionsElement}
     * kennt seine View nicht, nur die View kennt ihr Modell.
     *
     * Im Attribut wird das zuletzt geänderte Element gespeichert, über das diese View
     * informiert wurde. Während einer Animation ist das Original dieser Kopie möglicherweise
     * in einem anderen Zustand (an anderer Position o. ä.).
     */
    private PositionsElement positionsElement;

    /**
     * Erzeugt die Ansicht für ein PositionsElement.
     *
     * @param context          Context der App
     * @param positionsElement Darzustellendes Element
     */
    public PositionElementView(Context context, Animator animator, PositionsElement positionsElement) {
        super(context);

        this.animator = animator;

        // PositionsElement observieren
        positionsElement.fuegeBeobachterHinzu(this);

        // Kopie merken, nicht das original (s. o.)
        this.positionsElement = positionsElement.kopiere();

        // ID des PositionsElement übernehmen
        setId(positionsElement.gibId());

        // Bild setzen
        setPadding(4, 4, 4, 4);
        setImageBitmap(getAktuellesBild());
    }

    /**
     * @return Dargestelltes PositionsElement
     */
    public PositionsElement getPositionsElement() {
        return positionsElement;
    }

    /**
     * Die Klassen im Modell des HerdenManagers senden ihren View-Klassen ein
     * PropertyChangeEvent, wenn sich an einer Eigenschaft etwas geändert hat.
     * Die View-Klassen reagieren in der Regel mit der Anpssung ihrer grafischen Darstellung.
     *
     * @param evt PropertyChangeEvent, das die Änderung (Value) und die Art der Änderung (Property Name) beschreibt
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        PositionsElement positionsElement = (PositionsElement) evt.getNewValue();

        // Nachricht anzeigen
        if (PositionsElement.PROPERTY_NACHRICHT.equals(evt.getPropertyName())) {
            Object nachricht = positionsElement.gibNachricht();

            // Ist die Nachricht ein String, wird dieser direkt angezeigt
            if (nachricht instanceof String) {
                toast((String) nachricht);
            }

            // Ist die Nachricht eine Zahl, wird sie zunächst als ID im Ressourcen-Bundle interpretiert
            // Klappt das nicht, wird die Zahl gezeigt
            if (nachricht instanceof Number) {
                try {
                    int id = ((Number) nachricht).intValue();
                    String text = getContext().getResources().getString(id, positionsElement.gibName());
                    toast(text);
                } catch (Resources.NotFoundException e) {
                    toast("" + nachricht);
                }
            }
        }

        // Bei Änderungen der Position, muss ein neues Layout berechnet werden
        else if (PositionsElement.PROPERTY_POSITION.equals(evt.getPropertyName())) {
            // Möglicherweise ist die GUI noch nicht geladen
            AckerView ackerView = (AckerView) getParent();
            if (ackerView == null) {
                return;
            }

            // Bei Änderungen der Position, muss ein neues Layout berechnet werden
            animator.performAction(new Animator.Action() {
                @Override
                public void run() {
                    // Kopie aktualisieren
                    PositionElementView.this.positionsElement = positionsElement;

                    // remember current LayoutParams
                    final FrameLayout.LayoutParams lp = calculateLayoutParams(
                            ackerView.getWidth(), ackerView.getHeight());

                    // Animation starten
                    if (getParent() instanceof ViewGroup) {
                        TransitionManager.beginDelayedTransition((ViewGroup) getParent());
                    }

                    // Position setzen
                    setLayoutParams(lp);
                }
            });
        } else {

            // Bei Änderungen der Position, muss ein neues Layout berechnet werden
            animator.performAction(new Animator.Action() {
                @Override
                public void run() {
                    // Kopie aktualisieren
                    PositionElementView.this.positionsElement = positionsElement;

                    // Animation starten
                    if (getParent() instanceof ViewGroup) {
                        TransitionManager.beginDelayedTransition((ViewGroup) getParent());
                    }

                    // Bild aktualisieren
                    final Bitmap bitmap = getAktuellesBild();
                    setImageBitmap(bitmap);
                }
            });
        }
    }

    /**
     * @param width Breite der übergeordneten AckerView
     * @param height Höhe der übergeordneten AckerView
     * @return Kopie der aktuellen LayoutParams (left / right / top / bottom) basierend auf PositionsElement und AckerView
     */
    @NonNull
    public FrameLayout.LayoutParams calculateLayoutParams(float width, float height) {
        Acker acker = positionsElement.gibAcker();
        Position position = positionsElement.gibPosition();

        int columns = acker == null ? 0 : acker.zaehleSpalten();
        float columnWidth = (int) (width / columns);

        int rows = acker == null ? 0 : acker.zaehleZeilen();
        float rowHeight = (int) (height / rows);

        // LayoutParams for child
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
        FrameLayout.LayoutParams result = new FrameLayout.LayoutParams(lp);

        result.width = (int) columnWidth;
        result.height = (int) rowHeight;
        result.leftMargin = (int) (position.x * columnWidth);
        result.topMargin = (int) (position.y * rowHeight);

        System.out.println(" lm: " + result.leftMargin);

        return result;
    }

    /**
     * Zeigt den Toast mittels Animator, dadurch wird auch stets der
     * UI Thread zur Anzeige verwendet.
     *
     * @param text Anzuzeigender Text
     */
    private void toast(final String text) {
        animator.performAction(new Animator.Action() {
            @Override
            public void run() {
                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Diese Methode sollte von allen erbenden Klassen überladen werden, um entsprechend des
     * PositionsElements ein passendes Bild zu liefern.
     *
     * @return Bild auf Basis des {@link PositionsElement}
     */
    protected Bitmap getAktuellesBild() {
        return null;
    }
}
