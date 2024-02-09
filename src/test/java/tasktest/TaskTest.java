package tasktest;

import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.Task;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TaskTest {
    @Test
    public void testStartAndStopMethods() {
        // Mock für DurationTracker erstellen
        DurationTracker mockDurationTracker = mock(DurationTracker.class);

        // Task erstellen und Mock übergeben
        Task task = new Task(1, "TestTask");
        task.setDurationTracker(mockDurationTracker);

        // Task sollte zu Beginn inaktiv sein
        assertFalse("Task sollte zu Beginn inaktiv sein.", task.isActive());

        // Task starten und überprüfen
        task.start();
        assertTrue("Task sollte aktiv sein, nachdem er gestartet wurde.", task.isActive());


        // Task stoppen und überprüfen
        task.stop();
        assertFalse("Task sollte inaktiv sein, nachdem er gestoppt wurde.", task.isActive());

    }
    @Test
    public void testGetOverallDuration() {
        // Mock für DurationTracker erstellen
        DurationTracker mockDurationTracker = mock(DurationTracker.class);
        when(mockDurationTracker.getDuration()).thenReturn(1000L); // Beispielwert

        // Task erstellen und Mock übergeben
        Task task = new Task(1, "TestTask");
        task.setDurationTracker(mockDurationTracker);

        // Überprüfen, ob die getOverallDuration den Wert des DurationTrackers zurückgibt
        assertEquals(1000L, task.getOverallDuration());

    }

}
