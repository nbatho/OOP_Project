package main.java.controller;

import main.java.component.ProjectCard;
import main.java.component.TaskCard;
import main.java.model.Project;
import main.java.model.ProjectMember;
import main.java.model.Task;
import main.java.model.User;
import main.java.service.ProjectMemberService;
import main.java.service.impl.ProjectMemberServiceImpl;
import main.java.service.impl.ProjectServiceImpl;
import main.java.service.impl.TaskServiceImpl;
import main.java.service.impl.UserServiceImpl;
import main.java.view.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    private final DashboardView view;
    private final KanbanView kanbanView;
    private final TableView tableView;
    private final CalendarView calendarView;
    private final UserServiceImpl userService = UserServiceImpl.getInstance();
    private final ProjectServiceImpl projectService = new ProjectServiceImpl();
    private final ProjectMemberServiceImpl projectMemberService = new ProjectMemberServiceImpl();
    private final TaskServiceImpl taskService = new TaskServiceImpl();
    public DashboardController(DashboardView view) {
        this.view = view;
        this.kanbanView = new KanbanView();
        this.tableView = new TableView();
        this.calendarView = new CalendarView();

        // Gắn các view vào mainContentPanel
        view.getMainContentPanel().add(kanbanView, "KANBAN");
        view.getMainContentPanel().add(tableView, "TABLE");
        view.getMainContentPanel().add(calendarView, "CALENDAR");

        // Lắng nghe các nút chuyển view
        view.getKanbanButton().addActionListener(e -> showView("KANBAN"));
        view.getTableButton().addActionListener(e -> showView("TABLE"));
        view.getCalendarButton().addActionListener(e -> showView("CALENDAR"));

        view.getSearchButton().addActionListener(e -> handleSearch());
        view.getCreateButton().addActionListener(e -> handleShowCard());

        // Menu items
        view.getInfoMenuItem().addActionListener(e -> handleShowInfo());
        view.getLogoutMenuItem().addActionListener(e -> handleLogout());

        // Lắng nghe sự kiện chọn dự án
        view.setProjectSelectionListener(projectName -> handleProjectSelected(projectName));

        // Lắng nghe sự kiện tạo dự án mới
        view.getCreateProjectMenuItem().addActionListener(e -> handleCreateProject());

        // Load danh sách dự án ban đầu
        loadProjectList();


    }
    private void loadProjectMembers(String projectId) {
        try {
            List<ProjectMember> listProjectMembers = projectMemberService.getByProjectId(projectId);
            List<User> listMembers = new ArrayList<>();
            for (ProjectMember projectMember : listProjectMembers) {
                User users = userService.getUserById(projectMember.getUserId());
                if (users != null) {

                listMembers.add(users);
                }
            }
            view.updateMembersList(listMembers);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tải danh sách thành viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void loadProjectTasks(String projectId) {
        try {
          List<Task> listTasks = taskService.getTasksByProjectId(projectId);

//            // 3. Cập nhật TẤT CẢ các view
            kanbanView.updateTasks(listTasks);


//            tableView.updateTasks(tasks);
//            calendarView.updateTasks(tasks);
//

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tải danh sách task: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Load danh sách dự án từ database và cập nhật vào dropdown menu
     */
    private void loadProjectList() {
        try {
            List<ProjectMember> listProjectMembers = projectMemberService
                    .getByUserId(userService.getCurrentUser().getUserId());
            String[] projectNames = new String[listProjectMembers.size()];

            for (int i = 0; i < listProjectMembers.size(); i++) {
                Project project = projectService.getProjectById(
                        listProjectMembers.get(i).getProjectId()
                );
                projectNames[i] = project.getName();
            }

            view.updateProjectList(projectNames);

            if (projectNames.length > 0) {
                handleProjectSelected(projectNames[0]);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tải danh sách dự án: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xử lý khi người dùng chọn một dự án từ dropdown
     */
    private void handleProjectSelected(String projectName) {
        try {
            view.setCurrentProjectName(projectName);

            // Lấy thông tin chi tiết dự án từ database
            Project project = projectService.getProjectByName(projectName);

            if (project != null) {
                String projectInfo = getProjectInfo(project);

                view.updateSidebarProjectInfo(projectInfo);

                loadProjectMembers(project.getProjectId());

                // TODO: Load tasks của dự án này vào KanbanView/TableView/CalendarView
                 loadProjectTasks(project.getProjectId());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tải thông tin dự án: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lấy thông tin dự án để hiển thị
     */
    private String getProjectInfo(Project project) {
        StringBuilder info = new StringBuilder();
        info.append("Tên: ").append(project.getName()).append("\n");
        info.append("Mô tả: ").append(project.getDescription()).append("\n");
        info.append("Ngày tạo: ").append(project.getCreatedAt()).append("\n");
        return info.toString();
    }

    /**
     * Xử lý tạo dự án mới
     */
    private void handleCreateProject() {
        ProjectCard projectCardView = new ProjectCard();
        projectCardView.getBtnSave().addActionListener(e -> {
            String title = projectCardView.getTxtTitle().getText();
            String description = projectCardView.getTxtDescription().getText();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(projectCardView,
                        "Vui lòng nhập tên dự án!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
//                // Tạo project mới
                Project newProject = new Project();
                newProject.setName(title);
                newProject.setDescription(description);
//
                String projectId = projectService.createProjectEmpty(newProject);
//
                projectMemberService.create(new ProjectMember(projectId,userService.getCurrentUser().getUserId(),"R2"));
//                // Reload danh sách dự án
                loadProjectList();
//                // Tự động chọn dự án mới tạo
                handleProjectSelected(title);
                JOptionPane.showMessageDialog(projectCardView,
                        "Tạo dự án thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                projectCardView.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(projectCardView,
                        "Lỗi khi tạo dự án: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void handleSearch() {
        System.out.println("Dang Tim kiem");
    }

    private void handleShowCard() {
        TaskCard taskCardView = new TaskCard();
        taskCardView.getBtnSave().addActionListener(e -> {
            String title = taskCardView.getTxtTitle().getText();
            String description = taskCardView.getTxtDescription().getText();
            String assignee = taskCardView.getTxtAssignee().getText();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(taskCardView,
                        "Vui lòng nhập tiêu đề!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // TODO: Lưu task vào database/model
            System.out.println("Task created: " + title);

            taskCardView.dispose();
        });
    }

    private void showView(String name) {
        view.getCardLayout().show(view.getMainContentPanel(), name);
    }

    private void handleShowInfo() {
        System.out.println("thong tin");
        JOptionPane.showMessageDialog(view,
                "Thông tin người dùng",
                "Thông tin",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleLogout() {
        System.out.println("Da dang xuat");
        int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
            new LoginView();
        }
    }

    public KanbanView getKanbanView() {
        return kanbanView;
    }
}