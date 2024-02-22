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
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class TimeKillerApplication extends Application {
    private Scene scene;
    private boolean createNewDatabase;

    @Override

    public void start(Stage stage) throws Exception {
        // Zeigen Sie einen Dialog an, um den Benutzer zu fragen, ob eine neue Datenbank erstellt werden soll oder nicht
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Neue Datenbank erstellen?");
        alert.setHeaderText(null);
        alert.setContentText("Möchten Sie eine neue Datenbank erstellen?");

        ButtonType yesButton = new ButtonType("Ja");
        ButtonType noButton = new ButtonType("Nein");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton){
            createNewDatabase = true;
            DbManager dbManager = new DbManager();
            dbManager.dropTables();
        } else {
            createNewDatabase = false;
        }

        FXMLLoader fxmlLoader =
            new FXMLLoader(TimeKillerApplication.class.getResource("gui.fxml"));
        scene = new Scene(fxmlLoader.load(), 640, 480);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        stage.setTitle("TimeKiller");
        stage.setScene(scene);
        stage.show();

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