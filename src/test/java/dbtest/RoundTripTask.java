package dbtest;

import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoundTripTask {
    private Task task;

    @BeforeEach
    void setUp() {
        //Vor jedem Testfall wird eine neue Task-Instanz erstellt
        try {
            task = new Task("TestTask");
        } catch (IllegalNameException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        //Nach jedem Testfall wird die Task-Instanz zurückgesetzt
        task = null;
    }

    /**
     * Round Trip
     * Überprüfen ob bei Task.stop() der dazugehörige DurationTracker gespeichert wurde
     */
    @Test
    void testSaveRecordOnTaskStop() {
        //Starten der Task
        task.start();
        //Für eine Sekunde Task aktiv lassen
        try {
            Thread.sleep(1000); // Eine Sekunde warten
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Stoppen der Task
        task.stop();

        //Überprüfen, ob ein DurationTracker gespeichert wurde
        Collection<DurationTracker> durationTrackers = task.getRecords();
        assertNotNull(durationTrackers);
        assertEquals(1, durationTrackers.size(), "Es wurde erwartet, dass ein DurationTracker in der Datenbank gespeichert wurde");
    }
}
