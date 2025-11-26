package main.java.controller;

import com.toedter.calendar.JDateChooser;
import main.java.component.ProjectCard;
import main.java.component.TaskCard;
import main.java.model.*;
import main.java.service.impl.*;
import main.java.view.*;


import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardController {
    private final DashboardView view;
    private final KanbanView kanbanView;
    private final TableView tableView;
    private final CalendarView calendarView;
    private final UserServiceImpl userService = UserServiceImpl.getInstance();
    private final ProjectServiceImpl projectService = new ProjectServiceImpl();
    private final ProjectMemberServiceImpl projectMemberService = new ProjectMemberServiceImpl();
    private final TaskServiceImpl taskService = new TaskServiceImpl();
    private final TaskAssigneesServiceImpl taskAssignessService = new TaskAssigneesServiceImpl();
    private String currentProjectId;
    private List<User> currentProjectMembers = new ArrayList<>();
    public DashboardController(DashboardView view) {
        this.view = view;
        try {
            if (userService.getCurrentUser() != null) {
                view.setUserInitials(userService.getCurrentUser().getFullName());
            }
        } catch (Exception ex) {

        }

        this.kanbanView = view.getKanbanView();
        this.tableView = new TableView();
        this.calendarView = new CalendarView();

        try {
            Map<String, JButton> createButtons = kanbanView.getColumnCreateButtons();
            for (Map.Entry<String, JButton> e : createButtons.entrySet()) {
                JButton btn = e.getValue();
                btn.addActionListener(ae -> handleShowCard());
            }
        } catch (Exception ex) {

        }

        // Attach other views (table/calendar) into the same mainContentPanel
        view.getMainContentPanel().add(tableView, "TABLE");
        view.getMainContentPanel().add(calendarView, "CALENDAR");

        // Lắng nghe các nút chuyển view
        view.getKanbanButton().addActionListener(e -> showView("KANBAN"));
        view.getTableButton().addActionListener(e -> showView("TABLE"));
        view.getCalendarButton().addActionListener(e -> showView("CALENDAR"));

        view.getSearchButton().addActionListener(e -> handleSearch());
        // header create button hidden; per-column create buttons are used instead

        // Menu items
        view.getInfoMenuItem().addActionListener(e -> handleShowInfo());
        view.getLogoutMenuItem().addActionListener(e -> handleLogout());

        // Lắng nghe sự kiện chọn dự án
        view.setProjectSelectionListener(projectName -> handleProjectSelected(projectName));

        // Lắng nghe sự kiện tạo dự án mới
        view.getCreateProjectMenuItem().addActionListener(e -> handleCreateProject());

        // Load danh sách dự án ban đầu
        loadProjectList();

        kanbanView.setTaskClickListener(task -> onTaskClicked(task));
        tableView.setTaskRowClickListener((projectId, taskId) -> {
            onTaskClickedFromTable(projectId, taskId);
        });

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
            this.currentProjectMembers = listMembers;
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
            for (Task task : listTasks) {
                List<TaskAssignees> taskAsignesses = taskAssignessService.getByTaskId(task.getTaskId());
                List <User> listAssignees = new ArrayList<>();
                for (TaskAssignees taskAsigness : taskAsignesses) {
                    User users = userService.getUserById(taskAsigness.getUserId());
                    listAssignees.add(users);
                }
                task.setAssignedUsers(listAssignees);
            }
            // 3. Cập nhật TẤT CẢ các view
            kanbanView.updateTasks(listTasks);


            tableView.updateTasks(listTasks);
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
                currentProjectId = project.getProjectId();
                String projectInfo = getProjectInfo(project);

                view.updateSidebarProjectInfo(projectInfo);

                loadProjectMembers(currentProjectId);

                 loadProjectTasks(currentProjectId);
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
                String projectId = projectService.createProject(newProject);
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

        // Kiểm tra đã chọn project chưa
        if (currentProjectId == null) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn dự án trước khi tạo task!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mở TaskCard
        TaskCard card = new TaskCard(currentProjectId, currentProjectMembers,false);

        card.getBtnSave().addActionListener(e -> {

            try {
                String title = card.getTxtTitle().getText().trim();
                String description = card.getTxtDescription().getText().trim();
                User assignee = (User) card.getCmbUser().getSelectedItem();
                String status = (String) card.getCmbStatus().getSelectedItem();
                String priority = (String) card.getCmbPriority().getSelectedItem();

                Date startDate = card.getStartDateChooser().getDate();
                Date endDate = card.getEndDateChooser().getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                // Validate
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(card,
                            "Tiêu đề không được để trống!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Task newTask = new Task();
                newTask.setProjectId(currentProjectId);
                newTask.setTitle(title);
                newTask.setDescription(description);
                newTask.setStatus(status);
                newTask.setPriority(priority);
                newTask.setCreatedBy(userService.getCurrentUser().getUserId());
                if (endDate != null) {
                    java.sql.Date sqlDate = new java.sql.Date(endDate.getTime());
                    newTask.setDueDate(sqlDate);
                }

//                System.out.println(newTask);

                if (assignee != null) {
                    taskService.createTask(newTask,assignee.getUserId());
                }
                  // Reload lại danh sách task trên UI
                loadProjectTasks(currentProjectId);

                JOptionPane.showMessageDialog(card,
                        "Tạo task thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                card.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(card,
                        "Không thể tạo task: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
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
    private void onTaskClickedFromTable(String projectId, String taskId) {

        Task task = taskService.getTaskById(taskId, projectId);

        List<ProjectMember> memberList = projectMemberService.getByProjectId(projectId);
        List<User> users = new ArrayList<>();

        for (ProjectMember pm : memberList) {
            users.add(userService.getUserById(pm.getUserId()));
        }

        TaskCard card = new TaskCard(projectId, users, true);
        card.setTaskData(task);

        // Handle update
        card.getBtnSave().addActionListener(e -> handleUpdateTask(card, task));
    }
    private void onTaskClicked(Task task) {
        try {
            // Load user list for this project
            List<ProjectMember> members = projectMemberService.getByProjectId(task.getProjectId());
            List<User> users = new ArrayList<>();

            for (ProjectMember m : members) {
                users.add(userService.getUserById(m.getUserId()));
            }

            // Open taskCard edit form
            TaskCard taskCard = new TaskCard(task.getProjectId(), users, true);
            taskCard.setTaskData(task);

            // handle update
            taskCard.getBtnSave().addActionListener(e -> handleUpdateTask(taskCard, task));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Không thể mở task: " + ex.getMessage());
        }
    }
    private void handleUpdateTask(TaskCard card, Task oldTask) {
        try {
            System.out.println("Updated");
//            String title = card.getTxtTitle().getText().trim();
//            String description = card.getTxtDescription().getText().trim();
//            User assignee = (User) card.getCmbUser().getSelectedItem();
//            String priority = (String) card.getCmbPriority().getSelectedItem();
//            String status = (String) card.getCmbStatus().getSelectedItem();
//            Date endDate = card.getEndDateChooser().getDate();
//
//            if (title.isEmpty()) {
//                JOptionPane.showMessageDialog(card,
//                        "Tiêu đề không được để trống!",
//                        "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // Tạo task đã chỉnh sửa
//            Task updatedTask = new Task();
//            updatedTask.setTaskId(oldTask.getTaskId());
//            updatedTask.setProjectId(oldTask.getProjectId());
//            updatedTask.setTitle(title);
//            updatedTask.setDescription(description);
//            updatedTask.setPriority(priority);
//            updatedTask.setStatus(status);
//
//            if (endDate != null) {
//                updatedTask.setDueDate(new java.sql.Date(endDate.getTime()));
//            }
//
//            // Update vào DB
//            taskService.updateTask(updatedTask,
//                    assignee != null ? assignee.getUserId() : null);

            JOptionPane.showMessageDialog(card,
                    "Cập nhật task thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            // Reload UI
//            loadProjectTasks(updatedTask.getProjectId());

            card.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(card,
                    "Không thể cập nhật task: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public KanbanView getKanbanView() {
        return kanbanView;
    }
}