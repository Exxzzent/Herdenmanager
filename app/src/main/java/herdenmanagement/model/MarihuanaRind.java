package herdenmanagement.model;

public class MarihuanaRind extends Rindvieh {

    /**
     * Setzt den Namen des Rindviehs, setzt den Status {@link StatusTyp#WARTET} und ruft
     * den geerbeten Constructor auf. Initial wird die Kuh zur Morgensonne ausgerichtet.
     *
     * @param name Name der Kuh
     */
    public MarihuanaRind(String name) {
        super(name);
    }
}
