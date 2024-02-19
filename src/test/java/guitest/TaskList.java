package guitest;
import de.hdm.bd.timekiller.TimeKillerApplication;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.Task;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;
import javafx.scene.chart.PieChart;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)

public class TaskList {

    private static final TimeKillerApplication timeKiller = new TimeKillerApplication();
    private static final Logger logger = Logger.getLogger(TaskList.class.getName());

    @Start
    public void start(Stage stage) throws IOException, IllegalNameException {
        timeKiller.start(stage);
        stage.setScene(timeKiller.getScene());
        stage.show();
        stage.toFront();
    }

    @Test
    void initialStateOfTaskList(FxRobot robot) {
        ListView lv = robot.lookup("#listView").queryAs(ListView.class);
        Assertions.assertThat(lv).hasExactlyNumItems(4);

    }

    @Test
    void userClickReactionsTaskList(FxRobot robot){
        ListView lv = robot.lookup("#listView").queryAs(ListView.class);
        List<Task> tasks = lv.getItems();
        Task task = getTaskForName(tasks, "Arbeit");


        // Mausklick und isActive() nur überprüfen, wenn der Task gefunden wird
        if (task != null) {
            assertFalse(task.isActive());
            lv.getSelectionModel().select(task);
            assertTrue(task.isActive());

            WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);

            lv.getSelectionModel().clearSelection();
            lv.getSelectionModel().select(task);
            assertFalse(task.isActive());

        } else {
            // Falls kein passender Task gefunden wurde, Meldung loggen
            logger.warning("Der Task 'Arbeit' wurde nicht gefunden.");
        }
    }

    @Test
    public void checkEvalView(FxRobot robot) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                ListView lv = robot.lookup("#listView").queryAs(ListView.class);
                //Tasks starten und nach bestimmter Zeit beenden
                startTasks(lv);
            }
        });
        //Wechsel zur Evaluationssicht
        Button button = robot.lookup("#evalButton").queryAs(Button.class);
        robot.clickOn(button);

        //Prüfen der Anzahl an Tasks und der angezeigten Dauer
        PieChart pChart = robot.lookup("#pieChart").queryAs(PieChart.class);
        List<PieChart.Data> chartData = pChart.getData();
        assertEquals(2, chartData.size());
        PieChart.Data entry1 = chartData.get(0);
        assertEquals(3000, entry1.getPieValue(),100);
        PieChart.Data entry2 = chartData.get(1);
        assertEquals(4000, entry2.getPieValue(),100);

        //Prüfen der Tasknamen
        assertEquals("Arbeit", entry1.getName());
        assertEquals("Studium", entry2.getName());

    }

    @Test
    public void checkDatePicker(FxRobot robot) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ListView lv = robot.lookup("#listView").queryAs(ListView.class);
                //Tasks starten und nach bestimmter Zeit beenden
                startTasks(lv);
            }
        });
        //Wechsel zur Evaluationssicht
        Button button = robot.lookup("#evalButton").queryAs(Button.class);
        robot.clickOn(button);

        // DatePicker finden
        DatePicker dpStart = robot.lookup("#startDatePicker").queryAs(DatePicker.class);
        DatePicker dpEnd = robot.lookup("#endDatePicker").queryAs(DatePicker.class);
        String date = Calendar.getInstance().get(Calendar.YEAR) + "-12-31";

        Platform.runLater(new Runnable() {
        public void run() {
            //Datum setzten
            dpStart.setValue(LocalDate.parse(date));
            dpEnd.setValue(LocalDate.parse(date));
        }});
        //angezeigte Werte überprüfen
        WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);
        assertEquals(LocalDate.parse(date),dpStart.getValue());
        assertEquals(LocalDate.parse(date),dpEnd.getValue());
    }

    @Test
    public void addTask(FxRobot robot) {
        //click auf "+"-Button
        Button addButton = robot.lookup("#addButton").queryAs(Button.class);
        robot.clickOn(addButton);
        //Texteingabe in Dialogfenster
        robot.interact(() -> {
            robot.lookup(".text-field").queryTextInputControl().setText("TestInput");
        });
        //Bestätigen
        robot.clickOn("OK");
        //Prüfen, ob ein neuer Task erstellt wurde
        ListView lv = robot.lookup("#listView").queryAs(ListView.class);
        List<Task> tasks = lv.getItems();
        assertNotNull(getTaskForName(tasks, "TestInput"));
    }

    @Test
    public void deleteTask(FxRobot robot) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ListView lv = robot.lookup("#listView").queryAs(ListView.class);
                //Tasks starten und nach bestimmter Zeit beenden
                startTasks(lv);
                List<Task> tasks = lv.getItems();
                Task yogaTask = getTaskForName(tasks, "Yoga");
                lv.getSelectionModel().select(yogaTask);
            }
        });
        //nacheinander prüfen, ob eine noch nicht benutze, ein bereits benutze und eine noch aktive Task erfolgreich gelöscht werden kann
        for (int i = 0; i <= 2; i++) {
            System.out.println("Zahl: " + i);
            Set<Node> deleteButtons = robot.lookup(".deleteButton").queryAll();
            assertEquals(4-i, deleteButtons.size());
            Node deleteButton = deleteButtons.toArray(new Node[0])[1];
            robot.clickOn(deleteButton);
            //Bestätigen
            robot.clickOn("OK");
        }

    }



    private void startTasks(ListView lv){

        List<Task> tasks = lv.getItems();
        Task task1 = getTaskForName(tasks, "Arbeit");
        Task task2 = getTaskForName(tasks, "Studium");

        lv.getSelectionModel().select(task1);
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        lv.getSelectionModel().clearSelection();
        lv.getSelectionModel().select(task1);

        lv.getSelectionModel().select(task2);
        WaitForAsyncUtils.sleep(4, TimeUnit.SECONDS);
        lv.getSelectionModel().clearSelection();
        lv.getSelectionModel().select(task2);
    }


    private Task getTaskForName(List<Task> tasks, String name) {
        for (Task task : tasks) {
            if (task.getName().equals(name)) {
                return task;
            }
        }
        return null;
    }





}
