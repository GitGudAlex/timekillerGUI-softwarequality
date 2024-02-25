package dbtest;

import de.hdm.bd.timekiller.ctrl.PieChartHelper;

import de.hdm.bd.timekiller.model.task.DbManager;
import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.Task;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PieChartHelperTest {
    private Connection conn;
    private PieChartHelper pieChartHelper;
    private ITaskList taskList;
    private String databaseName = "test_timekiller.db";
    private DbManager dbManager;

    @BeforeEach
    void setUp() throws Exception {
        //Manuelle Initialisierung des JavaFX Toolkits, da PieChart sonst nicht initierbar ist
        Platform.startup(() -> {
        });

        //DB Connection
        conn = DriverManager.getConnection("jdbc:sqlite:test_timekiller.db");

        taskList = new TaskListImpl(databaseName);
        dbManager = new DbManager(databaseName);

        Task task1 = new Task("Studium");
        Task task2 = new Task("Arbeit");

        task1.start();
        task2.start();
        try {
            Thread.sleep(2000);
            task1.stop();
            task2.stop();
        }catch (Exception e){
            e.printStackTrace();
        }

        //Task in DB-Tabelle Task speichern
        taskList.insertTask(task1.getName());
        taskList.insertTask(task2.getName());

        //DurationTrackers in DB-Tabelle DurationTracker speichern
        Collection<DurationTracker> durationTrackerColl = task1.getRecords();
        dbManager.getDurationTrackerDao().create(durationTrackerColl);
        Collection<DurationTracker> durationTrackerColl2 = task2.getRecords();
        dbManager.getDurationTrackerDao().create(durationTrackerColl2);

        pieChartHelper = new PieChartHelper(new PieChart(), taskList);
    }
    @AfterEach
    public void closeConnection() throws SQLException {
        dbManager.dropTables();
    }

    /**
     * Round Trip
     * Auslesen der Tasks aus der Datenbank für das Kuchendiagramm
     */
    @Disabled
    @Test
    void testPieChart() throws ParseException {
        pieChartHelper.updatePieChart();

        ObservableList<PieChart.Data> entries = pieChartHelper.getEntries();
        assertEquals(2, entries.size());
        assertEquals("Studium", entries.get(0).getName());
        assertEquals("Arbeit", entries.get(1).getName());
        //Datum
    }
}
