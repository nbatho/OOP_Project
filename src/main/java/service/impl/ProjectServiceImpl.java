package main.java.service.impl;

import java.util.List;
import java.util.UUID;
import main.java.model.Project;
import main.java.repository.ProjectRepository;
import main.java.service.ProjectService;
import main.java.service.TeamService;

public class ProjectServiceImpl implements ProjectService {
    
    private final ProjectRepository projectRepository;
    private final TeamService teamService;
    
    public ProjectServiceImpl() {
        this.projectRepository = new ProjectRepository();
        this.teamService = new main.java.service.impl.TeamServiceImpl();
    }
    
    @Override
    public boolean createProject(Project project) {
        if (project == null) {
            System.err.println("Project object không được null");
            return false;
        }
        
        // Validate dữ liệu
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            System.err.println("Tên project không được để trống");
            return false;
        }
        
        if (project.getTeamId() == null || project.getTeamId().trim().isEmpty()) {
            System.err.println("Team ID không được để trống");
            return false;
        }
        
        // Kiểm tra team có tồn tại không
        if (teamService.getTeamById(project.getTeamId()) == null) {
            System.err.println("Team không tồn tại với ID: " + project.getTeamId());
            return false;
        }
        
        // Kiểm tra tên project đã tồn tại chưa
        if (isProjectNameExists(project.getName())) {
            System.err.println("Tên project đã tồn tại: " + project.getName());
            return false;
        }
        
        // Tự động tạo ID nếu chưa có
        if (project.getProjectId() == null || project.getProjectId().trim().isEmpty()) {
            project.setProjectId(UUID.randomUUID().toString());
        }
        
        return projectRepository.createProject(project);
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
        
        // Kiểm tra team có tồn tại không
        if (project.getTeamId() != null && teamService.getTeamById(project.getTeamId()) == null) {
            System.err.println("Team không tồn tại với ID: " + project.getTeamId());
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
