package intTest;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.DbManager;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.Task;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TaskListImplTest {
    private ITaskList list;
    private DbManager dbManager;
    private String databaseName = "test_timekiller.db";

    @BeforeEach
    public void setUp() throws Exception {
        dbManager = new DbManager(databaseName);
        list = new TaskListImpl(databaseName);
    }
    @AfterEach
    public void dropTables() throws SQLException {
        dbManager.dropTables();
    }

    /**
     * Testfälle Insert-Methode:
     */
    //1. Sinnvoller Task: "Turnen" -> Neuer Task in der Taskliste "tasks" hinzugefügt
    @Test
    public void insertTaskTest()
            throws DuplicatedNameException, IllegalNameException {
        int taskId= list.insertTask("Turnen");
        assertEquals("Turnen", list.getTask(taskId).toString());
    }

    //2. Schon vorhandener Task: Studium -> throw DuplicatedNameException
    @Test
    public void insertDuplicatedTaskTest()
            throws IllegalNameException, DuplicatedNameException {
        list.insertTask("Studium");

        Exception exception = assertThrows(DuplicatedNameException.class, () -> {
            list.insertTask("Studium");
        });

    }

    //3. Ungültiger Task: "3" -> throw IllegalName Exception
    @Test
    public void insertInvalidTaskTest() throws IllegalNameException {
        Exception exception = assertThrows(IllegalNameException.class, () -> list.insertTask("3"));
    }

    /**
     * Testfälle Update-Methode:
     */
    @Test
    public void updateTaskValidNameTest() throws DuplicatedNameException, IllegalNameException {
        int taskId = list.insertTask("Turnen");
        Task updatedTask = new Task(taskId, "Joggen");

        list.updateTask(updatedTask);

        assertEquals("Joggen", list.getTask(taskId).toString());
    }
    @Test
    public void updateTaskInvalidNameTest() throws DuplicatedNameException, IllegalNameException {
        // Mock für die Task-Klasse erstellen
        Task mockTask = Mockito.mock(Task.class);

        // Task mit ungültigem Name mocken
        when(mockTask.getId()).thenReturn(1);
        when(mockTask.getName()).thenReturn("4");
        // Die updateTask-Methode aufrufen und Exception abfangen
         assertThrows(IllegalNameException.class, () -> {
            list.updateTask(mockTask);
        });
    }
    @Test
    public void updateTaskDuplicateNameTest() throws DuplicatedNameException, IllegalNameException {
        int taskId1 = list.insertTask("Turnen");
        int taskId2 = list.insertTask("Schwimmen");

        Task updatedTask = new Task(taskId1, "Schwimmen");

     assertThrows(DuplicatedNameException.class, () -> list.updateTask(updatedTask));

    }

    /**
     *Testfälle Remove-Methode:
     */
    @Test
    public void deleteTaskValidTest()
            throws DuplicatedNameException, IllegalNameException {
        int taskId = list.insertTask("Turnen");

        // Sinnvolle Eingabe: Löscht eine existierende Aufgabe
        assertTrue(list.deleteTask(list.getTask(taskId)));
        assertNull(list.getTask(taskId));
    }

    @Test
    public void deleteTaskInvalidTest()
            throws DuplicatedNameException, IllegalNameException {
        int taskId = list.insertTask("Turnen");

        // Ungültige Eingabe: Löscht eine nicht existierende Aufgabe
        Task nonExistingTask = new Task(999, "NonExistingTask");
        assertFalse(list.deleteTask(nonExistingTask));
    }

    /**
     * Testfälle getAllTasks-Methode
     */
    @Test
    public void testGetAllTasks()
            throws IllegalNameException, DuplicatedNameException {
        String[] tasks = {"Arbeit", "Sport", "Studium", "Yoga"};
        for (String task : tasks) {
            list.insertTask(task);
        }

        List<Task> allTasks = list.getAllTasks();

        assertNotNull(allTasks);
        assertEquals(4, allTasks.size());
        assertEquals("Arbeit", allTasks.get(0).getName());
        assertEquals("Sport", allTasks.get(1).getName());
        assertEquals("Studium", allTasks.get(2).getName());
        assertEquals("Yoga", allTasks.get(3).getName());
    }


}
