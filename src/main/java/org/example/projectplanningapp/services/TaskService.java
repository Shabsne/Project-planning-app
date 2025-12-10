package org.example.projectplanningapp.services;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Status;
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

    public List<Task> getTasksWithSubTasks(int projectId) {
        List<Task> tasks = taskRepository.getTasksInProject(projectId);
        for (Task task : tasks) {
            List<Task> subTasks = taskRepository.getSubTasks(task.getTaskId());
            task.setSubTasks(subTasks);
        }
        return tasks;
    }

    public Task getTaskWithSubTasks(int taskId) {
        Task task = taskRepository.getTaskFromId(taskId);
        if (task != null) {
            List<Task> subTasks = taskRepository.getSubTasks(taskId);
            task.setSubTasks(subTasks);
        }
        return task;
    }

    //Getters sorted by deadline
    public List<Task> getTasksInProjectSortedByDeadline(int projectId){
        List<Task> tasks = taskRepository.getTasksInProject(projectId);
        tasks.sort(deadlineComparator);
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


    //Update Method
    public void updateTask (Task task){
        taskRepository.updateTask(task);
    }



    //Delete Method
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
}
