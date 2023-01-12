package herdenmanagement.view

import android.content.Context
import android.widget.FrameLayout
import android.transition.TransitionManager
import androidx.appcompat.widget.AppCompatImageView
import herdenmanagement.model.PositionsElement
import android.content.res.Resources.NotFoundException
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Toast
import herdenmanagement.model.Keys
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

/**
 * Basisklasse für die Darstellung von Kälbern, Kühen, etc. Diese View kann auf Änderungen
 * an den Properties ihres PositionsElements reagieren, in der Regel durch Anpassung der Anzeige.
 * Wird die Eigenschaft elevation mit [setElevation] angepasst, werfen
 * Objekte dieser Klasse einen Schatten auf den Acker.
 *
 * Im Muster Model View Controller (MVC) sind Objekte dieser Klasse Bestandteil der View.
 *
 * Im Muster Obersever ist die Klasse ein ConcreteObserver, der PropertyChangeListener
 * ist das implementierte Observer Interface.
 *
 * @author Steffen Greiffenberg
 */
open class PositionElementView : AppCompatImageView, PropertyChangeListener {

    constructor(
        context: Context,
        animator: Animator,
        positionsElement: PositionsElement
    ) : super(context) {
        this.animator = animator
        this.positionsElement = positionsElement.copy()

        // ID des PositionsElement übernehmen
        id = positionsElement.id

        // leeres Mini-Bild setzen
        setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8))
    }

    /**
     * Default Constructor für alle Android View-Klassen
     * @constructor
     */
    constructor(context: Context) : super(context) {
        animator = Animator()
        positionsElement = PositionsElement()
    }

    /**
     * Default Constructor für alle Android View-Klassen
     * @constructor
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        animator = Animator()
        positionsElement = PositionsElement()
    }

    /**
     * Default Constructor für alle Android View-Klassen
     * @constructor
     */
    constructor(context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style) {
        animator = Animator()
        positionsElement = PositionsElement()
    }

    /**
     * Animator für die Aktualisierung der GUI
     */
    private var animator: Animator

    /**
     * [PositionsElement], welches hier dargestellt wird. Das [PositionsElement]
     * kennt seine View nicht, nur die View kennt ihr Modell.
     *
     * Im Attribut wird das zuletzt geänderte Element gespeichert, über das diese View
     * informiert wurde. Während einer Animation ist das Original dieser Kopie möglicherweise
     * in einem anderen Zustand (an anderer Position o. ä.).
     */
    var positionsElement: PositionsElement
        private set

    /**
     * Bei Änderung der Höhe des Elements muss der Schatten des
     * Bildes neu berechnet werden
     *
     * @param elevation Höhe dieser View über der ViewGroup
     */
    override fun setElevation(elevation: Float) {
        if (elevation != getElevation()) {
            super.setElevation(elevation)
            setImageBitmap(aktuellesBild)
        }
    }

    /**
     * Setzt das Bild für das Positionselement.
     * Wenn diese View einen Abstand auf der z-Achse im Property elevation
     * besitzt, wird ein Schatten hinzugefügt.
     *
     * @param bild darzustellendes Bild
     */
    final override fun setImageBitmap(bild: Bitmap) {
        if ((elevation == 0f) || (bild.width == 0)) {
            super.setImageBitmap(bild)
            return
        }

        // je größer das Bild, um so größer muss auch der Schatten im Verhältnis sein
        val shadowSize = elevation * bild.width / 500

        // Paint für den Schatten des Bildes
        val ptBlur = Paint()
        ptBlur.maskFilter = BlurMaskFilter(shadowSize, BlurMaskFilter.Blur.NORMAL)

        // Der Schatten ist nicht genau unter dem Original, sondern leicht versetzt
        val offsetXY = IntArray(2)
        val bmAlpha = bild.extractAlpha(ptBlur, offsetXY)

        // Gesamtbild erzeugen und mit transparenter Farbe füllen
        val bildMitSchatten =
            Bitmap.createBitmap(bmAlpha.width, bmAlpha.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bildMitSchatten)
        canvas.drawColor(0, PorterDuff.Mode.CLEAR)

        // Farbe für den Schatten erzeugen
        val ptAlphaColor = Paint()
        ptAlphaColor.color = Color.DKGRAY

        // Schatten zeichnen
        canvas.drawBitmap(bmAlpha, 0f, 0f, ptAlphaColor)
        bmAlpha.recycle()

        // Originales Bild zeichnen und geerbete Methode damit aufrufen
        canvas.drawBitmap(bild, -offsetXY[0] / 1.5f, -offsetXY[1] / 1.5f, null)
        super.setImageBitmap(bildMitSchatten)
    }

    /**
     * Die Klassen im Modell des HerdenManagers senden ihren View-Klassen ein
     * PropertyChangeEvent, wenn sich an einer Eigenschaft etwas geändert hat.
     * Die View-Klassen reagieren in der Regel mit der Anpssung ihrer grafischen Darstellung.
     *
     * @param evt PropertyChangeEvent, das die Änderung (Value) und die Art der Änderung (Property Name) beschreibt
     */
    override fun propertyChange(evt: PropertyChangeEvent) {
        val positionsElement = evt.newValue as PositionsElement

        // Nachricht anzeigen
        if (Keys.PROPERTY_NACHRICHT == evt.propertyName) {
            val nachricht = positionsElement.gibNachricht()

            // Ist die Nachricht ein String, wird dieser direkt angezeigt
            if (nachricht is String) {
                toast(nachricht)
            }

            // Ist die Nachricht eine Zahl, wird sie zunächst als ID im Ressourcen-Bundle interpretiert
            // Klappt das nicht, wird die Zahl gezeigt
            if (nachricht is Number) {
                try {
                    val id = nachricht.toInt()
                    val text = context.resources.getString(id, positionsElement.name)
                    toast(text)
                } catch (e: NotFoundException) {
                    toast("" + nachricht)
                }
            }
        } else if (Keys.PROPERTY_POSITION == evt.propertyName) {
            // Möglicherweise ist die GUI noch nicht geladen
            // val ackerView = parent as? AckerView ?: return

            // Bei Änderungen der Position, muss ein neues Layout berechnet werden
            animator.performAction(object : Animator.Action() {
                override fun run() {
                    // Kopie aktualisieren
                    this@PositionElementView.positionsElement = positionsElement

                    // Darstellung aktualisieren
                    val ackerView = parent as? AckerView ?: return

                    // remember current LayoutParams
                    val lp = calculateLayoutParams(
                        ackerView.width.toFloat(), ackerView.height.toFloat()
                    )

                    // Animation starten
                    if (parent is ViewGroup) {
                        TransitionManager.beginDelayedTransition(parent as ViewGroup)
                    }

                    // Position setzen
                    layoutParams = lp
                }
            })
        } else {

            // Bei Änderungen der Position, muss ein neues Layout berechnet werden
            animator.performAction(object : Animator.Action() {
                override fun run() {
                    // Kopie aktualisieren
                    this@PositionElementView.positionsElement = positionsElement

                    // Animation starten
                    if (parent is ViewGroup) {
                        TransitionManager.beginDelayedTransition(parent as ViewGroup)
                    }

                    // Bild aktualisieren
                    setImageBitmap(aktuellesBild)
                }
            })
        }
    }

    /**
     * @param width Breite der übergeordneten AckerView
     * @param height Höhe der übergeordneten AckerView
     * @return Kopie der aktuellen LayoutParams (left / right / top / bottom) basierend auf PositionsElement und AckerView
     */
    fun calculateLayoutParams(width: Float, height: Float): FrameLayout.LayoutParams {
        val acker = positionsElement.acker
        val position = positionsElement.position
        val columnWidth: Float = width / acker.spalten.toFloat()
        val rowHeight: Float = height / acker.zeilen.toFloat()

        // LayoutParams for child
        val lp = layoutParams as FrameLayout.LayoutParams
        val result = FrameLayout.LayoutParams(lp)
        result.width = columnWidth.toInt()
        result.height = rowHeight.toInt()
        result.leftMargin = (position.x * columnWidth).toInt()
        result.topMargin = (position.y * rowHeight).toInt()
        return result
    }

    /**
     * Zeigt den Toast mittels Animator, dadurch wird auch stets der
     * UI Thread zur Anzeige verwendet.
     *
     * @param text Anzuzeigender Text
     */
    private fun toast(text: String) {
        animator.performAction(object : Animator.Action() {
            override fun run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Diese Methode sollte von allen erbenden Klassen überladen werden, um entsprechend des
     * PositionsElements ein passendes Bild zu liefern.
     *
     * Bild auf Basis des [PositionsElement]
     */
    protected open val aktuellesBild: Bitmap
        get() = Bitmap.createBitmap(0, 0, Bitmap.Config.ALPHA_8)
}