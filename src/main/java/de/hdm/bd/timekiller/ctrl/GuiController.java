package de.hdm.bd.timekiller.ctrl;


import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import de.hdm.bd.timekiller.model.task.ITaskList;

import de.hdm.bd.timekiller.model.task.Task;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

import de.hdm.bd.timekiller.model.utils.DateUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
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
import javafx.stage.Stage;
import javafx.util.Callback;

public class GuiController {
    @FXML
    private ListView<Task> listView;

    @FXML
    private PieChart pieChart;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    private PieChartHelper helper;

    @FXML
    private AnchorPane aPane;

    private static ITaskList taskList;

    private ObservableList<Task> items;

    @FXML
    private AnchorPane dataInputListView;
    @FXML
    private AnchorPane evaluationGridPane;

    private static final PseudoClass DEFAULT_BACKGROUND_CLASS = PseudoClass.getPseudoClass("default");
    private static final PseudoClass HIGHLIGHTED_BACKGROUND_CLASS = PseudoClass.getPseudoClass("active");

    public void setInput(ITaskList taskList) throws Exception {
        this.taskList = taskList;
        items = FXCollections.observableArrayList (taskList.getAllTasks());
        listView.setItems(items);

        listView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Task>() {
                    @Override
                    public void changed(ObservableValue<? extends Task> observable, Task oldValue,
                                        Task newValue) {

                        if(newValue != null) {
                            if (newValue.isActive()) {
                                System.out.println("newValue stop");
                                newValue.stop();
                            } else {
                                System.out.println("newValue Start: "+ newValue);
                                newValue.start();
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
        @Override
        public void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                setText(null);
                setGraphic(null);

            } else {
                if(!task.isActive()) {
                    pseudoClassStateChanged(HIGHLIGHTED_BACKGROUND_CLASS, false);
                    pseudoClassStateChanged(DEFAULT_BACKGROUND_CLASS, true);
                } else {
                    pseudoClassStateChanged(HIGHLIGHTED_BACKGROUND_CLASS, true);
                    pseudoClassStateChanged(DEFAULT_BACKGROUND_CLASS, false);
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
                editButton.getStyleClass().add("editButton");


                Button deleteButton = new Button("", new ImageView("de/hdm/bd/timekiller/img/icons8-delete-24.png"));
                deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                deleteButton.setMaxSize(30, 30);
                deleteButton.setMinSize(30, 30);
                deleteButton.setOnAction(event -> deleteTask(task));
                deleteButton.getStyleClass().add("deleteButton");

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
                try {
                    int taskId = taskList.insertTask(name);
                    Task task = taskList.getTask(taskId);
                    System.out.println("task in addTask: " + task);

                    // Aktualisieren Sie die ListView
                    refreshTaskListView(task);
                } catch (IllegalNameException | DuplicatedNameException e) {
                    System.out.println(e.getMessage());
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
                taskList.deleteTask(task);
                refreshDeletedTask(task);
            }
        });
    }

    private void editTask(Task task) {
        TextInputDialog dialog = new TextInputDialog(task.toString());
        dialog.setTitle("Edit Task");
        dialog.setHeaderText(null);
        dialog.setContentText("Task name:");

        dialog.showAndWait().ifPresent(name -> {
            try {
                task.setName(name);
                taskList.updateTask(task);
                refreshUpdatedTask(task);
            } catch (IllegalNameException | DuplicatedNameException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void refreshTaskListView(Task task) {;
        System.out.println("task " + task);
        listView.getItems().add(task);
    }

    private void refreshDeletedTask(Task task) {
        listView.getItems().remove(task);
    }

    private void refreshUpdatedTask(Task task) {
        listView.getItems().remove(task);
        listView.getItems().add(task);
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

        //StartDatePicker
        EventHandler<ActionEvent> startEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // get the date picker value
                LocalDate i = startDatePicker.getValue();
                helper.setStartDate(DateUtils.startAsDate(i));
                helper.updatePieChart();
            }
        };

        // show week numbers
        startDatePicker.setShowWeekNumbers(true);
        // when datePicker is pressed
        startDatePicker.setOnAction(startEvent);

        String date = Calendar.getInstance().get(Calendar.YEAR) + "-01-01";
        startDatePicker.setValue(LocalDate.parse(date));


        //EndDatePicker
        EventHandler<ActionEvent> endEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // get the date picker value
                LocalDate i = endDatePicker.getValue();
                helper.setEndDate(DateUtils.endAsDate(i));
                helper.updatePieChart();
            }
        };
        // show week numbers
        endDatePicker.setShowWeekNumbers(true);
        // when datePicker is pressed
        endDatePicker.setOnAction(endEvent);
        LocalDate currentDate = LocalDate.now();
        endDatePicker.setValue(currentDate);
    }

}

