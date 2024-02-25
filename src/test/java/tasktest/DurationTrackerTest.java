package tasktest;

import de.hdm.bd.timekiller.model.task.DurationTracker;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DurationTrackerTest {

        @Test
        public void testSetStart() {
                DurationTracker durationTracker = new DurationTracker();
                long currentTime = new Date().getTime();
                durationTracker.setStart(currentTime);
                assertEquals(currentTime, durationTracker.getStartTime());
        }

        @Test
        public void testSetEnd() {
                DurationTracker durationTracker = new DurationTracker();
                long currentTime = new Date().getTime();
                durationTracker.setEnd(currentTime);
                assertEquals(currentTime, durationTracker.getEndTime());
        }


        @Test
        public void testStartAndStopMethods() {
                DurationTracker durationTracker = new DurationTracker();
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



}