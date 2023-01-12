package herdenmanagement.view

import android.content.Context
import herdenmanagement.model.Gras
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import de.ba.herdenmanagement.R
import herdenmanagement.model.PositionsElement

/**
 * Die Klasse erbt von [PositionElementView] und überschreibt das dortige
 * Value [aktuellesBild], um Gras darzustellen.
 *
 * @author Steffen Greiffenberg
 */
class GrasView : PositionElementView {

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
    constructor(context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style) {}

    /**
     * Bild eines Grasbüschels aus den Ressourcen
     */
    override val aktuellesBild: Bitmap
        get() = BitmapFactory.decodeResource(context.resources, R.drawable.gras)

    /**
     * Setzt den Schatten
     */
    init {
        elevation = 20f
    }
}