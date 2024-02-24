package dbtest;

import de.hdm.bd.timekiller.ctrl.PieChartHelper;

import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PieChartHelperTest {
    private Connection conn;
    private PieChartHelper pieChartHelper;
    private ITaskList taskList;
    private String databaseName = "test_timekiller.db";

    @BeforeEach
    void setUp() throws Exception {
        //Manuelle Initialisierung des JavaFX Toolkits, da PieChart sonst nicht initierbar ist
        Platform.startup(() -> {
        });

        //DB Connection
        conn = DriverManager.getConnection("jdbc:sqlite:test_timekiller.db");

        taskList = new TaskListImpl(databaseName);
        pieChartHelper = new PieChartHelper(new PieChart(), taskList);
    }

    /**
     * Round Trip
     * Auslesen der Tasks aus der Datenbank für das Kuchendiagramm
     */
    @Test
    void testPieChart() {
        pieChartHelper.updatePieChart();

        ObservableList<PieChart.Data> entries = pieChartHelper.getEntries();
        assertEquals(2, entries.size());
        assertEquals("test1", entries.get(0).getName());
        assertEquals("test2", entries.get(1).getName());
    }
}
