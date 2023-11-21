package tasktest;

import de.hdm.bd.timekiller.model.task.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {
    @Test
    public void testStartAndStopMethods() {
        //Hier lieber Mockito verwenden mit times1?
        Task task = new Task(1, "TestTask");

        //Task inaktiv
        assertFalse(task.isActive());

        //Task starten und überprüfen
        task.start();
        assertTrue(task.isActive());

        // Task stoppen und überprüfen
        task.stop();
        assertFalse(task.isActive());
    }

    @Test
    public void testGetOverallDuration() {
        //Eventuell mit Spy Milliseconds returnen?
        Task task = new Task(1, "TestTask");
        task.start();

        //Zeit simulieren
        simulateDuration(task, 5000);

        task.stop();
        assertEquals(5, task.getOverallDuration());
    }
    private void simulateDuration(Task task, long milliseconds) {
        try {
            //Zeit simulieren
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
