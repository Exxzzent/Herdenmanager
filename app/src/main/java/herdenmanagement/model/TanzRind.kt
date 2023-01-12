package herdenmanagement.model

/**
 * Tanzrinder erben alle Eigenschaften der Klasse [Rindvieh]. Sie können sich also
 * genauso auf einem Acker bewegen. Tanzrinder können zusätzlich seitwärts gehen.
 * Dies ist wichtig zur Beherrschung der meisten Standardtänze und lateinamerikanische
 * Bewegungsabläufe.
 *
 * Im Muster Model View Controller sind Objekte dieser Klasse Bestandteil des Model.
 *
 * @author Steffen Greiffenberg
 *
 * @constructor Erzeugt ein TanzRind. Es wird nur der geerbte Contructor aufgerufen.
 * @param name des Rindviehs. Kühe können nur bei ihrer Erzeugung benannt werden. Ein späteres Umbenennen ist nicht möglich.
 */
class TanzRind(name: String) : Rindvieh(name) {

    /**
     * @return Nächste Position rechts von der Kuh
     */
    protected fun positionRechts(): Position {
        return position
    }

    /**
     * @return Nächste Position links von der Kuh
     */
    protected fun positionLinks(): Position {
        return position
    }

    /**
     * Bewegt das Rind seitwärts nach links
     */
    fun geheSeitwaertsNachLinks() {}

    /**
     * Bewegt das Rind seitwärts nach rechts
     */
    fun geheSeitwaertsNachRechts() {}

    /**
     * Prüft die Grenzen des Ackers.
     *
     * @return true, wenn die Kuh auf dem Acker weiter nach lnks gehen kann
     */
    fun gehtsDaLinksWeiter(): Boolean {
        return false
    }

    /**
     * Prüft die Grenzen des Ackers.
     *
     * @return true, wenn die Kuh auf dem Acker weiter nach rechts gehen kann
     */
    fun gehtsDaRechtsWeiter(): Boolean {
        return false
    }

    /**
     * Das TanzRind tanzt Cha-Cha-Cha!
     */
    fun chaChaCha() {}
}