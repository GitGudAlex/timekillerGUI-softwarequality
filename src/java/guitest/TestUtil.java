package guitest;
import de.hdm.bd.light.ctrl.Controller;
import de.hdm.bd.light.model.Bulb;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestUtil {
    Bulb bulbspy = Mockito.spy(new Bulb());
    Bulb bulbMock = Mockito.mock(Bulb.class);
    Bulb actualBulb = new Bulb();

    @Test
    public void zustandTest1(){
        doReturn(50).when(bulbspy).getRandomNumber(); //Farbe rot
        bulbspy.setState("on");
        //bulbspy.setIntensity(55);

        assertEquals("on", bulbspy.getState());
        assertEquals(50, bulbspy.getIntensity());
        assertEquals("red", bulbspy.getColor());
    }
    @Test
    public void zustandTest2(){
        doReturn(50).when(bulbspy).getRandomNumber(); //Farbe rot
        bulbspy.setState("off");
        //bulbspy.setIntensity(55);

        assertEquals("off", bulbspy.getState());
        assertEquals(0, bulbspy.getIntensity());
        assertEquals("black", bulbspy.getColor());
    }
    @Test
    public void zustandTest3(){
        doReturn(50).when(bulbspy).getRandomNumber(); //Farbe rot
        bulbspy.setState("off");
        bulbspy.setIntensity(55);

        assertEquals("off", bulbspy.getState());
        assertEquals(0, bulbspy.getIntensity());
        assertEquals("black", bulbspy.getColor());
    }
    /*
    @ParameterizedTest
    @CsvSource({"50, off, 0", "50, off, 0", "50, off, 55"})
    public void zustandTest(int selectValue, String state, int intensity, int expectedIntensity){
        doReturn(selectValue).when(bulbspy).getRandomNumber();
        bulbspy.setState(state);
        bulbspy.setIntensity(intensity);

        assertEquals(state, bulbspy.getState());
        assertEquals(intensity, bulbspy.getIntensity());
    }
    */

    /**
     * Methode zum Erstellen von Screenshots der Anwendung
     */
    public static Image snapShot(final Node node) throws Exception {
        if (!Platform.isFxApplicationThread()) {
            Task<Image> task = new Task<Image>() {

                @Override
                protected Image call() throws Exception {
                    WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);
                    return snapshot;
                }
            };
            Platform.runLater(task);
            return task.get();
        } else {
            WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);
            return snapshot;
        }

    }

    /**
     * Hinweis: hier wird die Farbe zweier Bilder verglichen.
     * Da angenommen wird, dass das gesamte Bild in einer Farbe eingefärbt ist, wird die Farbe
     * eines einzigen Pixels (Koordinaten: (pixel_x, pixel_y)) verglichen.
     * Der Vergelich erfolgt mit einer Toleranz.
     */
    public static boolean isSameImage(Image image, Image image2) {
        double tolerance = 0.0000001;
        int pixel_x = 45;
        int pixel_y = 45;
        if (image.getWidth() != image2.getWidth() || image.getHeight() != image2.getHeight()) {
            return false;
        }
        PixelReader pixelReader = image.getPixelReader();
        PixelReader pixelReader1 = image2.getPixelReader();

                Color color1 = pixelReader.getColor(pixel_x, pixel_y);
                double red1 = color1.getRed();
                double blue1 = color1.getBlue();
                double green1 = color1.getGreen();
                Color color2 = pixelReader1.getColor(pixel_x, pixel_y);
                double red2 = color2.getRed();
                double blue2 = color2.getBlue();
                double green2 = color2.getGreen();

                double redDiff = Math.abs(red1 - red2);
                double blueDiff = Math.abs(blue1 - blue2);
                double greenDiff = Math.abs(green1 - green2);

                if (redDiff > tolerance || blueDiff > tolerance || greenDiff  > tolerance) {
                    return false;
                }
        return true;
    }
}
