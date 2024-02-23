package tasktest;


import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.Task;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class DurationTrackerTest {

        private DurationTracker durationTracker;

        @BeforeEach
        public void setUp() {
                durationTracker = new DurationTracker(1);
                Task task = new Task(1, "TestTask");
        }

        @Test
        public void testStart() {
                assertNull(durationTracker.getStartTime());

                durationTracker.start();

                assertNotNull(durationTracker.getStartTime());
        }

        @Test
        public void testStop() {
                assertNull(durationTracker.getEndTime());

                durationTracker.start();
                durationTracker.stop();

                assertNotNull(durationTracker.getEndTime());
        }

        @Test
        public void testGetDuration() {
                assertEquals(0, durationTracker.getDuration());

                durationTracker.start();
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                durationTracker.stop();

                assertEquals (1, durationTracker.getDuration(), 0.1 );
        }

        @Test
        public void testReset() {
                durationTracker.start();

                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                durationTracker.stop();

                durationTracker.reset();

                assertEquals(0, durationTracker.getDuration());
        }

}