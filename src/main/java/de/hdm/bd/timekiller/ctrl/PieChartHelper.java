package de.hdm.bd.timekiller.ctrl;

import de.hdm.bd.timekiller.model.task.DbManager;
import de.hdm.bd.timekiller.model.task.ITaskList;
import de.hdm.bd.timekiller.model.task.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import de.hdm.bd.timekiller.model.task.DurationTracker;


import java.util.Date;
import java.util.List;

public class PieChartHelper {
    private Date startDate;
    private Date endDate;
    private PieChart pieChart;
    private DbManager dbManager;

    ITaskList tasks;

    public PieChartHelper(PieChart p, ITaskList tasks) throws Exception {
        pieChart = p;
        initPieChart();
        this.tasks = tasks;
        this.dbManager = new DbManager();
    }

    public void initPieChart() {
        pieChart.setTitle("Tasks durations");
        //setting the direction to arrange the data
        pieChart.setClockwise(true);
        //Setting the length of the label line
        pieChart.setLabelLineLength(20);
        //Setting the labels of the pie chart visible
        pieChart.setLabelsVisible(true);
        //Setting the start angle of the pie chart
        pieChart.setStartAngle(180);

    }

    public void updatePieChart() {
        System.out.println("startDate: " + startDate);
        System.out.println("EndDate: " + endDate);
        //pieChart.getData().clear();
        if (startDate != null && endDate != null) {
            pieChart.getData().clear();
            pieChart.setData(getEntries());
        } else {
            System.out.println("Error: startDate or endDate is null.");
        }
    }

    public ObservableList<PieChart.Data> getEntries() {
        System.out.println("getEntries Database");
        List<Task> taskList = tasks.getAllTasks();
        System.out.println("taskList getEntries: "+ taskList);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        System.out.println("ObservableList PiechartData: " + pieChartData);
        for(Task task: taskList) {
            float duration = 0;
            if(startDate != null && endDate != null) {
                duration = task.getOverallDurationForTimePeriod(getStartDate(), getEndDate());
            } else {
                duration = task.getOverallLifetimeDuration();
            }
            if(duration > 0) {
                pieChartData.add(new PieChart.Data(task.getName(), duration));
            }
        }
        return pieChartData;
    }



    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}

