package tasktest;


import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.Task;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

                assertTrue("Startzeit sollte vor der Endzeit liegen.", startTime <= endTime);
                assertTrue("Die Dauer sollte größer oder gleich 0 sein.", duration > 0);
        }



}