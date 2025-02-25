package herdenmanagement.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import de.ba.herdenmanagement.R
import herdenmanagement.model.Kalb
import herdenmanagement.model.PositionsElement

/**
 * Stellt die visuelle Darstellung eines [Kalb] dar.
 *
 * Diese Klasse erbt von [PositionElementView] und überschreibt die Eigenschaft [aktuellesBild],
 * um ein Bild eines Kalbs aus den Ressourcen anzuzeigen. Damit wird das Model-Objekt [Kalb]
 * in der View abgebildet.
 *
 * Didaktische Hinweise:
 * - **Vererbung und Spezialisierung:**
 *   Durch die Erweiterung von [PositionElementView] wird gezeigt, wie spezifische Darstellungen
 *   für unterschiedliche Model-Objekte (hier Kalb) realisiert werden können, während gemeinsame
 *   Funktionalitäten in der Basisklasse zentral gehalten werden.
 * - **Ressourcenbasierte Darstellung:**
 *   Die Eigenschaft [aktuellesBild] demonstriert den Zugriff auf Android-Ressourcen, um ein
 *   Bitmap zur Darstellung des Kalbs zu laden.
 * - **Schattenwurf:**
 *   Durch das Setzen der [elevation] wird ein Schatteneffekt erzeugt, der das Bild visuell vom
 *   Hintergrund abhebt.
 *
 * @author Steffen Greiffenberg
 */
class KalbView : PositionElementView {

    /**
     * Konstruktor, der einen Context, einen Animator und ein [PositionsElement] (hier ein [Kalb])
     * entgegennimmt.
     *
     * @param context Der Context, in dem die View erstellt wird.
     * @param animator Animator zur Steuerung der Animationen.
     * @param positionsElement Das [PositionsElement]-Modell, das diese View darstellt.
     */
    constructor(
        context: Context,
        animator: Animator,
        positionsElement: PositionsElement
    ) : super(context, animator, positionsElement)

    /**
     * Default Constructor für Android Views.
     *
     * @param context Der Context, in dem die View erstellt wird.
     */
    constructor(context: Context) : super(context)

    /**
     * Default Constructor für Android Views mit XML-Attributen.
     *
     * @param context Der Context, in dem die View erstellt wird.
     * @param attrs XML-Attribute.
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    /**
     * Default Constructor für Android Views mit XML-Attributen und Style.
     *
     * @param context Der Context, in dem die View erstellt wird.
     * @param attrs XML-Attribute.
     * @param style Style-Attribut.
     */
    constructor(context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style)

    /**
     * Liefert das aktuell darzustellende Bild eines Kalbs.
     *
     * Das Bitmap wird aus den Android-Ressourcen geladen (hier: [R.drawable.kalb]).
     */
    override val aktuellesBild: Bitmap
        get() = BitmapFactory.decodeResource(context.resources, R.drawable.kalb)

    /**
     * Initialisierungsblock zur Konfiguration der View.
     *
     * Hier wird die [elevation] gesetzt, was für einen Schattenwurf sorgt und das Bild vom Hintergrund abhebt.
     */
    init {
        elevation = 22f
    }
}
