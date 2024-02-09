package tasktest;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.Task;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListImplTest {

    //Testfälle Insert-Methode:
    //1. Sinnvoller Task: "Turnen" -> Neuer Task in der Taskliste "tasks" hinzugefügt
    //2. Schon vorhandener Task: Studium -> throw DuplicatedNameException
    //3. Ungültiger Task: "3" -> throw IllegalName Exception
    @Test
    public void insertTaskTest()
            throws DuplicatedNameException, IllegalNameException {
        TaskListImpl list = new TaskListImpl();
        int taskId= list.insertTask("Turnen");
        assertEquals("Turnen", list.getTask(taskId).toString());
    }

    @Test
    public void insertInvalidTaskTest(){
        TaskListImpl list = new TaskListImpl();

        Exception exception = assertThrows(IllegalNameException.class, () -> {
            list.insertTask("3");
        });
        String expectedMessage = "Invalid task name.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void insertDuplicatedTaskTest(){
        TaskListImpl list = new TaskListImpl();

        Exception exception = assertThrows(DuplicatedNameException.class, () -> {
            list.insertTask("Studium");
        });
        String expectedMessage = "Whoops thats a duplicate!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    //Testfälle Update-Methode:

    @Test
    public void updateTaskValidNameTest() throws DuplicatedNameException, IllegalNameException {
        TaskListImpl list = new TaskListImpl();
        int taskId = list.insertTask("Turnen");
        Task updatedTask = new Task(taskId, "Joggen");

        list.updateTask(updatedTask);

        assertEquals("Joggen", list.getTask(taskId).toString());
    }

    @Test
    public void updateTaskInvalidNameTest() throws DuplicatedNameException, IllegalNameException {
        TaskListImpl list = new TaskListImpl();
        int taskId = list.insertTask("Turnen");
        Task updatedTask = new Task(taskId, "3");

        Exception exception = assertThrows(IllegalNameException.class, () -> {
            list.updateTask(updatedTask);
        });

        String expectedMessage = "Invalid Name.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updateTaskDuplicateNameTest() throws DuplicatedNameException, IllegalNameException {
        TaskListImpl list = new TaskListImpl();
        int taskId1 = list.insertTask("Turnen");
        int taskId2 = list.insertTask("Schwimmen");

        Task updatedTask = new Task(taskId1, "Schwimmen");

        Exception exception = assertThrows(DuplicatedNameException.class, () -> {
            list.updateTask(updatedTask);
        });

        String expectedMessage = "Whoopsie gibts schon! :)";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }



    //Testfälle Remove-Methode:

    @Test
    public void deleteTaskValidTest()
            throws DuplicatedNameException, IllegalNameException {
        TaskListImpl list = new TaskListImpl();
        int taskId = list.insertTask("Turnen");

        // Sinnvolle Eingabe: Löscht eine existierende Aufgabe
        assertTrue(list.deleteTask(list.getTask(taskId)));
        assertNull(list.getTask(taskId));
    }

    @Test
    public void deleteTaskInvalidTest()
            throws DuplicatedNameException, IllegalNameException {
        TaskListImpl list = new TaskListImpl();
        int taskId = list.insertTask("Turnen");

        // Ungültige Eingabe: Löscht eine nicht existierende Aufgabe
        Task nonExistingTask = new Task(999, "NonExistingTask");
        assertFalse(list.deleteTask(nonExistingTask));
    }


    //Testfälle getAllTasks-Methode

    @Test
    public void testGetAllTasks() {
        // Arrange
        TaskListImpl taskList = new TaskListImpl();

        // Act
        List<Task> allTasks = taskList.getAllTasks();

        // Assert
        assertNotNull(allTasks);
        assertEquals(3, allTasks.size());
        assertEquals("Arbeit", allTasks.get(0).getName());
        assertEquals("Sport", allTasks.get(1).getName());
        assertEquals("Studium", allTasks.get(2).getName());
    }


}
