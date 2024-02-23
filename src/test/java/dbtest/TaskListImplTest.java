package dbtest;

import de.hdm.bd.timekiller.model.task.DbManager;
import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.Task;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskListImplTest {
    private DbManager dbManager;
    private Connection conn;
    private TaskListImpl taskList;

    @BeforeEach
    public void setUp() throws Exception{
        //DB Connection
        conn = DriverManager.getConnection("jdbc:sqlite:test_timekiller.db");
        dbManager = new DbManager("test_timekiller.db");
        taskList = new TaskListImpl();
    }

    @AfterEach
    public void closeDatabase() throws Exception{
        System.out.println("closeDatabase Tasklist outside if statement");
        dbManager.dropTables();
        if (conn != null){
            conn.close();
            System.out.println("conn closed Tasklistimpl");
        }
    }

    @Test
    public void testSaveRecordOnTaskStop() throws Exception {
        System.out.println("testSaveRecordOnTaskStop");

        // Create and start a new task
        Task testTask = new Task("testTask");
        testTask.start();
        Thread.sleep(2000);
        // Stop the task
        testTask.stop();

        dbManager.getTaskDao().create(testTask);

        // Check if the task was saved in the database
        List<Task> testList = dbManager.getTaskDao().queryForEq("name", "testTask");
        assertNotNull(testList);
        assertEquals(1, testList.size(), "Expected one task in the database");

        int taskId = testList.get(0).getId();

        List<DurationTracker> durationTrackers = dbManager.getDurationTrackerDao().queryForEq(DurationTracker.TASK_FIELD_NAME, taskId);
        System.out.println("getDurTrackerTest: " +dbManager.getDurationTrackerDao().queryForEq(DurationTracker.TASK_FIELD_NAME, taskId));
        assertNotNull(durationTrackers);
        assertEquals(1, durationTrackers.size(), "Expected one duration tracker in the database");

        System.out.println("Test completed successfully");

    }
}
