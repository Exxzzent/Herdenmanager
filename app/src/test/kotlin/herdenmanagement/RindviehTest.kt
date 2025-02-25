package herdenmanagement.model

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class RindviehTest {

    private val acker = Acker(5, 5)
    private val rind = Rindvieh("Berta")

    @Before
    fun setUp() {
        rind.acker = acker
        rind.position = Position(2, 2)
    }

    @Test
    fun geheVorTest() {
        rind.richtung = Richtung.NORD
        rind.geheVor()
        assertEquals(Position(2, 1), rind.position)

        rind.richtung = Richtung.OST
        rind.geheVor()
        assertEquals(Position(3, 1), rind.position)

        rind.richtung = Richtung.SUED
        rind.geheVor()
        assertEquals(Position(3, 2), rind.position)

        rind.richtung = Richtung.WEST
        rind.geheVor()
        assertEquals(Position(2, 2), rind.position)
    }

    @Test
    fun geheVorRandtest() {
        rind.position = Position(0, 0)
        rind.richtung = Richtung.NORD
        rind.geheVor()
        assertEquals(Position(0, 0), rind.position)

        rind.richtung = Richtung.WEST
        rind.geheVor()
        assertEquals(Position(0, 0), rind.position)

        rind.position = Position(acker.spalten - 1, acker.zeilen - 1)
        rind.richtung = Richtung.SUED
        rind.geheVor()
        assertEquals(Position(acker.spalten - 1, acker.zeilen - 1), rind.position)

        rind.richtung = Richtung.OST
        rind.geheVor()
        assertEquals(Position(acker.spalten - 1, acker.zeilen - 1), rind.position)
    }

    @Test
    fun `frissGras kein Gras test`() {
        rind.frissGras()
        assertEquals(Rindvieh.Status.WARTET, rind.status)
    }
}