package herdenmanagement.view

import android.content.Context
import de.ba.herdenmanagement.R
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
 * Basisklasse für die Darstellung von Bundesländern wie Macklemburg-Vorpommern.
 *
 * Die AckerView ist Observer des Ackers. Werden auf letzterem Kälber, Gräser und Lebewesen
 * (insbesondere Kühe) eingefügt, informiert der Acker Objekte dieser Klasse AckerView über
 * die Änderungen. Es ist Aufgabe der AckerView für die neuen Elemente korrespondierend eine
 * [KalbView], [GrasView] oder [RindviehView] zu erzeugen und als Child-Element
 * (siehe [addViewAmimated]) anzuzeigen.
 *
 * @author Steffen Greiffenberg
 */
class AckerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), PropertyChangeListener {

    /**
     * Versieht die Statusänderung von Objekten mit einer Animation
     */
    private val animator: Animator = Animator()

    /**
     * Dargestellter Acker
     */
    var acker = Acker(3, 4)
        /**
         * Beim Setzen des Ackers werden die momentan auf diesem vorhandene PositionsElement Objekte
         * angezeigt.
         */
        set(value) {
            // Wenn die View bereits mit einem verknüpft ist,
            // wird diese Verknüpfiung jetzt aufgehoben
            animator.threading = Threading.ASYNCHRONOUS_NO_WAIT

            @Suppress("SENSELESS_COMPARISON")
            if (field != null) {
                field.entferneBeobachter(this)

                // add initial objects from acker
                field.viecher.forEach { aktualisiereVieh(it, null) }
                field.kaelber.forEach { aktualisiereKalb(it, null) }
                field.graeser.forEach { aktualisiereGraeser(it, null) }
            }

            // Der neue Acker wird gespeichert
            field = value

            // Die Verknüpfung mit dem neuen Acker herstellen
            field.fuegeBeobachterHinzu(this)
            field.viecher.forEach { aktualisiereVieh(null, it) }
            field.kaelber.forEach { aktualisiereKalb(null, it) }
            field.graeser.forEach { aktualisiereGraeser(null, it) }

            animator.threading = field.animation
        }

    /**
     * Paint to draw a text. Reused in [onDraw]
     */
    private val textPaint = TextPaint()

    /**
     * Paint to draw lines. Reused in [onDraw]
     */
    private val paint = Paint()

    /**
     * Abstand der Zellen
     */
    private var cellSpacing = 0f

    /**
     * Initialisieren der View mit ihren Attributen
     */
    init {
        setWillNotDraw(false)

        context.withStyledAttributes(attrs, R.styleable.AckerView) {
            cellSpacing = getDimension(R.styleable.AckerView_cellSpacing, 8f)
            paint.color = getColor(R.styleable.AckerView_cellBackgroundColor, Color.WHITE)
            textPaint.color = getColor(R.styleable.AckerView_android_textColor, Color.LTGRAY)
            textPaint.textSize = getDimension(R.styleable.AckerView_cellSpacing, 40f)
        }

        // Dicke der Zellen
        paint.strokeWidth = 6f
        acker.fuegeBeobachterHinzu(this)
    }

    /**
     * Measure the view and its content to determine the measured width and the
     * measured height. This method is invoked by [measure] and
     * should be overriden by subclasses to provide accurate and efficient
     * measurement of their contents.
     *
     * @param widthMeasureSpec  horizontal space requirements as imposed by the parent.
     * The requirements are encoded with
     * [android.view.View.MeasureSpec].
     * @param heightMeasureSpec vertical space requirements as imposed by the parent.
     * The requirements are encoded with
     * [android.view.View.MeasureSpec].
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // get the width from widthMeasureSpec
        val width = max(MeasureSpec.getSize(widthMeasureSpec), 0).toFloat()

        // get the height from widthMeasureSpec
        val height = max(MeasureSpec.getSize(heightMeasureSpec), 0).toFloat()

        // set LayoutParams for all childs
        var i = 0
        val count = childCount
        while (i < count) {

            // check the class, avoid ClassCastExceptions for
            // custom child views
            if (getChildAt(i) !is PositionElementView) {
                i++
                continue
            }
            val child = getChildAt(i) as PositionElementView
            val lp = child.calculateLayoutParams(width, height)

            // set exact size for child
            child.layoutParams = lp
            child.measure(lp.width or MeasureSpec.EXACTLY, lp.height or MeasureSpec.EXACTLY)
            i++
        }

        // set own size
        setMeasuredDimension(width.toInt(), height.toInt())
    }

    /**
     * Cache zur Berechnung des Textbereichs
     */
    private val textRect = Rect()

    /**
     * Während onDraw() sollten keine Objekte erzeugt werden. Deshalb wird
     * dieser Cache genutzt.
     */
    private val positionCache = Position(0, 0)

    /**
     * Zeichnet den Acker
     *
     * @param canvas Zeichenfläche
     */
    override fun onDraw(canvas: Canvas) {
        // Anzahl der Spalten und deren Breite ermitteln
        val columnWidth = width / acker.spalten.toFloat()

        // Anzahl der Zeilen und deren Höhe ermitteln
        val rowHeight = height / acker.zeilen.toFloat()

        // Zeilenweise ....
        for (y in 0 until acker.zeilen) {

            // .... alle Spalten zeichnen
            for (x in 0 until acker.spalten) {
                // Hintergrund der Zellen füllen
                canvas.drawRect(
                    x * columnWidth + cellSpacing / 2,
                    y * rowHeight + cellSpacing / 2,
                    (x + 1) * columnWidth - cellSpacing,
                    (y + 1) * rowHeight - cellSpacing,
                    paint
                )
                positionCache.x = x
                positionCache.y = y
                if (acker.istDaGras(positionCache) || acker.istDaEinKalb(positionCache)) {
                    continue
                }

                // textgröße berechnen und Text zentriert zeichnen
                val text = "$x:$y"
                textPaint.getTextBounds(text, 0, text.length, textRect)
                canvas.drawText(
                    text,
                    (x + 0.5f) * columnWidth - textRect.width() / 2.0f,
                    (y + 0.5f) * rowHeight + textRect.height() / 2.0f,
                    textPaint
                )
            }
        }
        super.onDraw(canvas)
    }

    /**
     * Called from layout when this view should
     * assign a size and position to each of its children.
     *
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
     * Legt die GUI Elemente an, wenn Kalb, Gräser etc. angelegt wurden
     *
     * @param evt Interessant sind z. B. die Nachrichten mit den Property-Name Acker.PROPERTY_KALB
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
     * Aktualisiert die Ansicht für die Gräser. Für neue Objekte
     * (newValue != null && oldValue == null) wird eine View erzeugt.
     * Bei zu entfernenden Objekte (newValue == null && oldValue != null)
     * wird auch die View entfernt.
     *
     * @param oldValue Neues Objekt auf dem Acker
     * @param newValue Bereits auf dem Acker vorhandenes Objekt
     */
    private fun aktualisiereGraeser(oldValue: Gras?, newValue: Gras?) {
        if (newValue != null && oldValue == null) {
            newValue.fuegeBeobachterHinzu(this)
            val v = GrasView(context, animator, newValue)
            newValue.fuegeBeobachterHinzu(v)
            addViewAmimated(v)
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
     * Aktualisiert die Ansicht für die Rinder. Für neue Objekte
     * (newValue != null && oldValue == null) wird eine View erzeugt.
     * Bei zu entfernenden Objekte (newValue == null && oldValue != null)
     * wird auch die View entfernt.
     *
     * @param oldValue Neues Objekt auf dem Acker
     * @param newValue Bereits auf dem Acker vorhandenes Objekt
     */
    private fun aktualisiereVieh(oldValue: Rindvieh?, newValue: Rindvieh?) {
        if (newValue != null && oldValue == null) {
            newValue.fuegeBeobachterHinzu(this)
            val v = RindviehView(context, animator, newValue)
            newValue.fuegeBeobachterHinzu(v)
            addViewAmimated(v)
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
     * Aktualisiert die Ansicht für die Kälber. Für neue Objekte
     * (newValue != null && oldValue == null) wird eine View erzeugt.
     * Bei zu entfernenden Objekte (newValue == null && oldValue != null)
     * wird auch die View entfernt.
     *
     * @param oldValue Neues Objekt auf dem Acker
     * @param newValue Bereits auf dem Acker vorhandenes Objekt
     */
    private fun aktualisiereKalb(oldValue: Kalb?, newValue: Kalb?) {
        if (newValue != null && oldValue == null) {
            newValue.fuegeBeobachterHinzu(this)
            val v = KalbView(context, animator, newValue)
            newValue.fuegeBeobachterHinzu(v)
            addViewAmimated(v)
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
     * @param view zu entfernende View
     */
    private fun removeViewAnimated(view: View) {
        animator.performAction(object : Action() {
            override fun run() {
                // Ausblenden -> Alpha = 0
                TransitionManager.beginDelayedTransition(this@AckerView)
                view.alpha = 0f

                // Ausgeblendete View entfernen
                removeView(view)

                // layout anpassen?
                requestLayout()
                invalidate()
            }
        })
    }

    /**
     * Fügt dem Acker eine neue Darstellung für Kalb, Gras, etc. hinzu
     *
     * @param view  Hinzufügende View
     */
    private fun addViewAmimated(view: View) {
        animator.performAction(object : Action() {
            override fun run() {
                // langsam einblenden: Alpha = 1
                view.alpha = 0f
                TransitionManager.beginDelayedTransition(this@AckerView)
                view.alpha = 1f

                // View hinzufügen
                addView(view)

                // layout anpassen?
                requestLayout()
                invalidate()
            }
        })
    }
}