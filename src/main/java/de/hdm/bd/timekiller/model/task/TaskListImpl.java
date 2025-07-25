package de.hdm.bd.timekiller.model.task;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;

import java.sql.SQLException;
import java.util.List;

public class TaskListImpl implements ITaskList {
    private DbManager dbManager;

    public TaskListImpl() {
        this("timekiller.db");
    }
    public TaskListImpl(String databaseName)  {
        try{
            //Datenbanknamen angeben
            dbManager = new DbManager(databaseName);
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
            System.out.println("getTask id: " + id);
            return dbManager.getTaskDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int insertTask(String name) throws DuplicatedNameException, IllegalNameException {
        System.out.println("insertTask: " + name);
        if (!checkValidName(name)) {
            throw new IllegalNameException("Invalid task name.");
        }
        try {
            List<Task> existingTasks = dbManager.getTaskDao().queryForEq("name", name);
            if (!existingTasks.isEmpty()) {
                throw new DuplicatedNameException("Task with the same name already exists.");
            }
            Task task = new Task(name);
            dbManager.getTaskDao().create(task);

            return task.getId();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void updateTask(Task task) throws DuplicatedNameException, IllegalNameException {
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
        return name.matches("^[a-zA-Z_][a-zA-Z0-9_]*");
    }
}

