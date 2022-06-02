package herdenmanagement.model

import herdenmanagement.model.Threading
import android.app.Activity
import kotlin.jvm.JvmOverloads
import herdenmanagement.model.Gras
import herdenmanagement.view.PositionElementView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import de.ba.herdenmanagement.R
import herdenmanagement.model.Kalb
import android.widget.FrameLayout
import herdenmanagement.model.Acker
import android.text.TextPaint
import android.content.res.TypedArray
import herdenmanagement.model.Rindvieh
import android.view.View.MeasureSpec
import herdenmanagement.view.GrasView
import herdenmanagement.view.RindviehView
import herdenmanagement.view.KalbView
import android.transition.TransitionManager
import herdenmanagement.model.Rindvieh.StatusTyp
import herdenmanagement.model.Rindvieh.RichtungsTyp
import androidx.appcompat.widget.AppCompatImageView
import herdenmanagement.model.PositionsElement
import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.content.res.Resources.NotFoundException
import herdenmanagement.view.AckerView
import android.view.ViewGroup
import android.widget.Toast
import herdenmanagement.model.BeobachtbaresElement
import androidx.appcompat.app.AppCompatActivity
import herdenmanagement.HerdenManager
import android.os.Bundle
import herdenmanagement.MainActivity

/**
 * Tanzrinder erben alle Eigenschaften der Klasse [Rindvieh]. Sie können sich also
 * genauso auf einem Acker bewegen. Tanzrinder können zusätzlich seitwärts gehen.
 * Dies ist wichtig, zur Beherrschung für die meisten Standardtänze und lateinamerikanische
 * Bewegungsabläufe.
 *
 *
 * Es wird sichergestellt, dass die Kuh nicht über den Rand des Ackers hinaus gehen kann.
 *
 *
 * Im Muster Model View Controller sind Objekte dieser Klasse Bestandteil des Model.
 *
 * @author Steffen Greiffenberg
 */
class TanzRind
    /**
     * Erzeugt ein TanzRind. Es wird nur der geerbte Contructor aufgerufen.
     */
    (name: String) : Rindvieh(name) {

    /**
     * @return Nächste Position rechts von der Kuh
     */
    protected fun gibNaechstePositionRechts(): Position {
        return position
    }

    /**
     * @return Nächste Position links von der Kuh
     */
    protected fun gibNaechstePositionLinks(): Position {
        return position
    }

    /**
     * Bewegt das Rind seitwärts nach links
     */
    fun geheSeitwaertsNachLinks() {}

    /**
     * Bewegt das Rind seitwärts nach rechts
     */
    fun geheSeitwaertsNachRechts() {}

    /**
     * Prüft die Grenzen des Ackers.
     *
     * @return true, wenn die Kuh auf dem Acker weiter nach lnks gehen kann
     */
    fun gehtsDaLinksWeiter(): Boolean {
        return false
    }

    /**
     * Prüft die Grenzen des Ackers.
     *
     * @return true, wenn die Kuh auf dem Acker weiter nach rechts gehen kann
     */
    fun gehtsDaRechtsWeiter(): Boolean {
        return false
    }

    /**
     * Das TanzRind tanzt Cha-Cha-Cha!
     */
    fun chaChaCha() {}
}