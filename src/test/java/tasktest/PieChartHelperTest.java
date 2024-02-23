package tasktest;
import de.hdm.bd.timekiller.ctrl.PieChartHelper;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PieChartHelperTest extends ApplicationTest {

    private PieChartHelper pieChartHelper;
    private ITaskList mockTaskList;

    @BeforeEach
    public void setUp() {
        // Mock für ITaskList erstellen
        mockTaskList = mock(ITaskList.class);

        // PieChartHelper mit einem PieChart und dem Mock von ITaskList erstellen
        PieChart pieChart = new PieChart();
        pieChartHelper = new PieChartHelper(pieChart, mockTaskList);
    }

    @Test
    public void testGetEntries() throws IllegalNameException {
        // Mock-Verhalten für getAllTasks() festlegen
        Task activeTask = null;
        try {
            activeTask = new Task(1, "ActiveTask");
        } catch (IllegalNameException e) {
            throw new RuntimeException(e);
        }
        activeTask.start();
        when(mockTaskList.getAllTasks()).thenReturn(FXCollections.observableArrayList(activeTask));

        // getEntries() aufrufen und überprüfen, ob die erwarteten Daten zurückgegeben werden
        ObservableList<PieChart.Data> entries = pieChartHelper.getEntries();
        assertFalse(entries.isEmpty());
        assertEquals("ActiveTask", entries.get(0).getName());
    }

    // Weitere Methoden nicht gut testbar, die Funktion wird aber in den GUI-Tests geprüft
}