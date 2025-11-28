package main.java.controller;

import main.java.component.ProjectCard;
import main.java.component.TaskCard;
import main.java.model.*;
import main.java.service.impl.*;
import main.java.Utility.Helper;
import main.java.view.*;

import javax.swing.*;
import java.util.*;

public class DashboardController {
    private final DashboardView view;
    private final KanbanView kanbanView;
    private final CalendarView calendarView;

    private final UserServiceImpl userService = UserServiceImpl.getInstance();
    private final ProjectServiceImpl projectService = ProjectServiceImpl.getInstance();
    private final ProjectMemberServiceImpl projectMemberService = ProjectMemberServiceImpl.getInstance();
    private final TaskServiceImpl taskService = new TaskServiceImpl();
    private final CommentServiceImpl commentService = new CommentServiceImpl();
    private String currentProjectId;
    private List<User> currentProjectMembers = new ArrayList<>();

    private final Helper helper;
    public DashboardController(DashboardView view) {
        this.view = view;
        this.kanbanView = view.getKanbanView();
        this.calendarView = new CalendarView();
        this.helper = new Helper();

        try {
            if (userService.getCurrentUser() != null) {
                view.setUserInitials(userService.getCurrentUser().getFullName());
            }
        } catch (Exception ex) {
            System.out.println("Error in getting current user" + ex.getMessage());
        }
        view.getMainContentPanel().add(calendarView, "CALENDAR");
        view.getKanbanButton().addActionListener(e -> showView("KANBAN"));
        view.getCalendarButton().addActionListener(e -> showView("CALENDAR"));
        view.getCreateTaskButton().addActionListener(e -> handleCreateTask());
        view.getLogoutMenuItem().addActionListener(e -> handleLogout());
        view.getCreateProjectMenuItem().addActionListener(e -> handleCreateProject());
        view.setMemberDeleteListener(this::handleDeleteUser);
        view.setProjectSelectionListener(this::handleProjectSelected);
        view.getaddMemberButton().addActionListener(e -> handleAddMember());

        loadProjectList();

        kanbanView.setTaskClickListener(this::onTaskClicked);
        calendarView.setTaskClickListener(this::onTaskClicked);
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

    private void loadProjectTasks(String projectId) {
        try {
            List<Task> listTasks = taskService.getTasksWithAssignees(projectId);
            kanbanView.updateTasks(listTasks);
            calendarView.updateTasks(listTasks);
        } catch (Exception e) {
            view.showErrorMessage("Không thể tải danh sách task: " + e.getMessage());
        }
    }

    private void loadProjectList() {
        try {
            String[] projectNames =  projectService.getProjectNameByUserId(userService.getCurrentUser().getUserId());
            view.updateProjectList(projectNames);

            if (projectNames.length > 0) {
                handleProjectSelected(projectNames[0]);
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
                loadProjectTasks(currentProjectId);
            }
        } catch (Exception e) {
            view.showErrorMessage("Không thể tải thông tin dự án: " + e.getMessage());
        }
    }

    private void handleAddMember() {
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

    private void handleDeleteUser(User userNeedDelete) {
        try {
            if (currentProjectMembers.size() > 1) {
                if (view.showConfirmDialog("Bạn có chắc muốn xóa " + userNeedDelete.getFullName() + " khỏi dự án?")) {
                    boolean isDeleted = projectMemberService.deleteByProjectIdAndUserId(currentProjectId, userNeedDelete.getUserId());
                    if (isDeleted) {
                        currentProjectMembers.removeIf(u -> u.getUserId().equals(userNeedDelete.getUserId()));
                        view.updateMembersList(currentProjectMembers);
                        view.showSuccessMessage("Xóa thành công!");
                    }
                }
            } else {
                view.showWarningMessage("Không thể xóa người dùng! Dự án phải có ít nhất 1 thành viên.");
            }
        } catch (Exception e) {
            view.showErrorMessage("Lỗi khi xóa người dùng: " + e.getMessage());
        }
    }

    private void handleCreateTask() {
        TaskCard card = new TaskCard(currentProjectId, currentProjectMembers, false);

        card.getBtnSave().addActionListener(e -> {
            try {
                if (!card.validateInput()) return;
                String title = card.getTxtTitle().getText().trim();
                String description = card.getTxtDescription().getText().trim();
                List<User> assignees = card.getAssignees();
                String status = (String) card.getCmbStatus().getSelectedItem();
                String priority = (String) card.getCmbPriority().getSelectedItem();
                Date startDate = card.getStartDateChooser().getDate();
                Date endDate = card.getEndDateChooser().getDate();

                Task newTask = new Task();
                newTask.setProjectId(currentProjectId);
                newTask.setTitle(title);
                newTask.setDescription(description);
                newTask.setStatus(status);
                newTask.setPriority(priority);
                newTask.setCreatedBy(userService.getCurrentUser().getUserId());
                java.sql.Date sqlDate = new java.sql.Date(endDate.getTime());
                newTask.setDueDate(sqlDate);

                if (!assignees.isEmpty()) {
                    taskService.createTask(newTask, assignees);
                }

                loadProjectTasks(currentProjectId);
                view.showSuccessMessage("Tạo task thành công");
                card.dispose();
            } catch (Exception ex) {
                view.showErrorMessage("Không thể tạo task");
            }
        });
    }

    private void showView(String name) {
        view.getCardLayout().show(view.getMainContentPanel(), name);
    }

    private void handleLogout() {
        if (view.showConfirmDialog("Bạn có chắc muốn đăng xuất?")) {
            view.dispose();
            new LoginView();
        }
    }

    private void onTaskClicked(Task task) {
        try {
            List<User> memberList = projectMemberService.getProjectMember(task.getProjectId());
            TaskCard taskCard = new TaskCard(task.getProjectId(), memberList, true);
            taskCard.setTaskData(task);

            loadCommentsForTask(taskCard, task.getTaskId());

            taskCard.getBtnSendComment().addActionListener(e -> handleCreateComment(taskCard, task));
            taskCard.getBtnSave().addActionListener(e -> handleUpdateTask(taskCard, task));
            taskCard.getBtnDelete().addActionListener(e -> handleDeleteTask(taskCard, task));
        } catch (Exception ex) {
            view.showErrorMessage("Không thể mở task: " + ex.getMessage());
        }
    }

    private void loadCommentsForTask(TaskCard card, String taskId) {
        try {
            List<Comments> comments = commentService.getCommentsByTaskId(taskId);
            card.clearComments();

            for (Comments comment : comments) {
                User user = userService.getUserById(comment.getUserId());
                String userName = user != null ? user.getFullName() : "Unknown User";
                String timestamp = helper.formatTimestamp(comment.getCreatedAt());
                card.addCommentToList(userName, comment.getBody(), timestamp);
            }
        } catch (Exception ex) {
            System.err.println("Không thể load comments: " + ex.getMessage());
        }
    }

    private void handleCreateComment(TaskCard card, Task task) {
        try {
            String commentText = card.getTxtComment().getText().trim();

            if (!card.validateComment()) return;

            Comments newComment = new Comments(null, task.getTaskId(), userService.getCurrentUser().getUserId(), commentText);
            boolean success = commentService.createComment(newComment);

            if (!success) {
                view.showErrorMessage("Không thể gửi bình luận");
                return;
            }

            String userName = userService.getCurrentUser().getFullName();
            String timestamp = helper.formatTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
            card.addCommentToList(userName, commentText, timestamp);
            card.getTxtComment().setText("");
            view.showSuccessMessage("Đã gửi bình luận");
        } catch (Exception ex) {
            view.showErrorMessage("Lỗi khi gửi bình luận" + ex.getMessage());
        }
    }

    private void handleDeleteTask(TaskCard card, Task task) {
        if (currentProjectId == null) {
            view.showErrorMessage("Vui lòng chọn dự án trước!");
            return;
        }

        if (!view.showConfirmDialog("Bạn có chắc muốn xóa task này?")) {
            return;
        }

        try {
            taskService.deleteTask(task.getTaskId(), currentProjectId);
            loadProjectTasks(currentProjectId);
            view.showSuccessMessage("Xóa task thành công!");
            card.dispose();
        } catch (Exception ex) {
            view.showErrorMessage("Không thể xóa task: " + ex.getMessage());
        }
    }

    private void handleUpdateTask(TaskCard card, Task oldTask) {
        try {
            if (!card.validateInput()) return;
            String title = card.getTxtTitle().getText().trim();
            String description = card.getTxtDescription().getText().trim();
            List<User> userAssignees = card.getAssignees();
            String priority = (String) card.getCmbPriority().getSelectedItem();
            String status = (String) card.getCmbStatus().getSelectedItem();
            Date endDate = card.getEndDateChooser().getDate();


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

            taskService.updateTask(updatedTask, userAssignees);
            loadProjectTasks(updatedTask.getProjectId());

            view.showSuccessMessage("Cập nhật task thành công!");
            card.dispose();
        } catch (Exception ex) {
            view.showErrorMessage("Không thể cập nhật task: " + ex.getMessage());
        }
    }
}