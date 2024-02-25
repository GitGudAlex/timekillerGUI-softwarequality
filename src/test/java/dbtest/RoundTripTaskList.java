package dbtest;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.DbManager;
import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.Task;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoundTripTaskList {
    private DbManager dbManager;
    private Connection conn;
    private ITaskList taskList;
    private String databaseName = "test_timekiller.db";

    @BeforeEach
    public void setUp() throws Exception{
        //DB Connection zur Testdatenbank
        conn = DriverManager.getConnection("jdbc:sqlite:test_timekiller.db");
        dbManager = new DbManager("test_timekiller.db");
        taskList = new TaskListImpl(databaseName);
    }

    @AfterEach
    public void closeDatabase() throws Exception{
        dbManager.dropTables();
        if (conn != null){
            conn.close();
        }
    }

    /**
     * RoundTrip
     * Speichern eines neuen Record-Objektes in der Datenbank beim Beenden einer Task
     */
    @Test
    public void testSaveRecordOnTaskStop() throws Exception {
        //Neuer Task erstellen und starten
        Task testTask = new Task("testTask");

        //Task für 2 Sekunden aktiv lassen und wieder stoppen
        testTask.start();
        Thread.sleep(2000);
        testTask.stop();

        //Task in DB-Tabelle Task speichern
        taskList.insertTask(testTask.getName());

        //Überprüfen ob Task in der Datenbank gespeichert wurde
        List<Task> testList = dbManager.getTaskDao().queryForEq("name", "testTask");
        assertNotNull(testList);
        assertEquals(1, testList.size(), "Expected one task in the database");

        //DurationTracker in DB-Tabelle DurationTracker speichern
        Collection<DurationTracker> durationTrackerColl = testTask.getRecords();
        dbManager.getDurationTrackerDao().create(durationTrackerColl);

        //Überprüfen ob DurationTracker für erstellten Task in der Datenbank gefunden wurde
        DurationTracker durationTrackers = dbManager.getDurationTrackerDao().queryForId(testList.get(0).getId());
        assertNotNull(durationTrackers);

        assertEquals(testList.get(0).getId(), durationTrackers.getId(), "Expected same ID Task and DurationTracker ");
        assertEquals(testTask.getOverallLifetimeDuration(), durationTrackers.getDuration());
    }

    /**
     * RoundTrip
     * Speichern mehrerer Tasks
     */
    @Test
    public void testSavedSizeRecords() throws Exception{
        //Neuer Task erstellen und starten
        Task testTask = new Task("testTask");
        Task testTask2 = new Task("testTask2");

        //Tasks für 2 Sekunden aktiv lassen und wieder stoppen
        testTask.start();
        testTask2.start();
        Thread.sleep(2000);
        testTask.stop();
        testTask2.stop();

        //Task in DB-Tabelle Task speichern
        taskList.insertTask(testTask.getName());
        taskList.insertTask(testTask2.getName());

        //DurationTrackers in DB-Tabelle DurationTracker speichern
        Collection<DurationTracker> durationTrackerColl = testTask.getRecords();
        dbManager.getDurationTrackerDao().create(durationTrackerColl);
        Collection<DurationTracker> durationTrackerColl2 = testTask2.getRecords();
        dbManager.getDurationTrackerDao().create(durationTrackerColl2);

        //Anzahl Tasks aus Datenbank abrufen
        int anzahlTasks = dbManager.getTaskDao().queryForAll().size();

        //Anzahl DurationTracker aus Datenbank abrufen
        int anzahlDurationTrackers = dbManager.getDurationTrackerDao().queryForAll().size();

        //Test ob gleiche Anzahl vorhanden
        assertEquals(anzahlTasks, anzahlDurationTrackers);
    }

    /**
     * RoundTrip
     * Entfernen einer Task (und ihrer Duration-Tracker-Objekte) aus der Task-Liste
     */
    @Test
    public void deleteTaskWithDurationTracker()
            throws SQLException, IllegalNameException, DuplicatedNameException {
        Task task = new Task("deleteMe");
        task.start();
        try {
            Thread.sleep(2000);
            task.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
        taskList.insertTask(task.getName());

        //Task aus DB holen
        List<Task> deleteMeTask = dbManager.getTaskDao().queryForEq("name", "deleteMe");
        assertNotNull(deleteMeTask);
        //DurationTracker des Tasks aus DB holen
        Collection <DurationTracker> durTrackerDelTask = deleteMeTask.get(0).getRecords();
        assertNotNull(durTrackerDelTask);

        //Überprüfen ob durationTracker Task id enthält
        for (DurationTracker durationTracker : durTrackerDelTask){
            assertEquals(durationTracker.getTaskId(), deleteMeTask.get(0).getId());
        }
    }


}
