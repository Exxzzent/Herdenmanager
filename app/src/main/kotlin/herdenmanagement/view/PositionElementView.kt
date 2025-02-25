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
 * Basis-View zur Darstellung von PositionsElementen wie Kälbern, Kühen etc.
 *
 * Diese Klasse implementiert [PropertyChangeListener] und reagiert auf Änderungen an den
 * Properties ihres zugeordneten [PositionsElement]. In der Regel erfolgt dies durch eine Anpassung
 * der grafischen Darstellung (z. B. Änderung der Position oder des Bildes).
 *
 * Im Model-View-Controller (MVC)-Muster bildet diese Klasse den View-Teil, während das zugehörige
 * [PositionsElement] das Model repräsentiert. Die Kommunikation zwischen Model und View erfolgt
 * über das Observer-Muster, wobei Änderungen am Model als [PropertyChangeEvent] an die View übermittelt werden.
 *
 * Didaktische Hinweise:
 * - **Observer-Muster:**
 *   Die Klasse demonstriert, wie View-Komponenten auf Änderungen im Model reagieren können, ohne
 *   direkt mit dem Model gekoppelt zu sein.
 * - **Layout-Anpassung:**
 *   Durch die Methode [calculateLayoutParams] wird gezeigt, wie dynamisch Layout-Parameter basierend
 *   auf Model-Daten und der Größe des übergeordneten Containers (AckerView) berechnet werden.
 * - **UI-Thread Kommunikation:**
 *   Der Animator wird genutzt, um sicherzustellen, dass alle Änderungen an der grafischen Darstellung
 *   im Main (UI-) Thread erfolgen, was in Android unabdingbar ist.
 *
 * @author Steffen Greiffenberg
 */
open class PositionElementView : AppCompatImageView, PropertyChangeListener {

    /**
     * Konstruktor, der einen Context, einen Animator und ein [PositionsElement] entgegennimmt.
     *
     * Hierbei wird eine Kopie des übergebenen [PositionsElement] gespeichert, um Veränderungen
     * im Model nachvollziehen zu können.
     *
     * @param context Der Context, in dem die View erstellt wird.
     * @param animator Der Animator, der für die Aktualisierung der UI zuständig ist.
     * @param positionsElement Das Model-Objekt, das diese View repräsentiert.
     */
    constructor(
        context: Context,
        animator: Animator,
        positionsElement: PositionsElement
    ) : super(context) {
        this.animator = animator
        this.positionsElement = positionsElement.copy()

        // ID des PositionsElements übernehmen
        id = positionsElement.id

        // Initialisiere mit einem leeren Bitmap, um spätere Anpassungen zu ermöglichen
        setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8))
    }

    /**
     * Default Constructor für Android Views.
     *
     * Erzeugt einen neuen Animator und ein Standard-[PositionsElement].
     *
     * @param context Der Context, in dem die View erstellt wird.
     */
    constructor(context: Context) : super(context) {
        animator = Animator()
        positionsElement = PositionsElement()
    }

    /**
     * Default Constructor für Android Views mit XML-Attributen.
     *
     * Erzeugt einen neuen Animator und ein Standard-[PositionsElement].
     *
     * @param context Der Context, in dem die View erstellt wird.
     * @param attrs XML-Attribute für die View.
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        animator = Animator()
        positionsElement = PositionsElement()
    }

    /**
     * Default Constructor für Android Views mit XML-Attributen und Style.
     *
     * Erzeugt einen neuen Animator und ein Standard-[PositionsElement].
     *
     * @param context Der Context, in dem die View erstellt wird.
     * @param attrs XML-Attribute für die View.
     * @param style Style-Attribut für die View.
     */
    constructor(context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style) {
        animator = Animator()
        positionsElement = PositionsElement()
    }

    /**
     * Der Animator steuert die Aktualisierung der grafischen Darstellung.
     *
     * Alle Animationen und Layout-Anpassungen erfolgen über diesen Animator, der
     * sicherstellt, dass Änderungen im UI-Thread ausgeführt werden.
     */
    private var animator: Animator

    /**
     * Das [PositionsElement], das von dieser View dargestellt wird.
     *
     * Es handelt sich um eine Kopie des Originals, um während Animationen den Zustand
     * nachvollziehen zu können, ohne das Original zu verändern.
     */
    var positionsElement: PositionsElement
        private set

    /**
     * Überschreibt die [setElevation]-Methode, um bei Änderung der Höhe auch den
     * Schatten des Bildes neu zu berechnen.
     *
     * @param elevation Neue Höhe über der ViewGroup.
     */
    override fun setElevation(elevation: Float) {
        if (elevation != getElevation()) {
            super.setElevation(elevation)
            setImageBitmap(aktuellesBild)
        }
    }

    /**
     * Überschreibt die Methode [setImageBitmap], um dem dargestellten Bild bei
     * gesetzter Elevation einen Schatten hinzuzufügen.
     *
     * Ist die Elevation 0 oder das Bild leer, wird das Bild ohne Schatten dargestellt.
     *
     * @param bild Das darzustellende Bitmap.
     */
    final override fun setImageBitmap(bild: Bitmap) {
        if ((elevation == 0f) || (bild.width == 0)) {
            super.setImageBitmap(bild)
            return
        }

        // Berechne die Schattenstärke proportional zur Bildgröße und Elevation
        val shadowSize = elevation * bild.width / 500

        // Konfiguriere Paint für den Schatten
        val ptBlur = Paint()
        ptBlur.maskFilter = BlurMaskFilter(shadowSize, BlurMaskFilter.Blur.NORMAL)

        // Extrahiere den Alphakanal des Bildes, um den Schatten zu generieren
        val offsetXY = IntArray(2)
        val bmAlpha = bild.extractAlpha(ptBlur, offsetXY)

        // Erzeuge ein neues Bitmap, das sowohl den Schatten als auch das Originalbild enthält
        val bildMitSchatten =
            Bitmap.createBitmap(bmAlpha.width, bmAlpha.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bildMitSchatten)
        canvas.drawColor(0, PorterDuff.Mode.CLEAR)

        // Zeichne den Schatten in dunkler Farbe
        val ptAlphaColor = Paint().apply { color = Color.DKGRAY }
        canvas.drawBitmap(bmAlpha, 0f, 0f, ptAlphaColor)
        bmAlpha.recycle()

        // Zeichne das Originalbild etwas versetzt, um den Schatteneffekt zu verstärken
        canvas.drawBitmap(bild, -offsetXY[0] / 1.5f, -offsetXY[1] / 1.5f, null)
        super.setImageBitmap(bildMitSchatten)
    }

    /**
     * Reagiert auf [PropertyChangeEvent]s, die vom [PositionsElement] gesendet werden.
     *
     * Je nach Art der Änderung (z. B. [Keys.PROPERTY_NACHRICHT] oder [Keys.PROPERTY_POSITION])
     * wird die grafische Darstellung der View angepasst oder eine Nachricht angezeigt.
     *
     * @param evt Das [PropertyChangeEvent], das die Art der Änderung und den neuen Zustand beschreibt.
     */
    override fun propertyChange(evt: PropertyChangeEvent) {
        val positionsElement = evt.newValue as PositionsElement

        // Bei Nachrichten werden diese als Toast angezeigt
        if (Keys.PROPERTY_NACHRICHT == evt.propertyName) {
            val nachricht = positionsElement.nachricht

            // Direkte Anzeige, falls die Nachricht ein String ist
            (positionsElement.nachricht as? String)?.let(::toast)

            // Falls die Nachricht als Ressourcen-ID vorliegt, wird der zugehörige String geladen
            (nachricht as? Number)?.toInt()?.let {
                try {
                    toast(context.resources.getString(it, positionsElement.name))
                } catch (e: NotFoundException) {
                    toast(nachricht.toString())
                }
            }
        } else if (Keys.PROPERTY_POSITION == evt.propertyName) {
            // Bei Positionsänderungen wird ein neues Layout berechnet
            animator.performAction(object : Action() {
                override fun run() {
                    // Aktualisiere die Kopie des PositionsElements
                    this@PositionElementView.positionsElement = positionsElement

                    // Berechne neue LayoutParams anhand der Größe der AckerView
                    val ackerView = parent as? AckerView ?: return
                    val lp = calculateLayoutParams(
                        ackerView.width.toFloat(), ackerView.height.toFloat()
                    )

                    // Starte eine verzögerte Layout-Transition für sanfte Animation
                    (parent as? ViewGroup)?.run(TransitionManager::beginDelayedTransition)

                    // Setze die neuen LayoutParams
                    layoutParams = lp
                }
            })
        } else {
            // Bei anderen Änderungen wird das Bild aktualisiert
            animator.performAction(object : Action() {
                override fun run() {
                    this@PositionElementView.positionsElement = positionsElement

                    if (parent is ViewGroup) {
                        TransitionManager.beginDelayedTransition(parent as ViewGroup)
                    }

                    setImageBitmap(aktuellesBild)
                }
            })
        }
    }

    /**
     * Berechnet die Layout-Parameter für diese View basierend auf dem [PositionsElement]
     * und der Größe der übergeordneten AckerView.
     *
     * Hierbei wird die Position innerhalb einer Matrix (definiert durch [acker.spalten] und
     * [acker.zeilen]) berücksichtigt, um die exakten Margen zu bestimmen.
     *
     * @param width Breite der AckerView.
     * @param height Höhe der AckerView.
     * @return Neue [FrameLayout.LayoutParams], die die Position und Größe dieser View festlegen.
     */
    fun calculateLayoutParams(width: Float, height: Float): FrameLayout.LayoutParams {
        val acker = positionsElement.acker
        val position = positionsElement.position
        val columnWidth: Float = width / acker.spalten.toFloat()
        val rowHeight: Float = height / acker.zeilen.toFloat()

        // Erhalte die aktuellen LayoutParams und erstelle eine Kopie
        val lp = layoutParams as FrameLayout.LayoutParams
        val result = FrameLayout.LayoutParams(lp)
        result.width = columnWidth.toInt()
        result.height = rowHeight.toInt()
        result.leftMargin = (position.x * columnWidth).toInt()
        result.topMargin = (position.y * rowHeight).toInt()
        return result
    }

    /**
     * Zeigt eine Toast-Nachricht an, indem eine Action über den Animator ausgeführt wird.
     *
     * Dadurch wird sichergestellt, dass die Toast-Anzeige stets im UI-Thread erfolgt.
     *
     * @param text Der anzuzeigende Text.
     */
    private fun toast(text: String) {
        animator.performAction(object : Action() {
            override fun run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Liefert das aktuell darzustellende Bild für dieses PositionsElement.
     *
     * Diese Methode sollte von erbenden Klassen überschrieben werden, um ein
     * spezifisches Bild basierend auf dem Zustand des [PositionsElement] bereitzustellen.
     *
     * @return Das Bitmap, das aktuell angezeigt werden soll.
     */
    protected open val aktuellesBild: Bitmap
        get() = Bitmap.createBitmap(0, 0, Bitmap.Config.ALPHA_8)
}
