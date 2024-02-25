package tasktest;

import de.hdm.bd.timekiller.ctrl.PieChartHelper;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MockedPieChart extends ApplicationTest {
    private PieChart pieChart;
    private ObservableList<Task> items;
    private PieChartHelper helper;

    @Override
    public void start(javafx.stage.Stage stage) {
    }

    /**
     * Isolierte Tests des PieCharts ohne Datenbankanbindung
     */
    @Test
    void testGetEntries() throws Exception {
        //Erstellen von Mock-Objekte für ITaskList und PieChart
        ITaskList taskList = Mockito.mock(ITaskList.class);
        items = FXCollections.observableArrayList(taskList.getAllTasks());

        //Erzeugen einer Instanz von PieChartHelper mit den Mock-Objekten
        PieChartHelper
                pieChartHelper = new PieChartHelper(new PieChart(), taskList);

        //Erstellen einer Liste von Dummy-Aufgaben für den Mock-TaskList
        List<Task> dummyTasks = new ArrayList<>();
        dummyTasks.add(createTaskWithDuration("Task1", 10.0f));
        dummyTasks.add(createTaskWithDuration("Task2", 15.0f));

        //Mock-Verhalten für getAllTasks setzen
        Mockito.when(taskList.getAllTasks()).thenReturn(dummyTasks);

        //getEntries-Methode aufrufen
        ObservableList<PieChart.Data> entries = pieChartHelper.getEntries();

        //Überprüfen, ob die zurückgegebene Liste nicht null ist
        assertNotNull(entries);

        //Überprüfen ob die Anzahl der Einträge in der Liste (basierend auf der Dummy-Aufgabenliste)
        assertEquals(dummyTasks.size(), entries.size());

        //Überprüfen, ob die Einträge in der Liste die erwarteten Namen und Dauern haben
        assertEquals("Task1", entries.get(0).getName());
        assertEquals(10000.0, entries.get(0).getPieValue());

        assertEquals("Task2", entries.get(1).getName());
        assertEquals(15000.0, entries.get(1).getPieValue());
    }

    private Task createTaskWithDuration(String name, float duration)
            throws Exception {
        Task task = new Task();
        task.setName(name);
        task.addRecordToTask(createDurationTrackerWithDuration(duration));
        return task;
    }

    private DurationTracker createDurationTrackerWithDuration(float duration) {
        DurationTracker durationTracker = new DurationTracker();
        durationTracker.setStart(System.currentTimeMillis());
        durationTracker.setEnd(durationTracker.getStartTime() + (long) (duration * 1000));
        return durationTracker;
    }

}
