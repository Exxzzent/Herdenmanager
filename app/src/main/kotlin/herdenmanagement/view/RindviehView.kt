package herdenmanagement.view

import android.content.Context
import de.dhsn.herdenmanagement.R
import android.text.TextPaint
import herdenmanagement.model.Rindvieh
import herdenmanagement.model.Rindvieh.Status
import herdenmanagement.model.Richtung
import android.graphics.*
import android.util.AttributeSet
import herdenmanagement.model.PositionsElement

/**
 * Stellt die visuelle Darstellung eines [Rindvieh] dar.
 *
 * Diese Klasse erbt von [PositionElementView] und überschreibt die Eigenschaft [aktuellesBild],
 * um ein Bild der Kuh in Abhängigkeit von ihrem Status und ihrer Blickrichtung anzuzeigen.
 * Zusätzlich wird der Name des Rindviehs unten zentriert über dem Bild dargestellt.
 *
 * Didaktische Hinweise:
 * - **Vererbung und Polymorphie:**
 *   Durch die Überschreibung von [aktuellesBild] wird gezeigt, wie eine spezifische Darstellung
 *   eines Modells (hier [Rindvieh]) realisiert werden kann, während die Grundfunktionalitäten in der
 *   Basisklasse [PositionElementView] definiert bleiben.
 * - **Ressourcen und Status-abhängige Darstellung:**
 *   Die Methode [aktuellesBild] demonstriert, wie anhand von Status- und Richtungsinformationen
 *   unterschiedliche Ressourcen (Bilder) ausgewählt und angezeigt werden.
 * - **Dynamische Textskalierung:**
 *   In [onDraw] wird gezeigt, wie der Name des Rindviehs dynamisch skaliert und zentriert
 *   gezeichnet wird, um eine gute Lesbarkeit zu gewährleisten.
 *
 * @author Steffen Greiffenberg
 */
class RindviehView : PositionElementView {

    /**
     * Konstruktor, der einen Context, einen Animator und ein [PositionsElement] (hier ein [Rindvieh])
     * entgegennimmt.
     *
     * @param context Der Context, in dem die View erstellt wird.
     * @param animator Animator zur Steuerung der Animationen.
     * @param positionsElement Das [PositionsElement]-Modell, das hier dargestellt wird.
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
     * Überschriebene Eigenschaft zur Ermittlung des aktuell anzuzeigenden Bildes einer Kuh.
     *
     * Das Bild wird anhand des aktuellen Status (z. B. [Status.FRISST], [Status.RAUCHT]) und der
     * Blickrichtung (z. B. [Richtung.NORD], [Richtung.OST], [Richtung.SUED], [Richtung.WEST])
     * ausgewählt.
     */
    override val aktuellesBild: Bitmap
        get() {
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
            return BitmapFactory.decodeResource(context.resources, drawableId)
        }

    /**
     * Liefert das [Rindvieh]-Modell, das von dieser View dargestellt wird.
     */
    val rindvieh: Rindvieh
        get() = positionsElement as Rindvieh

    /**
     * Cache für die Berechnung der Textbounds.
     */
    private val textBounds = Rect()

    /**
     * [TextPaint] zur Darstellung des Namens.
     */
    private val textPaint = TextPaint()

    /**
     * Zeichnet den Namen des Rindviehs zentriert am unteren Rand der View.
     *
     * Die Textgröße wird dynamisch angepasst, sodass der Name in die Breite der View passt.
     *
     * @param canvas Zeichenfläche, auf der das Bild und der Text gezeichnet werden.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val name = rindvieh.name
        if (name.isEmpty()) return

        // Testgröße für die Berechnung der Textgröße
        val testTextSize = 48f
        textPaint.textSize = testTextSize
        textPaint.getTextBounds(name, 0, name.length, textBounds)

        // Berechne die gewünschte Textgröße, sodass der Text in die View passt
        var desiredTextSize = testTextSize * (width - 20) / textBounds.width()
        if (desiredTextSize > 40) {
            desiredTextSize = 40f
        }
        textPaint.textSize = desiredTextSize
        textPaint.getTextBounds(name, 0, name.length, textBounds)

        // Zeichne den Namen zentriert am unteren Rand
        canvas.drawText(
            rindvieh.name,
            width / 2f - textBounds.centerX(),
            (height - textBounds.height() + 10).toFloat(),
            textPaint
        )
    }

    /**
     * Initialisierungsblock, in dem Standardwerte wie Elevation und Textformatierung gesetzt werden.
     *
     * Die Elevation bestimmt den Schattenwurf des Bildes. Zusätzlich wird [textPaint] konfiguriert,
     * um den Namen des Rindviehs lesbar darzustellen.
     */
    init {
        elevation = 25f

        textPaint.textSize = 40f
        textPaint.isAntiAlias = true
        textPaint.color = Color.BLACK
        textPaint.setShadowLayer(5.0f, 1.0f, 1.0f, Color.WHITE)
    }
}
