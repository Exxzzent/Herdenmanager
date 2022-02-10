package herdenmanagement.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.ba.herdenmanagement.R;
import herdenmanagement.model.Acker;
import herdenmanagement.model.Eimer;
import herdenmanagement.model.Gras;
import herdenmanagement.model.Rindvieh;

/**
 * Basisklasse für die Darstellung von Bundesländern wie Macklemburg-Vorpommern.
 * <p>
 * Die AckerView ist Observer des Ackers. Werden auf letzterem Eimer, Gräser und Lebewesen
 * (insbesondere Kühe) eingefügt, informiert der Acker Objekte dieser Klasse AckerView über
 * die Änderungen. Es ist Aufgabe der AckerView für die neuen Elemente korrespondierend eine
 * {@link EimerView}, {@link GrasView} oder {@link RindviehView} zu erzeugen und als Child-Element
 * (siehe {@link #addView(View)}) anzuzeigen.
 *
 * @author Steffen Greiffenberg
 */
public class AckerView extends FrameLayout implements PropertyChangeListener {

    /**
     * Versieht die Statusänderung von Objekten mit einer Animation
     */
    private final Animator animator;

    /**
     * Dargestellter Acker
     */
    private Acker acker;

    /**
     * Paint to draw a text. Reused in {@link #onDraw(Canvas)}
     */
    private final TextPaint textPaint = new TextPaint();

    /**
     * Paint to draw lines. Reused in {@link #onDraw(Canvas)}
     */
    private final Paint paint = new Paint();

    /**
     * Abstand der Zellen
     */
    private float cellSpacing;

    /**
     * Erzeugt eine neue Ansicht für einen Acker
     *
     * @param context Context der App
     */
    public AckerView(Context context) {
        super(context, null, R.attr.ackerViewStyle);
        initAckerView(null, R.attr.ackerViewStyle);

        setWillNotDraw(false);
        animator = new Animator(context);
    }

    /**
     * Erzeugt eine neue Ansicht für einen Acker
     *
     * @param context Context der App
     * @param attrs   Attribute zur grafischen Darstellung
     */
    public AckerView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.ackerViewStyle);
        initAckerView(attrs, R.attr.ackerViewStyle);

        setWillNotDraw(false);
        animator = new Animator(context);
    }

    /**
     * Erzeugt eine neue Ansicht für einen Acker
     *
     * @param context      Context der App
     * @param attrs        Attribute zur grafischen Darstellung
     * @param defStyleAttr Attribute zum Stil
     */
    public AckerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAckerView(attrs, defStyleAttr);

        setWillNotDraw(false);
        animator = new Animator(context);
    }

    /**
     * Initialisieren der View mit ihren Attributen
     *
     * @param attrs Attribute des aktuellen Layouts
     * @param defStyleAttr Voreingestellte Attribute
     */
    private void initAckerView(AttributeSet attrs, int defStyleAttr) {
        // Attribute incl. default-Werte lesen
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.AckerView, defStyleAttr, R.style.AckerViewStyle);

        // Abstand der Zellen
        cellSpacing = a.getDimensionPixelSize(R.styleable.AckerView_cellSpacing, 8);

        // Färbung der Zellen
        int cellColor = a.getColor(R.styleable.AckerView_cellBackgroundColor, Color.WHITE);
        paint.setColor(cellColor);
        paint.setStrokeWidth(6);

        // Text in den Zellen
        int textColor = a.getColor(R.styleable.AckerView_android_textColor, Color.LTGRAY);
        textPaint.setColor(textColor);
        int textSize = a.getDimensionPixelSize(R.styleable.AckerView_android_textSize, 40);
        textPaint.setTextSize(textSize);

        a.recycle();
    }

    public Animator.Threading getThreading() {
        return animator.getThreading();
    }

    public void setThreading(Animator.Threading threading) {
        animator.setThreading(threading);
    }

    /**
     * Beim Setzen des Ackers werden die momentan auf diesem vorhandene PositionsElement Objekte
     * angezeigt.
     *
     * @param acker Acker zur Ansicht
     */
    public void setAcker(Acker acker) {
        // Wenn die View bereits mit einem verknüpft ist,
        // wird diese Verknüpfiung jetzt aufgehoben
        if (this.acker != null) {
            this.acker.entferneBeobachter(this);

            // add initial objects from acker
            for (Rindvieh rindvieh : this.acker.getViecher()) {
                aktualisiereViecher(rindvieh, null);
            }

            for (Eimer eimer : this.acker.getEimer()) {
                aktualisiereEimer(eimer, null);
            }

            for (Gras gras : this.acker.getGraeser()) {
                aktualisiereGraeser(gras, null);
            }
        }

        // Der neue Acker wird gespeichert
        this.acker = acker;
        if (acker == null) {
            return;
        }

        // Die Verknüpfung mit dem neuen Acker herstellen
        this.acker.fuegeBeobachterHinzu(this);
        for (Rindvieh rindvieh : acker.getViecher()) {
            aktualisiereViecher(null, rindvieh);
        }

        for (Eimer eimer : acker.getEimer()) {
            aktualisiereEimer(null, eimer);
        }

        for (Gras gras : acker.getGraeser()) {
            aktualisiereGraeser(null, gras);
        }
    }

    /**
     * <p>
     * Measure the view and its content to determine the measured width and the
     * measured height. This method is invoked by {@link #measure(int, int)} and
     * should be overriden by subclasses to provide accurate and efficient
     * measurement of their contents.
     * </p>
     *
     * @param widthMeasureSpec  horizontal space requirements as imposed by the parent.
     *                          The requirements are encoded with
     *                          {@link android.view.View.MeasureSpec}.
     * @param heightMeasureSpec vertical space requirements as imposed by the parent.
     *                          The requirements are encoded with
     *                          {@link android.view.View.MeasureSpec}.
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // get the width from widthMeasureSpec
        float width = Math.max(MeasureSpec.getSize(widthMeasureSpec), 0);

        // get the height from widthMeasureSpec
        float height = Math.max(MeasureSpec.getSize(heightMeasureSpec), 0);

        // set LayoutParams for all childs
        for (int i = 0, count = getChildCount(); i < count; i++) {
            // check the class, avoid ClassCastExceptions for
            // custom child views
            if (!(getChildAt(i) instanceof PositionElementView)) {
                continue;
            }

            PositionElementView child = (PositionElementView) getChildAt(i);
            LayoutParams lp = child.calculateLayoutParams(width, height);

            // set exact size for child
            child.setLayoutParams(lp);
            child.measure(lp.width | MeasureSpec.EXACTLY, lp.height | MeasureSpec.EXACTLY);
        }

        // set own size
        setMeasuredDimension((int) width, (int) height);
    }

    /**
     * Cache zur Berechnung des Textbereichs
     */
    private final Rect TEXT_RECT = new Rect();

    /**
     * Zeichnet den Acker
     *
     * @param canvas Zeichenfläche
     */
    protected void onDraw(Canvas canvas) {
        // Anzahl der Spalten und deren Breite ermitteln
        int columns = acker == null ? 3 : acker.zaehleSpalten();
        float columnWidth = getWidth() / (float) columns;

        // Anzahl der Zeilen und deren Höhe ermitteln
        int rows = acker == null ? 5 : acker.zaehleZeilen();
        float rowHeight = getHeight() / (float) rows;

        // Zeilenweise ....
        for (int y = 0; y < rows; y++) {

            // .... alle Spalten zeichnen
            for (int x = 0; x < columns; x++) {
                // Hintergrund der Zellen füllen
                canvas.drawRect(
                        x * columnWidth + cellSpacing / 2,
                        y * rowHeight + cellSpacing / 2,
                        (x + 1) * columnWidth - cellSpacing,
                        (y + 1) * rowHeight - cellSpacing,
                        paint);

                // textgröße berechnen und Text zentriert zeichnen
                String text = x + ":" + y;
                textPaint.getTextBounds(text, 0, text.length(), TEXT_RECT);
                canvas.drawText(
                        text,
                        ((x + 0.5f) * columnWidth) - (TEXT_RECT.width() / 2.0f),
                        ((y + 0.5f) * rowHeight) + (TEXT_RECT.height() / 2.0f),
                        textPaint);
            }
        }

        super.onDraw(canvas);
    }

    /**
     * Called from layout when this view should
     * assign a size and position to each of its children.
     * <p/>
     * Derived classes with children should override
     * this method and call layout on each of
     * their children.
     *
     * @param changed This is a new size or position for this view
     * @param left    Left position, relative to parent
     * @param top     Top position, relative to parent
     * @param right   Right position, relative to parent
     * @param bottom  Bottom position, relative to parent
     */
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();

            child.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + lp.width, lp.topMargin + lp.height);
        }
    }

    /**
     * Legt die GUI Elemente an, wenn Eimer, Gräser etc. angelegt wurden
     *
     * @param evt Interessant sind z.B. die Nachrichten mit den Property-Name Acker.PROPERTY_EIMER
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (Acker.PROPERTY_EIMER.equals(evt.getPropertyName())) {
            aktualisiereEimer((Eimer) evt.getOldValue(), (Eimer) evt.getNewValue());
        }

        if (Acker.PROPERTY_VIECHER.equals(evt.getPropertyName())) {
            aktualisiereViecher((Rindvieh) evt.getOldValue(), (Rindvieh) evt.getNewValue());
        }

        if (Acker.PROPERTY_GRAESER.equals(evt.getPropertyName())) {
            aktualisiereGraeser((Gras) evt.getOldValue(), (Gras) evt.getNewValue());
        }
    }

    /**
     * Aktualisiert die Ansicht für die Gräser. Für neue Objekte
     * (newValue != null && oldValue == null) wird eine View erzeugt.
     * Bei zu entfernenden Objekte (newValue == null && oldValue != null)
     * wird auch die View entfernt.
     *
     * @param oldValue Neues Objekt auf dem Acker
     * @param newValue Bereits auf dem Acker vorhandenes Objekt
     */
    private void aktualisiereGraeser(final Gras oldValue, final Gras newValue) {
        if (newValue != null && oldValue == null) {
            newValue.fuegeBeobachterHinzu(this);
            addViewAmimated(new GrasView(getContext(), animator, newValue), false);
        } else if (newValue == null && oldValue != null) {
            oldValue.entferneBeobachter(this);
            removeViewAnimated(oldValue.gibId());
        }
    }

    /**
     * Aktualisiert die Ansicht für die Rinder. Für neue Objekte
     * (newValue != null && oldValue == null) wird eine View erzeugt.
     * Bei zu entfernenden Objekte (newValue == null && oldValue != null)
     * wird auch die View entfernt.
     *
     * @param oldValue Neues Objekt auf dem Acker
     * @param newValue Bereits auf dem Acker vorhandenes Objekt
     */
    private void aktualisiereViecher(final Rindvieh oldValue, final Rindvieh newValue) {
        if (newValue != null && oldValue == null) {
            newValue.fuegeBeobachterHinzu(this);
            addViewAmimated(new RindviehView(getContext(), animator, newValue), true);
        } else if (newValue == null && oldValue != null) {
            oldValue.entferneBeobachter(this);
            removeViewAnimated(oldValue.gibId());
        }
    }

    /**
     * Aktualisiert die Ansicht für die Eimer. Für neue Objekte
     * (newValue != null && oldValue == null) wird eine View erzeugt.
     * Bei zu entfernenden Objekte (newValue == null && oldValue != null)
     * wird auch die View entfernt.
     *
     * @param oldValue Neues Objekt auf dem Acker
     * @param newValue Bereits auf dem Acker vorhandenes Objekt
     */
    private void aktualisiereEimer(final Eimer oldValue, final Eimer newValue) {
        if (newValue != null && oldValue == null) {
            newValue.fuegeBeobachterHinzu(this);
            addViewAmimated(new EimerView(getContext(), animator, newValue), false);
        } else if (newValue == null && oldValue != null) {
            oldValue.entferneBeobachter(this);
            removeViewAnimated(oldValue.gibId());
        }
    }

    /**
     * @param id Ressourcen-ID der zu entfernenden View
     */
    private void removeViewAnimated(final int id) {
        animator.performAction(new Animator.Action() {
            @Override
            public void run() {
                // fade out the view
                View v = findViewById(id);

                // avoid NPE
                if (v == null) {
                    return;
                }

                // fade out
                TransitionManager.beginDelayedTransition(AckerView.this);
                v.setAlpha(0);

                // remove the view
                removeView(v);

                // layout anpassen?
                requestLayout();
                invalidate();
            }
        });
    }

    /**
     * Fügt dem Acker eine neue Darstellung für Eimer, Gras, etc. hinzu
     *
     * @param view  Hinzufügende View
     * @param onTop true fürgt die View über alle anderen ein
     */
    private void addViewAmimated(final View view, final boolean onTop) {
        animator.performAction(new Animator.Action() {
            @Override
            public void run() {
                // langsam einblenden
                view.setAlpha(0);

                TransitionManager.beginDelayedTransition(AckerView.this);
                view.setAlpha(1);

                if (onTop) {
                    addView(view);
                } else {
                    addView(view, 0);
                }

                // layout anpassen?
                requestLayout();
                invalidate();
            }
        });
    }

    /**
     * @return Dargestellter Acker
     */
    public Acker getAcker() {
        return acker;
    }
}
