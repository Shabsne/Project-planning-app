package org.example.projectplanningapp.services;

import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.TaskRepository;
import org.example.projectplanningapp.utils.taskComparators.DeadlineComparator;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final Comparator<Task> deadlineComparator = new DeadlineComparator();


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //Create task
    public void createTask(Task task) {
        taskRepository.createTask(task);
    }

    //Getters unsorted
    public Task getTaskFromId(int taskId){
        return taskRepository.getTaskFromId(taskId);
    }
    public List<Task> getTasksInProject(int projectId){
        return taskRepository.getTasksInProject(projectId);
    }
    public List<Task> getAssignedTasksForEmployee (int employeeId) {
        List<Task> tasks = taskRepository.getAssignedTasksForEmployee(employeeId);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }


    //Getters sorted by deadline
    public List<Task> getTasksInProjectSortedByDeadline(int projectId){
        List<Task> tasks = taskRepository.getTasksInProject(projectId);
        tasks.sort(deadlineComparator);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }

    public List<Task> getAssignedTasksForEmployeeSortedByDeadline(int employeeId){
        List<Task> tasks = taskRepository.getAssignedTasksForEmployee(employeeId);
        tasks.sort(deadlineComparator);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }

    //Update Method
    public void updateTask (Task task){
        taskRepository.updateTask(task);
    }

    //Delete Method
    public void deleteTask (int taskId){
        taskRepository.deleteTask(taskId);
    }


}
