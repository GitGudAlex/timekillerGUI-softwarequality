package de.hdm.bd.timekiller.model.task;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;

import java.util.List;

public interface ITaskList {
        List<Task> getAllTasks();
        Task getTask(int id);
        int insertTask(String name) throws DuplicatedNameException, IllegalNameException;
        void updateTask(Task task) throws DuplicatedNameException, IllegalNameException;
        boolean deleteTask(Task task);
}
