package herdenmanagement.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import de.ba.herdenmanagement.R;

import herdenmanagement.model.Eimer;

/**
 * Die Klasse erbt von {@link PositionElementView} und überschreibt die dortige
 * Methode {@link #getAktuellesBild()}, um einen Eimer darzustellen.
 */
public class EimerView extends PositionElementView {

    /**
     * Ruft den geerbeten Constructor auf
     *
     * @param context Context der Android App, entspricht i.d.R. der {@link herdenmanagement.MainActivity}
     * @param animator Animation der grafischen Darstellungen
     * @param eimer Dargestelltes Element
     */
    public EimerView(Context context, Animator animator, Eimer eimer) {
        super(context, animator, eimer);
    }

    /**
     * @return Bild eines Eimers aus den Ressourcen
     */
    protected Bitmap getAktuellesBild() {
        return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.eimer);
    }
}
