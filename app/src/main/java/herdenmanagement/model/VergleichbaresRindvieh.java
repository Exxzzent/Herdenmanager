package herdenmanagement.model;

public class VergleichbaresRindvieh extends Rindvieh {
    /**
     * Setzt den Namen des Rindviehs, setzt den Status {@link StatusTyp#WARTET} und ruft
     * den geerbeten Constructor auf. Initial wird die Kuh zur Morgensonne ausgerichtet.
     *
     * @param name Name der Kuh
     */
    public VergleichbaresRindvieh(String name) {
        super(name);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof VergleichbaresRindvieh)) {
            return false;
        }

        String name1 = ((VergleichbaresRindvieh)obj).gibName();
        String name2 = this.gibName();

        return name1.equals(name2);
    }
}
