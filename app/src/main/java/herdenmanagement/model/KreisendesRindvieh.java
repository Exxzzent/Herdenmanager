package herdenmanagement.model;

import android.os.SystemClock;

/**
 * Ein KreisendesRindvieh erbt alle Eigenschaften der Klasse {@link Rindvieh}. Es bewegt sich
 * durch einen eigenen {@link Thread} permanent im Kreis um einen Ursprung.
 * Dieser kann mit {@link #setzeUrsprung(int, int)} gesetzt werden.
 * <p>
 * Es wird sichergestellt, dass die Kuh nicht über den Rand des Ackers hinaus gehen kann.
 * <p>
 * Im Muster Model View Controller sind Objekte dieser Klasse Bestandteil des Model.
 *
 * @author Steffen Greiffenberg
 */
public class KreisendesRindvieh extends Rindvieh implements Runnable {

    /**
     * Der Constructor ruft nur den geerbeten Constructor auf
     * @param name Name des Rindviehs, zum Beispiel "Schantall"
     */
    public KreisendesRindvieh(String name) {
        super(name);
    }

    /**
     * Diese Position beschreibt den Mittelpunkt der Kreisbewegung.
     * Der Wert wird durch {@link #setzeUrsprung(int, int)} verändert.
     */
    private Position ursprung = new Position(1, 1);

    /**
     * Wird der Wert auf true gesetzt, wird die Kreisbewegung beendet.
     * Dies geschieht in {@link #beendeBewegung()}.
     */
    private boolean mussAufhoeren = false;

    /**
     * Wird der Wert auf true gesetzt, stoppt die Kuh ihre Kreisbewegung.
     * Dies wird insbesondere dann notwendig, wenn der {@link #ursprung}
     * verändert wird.
     */
    private boolean kurzUnterbrechen = false;

    /**
     * Starten der Kreisbewegung durch Erzeugung eines eigenen {@link Thread}.
     * Innerhalb des Thread wird die Methode {@link #run()} ausgeführt.
     */
    public void beginneBewegung() {
        new Thread(this).start();
    }

    /**
     * Setzt den Wert in {@link #mussAufhoeren} auf true. Damit wird in
     * {@link #run()} der mit {@link #beginneBewegung()} erzeugte Thread gestoppt.
     */
    public void beendeBewegung() {
        mussAufhoeren = true;
    }

    /**
     * Bewegt das Rindvieh im Kreis um seinen Ursprung. Dabei wird möglichst häufig
     * geprüft, ob die Bewegung beendet werden soll, also ob {@link #mussAufhoeren}
     * auf true gesetzt wurde.
     * <p>
     * Die einzelnen Schritte der Bewegung werden alle 50ms ausgelöst.
     */
    public void run() {
        // Die Schleife läuft solange, bis wir aufhören sollen
        while (!mussAufhoeren) {
            // Vier mal drehen und vorwärtsgehen schließt einen Kreis
            for (int i = 0; (i < 4) && (!mussAufhoeren); i++) {
                int j = 0;
                while ((j < 2) && (!mussAufhoeren)) {
                    while (kurzUnterbrechen) {
                        SystemClock.sleep(50);
                    }

                    // Der eigentliche Schritt
                    geheVor();
                    j++;
                }
                dreheDichRechtsRum();
            }
        }
    }

    /**
     * Setzt den Ursprung der Kreisbewegung. Dabei wird geprüft, ob die Kuh
     * innerhalb des Ackers noch einen vollständien Kreis laufen kann. Ist dies möglich,
     * wird die aktuelle Kreisbewegung unterbrochen, der Urspung verschoben und am neuen Ursprung
     * fortgesetzt.
     *
     * @param x X-Koordinate des Mittelpunkts der Kreisbewegung
     * @param y Y-Koordinate des Mittelpunkts der Kreisbewegung
     */
    public void setzeUrsprung(int x, int y) {
        // Das unterbricht die Kreisbewegung
        kurzUnterbrechen = true;

        // die Startbewegung des Kreisens ist links oberhalb des Ursprungs
        Position neuePosition = new Position(
                gibPosition().x + (x - ursprung.x),
                gibPosition().y + (y - ursprung.y));

        // Ist diese Position gültig?
        if (gibAcker().istPositionGueltig(neuePosition)) {
            setzePosition(neuePosition);
            ursprung = new Position(x, y);
        }

        // Kleinste und größte Position während der Kreisbewegung berechnen
        Position kleinstePosition = new Position(x - 1, y - 1);
        Position groesstePosition = new Position(x + 1, y + 1);

        // Sind diese Positionen möglich?
        if (gibAcker().istPositionGueltig(kleinstePosition) && gibAcker().istPositionGueltig(groesstePosition)) {
            // Das setzt die Kreisbewegung fort
            kurzUnterbrechen = false;
        } else {
            setzeNachricht(gibName() + " steht da und schüttelt den Kopf: So kann ich nicht arbeiten!\nIch kann keine Position am äußersten Feldrand beziehen.");
        }
    }
}
