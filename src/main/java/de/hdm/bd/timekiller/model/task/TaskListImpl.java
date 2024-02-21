package de.hdm.bd.timekiller.model.task;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskListImpl implements ITaskList {
    private DbManager dbManager;
    private List<Task> tasks;
    public TaskListImpl()  {
        try{
            dbManager = new DbManager();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> getAllTasks() {
        try{
            return dbManager.getTaskDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Task getTask(int id) {
        try{
            return dbManager.getTaskDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int insertTask(String name) throws DuplicatedNameException, IllegalNameException {
        System.out.println("insertTask");
        if (!checkValidName(name)) {
            throw new IllegalNameException("Invalid task name.");
        }
        try {
            List<Task> existingTasks = dbManager.getTaskDao().queryForEq("name", name);
            if (!existingTasks.isEmpty()) {
                throw new DuplicatedNameException("Task with the same name already exists.");
            }
            Task task = new Task(name);
            return dbManager.getTaskDao().create(task);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void updateTask(Task task) throws DuplicatedNameException, IllegalNameException {
        System.out.println("updateTask");
        if (!checkValidName(task.getName())) {
            throw new IllegalNameException("Invalid task name.");
        }
        try {
            List<Task> existingTasks = dbManager.getTaskDao().queryForEq("name", task.getName());
            for (Task existingTask : existingTasks) {
                if (existingTask.getId() != task.getId()) {
                    throw new DuplicatedNameException("Task with the same name already exists.");
                }
            }

            dbManager.getTaskDao().update(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteTask(Task task) {
        System.out.println("deleteTask");
        try {
            //Wenn Eintrag gelöscht gibt ORMLite 1 zurück
            dbManager.deleteDurationTrackersForTask(task);
            return dbManager.getTaskDao().delete(task) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkValidName(String name){
        System.out.println("checkValidName");
        return name.matches("^[a-zA-Z_][a-zA-Z0-9_]*");
    }
    /*
    public void saveOrUpdateDurationTrackers() {
        for (Task task : tasks) {
            for (DurationTracker tracker : task.getRecords()) {
                if (tracker.getId() == 0) {
                    // Wenn die ID des DurationTracker 0 ist, ist er neu und muss gespeichert werden
                    try {
                        dbManager.getDurationTrackerDao().create(tracker);
                        System.out.println("neuer Tracker");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Andernfalls aktualisieren Sie den vorhandenen DurationTracker
                    try {
                        dbManager.getDurationTrackerDao().update(tracker);
                        System.out.println("alter Tracker");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
    */


}

