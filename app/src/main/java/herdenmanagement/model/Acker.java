package herdenmanagement.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Der Acker besteht aus einer Matrix an Feldern, auf denen {@link Eimer}, {@link Gras} und
 * Instanzen von {@link Rindvieh} platziert werden können. Der Acker beschränkt die Position
 * dieser Objekte mit seiner Größe.
 * <p>
 * Der Acker kann prüfen, ob einer bestimmten Position Gras wächst {@link #istDaGras} oder
 * ein Eimer steht {@link #istDaEinEimer(Position)}.
 * <p>
 * Wird ein Eimer, Gras oder ein Rindvieh hinzugefügt oder entfernt, informiert der Acker
 * seine Beobachter.
 * <p>
 * Im Muster Model View Controller sind Objekte dieser Klasse Bestandteil des Model. Der Acker
 * kann mit einer {@link herdenmanagement.view.AckerView} dargestellt werden.
 */
public class Acker extends BeobachtbaresElement {

    /**
     * Schlüssel zur Kommunikation mit einem {@link PropertyChangeListener}.
     * Der Schlüssel wird als property der Methode {@link #informiereBeobachter(String, Object, Object)}
     * übergeben.
     * <p>
     * Der Schlüssel dient für Nachrichten zum Property {@link #viecher}.
     */
    public final static String PROPERTY_VIECHER = "herdenmanagement.model.Acker.viecher";

    /**
     * Schlüssel zur Kommunikation mit einem {@link PropertyChangeListener}.
     * Der Schlüssel wird als property der Methode {@link #informiereBeobachter(String, Object, Object)}
     * übergeben.
     * <p>
     * Der Schlüssel dient für Nachrichten zum Property {@link #eimer}.
     */
    public final static String PROPERTY_EIMER = "herdenmanagement.model.Acker.eimer";

    /**
     * Schlüssel zur Kommunikation mit einem {@link PropertyChangeListener}.
     * Der Schlüssel wird als property der Methode {@link #informiereBeobachter(String, Object, Object)}
     * übergeben.
     * <p>
     * Der Schlüssel dient für Nachrichten zum Property {@link #graeser}.
     */
    public final static String PROPERTY_GRAESER = "herdenmanagement.model.Acker.graeser";

    /**
     * Objekte der Klasse Rindvieh, die auf dem Acker unterwegs sind
     */
    private final List<Rindvieh> viecher;

    /**
     * Objekte der Klasse Gras, die auf dem Acker wachsen
     */
    private final List<Gras> graeser;

    /**
     * Objekte der Klasse Eimer, die auf dem Acker stehen
     */
    private final List<Eimer> eimer;

    /**
     * Anzahl der Zeilen auf dem Acker
     */
    private final int zeilen;

    /**
     * Anzahl der Spalten auf dem Acker
     */
    private final int spalten;

    /**
     * Erstellt einen neuen Acker und erzeugt die Listen für Gras, Eimer und Rinder.
     *
     * @param zeilen  Anzahl der Zeilen
     * @param spalten Anzahl der Spalten
     */
    public Acker(int spalten, int zeilen) {
        this.zeilen = zeilen;
        this.spalten = spalten;

        this.graeser = new ArrayList<>();
        this.eimer = new ArrayList<>();
        this.viecher = new ArrayList<>();
    }

    /**
     * @return Anzahl der Zeilen des Ackers
     */
    public int zaehleZeilen() {
        return zeilen;
    }

    /**
     * @return Anzahl der Spalten des Ackers
     */
    public int zaehleSpalten() {
        return spalten;
    }

    /**
     * Erzeugt an der Position eine Instanz von {@link Gras}.
     * <p>
     * Wird ein neues Gras auf dem Acker platziert, werden die Observer des Ackers informiert.
     *
     * @param position Position, an der Gras wachsen soll
     * @return Auf dem Acker eingefügtes Gras
     */
    public Gras lassGrasWachsen(Position position) {
        Gras gras = new Gras();
        gras.setzeAcker(this);
        gras.setzePosition(position);
        graeser.add(gras);

        informiereBeobachter(PROPERTY_GRAESER, null, gras);

        return gras;
    }

    /**
     * Erzeugt an der Position eine Instanz von {@link Gras}.
     * <p>
     * Wird ein neues Gras auf dem Acker platziert, werden die Observer des Ackers informiert.
     *
     * @param x X-Koordinate des Grases
     * @param y Y-Koordinate des Graes
     * @return Auf dem Acker eingefügtes Gras
     */
    @Deprecated
    public Gras lassGrasWachsen(int x, int y) {
        return lassGrasWachsen(new Position(x, y));
    }

    /**
     * Wenn ein Rind mit {@link herdenmanagement.model.Rindvieh#Rindvieh(String)} erzeugt wurde,
     * kann es auf einem Acker eingefügt werden. Jedes Rind hat nur einen Acker, ein Acker mehrere
     * Rinder.
     * <p>
     * Wird ein neues Rind auf dem Acker platziert, werden die Observer des Ackers informiert.
     *
     * @param rind Rind, welches zukünftig hier weidet
     */
    public void lassRindWeiden(Rindvieh rind) {
        rind.setzeAcker(this);
        viecher.add(rind);

        informiereBeobachter(PROPERTY_VIECHER, null, rind);
    }

    /**
     * Stellt einen Eimer auf den Acker. Rinder können hier zukünftig
     * mit {@link Rindvieh#gibMilch()} Milch geben.
     * <p>
     * Wird ein neuer Eimer auf dem Acker platziert, werden die Observer des Ackers informiert.
     *
     * @param position Position des aufzustellenden Eimers
     * @return Auf dem Acker platzierter Eimer
     */
    public Eimer stelleEimerAuf(Position position) {
        Eimer e = new Eimer();
        e.setzeAcker(this);
        e.setzePosition(position);
        eimer.add(e);

        informiereBeobachter(PROPERTY_EIMER, null, e);

        return e;
    }

    /**
     * Stellt einen Eimer auf den Acker. Rinder können hier zukünftig
     * mit {@link Rindvieh#gibMilch()} Milch geben.
     * <p>
     * Wird ein neuer Eimer auf dem Acker platziert, werden die Observer des Ackers informiert.
     *
     * @param x X-Koordinate des Eimers
     * @param y Y-Koordinate des Eimers
     * @return Auf dem Acker platzierter Eimer
     */
    @Deprecated
    public Eimer stelleEimerAuf(int x, int y) {
        return stelleEimerAuf(new Position(x, y));
    }

    /**
     * @param position Zu prüfende Position
     * @return true, wenn an der Position ein {@link Eimer} steht
     */
    public boolean istDaEinEimer(Position position) {
        for (Eimer e : eimer) {
            if (e.gibPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param position Zu prüfende Position
     * @return true, wenn an der Position ein {@link Gras} wächst
     */
    public boolean istDaGras(Position position) {
        for (Gras gras : graeser) {
            if (gras.gibPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kühe können mit {@link Rindvieh#frissGras()} Gras fressen oder
     * mit {@link Rindvieh#raucheGras()} Gras rauchen.
     *
     * @param position Zu prüfende Position
     * @return true, wenn an der Position {@link Gras} wächst
     */
    public boolean entferneGras(Position position) {
        for (Gras gras : graeser) {
            if (gras.gibPosition().equals(position)) {
                graeser.remove(gras);
                informiereBeobachter(PROPERTY_GRAESER, gras, null);
                return true;
            }
        }
        return false;
    }

    /**
     * Da sich Kühe bewegen können, muss verhindert werden, dass sie den Acker verlassen.
     * Vor einer jeden bewegung prüfen sie deshalb mit dieser Methode, ob die Zielposition
     * gültig ist.
     *
     * @param position Zu prüfende Position
     * @return true, wenn die Position auf dem Acker möglich ist
     */
    public boolean istPositionGueltig(Position position) {
        return (position.x > -1) &&
                (position.x < spalten) &&
                (position.y > -1) &&
                (position.y < zeilen);
    }

    /**
     * @return Liste der Rinder auf dem Acker
     */
    public List<Rindvieh> getViecher() {
        return viecher;
    }

    /**
     * @return Liste der Eimer auf dem Acker
     */
    public List<Eimer> getEimer() {
        return eimer;
    }

    /**
     * @return Liste der Gräser auf dem Acker
     */
    public List<Gras> getGraeser() {
        return graeser;
    }
}
