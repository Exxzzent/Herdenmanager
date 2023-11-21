package herdenmanagement

import herdenmanagement.model.*
import herdenmanagement.model.Richtung
import org.junit.Assert
import org.junit.Test
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

class ObserverTest {

    private var evt: PropertyChangeEvent? = null
    private var listener: PropertyChangeListener = PropertyChangeListener { evt -> this@ObserverTest.evt = evt }

    @Test
    fun observeAcker() {
        val acker = Acker(10, 10)
        acker.fuegeBeobachterHinzu(listener)

        acker.lassGrasWachsen(Position(1, 1))
        Assert.assertEquals(Keys.PROPERTY_GRAESER, evt!!.propertyName)
        Assert.assertTrue(evt!!.newValue is Gras)

        acker.lassKalbWeiden(Position(2, 2))
        Assert.assertEquals(Keys.PROPERTY_KALB, evt!!.propertyName)
        Assert.assertTrue(evt!!.newValue is Kalb)

        acker.lassRindWeiden("Rindvieh")
        Assert.assertEquals(Keys.PROPERTY_VIECHER, evt!!.propertyName)
        Assert.assertTrue(evt!!.newValue is Rindvieh)
    }

    @Test
    fun observeRindvieh() {
        val acker = Acker(10, 10)
        val rindvieh = acker.lassRindWeiden("Rindvieh")
        rindvieh.fuegeBeobachterHinzu(listener)

        rindvieh.geheVor()
        Assert.assertEquals(Keys.PROPERTY_POSITION, evt!!.propertyName)
        Assert.assertTrue(evt!!.newValue is Rindvieh)
        Assert.assertEquals(1, (evt!!.newValue as Rindvieh).position.x.toLong())

        rindvieh.dreheDichRechtsRum()
        Assert.assertEquals(Keys.PROPERTY_RICHTUNG, evt!!.propertyName)
        Assert.assertTrue(evt!!.newValue is Rindvieh)
        Assert.assertEquals(Richtung.SUED, (evt!!.newValue as Rindvieh).richtung)

        rindvieh.dreheDichLinksRum()
        Assert.assertEquals(Keys.PROPERTY_RICHTUNG, evt!!.propertyName)
        Assert.assertTrue(evt!!.newValue is Rindvieh)
        Assert.assertEquals(Richtung.OST, (evt!!.newValue as Rindvieh).richtung)
    }
}