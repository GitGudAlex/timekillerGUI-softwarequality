package tasktest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.Task;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ITaskListTest {
    private ITaskList taskList;

    @Before
    public void setUp() {
        // Mock-Objekt für ITaskList erstellen
        taskList = mock(ITaskList.class);
    }

    @Test
    public void testGetAllTasksReturnsEmptyList() {
        when(taskList.getAllTasks()).thenReturn(Arrays.asList());

        List<Task> tasks = taskList.getAllTasks();

        // Überprüfen, ob die zurückgegebene Liste leer ist
        assertEquals(0, tasks.size());

    }

    @Test
    public void testGetAllTasksReturnsNonEmptyList() throws DuplicatedNameException, IllegalNameException {
        Task task = new Task(1, "NewTask");

        // Mocken des erwarteten Verhaltens der getAllTasks-Methode für eine nicht leere Liste
        when(taskList.getAllTasks()).thenReturn(Arrays.asList(task));

        List<Task> tasks = taskList.getAllTasks();

        //Überprüfen, ob die zurückgegebene Aufgabe den erwarteten Namen und die erwartete ID hat
        assertEquals(1, tasks.get(0).getId());
        assertEquals("newTask", tasks.get(0).getName());


    }

    @Test
    public void testGetTaskReturnsNotNull() {
        when(taskList.getTask(anyInt())).thenReturn(new Task(2, "newTask2"));

        Task task = taskList.getTask(1);

        // Überprüfen Sie, ob die zurückgegebene Task-Instanz nicht null ist
        assertNotNull(task);

    }

    @Test
    public void testGetTaskWithNonExistingIdReturnsNull() {
        when(taskList.getTask(anyInt())).thenReturn(null);

        Task task = taskList.getTask(-1);

        // Überprüfen, ob die zurückgegebene Task-Instanz null ist
        assertEquals(null, task);
    }

    @Test
    public void testInsertTaskReturnsValidId() throws DuplicatedNameException, IllegalNameException {
        when(taskList.insertTask(anyString())).thenReturn(1); // Dummy-ID

        //Aufgabe mit einem gültigen Namen hinzufügen
        int taskId = taskList.insertTask("TaskName");

        // Überprüfen, ob die zurückgegebene ID größer als 0 ist (gültige ID)
        assertTrue(taskId > 0);

    }

    @Test
    public void testInsertTaskWithDuplicateNameThrowsDuplicatedNameException() throws DuplicatedNameException, IllegalNameException {
        // Mock für die taskList konfigurieren
        when(taskList.insertTask("TaskName2")).thenThrow(new DuplicatedNameException("Task with name already exists"));

        // Überprüfen, ob das Einfügen einer Aufgabe mit einem bereits vorhandenen Namen die erwartete Ausnahme auslöst
        DuplicatedNameException exception = assertThrows(DuplicatedNameException.class, () -> {
            taskList.insertTask("TaskName2");
        });

        // Überprüfen, ob die erwartete Fehlermeldung übereinstimmt
        assertEquals("Task with name already exists", exception.getMessage());

    }

    @Test
    public void testUpdateTaskWithValidTask() throws DuplicatedNameException, IllegalNameException {
        doNothing().when(taskList).updateTask(any(Task.class));

        // Aufgabe hinzufügen, die aktualisiert werden soll
        Task existingTask = new Task(1, "TaskName");

        //  Aufgabe aktualisieren
        taskList.updateTask(existingTask);

    }

    @Test
    public void testDeleteTaskWithValidTask() throws DuplicatedNameException, IllegalNameException {
        when(taskList.deleteTask(any(Task.class))).thenReturn(true);

        // Aufgabe hinzufügen, die gelöscht werden soll
        Task existingTask = new Task(1,"TaskName");

        // Aufgabe löschen
        boolean result = taskList.deleteTask(existingTask);

        // Überprüfen,ob die Aufgabe erfolgreich gelöscht wurde
        assertNull(taskList.getTask(1));

    }

    @Test
    public void testDeleteTaskWithNonExistingTask() throws DuplicatedNameException, IllegalNameException {
        // Mocken Sie das erwartete Verhalten der deleteTask-Methode
        when(taskList.deleteTask(any(Task.class))).thenReturn(false);

        //Task erstellen, die nicht in der Liste vorhanden ist
        Task nonExistingTask = new Task(10,"NonExistingTask");

        // Löschen der nicht vorhandene Task
        boolean result = taskList.deleteTask(nonExistingTask);

        // Überprüfen, ob die Aufgabe nicht gelöscht wurde
        assertFalse(result);

    }

    @Test
    public void testClearTaskList() throws DuplicatedNameException, IllegalNameException {
       // when(taskList.getAllTasks()).thenReturn(???);

        // Überprüfen, ob die Anzahl der Aufgaben vor dem Löschen größer als 0 ist
        int initialSize = taskList.getAllTasks().size();

        // Löschen aller Aufgaben
        taskList.clearTaskList();

        // Überprüfen, ob die Aufgabenliste nach dem Löschen leer ist
        assertEquals(0, taskList.getAllTasks().size());

    }

}
