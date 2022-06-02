package herdenmanagement.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import de.ba.herdenmanagement.R
import herdenmanagement.model.Kalb

/**
 * Die Klasse erbt von [PositionElementView] und überschreibt die dortige
 * Methode [.getAktuellesBild], um einen Kalb darzustellen.
 *
 * @author Steffen Greiffenberg
 */
class KalbView(context: Context, animator: Animator, kalb: Kalb) :
    PositionElementView(context, animator, kalb) {

    /**
     * @return Bild eines Kalbs aus den Ressourcen
     */
    override val aktuellesBild: Bitmap
        get() = BitmapFactory.decodeResource(context.resources, R.drawable.kalb)

    /**
     * Setzt den Schatten
     */
    init {
        elevation = 22f
    }
}