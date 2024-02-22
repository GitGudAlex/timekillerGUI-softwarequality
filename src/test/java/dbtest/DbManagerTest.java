package dbtest;

import de.hdm.bd.timekiller.model.task.DbManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbManagerTest {
    private DbManager dbManager;
    private Connection conn;

    @BeforeEach
    public void setUp() throws Exception{
        //DB Connection
        conn = DriverManager.getConnection("jdbc:sqlite:test_timekiller.db");
        dbManager = new DbManager("test_timekiller.db");

        //Tabellen zum Testen
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS task (id INTEGER PRIMARY KEY, name TEXT)");
        stmt.executeUpdate("INSERT INTO task (name) VALUES ('Task 1')");
        stmt.executeUpdate("INSERT INTO task (name) VALUES ('Task 2')");
        stmt.close();
    }
    @AfterEach
    public void closeDatabase() throws Exception{
        if (conn != null){
            conn.close();
        }
    }

    @Test
    public void testGetAllTasks() throws SQLException {
        System.out.println("testgetAllTasks: "+ dbManager.getTaskDao().queryForAll());
        assertEquals(2, dbManager.getTaskDao().queryForAll().size());
    }
}
