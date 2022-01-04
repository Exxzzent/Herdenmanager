package herdenmanagement.model;

/**
 * Eine Position speichert eine X- und Y-Koordinate für Objekte der Klasse {@link PositionsElement}.
 * Andere Positionen sind auch wichtig, aber nicht für Rindviecher.
 *
 * @author Steffen Greiffenberg
 */
public class Position {

    /**
     * X-Koordinate der Position
     */
    public int x;

    /**
     * Y-Koordinate der Funktion
     */
    public int y;

    /**
     * Erzeugt eine Position auf Basis einer bereits vorhandenen (Kopie).
     *
     * @param position Zu übernehmende Koordinaten
     */
    public Position(Position position) {
        x = position.x;
        y = position.y;
    }

    /**
     * @param x X-Koordinate der Position
     * @param y Y-Koordinate der Position
     */
    public Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * vergleich der Position mit einer anderen
     *
     * @param o Position,mit der verglichen wird
     * @return true, wenn X- und Y-Koordinate übereinstimmen
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (x != ((Position) o).x) {
            return false;
        }

        //noinspection RedundantIfStatement
        if (y != ((Position) o).y) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
