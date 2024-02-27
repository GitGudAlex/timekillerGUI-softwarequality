package de.hdm.bd.timekiller;

import de.hdm.bd.timekiller.ctrl.GuiController;
import de.hdm.bd.timekiller.model.task.DbManager;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import de.hdm.bd.timekiller.model.task.ITaskList;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class TimeKillerApplication extends Application {
    private Scene scene;
    private boolean showAlert = true;
    private String databaseName = "test_timekiller.db";

    @Override

    public void start(Stage stage) throws Exception {
        if (showAlert){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Neue Datenbank erstellen?");
            alert.setHeaderText(null);
            alert.setContentText("Möchten Sie eine neue Datenbank erstellen?");

            ButtonType yesButton = new ButtonType("Ja");
            ButtonType noButton = new ButtonType("Nein");
            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();
            boolean createNewDatabase;
            if (result.isPresent() && result.get() == yesButton){
                createNewDatabase = true;
                DbManager dbManager = new DbManager(databaseName);
                dbManager.dropTables();
                showAlert = false;
            } else {
                createNewDatabase = false;
                showAlert = false;
            }
        }

        FXMLLoader fxmlLoader =
            new FXMLLoader(TimeKillerApplication.class.getResource("gui.fxml"));
        scene = new Scene(fxmlLoader.load(), 640, 480);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        stage.setTitle("TimeKiller");
        stage.setScene(scene);
        stage.show();

        ITaskList taskList = new TaskListImpl(databaseName);

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