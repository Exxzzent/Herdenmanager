package herdenmanagement

import herdenmanagement.model.Acker
import herdenmanagement.model.Position
import herdenmanagement.model.Rindvieh
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Testet die Grundfunktionen des Ackers
 */
class AckerUnitTest {
    @Before
    fun setUp() {
    }

    @After
    fun shutDown() {
        // Daten löschen
    }

    @Test
    fun bewegeRind() {
        val acker = Acker(5, 5)
        val rindvieh = Rindvieh("Rindvieh")
        Assert.assertEquals("Rindvieh", rindvieh.name)

        acker.lassRindWeiden(rindvieh)
        val position = rindvieh.position
        rindvieh.geheVor()
        rindvieh.geheVor()

        // y bleibt gleich, x erhöht sich
        Assert.assertEquals((position.x + 2).toLong(), rindvieh.position.x.toLong())
        Assert.assertEquals(position.y.toLong(), rindvieh.position.y.toLong())

        rindvieh.position = Position(0, 0)
        var zurueck = rindvieh.gehtsDaWeiterZurueck
        Assert.assertFalse(zurueck)

        var vor = rindvieh.gehtsDaWeiterVor
        Assert.assertTrue(vor)

        rindvieh.geheVor()
        zurueck = rindvieh.gehtsDaWeiterZurueck
        Assert.assertTrue(zurueck)

        rindvieh.dreheDichRechtsRum()
        rindvieh.dreheDichRechtsRum()
        vor = rindvieh.gehtsDaWeiterVor
        Assert.assertTrue(vor)

        rindvieh.geheVor()
        vor = rindvieh.gehtsDaWeiterVor
        Assert.assertFalse(vor)

        rindvieh.geheZurueck()
        vor = rindvieh.gehtsDaWeiterVor
        Assert.assertTrue(vor)
    }

    @Test
    fun zubehoer() {
        val acker = Acker(5, 5)
        acker.lassGrasWachsen(Position(2, 2))
        Assert.assertFalse(acker.istDaGras(Position(1, 2)))
        Assert.assertTrue(acker.istDaGras(Position(2, 2)))

        val entfernt = acker.entferneGras(Position(2, 2))
        Assert.assertFalse(acker.istDaGras(Position(2, 2)))
        Assert.assertTrue(entfernt)
        acker.lassKalbWeiden(Position(4, 0))
        Assert.assertFalse(acker.istDaEinKalb(Position(4, 3)))
        Assert.assertTrue(acker.istDaEinKalb(Position(4, 0)))
    }

    @Test
    fun melkeRind() {
        val acker = Acker(5, 5)
        val rindvieh = Rindvieh("Rindvieh")
        acker.lassRindWeiden(rindvieh)
        acker.lassGrasWachsen(Position(0, 0))
        rindvieh.frissGras()

        // dort steht kein Kalb, kann also nicht funktionieren
        var milch = rindvieh.gibMilch()
        Assert.assertEquals(0, milch.toLong())

        // Kalb aufstellen
        acker.lassKalbWeiden(Position(0, 0))
        milch = rindvieh.gibMilch()
        Assert.assertEquals(1, milch.toLong())
    }
}