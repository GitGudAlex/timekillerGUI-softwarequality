package guitest;

import de.hdm.bd.light.ctrl.Controller;
import de.hdm.bd.light.ctrl.RGB;
import de.hdm.bd.light.model.Bulb;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ControllerTest {
    Bulb bulbMock = mock(Bulb.class);
    Controller spyController = spy(new Controller());
    @BeforeEach
    public void setUp(){
        //BulbMock Objekt zurückgeben sobald getBulb() aufgerufen wird
        doReturn(bulbMock).when(spyController).getBulb();
        //doCallRealMethod().when(spyController).getCurrentColor();
    }

    @Test
    public void testSwitchOnLightMock(){
        spyController.switchOnLight();
        verify(bulbMock, times(1)).setState("on");
    }
    @Test
    public void testSwitchOffLightMock(){
        spyController.switchOffLight();
        verify(bulbMock, times(1)).setState("off");
    }

    @Test
    public void testSwitchOnLight() {
        bulbMock.setState("on");
        verify(bulbMock, times(1)).setState("on");
    }

    @Test
    public void testSwitchOnLight2() {
        assertEquals("off", spyController.getBulb().getState());
        spyController.switchOnLight();
        assertEquals("on", spyController.getBulb().getState());
    }
    @Test
    public void testSwitchOnLight3() {
        assertEquals("off", spyController.getBulb().getState());
        spyController.switchOnLight();
        assertEquals("on", spyController.getBulb().getState());
        spyController.switchOnLight();
        assertEquals("on", spyController.getBulb().getState());
    }

    @Test
    public void testSwitchOffLight() {
        assertEquals("off", spyController.getBulb().getState());
        spyController.switchOnLight();
        assertEquals("on", spyController.getBulb().getState());
        spyController.switchOffLight();
        assertEquals("off", spyController.getBulb().getState());

    }

    @Test
    public void testDimUpLight() {
        spyController.switchOnLight();
        spyController.dimUpLight();
        assertEquals(55, spyController.getBulb().getIntensity());
    }

    @Test
    public void testDimDownLight() {
        spyController.switchOnLight();
        spyController.dimDownLight();
        assertEquals(45, spyController.getBulb().getIntensity());
    }
    /*
    @Test
    public void testCurrentColor() {
        when(spyController.getBulb().getColor()).thenReturn("red");
        assertEquals();
        assertEquals(50, controller.getBulb().getIntensity());
    }

     */
    @Test
    public void testGetCurrentColor() {
        //Erwartetes Verhalten verändern
        when(bulbMock.getColor()).thenReturn("red");
        when(bulbMock.getIntensity()).thenReturn(50);

        //assertEquals("red",spyController.getBulb().getColor()); //Verhalten funktioniert richtig

        //getCurrentColor() liefert nur Referenz mit .getRed()... Farbe ausgeben
        RGB result = spyController.getCurrentColor();

        //Erwartete RGB-Werte für "red" und Intensität 50
        RGB expected = new RGB(155, 0, 0);

        assertEquals(expected.getRed(), result.getRed());
        assertEquals(expected.getGreen(), result.getGreen());
        assertEquals(expected.getBlue(), result.getBlue());
    }
}
