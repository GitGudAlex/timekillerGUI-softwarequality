package de.hdm.bd.timekiller.ctrl;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.Task;
import java.util.Comparator;
import java.util.Optional;
import de.hdm.bd.timekiller.model.task.TaskListImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class GuiController {
    @FXML
    private ListView<Task> listView;

    @FXML
    private PieChart pieChart;

    private PieChartHelper helper;

    @FXML
    private AnchorPane aPane;

    private static ITaskList taskList = new TaskListImpl();

    private ObservableList<Task> items;

    @FXML
    private AnchorPane dataInputListView;
    @FXML
    private AnchorPane evaluationGridPane;

    private static final PseudoClass DEFAULT_BACKGROUND_CLASS = PseudoClass.getPseudoClass("default");
    private static final PseudoClass HIGHLIGHTED_BACKGROUND_CLASS = PseudoClass.getPseudoClass("active");

    public void setInput(ITaskList taskList) {
        this.taskList = taskList;
        items = FXCollections.observableArrayList (taskList.getAllTasks());
        listView.setItems(items);

        listView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Task>() {
                    @Override
                    public void changed(ObservableValue<? extends Task> observable, Task oldValue,
                                        Task newValue) {
                        // TODO: Reaktion auf den Klick auf eine Task muss implementiert werden:
                        // Wenn die Task gerade aktiv ist, wird sie beendet
                        // Ansonsten wird sie aktiviert
                        // Die Task steht in der Refernez newValue
                        if (newValue != null) {
                            if (newValue.isActive()) {
                                newValue.stop();
                                System.out.println(newValue.getOverallDuration());
                            } else {
                                newValue.start();
                                System.out.println("task aktiviert: "+ newValue);
                            }
                        }
                    }
                });

        listView.setCellFactory(new TaskListCellFactory());
        helper = new PieChartHelper(pieChart, taskList);
    };



    public class TaskListCellFactory implements Callback<ListView<Task>, ListCell<Task>> {

        @Override
        public ListCell<Task> call(ListView<Task> param) {
            ListCell<Task> cell = new TaskListCell();
            cell.setOnMousePressed((MouseEvent event) -> {
                param.getSelectionModel().clearSelection();
            });
            return cell;
        }
    }

    public final class TaskListCell extends ListCell<Task> {
        boolean active = true;
        @Override
        public void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                setText(null);
                setGraphic(null);
                pseudoClassStateChanged(HIGHLIGHTED_BACKGROUND_CLASS, false);
                pseudoClassStateChanged(DEFAULT_BACKGROUND_CLASS, false);
            } else {
                //TODO: hier sollte die Hintergrundfarbe passend zum aktuellen Task-Zustand gesetzt
                // werden: Wenn die DEFAULT_BACKGROUND_CLASS true ist, werden Standard-Farben benutzt
                // Wenn die HIGHLIGHTED_BACKGROUND_CLASS true ist, wird die in CSS definierte
                // Highlighting-Farbe benutzt.
                if(!task.isActive()) {
                    pseudoClassStateChanged(HIGHLIGHTED_BACKGROUND_CLASS, false);
                    pseudoClassStateChanged(DEFAULT_BACKGROUND_CLASS, true);
                    active = !active;
                } else {
                    pseudoClassStateChanged(HIGHLIGHTED_BACKGROUND_CLASS, true);
                    pseudoClassStateChanged(DEFAULT_BACKGROUND_CLASS, false);
                    active = !active;
                }
                Label descriptionLabel = new Label(task.toString());
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add(descriptionLabel);
                descriptionLabel.setFont(new Font(14));

                Button editButton = new Button("", new ImageView("de/hdm/bd/timekiller/img/icons8-edit-24.png"));
                editButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                editButton.setMaxSize(30, 30);
                editButton.setMinSize(30, 30);
                editButton.setOnAction(event -> editTask(task));

                Button deleteButton = new Button("", new ImageView("de/hdm/bd/timekiller/img/icons8-delete-24.png"));
                deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                deleteButton.setMaxSize(30, 30);
                deleteButton.setMinSize(30, 30);
                deleteButton.setOnAction(event -> deleteTask(task));

                Region region = new Region();
                HBox.setHgrow(region, Priority.ALWAYS);
                setGraphic(new HBox(hBox, region, editButton, deleteButton));
            }
        }
    }

    //Button-Methoden
    @FXML
    private void addTask() {
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("New task");
        dialog.setHeaderText("Enter the task's name:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            if (!name.equals("")) {
                Task task = null;
                try {
                    int taskId = taskList.insertTask(name);
                    Task newTask = taskList.getTask(taskId);
                    refreshTaskListView(newTask);
                } catch (DuplicatedNameException e) {
                    showAlert("Duplicated Name", "A task with this name already exists.");
                } catch (IllegalNameException e) {
                    showAlert("Invalid Name", "Please enter a valid task name.");
                }
            }
        });
    }

    private void deleteTask(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Task");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this task?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean deleted = taskList.deleteTask(task);
                if (deleted) {
                    refreshDeletedTask(task);
                    System.out.println("Delete worked" + taskList.getAllTasks());
                } else {
                    showAlert("Delete Failed", "Failed to delet task.");
                }
            }
        });
    }

    private void editTask(Task task) {
        TextInputDialog dialog = new TextInputDialog(task.toString());
        dialog.setTitle("Edit Task");
        dialog.setHeaderText(null);
        dialog.setContentText("Task name:");

        dialog.showAndWait().ifPresent(name -> {
            if(!name.equals(task.getName())){
                try{
                    task.setName(name);
                    taskList.updateTask(task);
                    refreshUpdatedTask(task);
                }catch (DuplicatedNameException e){
                    showAlert("Duplicated Name", "Whoops gibts schon");
                }catch (IllegalNameException e){
                    showAlert("Invalid Name", "Enter a valid name!");
                }
            }
        });
    }

    private void refreshTaskListView(Task task) {
        listView.getItems().add(task);
        listView.getItems().sort(Comparator.comparing(Task::getName));
        //items.sort(Comparator.comparing(Task::getName));
        System.out.println("refreshTaskListView");
        System.out.println(listView.getItems());
        // TODO: optional: hier kann die dargestellte Liste (listView) zusätzlich sortiert werden
    }

    private void refreshDeletedTask(Task task) {
        listView.getItems().remove(task);
    }

    private void refreshUpdatedTask(Task task) {
        listView.getItems().remove(task);
        listView.getItems().add(task);
        // TODO: optional: hier kann die dargestellte Liste (listView) zusätzlich sortiert werden
    }


    @FXML
    public void switchToDataInput() {
        aPane.getChildren().clear();
        aPane.getChildren().add(dataInputListView);
    }

    @FXML
    public void switchToEvaluation() {
        aPane.getChildren().clear();
        aPane.getChildren().add(evaluationGridPane);
        helper.updatePieChart();
    }

    //Anzeigen von Fehlermeldungen. Muss man nicht machen, aber vllt gibts Pluspunkte :D
    private void showAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

