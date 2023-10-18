package de.hdm.bd.timekiller.ctrl;

import de.hdm.bd.timekiller.model.task.ITaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class PieChartHelper {
    private PieChart pieChart;

    ITaskList tasks;

    public PieChartHelper(PieChart p, ITaskList tasks) {
        pieChart = p;
        initPieChart();
        this.tasks = tasks;
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
        pieChart.getData().removeAll();
        pieChart.setData(getEntries());
    }


    // TODO: hier sollten die echten Namen und Dauern aus den Tasks als Daten verwendet werden
    // pieChartData.add(new PieChart.Data(<Taskname>, <darzustellende Dauer>))
    // in der Membervariable tasks steht bereits die Refernez auf die benutzte ITaskList
    private ObservableList<PieChart.Data> getEntries() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(int i = 1; i < 4; i++) {
            pieChartData.add(new PieChart.Data("Task " + i, i));
        }
        return pieChartData;
    }
}
