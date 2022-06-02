package herdenmanagement.view

import android.content.Context
import de.ba.herdenmanagement.R
import android.widget.FrameLayout
import android.text.TextPaint
import android.transition.TransitionManager
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import herdenmanagement.model.*
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

/**
 * Basisklasse für die Darstellung von Bundesländern wie Macklemburg-Vorpommern.
 *
 *
 * Die AckerView ist Observer des Ackers. Werden auf letzterem Kälber, Gräser und Lebewesen
 * (insbesondere Kühe) eingefügt, informiert der Acker Objekte dieser Klasse AckerView über
 * die Änderungen. Es ist Aufgabe der AckerView für die neuen Elemente korrespondierend eine
 * [KalbView], [GrasView] oder [RindviehView] zu erzeugen und als Child-Element
 * (siehe [.addView]) anzuzeigen.
 *
 * @author Steffen Greiffenberg
 */
class AckerView : FrameLayout, PropertyChangeListener {

    /**
     * Versieht die Statusänderung von Objekten mit einer Animation
     */
    private val animator: Animator = Animator()

    /**
     * Dargestellter Acker
     */
    internal var acker: Acker
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
                for (rindvieh in field.viecher) {
                    aktualisiereVieh(rindvieh, null)
                }
                for (kalb in field.kaelber) {
                    aktualisiereKalb(kalb, null)
                }
                for (gras in field.graeser) {
                    aktualisiereGraeser(gras, null)
                }
            }

            // Der neue Acker wird gespeichert
            field = value

            // Die Verknüpfung mit dem neuen Acker herstellen
            field.fuegeBeobachterHinzu(this)
            for (rindvieh in field.viecher) {
                aktualisiereVieh(null, rindvieh)
            }
            for (kalb in field.kaelber) {
                aktualisiereKalb(null, kalb)
            }
            for (gras in field.graeser) {
                aktualisiereGraeser(null, gras)
            }
            animator.threading = field.animation
        }

    /**
     * Paint to draw a text. Reused in [.onDraw]
     */
    private val textPaint = TextPaint()

    /**
     * Paint to draw lines. Reused in [.onDraw]
     */
    private val paint = Paint()

    /**
     * Abstand der Zellen
     */
    private var cellSpacing = 0f

    /**
     * Erzeugt eine neue Ansicht für einen Acker
     *
     * @param context Context der App
     */
    constructor(context: Context?) : super(context!!, null, R.attr.ackerViewStyle) {
        acker = Acker(3, 4)
        initAckerView(null, R.attr.ackerViewStyle)
        setWillNotDraw(false)
    }

    /**
     * Erzeugt eine neue Ansicht für einen Acker
     *
     * @param context Context der App
     * @param attrs   Attribute zur grafischen Darstellung
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs,
        R.attr.ackerViewStyle
    ) {
        acker = Acker(3, 4)
        initAckerView(attrs, R.attr.ackerViewStyle)
        setWillNotDraw(false)
    }

    /**
     * Erzeugt eine neue Ansicht für einen Acker
     *
     * @param context      Context der App
     * @param attrs        Attribute zur grafischen Darstellung
     * @param defStyleAttr Attribute zum Stil
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        acker = Acker(3, 4)
        initAckerView(attrs, defStyleAttr)
        setWillNotDraw(false)
    }

    /**
     * Initialisieren der View mit ihren Attributen
     *
     * @param attrs Attribute des aktuellen Layouts
     * @param defStyleAttr Voreingestellte Attribute
     */
    private fun initAckerView(attrs: AttributeSet?, defStyleAttr: Int) {
        // Attribute incl. default-Werte lesen
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.AckerView, defStyleAttr, R.style.AckerViewStyle
        )

        // Abstand der Zellen
        cellSpacing = a.getDimensionPixelSize(R.styleable.AckerView_cellSpacing, 8).toFloat()

        // Färbung der Zellen
        val cellColor = a.getColor(R.styleable.AckerView_cellBackgroundColor, Color.WHITE)
        paint.color = cellColor
        paint.strokeWidth = 6f

        // Text in den Zellen
        val textColor = a.getColor(R.styleable.AckerView_android_textColor, Color.LTGRAY)
        textPaint.color = textColor
        val textSize = a.getDimensionPixelSize(R.styleable.AckerView_android_textSize, 40)
        textPaint.textSize = textSize.toFloat()
        a.recycle()
    }

    /**
     * Measure the view and its content to determine the measured width and the
     * measured height. This method is invoked by [.measure] and
     * should be overriden by subclasses to provide accurate and efficient
     * measurement of their contents.
     *
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
        val width = Math.max(MeasureSpec.getSize(widthMeasureSpec), 0).toFloat()

        // get the height from widthMeasureSpec
        val height = Math.max(MeasureSpec.getSize(heightMeasureSpec), 0).toFloat()

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
    private val TEXT_RECT = Rect()

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
                textPaint.getTextBounds(text, 0, text.length, TEXT_RECT)
                canvas.drawText(
                    text,
                    (x + 0.5f) * columnWidth - TEXT_RECT.width() / 2.0f,
                    (y + 0.5f) * rowHeight + TEXT_RECT.height() / 2.0f,
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
     * @param evt Interessant sind z.B. die Nachrichten mit den Property-Name Acker.PROPERTY_KALB
     */
    override fun propertyChange(evt: PropertyChangeEvent) {
        if (Keys.PROPERTY_KALB == evt.propertyName) {
            aktualisiereKalb(evt.oldValue as? Kalb, evt.newValue as? Kalb)
        }
        if (Keys.PROPERTY_VIECHER == evt.propertyName) {
            aktualisiereVieh(evt.oldValue as? Rindvieh, evt.newValue as? Rindvieh)
        }
        if (Keys.PROPERTY_GRAESER == evt.propertyName) {
            aktualisiereGraeser(evt.oldValue as? Gras, evt.newValue as? Gras)
        }
        if (Keys.PROPERTY_THREADING == evt.propertyName) {
            animator.threading = evt.newValue as Threading
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
            addViewAmimated(GrasView(context, animator, newValue))
        } else if (newValue == null && oldValue != null) {
            oldValue.entferneBeobachter(this)
            removeViewAnimated(oldValue.id)
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
            addViewAmimated(RindviehView(context, animator, newValue))
        } else if (newValue == null && oldValue != null) {
            oldValue.entferneBeobachter(this)
            removeViewAnimated(oldValue.id)
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
            addViewAmimated(KalbView(context, animator, newValue))
        } else if (newValue == null && oldValue != null) {
            oldValue.entferneBeobachter(this)
            removeViewAnimated(oldValue.id)
        }
    }

    /**
     * @param id Ressourcen-ID der zu entfernenden View
     */
    private fun removeViewAnimated(id: Int) {
        animator.performAction(object : Animator.Action() {
            override fun run() {
                // fade out the view
                val v = findViewById<View>(id) ?: return

                // avoid NPE

                // fade out
                TransitionManager.beginDelayedTransition(this@AckerView)
                v.alpha = 0f

                // remove the view
                removeView(v)

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
        animator.performAction(object : Animator.Action() {
            override fun run() {
                // langsam einblenden
                view.alpha = 0f
                TransitionManager.beginDelayedTransition(this@AckerView)
                view.alpha = 1f
                addView(view)

                // layout anpassen?
                requestLayout()
                invalidate()
            }
        })
    }
}