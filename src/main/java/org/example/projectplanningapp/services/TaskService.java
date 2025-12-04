package org.example.projectplanningapp.services;

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


    public void updateTask(Task updatedTask, List<Integer> employeeIds) {
        if (updatedTask.getTitle() == null) updatedTask.setTitle("New Task");
        if (updatedTask.getStatus() == null) updatedTask.setStatus(Status.TODO);

        taskRepository.updateTask(updatedTask);

        taskRepository.removeAllAssignedEmployeesFromTask(updatedTask.getTaskId());
        if (employeeIds != null) {
            for (Integer empId : employeeIds) {
                taskRepository.assignEmployeeToTask(updatedTask.getTaskId(), empId);
            }
        }
    }



    //Update Method
    public void updateTask (Task task){
        taskRepository.updateTask(task);
    }

    public void updateTaskEmployees(int taskId, List<Integer> employeeIds) {

        // 1. Slet alle tidligere assignments
        taskRepository.removeAllAssignedEmployeesFromTask(taskId);

        // 2. Hvis ingen valgt → færdig
        if (employeeIds == null || employeeIds.isEmpty()) {
            return;
        }

        // 3. Insert nye
        for (Integer empId : employeeIds) {
            taskRepository.assignEmployeeToTask(taskId, empId);
        }
    }


    //Delete Method
    public void deleteTask (int taskId){
        taskRepository.deleteTask(taskId);
    }


}
