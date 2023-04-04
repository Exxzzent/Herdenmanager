package herdenmanagement.view

import android.content.Context
import de.ba.herdenmanagement.R
import android.text.TextPaint
import herdenmanagement.model.Rindvieh
import herdenmanagement.model.Rindvieh.Status
import herdenmanagement.model.Richtung
import android.graphics.*
import android.util.AttributeSet
import herdenmanagement.model.PositionsElement

/**
 * Die Klasse erbt von [PositionElementView] und überschreibt die dortige
 * Methode [aktuellesBild], um ein Rindvieh darzustellen. Da das Rind in
 * verschiedene Richtungen schauen kann, ist die Klasse komplizierter aufgebaut
 * als zum Beispiel die [KalbView].
 *
 * @author Steffen Greiffenberg
 */
class RindviehView : PositionElementView {

    /**
     * Geerbter Constructor der Klasse [PositionElementView]
     * @constructor
     */
    constructor(
        context: Context,
        animator: Animator,
        positionsElement: PositionsElement
    ) : super(context, animator, positionsElement)

    /**
     * Default Constructor für alle Android View-Klassen
     * @constructor
     */
    constructor(context: Context) : super(context)

    /**
     * Default Constructor für alle Android View-Klassen
     * @constructor
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    /**
     * Default Constructor für alle Android View-Klassen
     * @constructor
     */
    constructor(context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style)

    /**
     * Bild einer Kuh, die in die richtige Richtung schaut
     */
    override val aktuellesBild: Bitmap
        get() {
            // ID des Bildes ermitteln
            val drawableId = when (rindvieh.status) {
                Status.FRISST -> R.drawable.kuh_gras
                Status.RAUCHT -> R.drawable.kuh_rauch
                else -> when (rindvieh.richtung) {
                    Richtung.NORD -> R.drawable.kuh_hinten
                    Richtung.WEST -> R.drawable.kuh_links
                    Richtung.SUED -> R.drawable.kuh_vorn
                    Richtung.OST -> R.drawable.kuh_rechts
                }
            }
            // Bild aus den Ressourcen ermitteln
            return BitmapFactory.decodeResource(context.resources, drawableId)
        }

    /**
     * Von dieser Klasse dargestelltes [Rindvieh]
     */
    val rindvieh: Rindvieh
        get() = positionsElement as Rindvieh

    /**
     * Reuse the text bounds in onDraw
     */
    private val textBounds = Rect()

    /**
     * Reuse the text paint in onDraw
     */
    private val textPaint = TextPaint()

    /**
     * Draw the name of the current rind centered on the bottom of the current view
     *
     * @param canvas canvas for drawing
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // test if there is a name to draw
        val name = rindvieh.name
        if (name.isEmpty()) {
            return
        }

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        val testTextSize = 48f

        // Get the bounds of the text, using our testTextSize.
        textPaint.textSize = testTextSize
        textPaint.getTextBounds(name, 0, name.length, textBounds)

        // Calculate the desired size as a proportion of our testTextSize.
        var desiredTextSize = testTextSize * (width - 20) / textBounds.width()

        // don't use font sizes larger than 40
        if (desiredTextSize > 40) {
            desiredTextSize = 40f
        }

        // Set the paint for that size.
        textPaint.textSize = desiredTextSize

        // get the text bounds with real font size
        textPaint.getTextBounds(name, 0, name.length, textBounds)

        // draw the name on bottom center
        canvas.drawText(
            rindvieh.name,
            width / 2f - textBounds.centerX(),
            (height - textBounds.height() + 10).toFloat(),
            textPaint
        )
    }

    /**
     * Setzt den Schatten
     */
    init {
        elevation = 25f

        textPaint.textSize = 40f
        textPaint.isAntiAlias = true
        textPaint.color = Color.BLACK
        textPaint.setShadowLayer(5.0f, 1.0f, 1.0f, Color.WHITE)
    }
}