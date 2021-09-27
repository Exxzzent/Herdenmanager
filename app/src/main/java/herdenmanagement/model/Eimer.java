package herdenmanagement.model;


/**
 * Im Muster Model View Controller sind Objekte dieser Klasse Bestandteil des Model.
 * Eimer sind meist silbern und aus Blech.
 */
public class Eimer extends PositionsElement implements DekoElement {

    /**
     * Jeder Eimer hat einen festen Standpunkt auf dem Acker. Mann kann auf eine Position
     * aber auch mehrere Eimer stellen.
     *
     * @param acker Acker des Eimers
     * @param position X- und Y-Koordinate der Position des Eimers
     */
    public void positioniereDich(Acker acker, Position position) {
        setzeAcker(acker);
        setzePosition(position);
    }

    @Override
    public void siehGutAus() {
        setzeNachricht("Hui, bin ich schön!");
    }
}
