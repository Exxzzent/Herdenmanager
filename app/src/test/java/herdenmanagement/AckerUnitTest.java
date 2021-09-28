package herdenmanagement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import herdenmanagement.model.Acker;
import herdenmanagement.model.Position;
import herdenmanagement.model.Rindvieh;

import static org.junit.Assert.*;

/**
 * Testet die Grundfunktionen des Ackers
 */
public class AckerUnitTest {

    @Before
    public void setUp() {

    }

    @After
    public void shutDown() {
        // Daten löschen
    }

    @Test
    public void bewegeRind() {
        Acker acker = new Acker(5, 5);

        Rindvieh rindvieh = new Rindvieh("Rindvieh");
        assertEquals("Rindvieh", rindvieh.gibName());
        acker.lassRindWeiden(rindvieh);

        Position position = rindvieh.gibPosition();
        rindvieh.geheVor();
        rindvieh.geheVor();

        // y bleibt gleich, x erhöht sich
        assertEquals(position.x + 2, rindvieh.gibPosition().x);
        assertEquals(position.y, rindvieh.gibPosition().y);

        rindvieh.setzePosition(new Position(0, 0));

        boolean zurueck = rindvieh.gehtsDaWeiterZurueck();
        assertFalse(zurueck);

        boolean vor = rindvieh.gehtsDaWeiterVor();
        assertTrue(vor);

        rindvieh.geheVor();
        zurueck = rindvieh.gehtsDaWeiterZurueck();
        assertTrue(zurueck);

        rindvieh.dreheDichRechtsRum();
        rindvieh.dreheDichRechtsRum();

        vor = rindvieh.gehtsDaWeiterVor();
        assertTrue(vor);

        rindvieh.geheVor();
        vor = rindvieh.gehtsDaWeiterVor();
        assertFalse(vor);

        rindvieh.geheZurueck();
        vor = rindvieh.gehtsDaWeiterVor();
        assertTrue(vor);
    }

    @Test
    public void zubehoer() {
        Acker acker = new Acker(5, 5);

        acker.lassGrasWachsen(new Position(2, 2));
        assertFalse(acker.istDaGras(new Position(1, 2)));
        assertTrue(acker.istDaGras(new Position(2, 2)));

        boolean entfernt = acker.entferneGras(new Position(2, 2));
        assertFalse(acker.istDaGras(new Position(2, 2)));
        assertTrue(entfernt);

        acker.stelleEimerAuf(new Position(4, 0));
        assertFalse(acker.istDaEinEimer(new Position(4, 3)));
        assertTrue(acker.istDaEinEimer(new Position(4, 0)));
    }

    @Test
    public void melkeRind() {
        Acker acker = new Acker(5, 5);

        Rindvieh rindvieh = new Rindvieh("Rindvieh");
        acker.lassRindWeiden(rindvieh);

        acker.lassGrasWachsen(new Position(0, 0));
        rindvieh.frissGras();

        // dort steht kein Eimer, kann also nicht funktionieren
        int milch = rindvieh.gibMilch();
        assertEquals(0, milch);

        // Eimer aufstellen
        acker.stelleEimerAuf(new Position(0, 0));
        milch = rindvieh.gibMilch();
        assertEquals(1, milch);
    }
}