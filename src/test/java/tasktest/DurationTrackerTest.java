package tasktest;

import de.hdm.bd.timekiller.model.task.DurationTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DurationTrackerTest {
        private DurationTracker durationTracker;

        @BeforeEach
        public void setUp(){
                durationTracker = new DurationTracker();
        }
        @Test
        public void testSetStart() {
                long currentTime = new Date().getTime();
                durationTracker.setStart(currentTime);
                assertEquals(currentTime, durationTracker.getStartTime());
        }

        @Test
        public void testSetEnd() {
                long currentTime = new Date().getTime();
                durationTracker.setEnd(currentTime);
                assertEquals(currentTime, durationTracker.getEndTime());
        }

        @Test
        public void testStartMethod() {
                //Vor dem Starten überprüfen, ob die Startzeit 0 ist
                assertEquals(0, durationTracker.getStartTime());

                //Starten der Zeitverfolgung
                durationTracker.start();

                //Nach dem Starten überprüfen, ob die Startzeit aktualisiert wurde
                long startTime = durationTracker.getStartTime();

                //Überprüfen, ob die Startzeit größer als 0 ist
                assertTrue(startTime > 0, "Startzeit sollte größer als 0 sein.");

                //Versuche erneut zu starten und überprüfe, ob eine Ausnahme geworfen wird
                assertThrows(IllegalStateException.class, () -> durationTracker.start());
        }

        @Test
        public void testStartAndStopMethods() {
                assertEquals(0, durationTracker.getStartTime());
                assertEquals(0, durationTracker.getEndTime());
                assertEquals(0, durationTracker.getDuration());

                durationTracker.start();
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                durationTracker.stop();

                long startTime = durationTracker.getStartTime();
                long endTime = durationTracker.getEndTime();
                long duration = durationTracker.getDuration();

                assertTrue(startTime <= endTime,
                        "Startzeit sollte vor der Endzeit liegen.");
                assertTrue(duration > 0,
                        "Die Dauer sollte größer oder gleich 0 sein.");
        }

        @Test
        public void testStopMethodWhenAlreadyStopped() {
                //Simuliere, dass der Task bereits gestoppt wurde
                durationTracker.setStart(System.currentTimeMillis());
                durationTracker.setEnd(System.currentTimeMillis());

                //Überprüfe, ob eine Ausnahme geworfen wird, wenn versucht wird, den gestoppten Task erneut zu stoppen
                assertThrows(IllegalStateException.class, () -> durationTracker.stop());
        }

        @Test
        public void testStopMethodWhenNeverStarted() {
                //Überprüfe, ob eine Ausnahme geworfen wird, wenn versucht wird, einen nie gestarteten Task zu stoppen
                assertThrows(IllegalStateException.class, () -> durationTracker.stop());
        }

        @Test
        public void testStartMethodWhenAlreadyStarted() {
                // Simuliere, dass der Task bereits gestartet wurde
                durationTracker.start();

                // Überprüfe, ob eine Ausnahme geworfen wird, wenn versucht wird, den gestarteten Task erneut zu starten
                assertThrows(IllegalStateException.class, () -> durationTracker.start());
        }

        @Test
        public void testStartMethodWhenAlreadyStopped() {
                // Simuliere, dass der Task bereits gestoppt wurde
                durationTracker.setStart(System.currentTimeMillis());
                durationTracker.setEnd(System.currentTimeMillis());

                // Überprüfe, ob eine Ausnahme geworfen wird, wenn versucht wird, den gestoppten Task zu starten
                assertThrows(IllegalStateException.class, () -> durationTracker.start());
        }



}