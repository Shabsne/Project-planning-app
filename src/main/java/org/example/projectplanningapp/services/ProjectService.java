package org.example.projectplanningapp.services;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Project;
import org.example.projectplanningapp.models.Status;
import org.example.projectplanningapp.models.Task;
import org.example.projectplanningapp.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void createProject(Project project) {
        projectRepository.createProject(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    public void deleteById(int id) {
        projectRepository.deleteProjectById(id);
    }

    public Project findById(int id) {
        return projectRepository.findProjectById(id);
    }

    public void updateProject(Project project) {
        projectRepository.update(project);
    }

    public List<Employee> getProjectEmployees(int projectId){
        return projectRepository.findEmployeesByProject(projectId);
    }

    public Project getProjectDetails(int id) {
        Project p = projectRepository.findProjectById(id);

        p.setSubProjects(projectRepository.findSubprojects(id));
        p.setTasks(projectRepository.findTasksByProject(id));
        p.setAssignedEmployees(getProjectEmployees(id));

        return p;
    }

    public List<Employee> getAvailableEmployeesForTask(int projectId, int taskId) {
        return projectRepository.getAvailableEmployeesForTask(projectId, taskId);
    }

    public List<Project> findProjectsByEmployee(int employeeId) {
        return projectRepository.findProjectsByEmployee(employeeId);
    }

    public int calculateEstimatedHours(Project project) {
        return project.getTasks()
                .stream()
                .mapToInt(task -> task.getEstimatedHours() != null ? task.getEstimatedHours() : 0)
                .sum();
    }

    public int calculateCompletionPercentage(int projectId) {
        List<Task> tasks = projectRepository.findTasksByProject(projectId);

        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }

        long completedTasks = tasks.stream()
                .filter(task -> task.getStatus() == Status.DONE)
                .count();

        return (int) Math.round((completedTasks * 100.0) / tasks.size());
    }

    // NYE METODER TIL EMPLOYEE ASSIGNMENT
    public void assignEmployeeToProject(int projectId, int employeeId) {
        projectRepository.assignEmployeeToProject(projectId, employeeId);
    }

    public void removeEmployeeFromProject(int projectId, int employeeId) {
        projectRepository.removeEmployeeFromProject(projectId, employeeId);
    }

    public List<Employee> getAvailableEmployeesForProject(int projectId) {
        return projectRepository.getAvailableEmployeesForProject(projectId);
    }

    public List<Project> getProjectsForEmployee(int employeeId) {
        return projectRepository.getProjectsForEmployee(employeeId);
    }
}