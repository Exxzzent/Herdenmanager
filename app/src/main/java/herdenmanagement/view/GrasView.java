package herdenmanagement.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import de.ba.herdenmanagement.R;

import herdenmanagement.model.Gras;

/**
 * Die Klasse erbt von {@link PositionElementView} und überschreibt die dortige
 * Methode {@link #getAktuellesBild()}, um Gras darzustellen.
 *
 * @author Steffen Greiffenberg
 */
public class GrasView extends PositionElementView {

    /**
     * Ruft den geerbeten Constructor auf
     *
     * @param context Context der Android App, entspricht i.d.R. der {@link herdenmanagement.MainActivity}
     * @param animator Animation der grafischen Darstellungen
     * @param gras Dargestelltes Element
     */
    public GrasView(Context context, Animator animator, Gras gras) {
        super(context, animator, gras);
    }

    protected Bitmap getAktuellesBild() {
        return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gras);
    }

    /**
     * @return Bild eines Grasbüschels aus den Ressourcen
     */
    public Gras getGras() {
        return (Gras) getPositionsElement();
    }
}