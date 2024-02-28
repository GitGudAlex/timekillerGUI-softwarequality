package tasktest;

import de.hdm.bd.timekiller.ctrl.PieChartHelper;
import de.hdm.bd.timekiller.model.task.DurationTracker;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.Task;
import de.hdm.bd.timekiller.model.utils.DateUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TddDateRangePicker extends ApplicationTest {
    private ObservableList<Task> items;
    private ITaskList taskList;
    private PieChartHelper pieChartHelper;

    @Override
    public void start(javafx.stage.Stage stage) {
    }

    @BeforeEach
    public void setUp() throws Exception {
        //Erstellen von Mock-Objekte für ITaskList und PieChart
        taskList = Mockito.mock(ITaskList.class);
        items = FXCollections.observableArrayList(taskList.getAllTasks());
        pieChartHelper = new PieChartHelper(new PieChart(), taskList);
    }

    /**
     * Isolierte Tests des PieCharts ohne Datenbankanbindung
     */

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

    //1. Es gibt eine Klasse mit einem Date StartDate und einem Date EndDate
    @Test
    public void testSelectStartDateAndEndDate() throws ParseException {
        // Das Datum als String im gewünschten Format
        String startDateString = "01.03.2022";
        String endDateString = "30.05.2022";

        // Ein SimpleDateFormat-Objekt erstellen, um das Datum zu parsen
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        // Das Datum parsen und in einer Date-Variable speichern
        Date startDate = dateFormat.parse(startDateString);
        Date endDate =dateFormat.parse(endDateString);

        pieChartHelper.setStartDate(startDate);
        pieChartHelper.setEndDate(endDate);

        assertEquals(startDate,pieChartHelper.getStartDate());
        assertEquals(endDate,pieChartHelper.getEndDate());
    }
    //2. In der GUI kann ein spezifisches Start- und Enddatum ausgewählt werden.
    //als GUI-Tests hinterlegt (checkDatePicker())
    //4. Das ausgewählte Start- und Enddatum kann mithilfe der Klasse DateUtils auf das ausgewählte Datum
    //ab 0:00 bzw. bis 23:59 gesetzt und als Typ Date
    //in der Variable StartDate bzw. EndDate der PieChartHelper-Klasse gespeichert werden.

    @Test
    public void testClassDateUtils() {
        // Ein bestimmtes Datum für den Test auswählen
        LocalDate testDate = LocalDate.of(2022, 3, 15);

        // Die Methode aufrufen, um das Datum als Date zu erhalten
        Date startDate = DateUtils.startAsDate(testDate);
        Date endDate = DateUtils.endAsDate(testDate);

        // Erwartetes Datum basierend auf LocalDateTime
        LocalDateTime
                expectedStartDateTime = LocalDateTime.of(2022, 3, 15, 0, 0, 0);
        Date expectedStartDate = Date.from(expectedStartDateTime.toInstant(ZoneOffset.UTC));
        LocalDateTime
                expectedEndDateTime = LocalDateTime.of(2022, 3, 15, 23, 59, 59);
        Date expectedEndDate = Date.from(expectedEndDateTime.toInstant(ZoneOffset.UTC));

        // Überprüfen, ob das tatsächliche Startdatum mit dem erwarteten übereinstimmt
        assertEquals(expectedStartDate, startDate);
        assertEquals(expectedEndDate, endDate);

        //speichern im PieChartHelper-Objekt
        pieChartHelper.setStartDate(startDate);
        pieChartHelper.setEndDate(endDate);

        assertEquals(startDate,pieChartHelper.getStartDate());
        assertEquals(endDate,pieChartHelper.getEndDate());
    }

    //5.Die Methode updatePieChart vom Typ void getriggert, wenn ein Datum im DatePicker angepasst wird.
    //-> Event-Handler nicht gut testbar
    //Wird über GuiIntegrationTests getestet

    //6. Durch das Aufrufen der Methode updatePieChart werden falls StartDate und EndDate
    //ungleich "null" sind, die Einträge im Kuchendiagramm gelöscht und die Methode getEntries() aufgerufen.
    @Test
    void testUpdatePieChartWithDates() throws Exception {
        //Liste mit Dummy Aufgaben erstellen
        List<Task> dummyTasks = new ArrayList<>();
        dummyTasks.add(createTaskWithDuration("Task1", 10.0f));
        dummyTasks.add(createTaskWithDuration("Task2", 15.0f));

        //Mock-Verhalten für getAllTasks setzen
        Mockito.when(taskList.getAllTasks()).thenReturn(dummyTasks);

        //Setzen des Start- und Enddatums im PieChartHelper
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        pieChartHelper.setStartDate(DateUtils.startAsDate(startDate));
        pieChartHelper.setEndDate(DateUtils.endAsDate(endDate));

        //Aufrufen von updatePieChart, da Start- und Enddatum gesetzt sind
        pieChartHelper.updatePieChart();

        //Überprüfen, ob das Kuchendiagramm aktualisiert wurde, indem die Anzahl der Einträge überprüft wird
        assertEquals(2, pieChartHelper.getEntries().size());
    }
    //7. Die Methode getEntries() frägt alle Einträge aus der TaskList ab und errechnet
    //für jeden einzelnen Task die duration anhand der Methode getOverallDurationForTimePeriod()
    //mit dem Start und EndDate als Inputparameter, sofern diese gesetzt sind.
    //Ansonsten wir die Duration mit der Methode getOverallLifetimeDuration() berechnet.
    @Test
    void testGetEntries() throws Exception {
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
}
