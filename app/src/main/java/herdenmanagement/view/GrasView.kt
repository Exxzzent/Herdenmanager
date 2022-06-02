package herdenmanagement.view

import android.content.Context
import herdenmanagement.model.Gras
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import de.ba.herdenmanagement.R

/**
 * Die Klasse erbt von [PositionElementView] und überschreibt die dortige
 * Methode [.getAktuellesBild], um Gras darzustellen.
 *
 * @author Steffen Greiffenberg
 */
class GrasView(context: Context, animator: Animator, gras: Gras) :
    PositionElementView(context, animator, gras) {

    override val aktuellesBild: Bitmap
        get() = BitmapFactory.decodeResource(context.resources, R.drawable.gras)

    /**
     * @return Bild eines Grasbüschels aus den Ressourcen
     */
    val gras: Gras
        get() = positionsElement as Gras

    /**
     * Setzt den Schatten
     */
    init {
        elevation = 20f
    }
}