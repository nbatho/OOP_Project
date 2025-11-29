package main.java.controller;

import main.java.Utility.Helper;
import main.java.component.ProjectCard;
import main.java.model.Project;
import main.java.model.ProjectMember;
import main.java.model.User;
import main.java.service.impl.ProjectMemberServiceImpl;
import main.java.service.impl.ProjectServiceImpl;
import main.java.service.impl.UserServiceImpl;
import main.java.view.DashboardView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProjectController {
    private final DashboardView view;
    private final Consumer<String> onProjectSelected;
    private final Helper helper;

    private String currentProjectId;
    private List<User> currentProjectMembers = new ArrayList<>();

    private final UserServiceImpl userService = UserServiceImpl.getInstance();
    private final ProjectServiceImpl projectService = ProjectServiceImpl.getInstance();
    private final ProjectMemberServiceImpl projectMemberService = ProjectMemberServiceImpl.getInstance();

    public ProjectController(DashboardView view, Consumer<String> onProjectSelected) {
        this.view = view;
        this.onProjectSelected = onProjectSelected;
        this.helper = new Helper();

        initListeners();
    }

    private void initListeners() {
        view.getCreateProjectMenuItem().addActionListener(e -> handleCreateProject());
        view.getaddMemberButton().addActionListener(e -> handleAddMember());
        view.setMemberDeleteListener(this::handleDeleteMember);
        view.setProjectSelectionListener(this::handleProjectSelected);
    }
    public void loadProjectList() {
        try {
            String[] projectNames = projectService.getProjectNameByUserId(userService.getCurrentUser().getUserId());
            view.updateProjectList(projectNames);

            if (projectNames.length > 0) {
                handleProjectSelected(projectNames[0]);
            } else {
                view.setCurrentProjectName("Chưa có dự án");
                view.updateSidebarProjectInfo(new Project());
                view.updateMembersList(new ArrayList<>());
                currentProjectId = null;
            }
        } catch (Exception e) {
            view.showErrorMessage("Không thể tải danh sách dự án: " + e.getMessage());
        }
    }
    private void handleProjectSelected(String projectName) {
        try {
            view.setCurrentProjectName(projectName);
            Project project = projectService.getProjectByName(projectName);

            if (project != null) {
                currentProjectId = project.getProjectId();
                view.updateSidebarProjectInfo(project);
                loadProjectMembers(currentProjectId);

                if (onProjectSelected != null) {
                    onProjectSelected.accept(currentProjectId);
                }
            }
        } catch (Exception e) {
            view.showErrorMessage("Không thể tải thông tin dự án: " + e.getMessage());
        }
    }
    private void loadProjectMembers(String projectId) {
        try {
            List<User> listMembers = projectMemberService.getProjectMember(projectId);
            this.currentProjectMembers = listMembers;
            view.updateMembersList(listMembers);
        } catch (Exception e) {
            view.showErrorMessage("Không thể tải danh sách thành viên: " + e.getMessage());
        }
    }
    private void handleAddMember() {
        if (currentProjectId == null) {
            view.showWarningMessage("Vui lòng chọn dự án trước khi thêm thành viên!");
            return;
        }
        try {
            List<User> availableUsers = projectMemberService.getAvailableUsers(currentProjectId);
            view.showAddMemberPopup(availableUsers, this::addMemberToProject);
        } catch (Exception e) {
            view.showErrorMessage("Không thể tải danh sách người dùng: " + e.getMessage());
        }
    }
    private void addMemberToProject(User user) {
        try {
            ProjectMember newMember = new ProjectMember(currentProjectId, user.getUserId(), "R2");

            if (view.showConfirmDialog("Xác nhận thêm " + user.getFullName() + " vào dự án?")) {
                projectMemberService.create(newMember);
                loadProjectMembers(currentProjectId);
                view.showSuccessMessage("Đã thêm " + user.getFullName() + " vào dự án!");
            }
        } catch (Exception ex) {
            view.showErrorMessage("Lỗi khi thêm thành viên: " + ex.getMessage());
        }
    }
    private void handleCreateProject() {
        ProjectCard projectCardView = new ProjectCard();
        projectCardView.getBtnSave().addActionListener(e -> {
            if (!projectCardView.validateInput()) return;
            String title = projectCardView.getTxtTitle().getText();
            String description = projectCardView.getTxtDescription().getText();
            try {
                Project newProject = new Project();
                newProject.setName(title);
                newProject.setDescription(description);
                String projectId = projectService.createProject(newProject);
                projectMemberService.create(new ProjectMember(projectId, userService.getCurrentUser().getUserId(), "R2"));

                loadProjectList();
                handleProjectSelected(title);

                view.showSuccessMessage("Tạo dự án thành công");
                projectCardView.dispose();
            } catch (Exception ex) {
                view.showErrorMessage("Lỗi khi tạo dự án" + ex.getMessage());
            }
        });
    }
    private void handleDeleteMember(User memberNeedDelete) {
        try {
            if (currentProjectMembers.size() > 1) {
                if (view.showConfirmDialog("Bạn có chắc muốn xóa " + memberNeedDelete.getFullName() + " khỏi dự án?")) {
                    boolean isDeleted = projectMemberService.deleteByProjectIdAndUserId(currentProjectId, memberNeedDelete.getUserId());
                    if (isDeleted) {
                        currentProjectMembers.removeIf(u -> u.getUserId().equals(memberNeedDelete.getUserId()));
                        view.updateMembersList(currentProjectMembers);
                        view.showSuccessMessage("Xóa thành viên thành công!");
                    }
                }
            }
            else {
                boolean confirm = view.showConfirmDialog("Đây là thành viên cuối cùng. Bạn có muốn xóa luôn dự án này không?");
                if (confirm) {
                    try {
                        projectService.deleteProject(currentProjectId);
                        view.showSuccessMessage("Đã xóa dự án thành công!");
                        loadProjectList();

                    } catch (Exception ex) {
                        view.showErrorMessage("Lỗi không thể xóa project: " + ex.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            view.showErrorMessage("Lỗi hệ thống khi xóa: " + e.getMessage());
        }
    }

    public String getCurrentProjectId() {
        return currentProjectId;
    }

    public List<User> getCurrentProjectMembers() {
        return currentProjectMembers;
    }
}