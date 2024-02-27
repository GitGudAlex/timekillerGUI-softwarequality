package dbtest;

import de.hdm.bd.timekiller.model.task.DbManager;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BackDoorManipulation {
    private final String databaseName = "test_timekiller.db";
    String createTableStatement =
            "CREATE TABLE IF NOT EXISTS durationtracker (" + "id INTEGER, " +
                    "start DATETIME, " + "end DATETIME, " +
                    "duration INTEGER, " + "task INTEGER, " +
                    "FOREIGN KEY (task) REFERENCES task(id)" + ")";
    private DbManager dbManager;
    private Connection conn;
    private ITaskList taskList;

    @BeforeEach
    public void setUp() throws Exception {
        //DB Connection
        conn = DriverManager.getConnection("jdbc:sqlite:test_timekiller.db");
        dbManager = new DbManager(databaseName);
        taskList = new TaskListImpl(databaseName);

        //Tabellen zum Testen
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS task (id INTEGER PRIMARY KEY, name TEXT)");
        stmt.executeUpdate(createTableStatement);
        stmt.executeUpdate("INSERT INTO task (name) VALUES ('Task 1')");
        stmt.executeUpdate("INSERT INTO task (name) VALUES ('Task 2')");
        stmt.close();
    }

    @AfterEach
    public void closeDatabase() throws Exception {
        System.out.println("closeDatabase outside if Statement");
        dbManager.dropTables();
        if (conn != null) {
            conn.close();
            System.out.println("Conn closed");
        }
    }

    /**
     * Back Door Manipulation
     * Auslesen der Tasks aus der Datenbank für die Task-Liste
     */
    @Test
    public void testGetAllTasks() throws SQLException {
        assertEquals(dbManager.getTaskDao().queryForAll().size(),
                taskList.getAllTasks().size());
    }

    /**
     * Back Door Manipulation
     * Auslesen des Tasks und mit erwarteten ID vergleichen
     */
    @Test
    public void getTaskIdByName() {
        int taskId = 0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM task WHERE name = 'Task 1'");
            taskId = rs.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, taskId);
    }

    /**
     * Back Door Manipulation
     * Task und Durationtracker einfügen
     */
    @Test
    public void testInsertDurationTracker() throws SQLException {
        // Eintrag für Task in die Datenbank einfügen
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(
                "INSERT INTO task (id, name) VALUES (3, 'TaskDurationTracker')");

        // Eintrag für DurationTracker in die Datenbank einfügen
        stmt.executeUpdate(
                "INSERT INTO durationtracker (id, start, end, duration, task)" +
                        "VALUES (3, '2024-02-25 10:00:00', '2024-02-25 10:05:00', 300000, 3)");

        // Überprüfen, ob der Eintrag erfolgreich eingefügt wurde
        ResultSet rsTracker =
                stmt.executeQuery("SELECT * FROM durationtracker WHERE id = 3");
        System.out.println("rsTracker: " + rsTracker.getInt("task"));
        assertEquals(3, rsTracker.getInt("task"));
        ResultSet rsTask = stmt.executeQuery("SELECT * FROM task where id = 3");
        assertTrue(rsTracker.next());
        assertEquals(3, rsTask.getInt("id"));

        // Aufräumen
        rsTracker.close();
        stmt.close();
    }

    /**
     * Back Door Manipulation
     * Dauer des DurationTrackers testen
     */
    @Test
    public void testDuration() throws SQLException {
        int sleep = 2000;
        long startTime = System.currentTimeMillis();

        //Einsetzen eines Datensatzes in die Datenbank mit Start- und Endzeit
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(
                "INSERT INTO durationtracker (id, start, end, task) VALUES (5, " +
                        startTime + ", " + (startTime + sleep) + "," + "5)");

        //Überprüfen der Dauer des DurationTrackers in der Datenbank
        long duration = 0;
        ResultSet rs =
                stmt.executeQuery("SELECT * FROM durationtracker WHERE id = 5");
        if (rs.next()) {
            long start = rs.getLong(
                    "start"); //Abrufen der Startzeit aus der Datenbank
            long stop =
                    rs.getLong("end"); //Abrufen der Endzeit aus der Datenbank
            duration = stop - start; //Berechnen der Dauer
        }
        //Überprüfen, ob die Dauer des DurationTrackers mindestens so lang wie die Schlafdauer ist
        assertTrue(duration >= sleep);
    }
}
