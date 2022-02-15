package herdenmanagement.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextPaint;

import de.ba.herdenmanagement.R;

import herdenmanagement.model.Rindvieh;

/**
 * Die Klasse erbt von {@link PositionElementView} und überschreibt die dortige
 * Methode {@link #getAktuellesBild()}, um ein Rindvieh darzustellen. Da das Rind in
 * verschiedene Richtungen schauen kann, ist die Klasse komplizierter aufgebaut
 * als zum Beispiel die {@link KalbView}.
 *
 * @author Steffen Greiffenberg
 */
public class RindviehView extends PositionElementView {

    /**
     * Ruft den geerbeten Constructor auf
     *
     * @param context Context der Android App, entspricht i.d.R. der {@link herdenmanagement.MainActivity}
     * @param animator Animation der grafischen Darstellungen
     * @param rindvieh Dargestelltes Element
     */
    public RindviehView(Context context, Animator animator, Rindvieh rindvieh) {
        super(context, animator, rindvieh);
        setElevation(25);
    }

    /**
     * @return Bild einer Kuh, die in die richtige Richtung schaut
     */
    protected Bitmap getAktuellesBild() {
        Rindvieh rindvieh = getRindvieh();

        if (Rindvieh.StatusTyp.FRISST.equals(rindvieh.gibStatus())) {
            return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kuh_gras);
        } else if (Rindvieh.StatusTyp.RAUCHT.equals(rindvieh.gibStatus())) {
            return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kuh_rauch);
        }

        if (rindvieh.gibRichtung() == Rindvieh.RichtungsTyp.NORD) {
            return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kuh_hinten);
        } else if (rindvieh.gibRichtung() == Rindvieh.RichtungsTyp.WEST) {
            return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kuh_links);
        } else if (rindvieh.gibRichtung() == Rindvieh.RichtungsTyp.SUED) {
            return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kuh_vorn);
        } else if (rindvieh.gibRichtung() == Rindvieh.RichtungsTyp.OST) {
            return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kuh_rechts);
        }

        return null;
    }

    /**
     * @return Von dieser Klasse dargestelltes {@link Rindvieh}
     */
    public Rindvieh getRindvieh() {
        return (Rindvieh) getPositionsElement();
    }

    /**
     * Reuse the text bounds in onDraw
     */
    private final Rect TEXT_BOUNDS = new Rect();


    /**
     * Reuse the text paint in onDraw
     */
    private final TextPaint TEXT_PAINT = new TextPaint();
    {
        TEXT_PAINT.setTextSize(40);
        TEXT_PAINT.setAntiAlias(true);
        TEXT_PAINT.setColor(Color.BLACK);
        TEXT_PAINT.setShadowLayer(5.0f, 1.0f, 1.0f, Color.WHITE);
    }

    /**
     * Draw the name of the current rind centered on the bottom of the current view
     *
     * @param canvas canvas for drawing
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // avoid NPE
        if (getRindvieh() == null) {
            return;
        }

        // test if there is a name to draw
        String name = getRindvieh().gibName();
        if (name == null || name.length() == 0) {
            return;
        }

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        TEXT_PAINT.setTextSize(testTextSize);
        TEXT_PAINT.getTextBounds(name, 0, name.length(), TEXT_BOUNDS);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * (getWidth() - 20) / TEXT_BOUNDS.width();

        // don't use font sizes larger than 40
        if (desiredTextSize > 40) {
            desiredTextSize = 40;
        }

        // Set the paint for that size.
        TEXT_PAINT.setTextSize(desiredTextSize);

        // get the text bounds with real font size
        TEXT_PAINT.getTextBounds(name, 0, name.length(), TEXT_BOUNDS);

        // draw the name on bottom center
        canvas.drawText(
                getRindvieh().gibName(),
                (getWidth() / 2f) - TEXT_BOUNDS.centerX(),
                getHeight() - TEXT_BOUNDS.height() + 10,
                TEXT_PAINT);
    }
}
