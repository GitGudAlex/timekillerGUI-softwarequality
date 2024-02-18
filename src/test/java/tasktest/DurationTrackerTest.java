package tasktest;


import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.Task;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DurationTrackerTest {

        @Test
        public void testStartAndStop() {
                DurationTracker durationTracker = new DurationTracker(1); // 1 ist eine Beispiel-Task-ID

                Task task = new Task(1, "TestTask");

                task.start();
                task.stop();

                assertEquals(0, durationTracker.getDuration());
        }
}