package tasktest;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.Task;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskTest {

    @Test
    public void testCreateTaskWithName() throws IllegalNameException {
        String taskName = "TestTask";
        Task task = new Task(taskName);

        assertEquals(taskName, task.getName());
    }

    @Test
    public void testCreateTaskWithIdAndName() throws IllegalNameException {
        int taskId = 1;
        String taskName = "TestTask2";
        Task task = new Task(taskId, taskName);

        assertEquals(taskId, task.getId());
        assertEquals(taskName, task.getName());
    }

    @Test
    public void testSetName() throws IllegalNameException {
        Task task = new Task("OldName");

        String newName = "NewName";
        task.setName(newName);

        assertEquals(newName, task.getName());
    }

    @Test
    public void testStartAndStop() throws IllegalNameException {
        Task task = new Task("TestTask3");

        task.start();
        assertTrue(task.isActive());

        task.stop();
        assertFalse(task.isActive());
    }

    @Test
    public void testGetOverallDuration() throws IllegalNameException {
        Task task = new Task("TestTask4");

        DurationTracker mockDurationTracker = mock(DurationTracker.class);
        when(mockDurationTracker.getDuration()).thenReturn(1000L);

        task.addRecordToTask(mockDurationTracker);

        assertEquals(1000L, task.getOverallDuration());
    }

    @Test
    public void testGetOverallDurationWithTimeRange() throws IllegalNameException {
        Task task = new Task("TestTask5");

        DurationTracker mockDurationTracker = mock(DurationTracker.class);
        when(mockDurationTracker.getStartTime()).thenReturn(new Date().getTime() - 2000L);
        when(mockDurationTracker.getEndTime()).thenReturn(new Date().getTime());
        when(mockDurationTracker.getDuration()).thenReturn(2000L);

        task.addRecordToTask(mockDurationTracker);

        Date startTime = new Date(new Date().getTime() - 3000L);
        Date endTime = new Date();

        assertEquals(2000L, task.getOverallDuration(startTime, endTime));
    }
}