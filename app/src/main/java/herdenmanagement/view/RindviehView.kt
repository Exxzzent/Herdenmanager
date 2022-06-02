package herdenmanagement.view

import android.content.Context
import de.ba.herdenmanagement.R
import android.text.TextPaint
import herdenmanagement.model.Rindvieh
import herdenmanagement.model.Rindvieh.StatusTyp
import herdenmanagement.model.Rindvieh.RichtungsTyp
import android.graphics.*

/**
 * Die Klasse erbt von [PositionElementView] und überschreibt die dortige
 * Methode [.getAktuellesBild], um ein Rindvieh darzustellen. Da das Rind in
 * verschiedene Richtungen schauen kann, ist die Klasse komplizierter aufgebaut
 * als zum Beispiel die [KalbView].
 *
 * @author Steffen Greiffenberg
 */
class RindviehView(context: Context, animator: Animator, rindvieh: Rindvieh) :
    PositionElementView(context, animator, rindvieh) {

    /**
     * @return Bild einer Kuh, die in die richtige Richtung schaut
     */
    override val aktuellesBild: Bitmap
        get() {
            if (StatusTyp.FRISST == this.rindvieh.status) {
                return BitmapFactory.decodeResource(context.resources, R.drawable.kuh_gras)
            } else if (StatusTyp.RAUCHT == this.rindvieh.status) {
                return BitmapFactory.decodeResource(context.resources, R.drawable.kuh_rauch)
            }

            if (this.rindvieh.richtung == RichtungsTyp.NORD) {
                return BitmapFactory.decodeResource(context.resources, R.drawable.kuh_hinten)
            } else if (this.rindvieh.richtung == RichtungsTyp.WEST) {
                return BitmapFactory.decodeResource(context.resources, R.drawable.kuh_links)
            } else if (this.rindvieh.richtung == RichtungsTyp.SUED) {
                return BitmapFactory.decodeResource(context.resources, R.drawable.kuh_vorn)
            } else if (this.rindvieh.richtung == RichtungsTyp.OST) {
                return BitmapFactory.decodeResource(context.resources, R.drawable.kuh_rechts)
            }

            return BitmapFactory.decodeResource(context.resources, R.drawable.kuh_vorn)
        }

    /**
     * @return Von dieser Klasse dargestelltes [Rindvieh]
     */
    val rindvieh: Rindvieh
        get() = positionsElement as Rindvieh

    /**
     * Reuse the text bounds in onDraw
     */
    private val TEXT_BOUNDS = Rect()

    /**
     * Reuse the text paint in onDraw
     */
    private val TEXT_PAINT = TextPaint()

    /**
     * Draw the name of the current rind centered on the bottom of the current view
     *
     * @param canvas canvas for drawing
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // test if there is a name to draw
        val name = rindvieh.name
        if (name.length == 0) {
            return
        }

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        val testTextSize = 48f

        // Get the bounds of the text, using our testTextSize.
        TEXT_PAINT.textSize = testTextSize
        TEXT_PAINT.getTextBounds(name, 0, name.length, TEXT_BOUNDS)

        // Calculate the desired size as a proportion of our testTextSize.
        var desiredTextSize = testTextSize * (width - 20) / TEXT_BOUNDS.width()

        // don't use font sizes larger than 40
        if (desiredTextSize > 40) {
            desiredTextSize = 40f
        }

        // Set the paint for that size.
        TEXT_PAINT.textSize = desiredTextSize

        // get the text bounds with real font size
        TEXT_PAINT.getTextBounds(name, 0, name.length, TEXT_BOUNDS)

        // draw the name on bottom center
        canvas.drawText(
            rindvieh.name,
            width / 2f - TEXT_BOUNDS.centerX(),
            (height - TEXT_BOUNDS.height() + 10).toFloat(),
            TEXT_PAINT
        )
    }

    /**
     * Setzt den Schatten
     */
    init {
        elevation = 25f

        TEXT_PAINT.textSize = 40f
        TEXT_PAINT.isAntiAlias = true
        TEXT_PAINT.color = Color.BLACK
        TEXT_PAINT.setShadowLayer(5.0f, 1.0f, 1.0f, Color.WHITE)
    }
}