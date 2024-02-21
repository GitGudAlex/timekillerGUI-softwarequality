package de.hdm.bd.timekiller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import de.hdm.bd.timekiller.ctrl.GuiController;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.DbManager;
import de.hdm.bd.timekiller.model.task.Task;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import de.hdm.bd.timekiller.model.task.ITaskList;
import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TimeKillerApplication extends Application {
    private Scene scene;

    @Override

    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader =
            new FXMLLoader(TimeKillerApplication.class.getResource("gui.fxml"));
        scene = new Scene(fxmlLoader.load(), 640, 480);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        stage.setTitle("TimeKiller");
        stage.setScene(scene);
        stage.show();

        // TODO: statt der MinimalTaskList sollte eine anwendungsspezifische Liste
        // mit vollständig implementierten Task-Objekten benutzt werden.
        ITaskList taskList = new TaskListImpl();

        ((GuiController) fxmlLoader.getController()).setInput(taskList);
        ((GuiController) fxmlLoader.getController()).switchToDataInput();

    }

    public static void main(String[] args) {
        launch();
    }

    public Scene getScene() {
        return scene;
    }
}