package main.java.service.impl;

import java.util.List;
import java.util.UUID;
import main.java.model.Project;
import main.java.repository.ProjectRepository;
import main.java.service.ProjectService;

public class ProjectServiceImpl implements ProjectService {
    
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl() {
        this.projectRepository = new ProjectRepository();
    }

    @Override
    public String createProject(Project project) {
        if (project == null) return null;
        if (project.getName() == null || project.getName().trim().isEmpty()) return null;

        if (isProjectNameExists(project.getName())) return null;

        if (project.getProjectId() == null || project.getProjectId().isEmpty()) {
            project.setProjectId(UUID.randomUUID().toString());
        }

        boolean created = projectRepository.createProject(project);
        return created ? project.getProjectId() : null;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }
    
    @Override
    public Project getProjectById(String projectId) {
        if (projectId == null || projectId.trim().isEmpty()) {
            System.err.println("Project ID không được để trống");
            return null;
        }
        return projectRepository.getProjectById(projectId);
    }
    
    @Override
    public List<Project> getProjectsByTeamId(String teamId) {
        if (teamId == null || teamId.trim().isEmpty()) {
            System.err.println("Team ID không được để trống");
            return List.of();
        }
        return projectRepository.getProjectsByTeamId(teamId);
    }
    
    @Override
    public Project getProjectByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Tên project không được để trống");
            return null;
        }
        return projectRepository.getProjectByName(name);
    }
    
    @Override
    public boolean updateProject(Project project) {
        if (project == null || project.getProjectId() == null) {
            System.err.println("Project hoặc Project ID không được null");
            return false;
        }
        
        // Kiểm tra project có tồn tại không
        Project existingProject = getProjectById(project.getProjectId());
        if (existingProject == null) {
            System.err.println("Project không tồn tại với ID: " + project.getProjectId());
            return false;
        }

        
        // Nếu tên thay đổi, kiểm tra tên mới có trùng không
        if (!existingProject.getName().equals(project.getName()) && isProjectNameExists(project.getName())) {
            System.err.println("Tên project mới đã tồn tại: " + project.getName());
            return false;
        }
        
        return projectRepository.updateProject(project);
    }
    
    @Override
    public boolean deleteProject(String projectId) {
        if (projectId == null || projectId.trim().isEmpty()) {
            System.err.println("Project ID không được để trống");
            return false;
        }
        
        // Kiểm tra project có tồn tại không
        Project existingProject = getProjectById(projectId);
        if (existingProject == null) {
            System.err.println("Project không tồn tại với ID: " + projectId);
            return false;
        }
        
        // TODO: Kiểm tra project có đang được sử dụng không (có task nào không)
        // Có thể thêm logic để không cho xóa project đang có task
        
        return projectRepository.deleteProject(projectId);
    }
    
    @Override
    public boolean isProjectNameExists(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return getProjectByName(name) != null;
    }

    @Override
    public boolean projectExists(String projectId) {
        if (projectId == null || projectId.trim().isEmpty()) {
            return false;
        }
        Project project = getProjectById(projectId);
        return project != null;
    }
}
