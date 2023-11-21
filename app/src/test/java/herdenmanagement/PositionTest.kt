import herdenmanagement.model.Position
import herdenmanagement.model.Richtung
import org.junit.Assert.*
import org.junit.Test

class PositionTest {

    @Test
    fun testNaechste() {
        val position = Position(3, 4)
        assertEquals(Position(3, 3), position.naechste(Richtung.NORD))
        assertEquals(Position(4, 4), position.naechste(Richtung.OST))
        assertEquals(Position(3, 5), position.naechste(Richtung.SUED))
        assertEquals(Position(2, 4), position.naechste(Richtung.WEST))
    }

    @Test
    fun testEquals() {
        val position1 = Position(3, 4)
        val position2 = Position(3, 4)
        val position3 = Position(5, 6)

        assertTrue(position1 == position2)
        assertFalse(position1 == position3)
    }

    @Test
    fun testHashCode() {
        val position1 = Position(3, 4)
        val position2 = Position(3, 4)
        val position3 = Position(5, 6)

        assertEquals(position1.hashCode(), position2.hashCode())
        assertNotEquals(position1.hashCode(), position3.hashCode())
    }
}
