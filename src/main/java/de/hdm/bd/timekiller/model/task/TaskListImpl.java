package de.hdm.bd.timekiller.model.task;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskListImpl implements ITaskList {
    private List<Task> tasks;
    public TaskListImpl() throws IllegalNameException {
        tasks = new ArrayList<>();
        tasks.add(new Task(2, "Arbeit"));
        tasks.add(new Task(3, "Sport"));
        tasks.add(new Task(1, "Studium"));
        tasks.add(new Task(3, "Yoga"));
        Collections.sort(tasks, Comparator.comparing(Task::getName));

    }
    @Override
    public List<Task> getAllTasks() {
        return tasks;
    }
    @Override
    public Task getTask(int id) {
        for(Task task : tasks){
            if(task.getId() == id){
                return task;
            }
        }
        return null; //Task not found!
    }

    @Override
    public int insertTask(String name) throws DuplicatedNameException, IllegalNameException {
        if(!checkValidName(name)){
            throw new IllegalNameException(name);
        }
        for (Task task : tasks){
            if(task.getName().equals(name)){
                throw new DuplicatedNameException();
            }
        }

        int newId = generateUniqueId();
        Task newTask = new Task(newId, name);
        tasks.add(newTask);
        return newId;
    }
    @Override
    public void updateTask(Task task) throws DuplicatedNameException, IllegalNameException {
        if(!checkValidName(task.getName())){
            throw new IllegalNameException();
        }
        for (Task existingTask : tasks){
            if(existingTask.getId() != task.getId() && existingTask.getName().equals(task.getName())){
                throw new DuplicatedNameException();
            }
        }

        for (int i = 0; i < tasks.size() ; i++) {
            if (tasks.get(i).getId() == task.getId()){
                tasks.set(i, task);
                return;
            }
        }
    }
    @Override
    public boolean deleteTask(Task task) {
        return tasks.remove(task);
    }

    //Generate Unique ID
    private int generateUniqueId(){
        int maxId = 0;
        for (Task task : tasks){
            maxId = Math.max(maxId, task.getId());
        }
        return maxId + 1;
    }
    private boolean checkValidName(String name){
        return name.matches("^[a-zA-Z_][a-zA-Z0-9_]*");
    }


    }

