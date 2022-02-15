package herdenmanagement.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import de.ba.herdenmanagement.R;

import herdenmanagement.model.Kalb;

/**
 * Die Klasse erbt von {@link PositionElementView} und überschreibt die dortige
 * Methode {@link #getAktuellesBild()}, um einen Kalb darzustellen.
 *
 * @author Steffen Greiffenberg
 */
public class KalbView extends PositionElementView {

    /**
     * Ruft den geerbeten Constructor auf
     *
     * @param context Context der Android App, entspricht i.d.R. der {@link herdenmanagement.MainActivity}
     * @param animator Animation der grafischen Darstellungen
     * @param kalb Dargestelltes Element
     */
    public KalbView(Context context, Animator animator, Kalb kalb) {
        super(context, animator, kalb);
        setElevation(22);
    }

    /**
     * @return Bild eines Kalbs aus den Ressourcen
     */
    protected Bitmap getAktuellesBild() {
        return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kalb);
    }
}
