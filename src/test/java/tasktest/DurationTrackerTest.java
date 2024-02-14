package tasktest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.Task;
import org.junit.Before;
import org.junit.Test;

public class DurationTrackerTest {

        private Task task;
        private DurationTracker durationTracker;

        @Before
        public void setUp() {
                task = new Task(1, "TestTask");
                durationTracker = mock(DurationTracker.class);
                task.setDurationTracker(durationTracker);
        }

        @Test
        public void testStart() {
                // Überprüfen, dass startTime null ist, bevor start aufgerufen wird
                assertNull(durationTracker.getStartTime());

                // Aufruf der start-Methode auf der Task-Instanz
                task.start();

                // Überprüfen, dass startTime nach dem Aufruf nicht mehr null ist
                assertNotNull(durationTracker.getStartTime());

                // Überprüfen, dass die start-Methode auf der DurationTracker-Instanz aufgerufen wurde
                verify(durationTracker).start();
        }

}