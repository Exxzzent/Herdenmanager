package herdenmanagement;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import herdenmanagement.model.Acker;
import herdenmanagement.model.Eimer;
import herdenmanagement.model.Gras;
import herdenmanagement.model.Position;
import herdenmanagement.model.PositionsElement;
import herdenmanagement.model.Rindvieh;

public class ObserverUnitTest {

    private PropertyChangeEvent evt;
    private PropertyChangeListener listener;

    @Before
    public void setUp() {
        listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                ObserverUnitTest.this.evt = evt;
            }
        };
    }

    @Test
    public void observeAcker() {
        Acker acker = new Acker(10, 10);
        acker.fuegeBeobachterHinzu(listener);

        acker.lassGrasWachsen(new Position(1, 1));
        assertEquals(Acker.PROPERTY_GRAESER, evt.getPropertyName());
        assertTrue(evt.getNewValue() instanceof Gras);

        acker.stelleEimerAuf(new Position(2, 2));
        assertEquals(Acker.PROPERTY_EIMER, evt.getPropertyName());
        assertTrue(evt.getNewValue() instanceof Eimer);

        Rindvieh rindvieh = new Rindvieh("Rindvieh");
        acker.lassRindWeiden(rindvieh);
        assertEquals(Acker.PROPERTY_VIECHER, evt.getPropertyName());
        assertTrue(evt.getNewValue() instanceof Rindvieh);
    }

    @Test
    public void observeRindvieh() {
        Acker acker = new Acker(10, 10);

        Rindvieh rindvieh = new Rindvieh("Rindvieh");
        rindvieh.fuegeBeobachterHinzu(listener);

        acker.lassRindWeiden(rindvieh);
        rindvieh.geheVor();

        assertEquals(PositionsElement.PROPERTY_POSITION, evt.getPropertyName());
        assertTrue(evt.getNewValue() instanceof Position);
        assertEquals(1, ((Position)evt.getNewValue()).x);

        rindvieh.dreheDichRechtsRum();
        assertEquals(Rindvieh.PROPERTY_RICHTUNG, evt.getPropertyName());
        assertTrue(evt.getNewValue() instanceof Rindvieh.RichtungsTyp);
        assertEquals(Rindvieh.RichtungsTyp.SUED, evt.getNewValue());

        rindvieh.dreheDichLinksRum();
        assertEquals(Rindvieh.PROPERTY_RICHTUNG, evt.getPropertyName());
        assertTrue(evt.getNewValue() instanceof Rindvieh.RichtungsTyp);
        assertEquals(Rindvieh.RichtungsTyp.OST, evt.getNewValue());
    }
}
