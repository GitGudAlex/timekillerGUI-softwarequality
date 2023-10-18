package de.hdm.bd.timekiller.model.task;

import java.util.ArrayList;
import java.util.List;

public class MinimalTaskList implements ITaskList {

    private List<Task> taskList;

    public MinimalTaskList() {
        init();
    }

    private void init() {
        taskList = new ArrayList();
        taskList.add(new Task("Task 1"));
        taskList.add(new Task("Task 2"));
        taskList.add(new Task("Task 3"));
    }


    @Override
    public List<Task> getAllTasks() {
       return taskList;
    }

    @Override
    public int insertTask(String name)  {
        return -1;
    }

    @Override
    public int updateTask(Task task) {
        return 0;
    }

    @Override
    public int deleteTask(Task task) {
        return 0;
    }
}
