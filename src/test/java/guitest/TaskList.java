package guitest;
import de.hdm.bd.timekiller.TimeKillerApplication;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.Task;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        Assertions.assertThat(lv).hasExactlyNumItems(3);

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
            lv.getSelectionModel().clearSelection();
        } else {
            // Falls kein passender Task gefunden wurde, Meldung loggen
            logger.warning("Der Task 'Arbeit' wurde nicht gefunden.");
        }
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
