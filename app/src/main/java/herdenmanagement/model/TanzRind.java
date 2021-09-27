package herdenmanagement.model;

/**
 * Tanzrinder erben alle Eigenschaften der Klasse {@link Rindvieh}. Sie können sich also
 * genauso auf einem Acker bewegen. Tanzrinder können zusätzlich seitwärts gehen.
 * Dies ist wichtig, zur Beherrschung für die meisten Standardtänze und lateinamerikanische
 * Bewegungsabläufe.
 * <p>
 * Es wird sichergestellt, dass die Kuh nicht über den Rand des Ackers hinaus gehen kann.
 * <p>
 * Im Muster Model View Controller sind Objekte dieser Klasse Bestandteil des Model.
 */
public class TanzRind extends Rindvieh {

    /**
     * Erzeugt ein TanzRind. Es wird nur der geebrte Contructor aufgerufen.
     *
     * @param name Name des Rindviehs
     */
    public TanzRind(String name) {
        super(name);
    }

    /**
     * @return Nächste Position rechts von der Kuh
     */
    protected Position gibNaechstePositionRechts() {
        Position position = gibPosition();


        return position;
    }

    /**
     * @return Nächste Position links von der Kuh
     */
    protected Position gibNaechstePositionLinks() {
        Position position = gibPosition();

        return position;
    }

    /**
     * Bewegt das Rind seitwärts nach links
     */
    public void geheSeitwaertsNachLinks() {

    }

    /**
     * Bewegt das Rind seitwärts nach rechts
     */
    public void geheSeitwaertsNachRechts() {

    }

    /**
     * Prüft die Grenzen des Ackers.
     *
     * @return true, wenn die Kuh auf dem Acker weiter nach lnks gehen kann
     */
    public boolean gehtsDaLinksWeiter() {
        return false;
    }

    /**
     * Prüft die Grenzen des Ackers.
     *
     * @return true, wenn die Kuh auf dem Acker weiter nach links gehen kann
     */
    public boolean gehtsDaRechtsWeiter() {
        return false;
    }

    /**
     * Das TanzRind tanzt Cha Cha Cha!
     */
    public void chaChaCha() {

    }
}
