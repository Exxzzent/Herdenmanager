package herdenmanagement.view

import android.content.Context
import de.dhsn.herdenmanagement.R
import android.widget.FrameLayout
import android.text.TextPaint
import android.transition.TransitionManager
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import herdenmanagement.model.*
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import kotlin.math.max

/**
 * Repräsentiert die AckerView, die als visuelle Darstellung eines Ackers dient.
 *
 * Die AckerView ist Observer des [Acker]-Modells. Wenn im Acker neue Elemente wie Kälber, Gräser
 * oder Rinder eingefügt bzw. entfernt werden, wird die AckerView über diese Änderungen informiert und
 * passt ihre Darstellung entsprechend an. Hierzu werden beispielsweise passende Child-Views (z. B. [KalbView],
 * [GrasView] oder [RindviehView]) erzeugt und animiert hinzugefügt oder entfernt.
 *
 * Didaktische Hinweise:
 * - **Model-View-Controller (MVC):**
 *   Diese Klasse illustriert die Trennung von Model (Acker, PositionsElemente) und View (AckerView).
 *   Änderungen im Model werden über das Observer-Muster (PropertyChangeListener) an die View kommuniziert.
 * - **Dynamische Layout-Anpassung:**
 *   Die Methode [calculateLayoutParams] zeigt, wie aus den Daten des Models dynamisch Layout-Parameter
 *   berechnet werden, um die Position und Größe der Kind-Views festzulegen.
 * - **Animation und Transition:**
 *   Mithilfe von [TransitionManager.beginDelayedTransition] werden sanfte Übergänge bei Änderungen im Layout
 *   und bei der Sichtbarkeit der Elemente realisiert.
 *
 * @author Steffen Greiffenberg
 */
class AckerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), PropertyChangeListener {

    /**
     * Animator zur Steuerung der Animationen und UI-Aktualisierungen.
     */
    private val animator: Animator = Animator()

    /**
     * Das im Model dargestellte Acker-Objekt.
     *
     * Beim Setzen eines neuen Ackers wird die View zunächst von der alten Instanz abgemeldet
     * und anschließend an den neuen Acker gekoppelt. Die Initialobjekte des Ackers werden dann
     * anhand ihrer Typen (Rindvieh, Kalb, Gras) in der View angezeigt.
     */
    var acker = Acker(3, 4)
        set(value) {
            // Wechsle in den asynchronen No-Wait Modus, um initiale Aktionen ohne Verzögerung anzuzeigen
            animator.threading = Threading.ASYNCHRONOUS_NO_WAIT

            @Suppress("SENSELESS_COMPARISON")
            if (field != null) {
                // View von alter Acker-Instanz abmelden und vorhandene Elemente initial anzeigen
                field.entferneBeobachter(this)
                field.viecher.forEach { aktualisiereVieh(it, null) }
                field.kaelber.forEach { aktualisiereKalb(it, null) }
                field.graeser.forEach { aktualisiereGraeser(it, null) }
            }

            // Speichern des neuen Ackers
            field = value

            // View an den neuen Acker binden
            field.fuegeBeobachterHinzu(this)
            field.viecher.forEach { aktualisiereVieh(null, it) }
            field.kaelber.forEach { aktualisiereKalb(null, it) }
            field.graeser.forEach { aktualisiereGraeser(null, it) }

            animator.threading = field.animation
        }

    /**
     * Paint-Objekt zur Darstellung von Zell-Hintergrundfarben.
     */
    private val paint = Paint()

    /**
     * TextPaint zur Darstellung von Text in den Zellen.
     */
    private val textPaint = TextPaint()

    /**
     * Abstand zwischen den Zellen (in Pixeln).
     */
    private var cellSpacing = 0f

    /**
     * Cache für die Berechnung von Textbereichen, um during onDraw() keine neuen Objekte zu erzeugen.
     */
    private val textRect = Rect()

    /**
     * Wiederverwendbarer Cache für Positionen während des Zeichnens.
     */
    private val positionCache = Position(0, 0)

    /**
     * Initialisierung der AckerView.
     *
     * Hier werden die benutzerdefinierten Attribute (z. B. cellSpacing, Zellhintergrundfarbe) aus dem XML
     * geladen und die AckerView als Observer beim Acker registriert.
     */
    init {
        setWillNotDraw(false)
        context.withStyledAttributes(attrs, R.styleable.AckerView) {
            cellSpacing = getDimension(R.styleable.AckerView_cellSpacing, 8f)
            paint.color = getColor(R.styleable.AckerView_cellBackgroundColor, Color.WHITE)
            textPaint.color = getColor(R.styleable.AckerView_android_textColor, Color.LTGRAY)
            textPaint.textSize = getDimension(R.styleable.AckerView_cellSpacing, 40f)
        }
        paint.strokeWidth = 6f // Definiert die Dicke der Zellränder
        acker.fuegeBeobachterHinzu(this)
    }

    /**
     * Misst die Größe der AckerView sowie ihrer Kind-Views.
     *
     * Die Methode berechnet die Größe jeder Zelle basierend auf der Anzahl der Spalten und Zeilen im Acker
     * und weist diesen Wert dann den Kind-Views zu.
     *
     * @param widthMeasureSpec Horizontaler Messmodus, wie von der Parent-View vorgegeben.
     * @param heightMeasureSpec Vertikaler Messmodus, wie von der Parent-View vorgegeben.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = max(MeasureSpec.getSize(widthMeasureSpec), 0).toFloat()
        val height = max(MeasureSpec.getSize(heightMeasureSpec), 0).toFloat()

        // Passe die Layout-Parameter aller Kind-Views an
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child !is PositionElementView) continue

            val lp = child.calculateLayoutParams(width, height)
            child.layoutParams = lp
            child.measure(lp.width or MeasureSpec.EXACTLY, lp.height or MeasureSpec.EXACTLY)
        }
        setMeasuredDimension(width.toInt(), height.toInt())
    }

    /**
     * Zeichnet den Acker-Hintergrund und die Zell-Gitter.
     *
     * Für jede Zelle wird ein farbiger Hintergrund gezeichnet. In leeren Zellen (ohne Gras oder Kalb)
     * wird zudem die Zellkoordinate als Text zentriert dargestellt.
     *
     * @param canvas Die Zeichenfläche, auf der der Acker gezeichnet wird.
     */
    override fun onDraw(canvas: Canvas) {
        val columnWidth = width / acker.spalten.toFloat()
        val rowHeight = height / acker.zeilen.toFloat()

        // Zeichne alle Zellen in einer Matrix
        for (y in 0 until acker.zeilen) {
            for (x in 0 until acker.spalten) {
                canvas.drawRect(
                    x * columnWidth + cellSpacing / 2,
                    y * rowHeight + cellSpacing / 2,
                    (x + 1) * columnWidth - cellSpacing,
                    (y + 1) * rowHeight - cellSpacing,
                    paint
                )
                positionCache.x = x
                positionCache.y = y

                // Überspringe Zellen, in denen Gras oder ein Kalb vorhanden sind
                if (acker.istDaGras(positionCache) || acker.istDaEinKalb(positionCache)) continue

                val text = "$x:$y"
                textPaint.getTextBounds(text, 0, text.length, textRect)
                canvas.drawText(
                    text,
                    (x + 0.5f) * columnWidth - textRect.width() / 2f,
                    (y + 0.5f) * rowHeight + textRect.height() / 2f,
                    textPaint
                )
            }
        }
        super.onDraw(canvas)
    }

    /**
     * Ordnet den Child-Views ihre Positionen und Größen zu.
     *
     * Diese Methode wird vom Parent-Layout aufgerufen und weist den Child-Views basierend auf
     * ihren Layout-Params die Position innerhalb der AckerView zu.
     *
     * @param changed Flag, das angibt, ob sich Größe oder Position der View geändert haben.
     * @param left Linke Position relativ zum Parent.
     * @param top Obere Position relativ zum Parent.
     * @param right Rechte Position relativ zum Parent.
     * @param bottom Untere Position relativ zum Parent.
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            child.layout(
                lp.leftMargin,
                lp.topMargin,
                lp.leftMargin + lp.width,
                lp.topMargin + lp.height
            )
        }
    }

    /**
     * Reagiert auf [PropertyChangeEvent]s des Ackers.
     *
     * Abhängig vom Property-Namen (z. B. [Keys.PROPERTY_KALB], [Keys.PROPERTY_VIECHER],
     * [Keys.PROPERTY_GRAESER] oder [Keys.PROPERTY_SIZE]) wird die Darstellung aktualisiert, indem
     * entsprechende Views hinzugefügt, entfernt oder angepasst werden.
     *
     * @param evt Das Event, das die Änderung im Model beschreibt.
     */
    override fun propertyChange(evt: PropertyChangeEvent) {
        when (evt.propertyName) {
            Keys.PROPERTY_KALB -> aktualisiereKalb(evt.oldValue as? Kalb, evt.newValue as? Kalb)
            Keys.PROPERTY_VIECHER -> aktualisiereVieh(
                evt.oldValue as? Rindvieh,
                evt.newValue as? Rindvieh
            )
            Keys.PROPERTY_GRAESER -> aktualisiereGraeser(
                evt.oldValue as? Gras,
                evt.newValue as? Gras
            )
            Keys.PROPERTY_SIZE -> animator.performAction(object : Action() {
                override fun run() {
                    requestLayout()
                    invalidate()
                }
            })
            Keys.PROPERTY_THREADING -> animator.threading = evt.newValue as Threading
        }
    }

    /**
     * Aktualisiert die Ansicht für Gräser.
     *
     * Für neue Gräser (newValue != null und oldValue == null) wird eine neue [GrasView] erzeugt und
     * animiert hinzugefügt. Für entfernte Gräser (newValue == null und oldValue != null) wird die
     * zugehörige View entfernt.
     *
     * @param oldValue Das zuvor vorhandene [Gras]-Objekt (falls vorhanden).
     * @param newValue Das neue [Gras]-Objekt (falls vorhanden).
     */
    private fun aktualisiereGraeser(oldValue: Gras?, newValue: Gras?) {
        if (newValue != null && oldValue == null) {
            newValue.fuegeBeobachterHinzu(this)
            val v = GrasView(context, animator, newValue)
            newValue.fuegeBeobachterHinzu(v)
            addViewAmated(v)
        } else if (newValue == null && oldValue != null) {
            oldValue.entferneBeobachter(this)
            val v = findViewById<View>(oldValue.id)
            if (v is GrasView) {
                oldValue.entferneBeobachter(v)
                removeViewAnimated(v)
            }
        }
    }

    /**
     * Aktualisiert die Ansicht für Rinder.
     *
     * Für neue Rinder (newValue != null und oldValue == null) wird eine neue [RindviehView] erzeugt und
     * animiert hinzugefügt. Für entfernte Rinder (newValue == null und oldValue != null) wird die
     * zugehörige View entfernt.
     *
     * @param oldValue Das zuvor vorhandene [Rindvieh]-Objekt (falls vorhanden).
     * @param newValue Das neue [Rindvieh]-Objekt (falls vorhanden).
     */
    private fun aktualisiereVieh(oldValue: Rindvieh?, newValue: Rindvieh?) {
        if (newValue != null && oldValue == null) {
            newValue.fuegeBeobachterHinzu(this)
            val v = RindviehView(context, animator, newValue)
            newValue.fuegeBeobachterHinzu(v)
            addViewAmated(v)
        } else if (newValue == null && oldValue != null) {
            oldValue.entferneBeobachter(this)
            val v = findViewById<View>(oldValue.id)
            if (v is RindviehView) {
                oldValue.entferneBeobachter(v)
                removeViewAnimated(v)
            }
        }
    }

    /**
     * Aktualisiert die Ansicht für Kälber.
     *
     * Für neue Kälber (newValue != null und oldValue == null) wird eine neue [KalbView] erzeugt und
     * animiert hinzugefügt. Für entfernte Kälber (newValue == null und oldValue != null) wird die
     * zugehörige View entfernt.
     *
     * @param oldValue Das zuvor vorhandene [Kalb]-Objekt (falls vorhanden).
     * @param newValue Das neue [Kalb]-Objekt (falls vorhanden).
     */
    private fun aktualisiereKalb(oldValue: Kalb?, newValue: Kalb?) {
        if (newValue != null && oldValue == null) {
            newValue.fuegeBeobachterHinzu(this)
            val v = KalbView(context, animator, newValue)
            newValue.fuegeBeobachterHinzu(v)
            addViewAmated(v)
        } else if (newValue == null && oldValue != null) {
            oldValue.entferneBeobachter(this)
            val v = findViewById<View>(oldValue.id)
            if (v is KalbView) {
                oldValue.entferneBeobachter(v)
                removeViewAnimated(v)
            }
        }
    }

    /**
     * Entfernt eine Child-View mit einer sanften Animation.
     *
     * Die View wird zunächst ausgeblendet (Alpha auf 0 gesetzt), dann entfernt und das Layout
     * neu berechnet.
     *
     * @param view Die zu entfernende View.
     */
    private fun removeViewAnimated(view: View) {
        animator.performAction(object : Action() {
            override fun run() {
                TransitionManager.beginDelayedTransition(this@AckerView)
                view.alpha = 0f
                removeView(view)
                requestLayout()
                invalidate()
            }
        })
    }

    /**
     * Fügt eine neue Child-View mit einer sanften Animation hinzu.
     *
     * Die View wird langsam eingeblendet (Alpha von 0 auf 1) und anschließend dem Layout hinzugefügt.
     *
     * @param view Die hinzuzufügende View.
     */
    private fun addViewAmated(view: View) {
        animator.performAction(object : Action() {
            override fun run() {
                view.alpha = 0f
                TransitionManager.beginDelayedTransition(this@AckerView)
                view.alpha = 1f
                addView(view)
                requestLayout()
                invalidate()
            }
        })
    }
}