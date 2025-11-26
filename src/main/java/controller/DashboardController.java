package main.java.controller;

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
    private final TaskAssigneesServiceImpl taskAssigneesService = new TaskAssigneesServiceImpl();
    private String currentProjectId;
    private List<User> currentProjectMembers = new ArrayList<>();
    private final CommentServiceImpl commentService = new CommentServiceImpl();
    public DashboardController(DashboardView view) {
        this.view = view;
        try {
            if (userService.getCurrentUser() != null) {
                view.setUserInitials(userService.getCurrentUser().getFullName());
            }
        } catch (Exception ex) {
            System.out.println("Error in getting current user" + ex.getMessage());
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
            System.out.println("Error in getting create buttons" + ex.getMessage());
        }

        view.getMainContentPanel().add(tableView, "TABLE");
        view.getMainContentPanel().add(calendarView, "CALENDAR");


        view.getKanbanButton().addActionListener(e -> showView("KANBAN"));
        view.getTableButton().addActionListener(e -> showView("TABLE"));
        view.getCalendarButton().addActionListener(e -> showView("CALENDAR"));

        view.getCreateTaskButton().addActionListener(e -> handleShowCard());

        view.getInfoMenuItem().addActionListener(e -> handleShowInfo());
        view.getLogoutMenuItem().addActionListener(e -> handleLogout());
        view.getCreateProjectMenuItem().addActionListener(e -> handleCreateProject());


        view.setProjectSelectionListener(this::handleProjectSelected);



        loadProjectList();

        kanbanView.setTaskClickListener(this::onTaskClicked);
        tableView.setTaskRowClickListener(this::onTaskClickedFromTable);
        calendarView.setTaskClickListener(this::onTaskClicked);

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
        }
    }
    private void loadProjectTasks(String projectId) {
        try {
            List<Task> listTasks = taskService.getTasksByProjectId(projectId);
            for (Task task : listTasks) {
                List<TaskAssignees> taskAssignees = taskAssigneesService.getByTaskId(task.getTaskId());
                List <User> listAssignees = new ArrayList<>();
                for (TaskAssignees taskAssigned : taskAssignees) {
                    User users = userService.getUserById(taskAssigned.getUserId());
                    listAssignees.add(users);
                }
                task.setAssignedUsers(listAssignees);
            }
            kanbanView.updateTasks(listTasks);


            tableView.updateTasks(listTasks);
            calendarView.updateTasks(listTasks);
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
        return "Tên: " + project.getName() + "\n" +
                "Mô tả: " + project.getDescription() + "\n" +
                "Ngày tạo: " + project.getCreatedAt() + "\n";
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


    private void handleShowCard() {

        if (currentProjectId == null) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn dự án trước khi tạo task!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

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



                if (assignee != null) {
                    taskService.createTask(newTask,assignee.getUserId());
                }

                loadProjectTasks(currentProjectId);

                JOptionPane.showMessageDialog(card,
                        "Tạo task thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                card.dispose();

            } catch (Exception ex) {
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


        card.getBtnSave().addActionListener(e -> handleUpdateTask(card, task));
    }
    private void onTaskClicked(Task task) {
        try {
            List<ProjectMember> members = projectMemberService.getByProjectId(task.getProjectId());
            List<User> users = new ArrayList<>();

            for (ProjectMember m : members) {
                users.add(userService.getUserById(m.getUserId()));
            }

            TaskCard taskCard = new TaskCard(task.getProjectId(), users, true);
            taskCard.setTaskData(task);

            loadCommentsForTask(taskCard, task.getTaskId());

            taskCard.getBtnSendComment().addActionListener(e -> handleCreateComment(taskCard, task));
            taskCard.getBtnSave().addActionListener(e -> handleUpdateTask(taskCard, task));
            taskCard.getBtnDelete().addActionListener(e -> handleDeleteTask(taskCard,task));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Không thể mở task: " + ex.getMessage());
        }
    }
    private void loadCommentsForTask(TaskCard card, String taskId) {
        try {
            List<Comments> comments = commentService.getCommentsByTaskId(taskId);

            card.clearComments();

            for (Comments comment : comments) {
                User user = userService.getUserById(comment.getUserId());
                String userName = user != null ? user.getFullName() : "Unknown User";
                String timestamp = formatTimestamp(comment.getCreatedAt());

                card.addCommentToList(userName, comment.getBody(), timestamp);
            }

        } catch (Exception ex) {
            System.err.println("Không thể load comments: " + ex.getMessage());
        }
    }
    private void handleCreateComment(TaskCard card, Task task) {
        try {
            // 1. Validate input
            String commentText = card.getTxtComment().getText().trim();

            if (commentText.isEmpty()) {
                JOptionPane.showMessageDialog(card,
                        "Vui lòng nhập nội dung bình luận!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }


            Comments newComment = new Comments(
                    null,
                    task.getTaskId(),
                    userService.getCurrentUser().getUserId(),
                    commentText
            );
            // 3. Lưu vào database
            boolean success = commentService.createComment(newComment);

            if (!success) {
                JOptionPane.showMessageDialog(card,
                        "Không thể gửi bình luận!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Cập nhật UI
            String userName = userService.getCurrentUser().getFullName();
            String timestamp = formatTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

            card.addCommentToList(userName, commentText, timestamp);

            // 5. Clear textarea để người dùng tiếp tục comment
            card.getTxtComment().setText("");

            // 6. Thông báo thành công
            JOptionPane.showMessageDialog(card,
                    "Đã gửi bình luận!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);



        } catch (Exception ex) {
            JOptionPane.showMessageDialog(card,
                    "Lỗi khi gửi bình luận: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Format Timestamp thành chuỗi hiển thị
     */
    private String formatTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(timestamp);
    }
    private void handleDeleteTask(TaskCard card, Task task) {
        if (currentProjectId == null) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn dự án trước khi tạo task!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            taskAssigneesService.deleteByTaskId(task.getTaskId());
            taskService.deleteTask(task.getTaskId(),currentProjectId);
            loadProjectTasks(currentProjectId);

            JOptionPane.showMessageDialog(card,
                    "Xóa task thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            card.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(card,
                    "Không thể xóa task: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void handleUpdateTask(TaskCard card, Task oldTask) {
        try {
            String title = card.getTxtTitle().getText().trim();
            String description = card.getTxtDescription().getText().trim();
            User assignee = (User) card.getCmbUser().getSelectedItem();
            String priority = (String) card.getCmbPriority().getSelectedItem();
            String status = (String) card.getCmbStatus().getSelectedItem();
            Date endDate = card.getEndDateChooser().getDate();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(card,
                        "Tiêu đề không được để trống!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Task updatedTask = new Task();
            updatedTask.setTaskId(oldTask.getTaskId());
            updatedTask.setProjectId(oldTask.getProjectId());
            updatedTask.setTitle(title);
            updatedTask.setDescription(description);
            updatedTask.setPriority(priority);
            updatedTask.setStatus(status);

            if (endDate != null) {
                updatedTask.setDueDate(new java.sql.Date(endDate.getTime()));
            }
            System.out.println(updatedTask);
            taskService.updateTask(updatedTask);
            JOptionPane.showMessageDialog(card,
                    "Cập nhật task thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            loadProjectTasks(updatedTask.getProjectId());

            card.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(card,
                    "Không thể cập nhật task: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}