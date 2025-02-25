package herdenmanagement.view

import android.content.Context
import herdenmanagement.model.Gras
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import de.ba.herdenmanagement.R
import herdenmanagement.model.PositionsElement

/**
 * Stellt die visuelle Darstellung von Gras dar.
 *
 * Diese Klasse erbt von [PositionElementView] und überschreibt die Eigenschaft [aktuellesBild],
 * um ein Bild eines Grasbüschels aus den Ressourcen anzuzeigen. Dadurch wird verdeutlicht,
 * wie ein spezifisches Model-Objekt (hier [Gras]) in eine passende View übersetzt werden kann.
 *
 * Didaktische Hinweise:
 * - **Vererbung:**
 *   Durch die Erweiterung von [PositionElementView] wird gezeigt, wie gemeinsame Funktionalitäten in
 *   einer Basisklasse definiert und in abgeleiteten Klassen spezifisch angepasst werden können.
 * - **Ressourcenverwaltung:**
 *   Die Eigenschaft [aktuellesBild] demonstriert, wie Bilder aus den Android-Ressourcen geladen und
 *   zur Darstellung genutzt werden.
 * - **Layout und Schatten:**
 *   Der Initialisierungsblock zeigt, wie durch das Setzen der [elevation] ein Schattenwurf erzeugt wird,
 *   der zur visuellen Abhebung der View beiträgt.
 *
 * @author Steffen Greiffenberg
 */
class GrasView : PositionElementView {

    /**
     * Konstruktor, der einen Context, einen Animator und ein [PositionsElement] (hier ein [Gras])
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
     * Liefert das aktuell darzustellende Bild eines Grasbüschels.
     *
     * Die Methode lädt das Bitmap aus den Android-Ressourcen, basierend auf der Bild-ID
     * [R.drawable.gras]. Dadurch wird das Model-Objekt [Gras] visuell repräsentiert.
     */
    override val aktuellesBild: Bitmap
        get() = BitmapFactory.decodeResource(context.resources, R.drawable.gras)

    /**
     * Initialisierungsblock zur Konfiguration der View.
     *
     * Hier wird die [elevation] gesetzt, was für einen Schattenwurf sorgt und das Bild visuell
     * vom Hintergrund abhebt.
     */
    init {
        elevation = 20f
    }
}
