package de.hdm.bd.timekiller.model.task;

import java.util.List;

public interface ITaskList {
        public List<Task> getAllTasks();
        // TODO: Exceptions berücksichtigen
        public int insertTask(String name);
        // TODO: Exceptions berücksichtigen
        public int updateTask(Task task);
        public int deleteTask(Task task);
}
