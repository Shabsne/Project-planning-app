package org.example.projectplanningapp.services;

import org.example.projectplanningapp.exceptions.ResourceNotFoundException;
import org.example.projectplanningapp.models.Employee;
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


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void createTask(Task task) {
        taskRepository.createTask(task);
    }

    public Task getTaskFromId(int taskId) {
        Task task = taskRepository.getTaskFromId(taskId);

        if (task == null) {
            throw new ResourceNotFoundException("Task", taskId);
        }

        return task;
    }

    public List<Task> getTasksInProject(int projectId) {
        return taskRepository.getTasksInProject(projectId);
    }

    public List<Task> getAssignedTasksForEmployee (int employeeId) {
        List<Task> tasks = taskRepository.getAssignedTasksForEmployee(employeeId);
        return tasks.isEmpty() ? Collections.emptyList() : tasks;
    }


    public List<Task> getNextTasksForEmployee(int employeeId) {
        return taskRepository.getNextTasksForEmployee(employeeId);
    }



    public void assignEmployeeToTask(int taskId, int empId) {
        taskRepository.assignEmployeeToTask(taskId, empId);
    }

    public void removeEmployeeFromTask(int taskId, int empId) {
        taskRepository.removeEmployeeFromTask(taskId, empId);
    }


    public void updateTask (Task task){
        taskRepository.updateTask(task);
    }



    public void deleteTask (int taskId){
        taskRepository.deleteTask(taskId);
    }


    public List<Employee> getAssignedEmployeesForTask(int taskId) {
        return taskRepository.getAssignedEmployeesForTask(taskId);
    }

    public List<Task> getSubTasks(int taskId) {
        return taskRepository.getSubTasks(taskId);
    }

    public List<Task> getRootTasks(int projectId) {
        return taskRepository.getRootTasks(projectId);
    }

    public boolean hasAnyTasksInProject(int projectId) {
        return taskRepository.countTasksByProjectId(projectId) > 0;
    }
}
