package herdenmanagement.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.ba.herdenmanagement.R;
import herdenmanagement.model.Acker;
import herdenmanagement.model.Position;
import herdenmanagement.model.PositionsElement;

/**
 * Basisklasse für die Darstellung von Eimer, Kühen, etc. Diese View kann auf Änderungen
 * an den Properties ihres PositionsElements reagieren, in der Regel durch Anpassung der Anzeige.
 * Wird die Eigenschaft elevation mit {@link #setElevation(float)} angepasst, werfen
 * Objekte dieser Klasse einen Schatten auf den Acker.
 * <p>
 * Im Muster Model View Controller (MVC) sind Objekte dieser Klasse Bestandteil der View.
 * <p>
 * Im Muster Obersever ist die Klasse ein ConcreteObserver, der PropertyChangeListener
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
        // Bild aktualisieren
        final Bitmap bitmap = getAktuellesBild();
        setImageBitmap(bitmap);
    }

    /**
     * Setzt das Bild für das Positionselement.
     * Wenn diese View einen Abstand auf der z-Achse im Property elevation
     * besitzt, wird ein Schatten hinzugefügt.
     *
     * @param bild darzustellendes Bild
     */
    @Override
    public void setImageBitmap(Bitmap bild) {
        if (getElevation() == 0 || bild.getWidth() == 0) {
            super.setImageBitmap(bild);
            return;
        }

        // je größer das Bild, um so größer muss auch der Schatten im Verhältnis sein
        float shadowSize = getElevation() * 500 / bild.getWidth();

        // Paint für den Schatten des Bildes
        final Paint ptBlur = new Paint();
        ptBlur.setMaskFilter(new BlurMaskFilter(shadowSize, BlurMaskFilter.Blur.NORMAL));

        // Der Schatten ist nicht genau unter dem Original, sondern leicht versetzt
        int[] offsetXY = new int[2];
        Bitmap bmAlpha = bild.extractAlpha(ptBlur, offsetXY);

        // Gesamtbild erzeugen und mit transparenter Farbe füllen
        final Bitmap bildMitSchatten = Bitmap.createBitmap(bmAlpha.getWidth(), bmAlpha.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bildMitSchatten);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // Farbe für den Schatten erzeugen
        final Paint ptAlphaColor = new Paint();
        ptAlphaColor.setColor(Color.DKGRAY);

        // Schatten zeichnen
        canvas.drawBitmap(bmAlpha, 0, 0, ptAlphaColor);
        bmAlpha.recycle();

        // Originales Bild zeichnen und geerbete Methode damit aufrufen
        canvas.drawBitmap(bild, - offsetXY[0] / 1.5f, - offsetXY[1] / 1.5f, null);
        super.setImageBitmap(bildMitSchatten);
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
            final AckerView ackerView = (AckerView) getParent();
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
        final Acker acker = positionsElement.gibAcker();
        final Position position = positionsElement.gibPosition();

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
