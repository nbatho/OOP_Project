package main.java.controller;

import main.java.component.TaskCard;
import main.java.model.Task;
import main.java.model.User;
import main.java.service.impl.ProjectMemberServiceImpl;
import main.java.service.impl.TaskServiceImpl;
import main.java.service.impl.UserServiceImpl;
import main.java.view.CalendarView;
import main.java.view.DashboardView;
import main.java.view.KanbanView;

import java.util.Date;
import java.util.List;

public class TaskController {
    private final DashboardView mainView;
    private final KanbanView kanbanView;
    private final CalendarView calendarView;
    private final TaskServiceImpl taskService = new TaskServiceImpl();
    private final ProjectMemberServiceImpl projectMemberService = ProjectMemberServiceImpl.getInstance();
    private final UserServiceImpl userService = UserServiceImpl.getInstance();
    private final ProjectController projectController;

    public TaskController(DashboardView mainView, ProjectController projectController) {
        this.mainView = mainView;
        this.projectController = projectController;
        this.kanbanView = mainView.getKanbanView();
        this.calendarView = new CalendarView();
        this.mainView.getMainContentPanel().add(calendarView, "CALENDAR");

        initListeners();
    }

    private void initListeners() {
        mainView.getCreateTaskButton().addActionListener(e -> handleCreateTask());
        kanbanView.setTaskClickListener(this::onTaskClicked);
        calendarView.setTaskClickListener(this::onTaskClicked);
    }
    public void loadProjectTasks(String projectId) {
        try {
            List<Task> listTasks = taskService.getTasksWithAssignees(projectId);
            kanbanView.updateTasks(listTasks);
            calendarView.updateTasks(listTasks);
        } catch (Exception e) {
            mainView.showErrorMessage("Không thể tải danh sách task: " + e.getMessage());
        }
    }

    private void handleCreateTask() {
        String currentProjectId = projectController.getCurrentProjectId();
        List<User> currentMembers = projectController.getCurrentProjectMembers();

        if (currentProjectId == null) {
            mainView.showErrorMessage("Vui lòng chọn dự án trước!");
            return;
        }
        TaskCard card = new TaskCard(currentProjectId, currentMembers, false);

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
                if (startDate != null) {
                    newTask.setStartDate(new java.sql.Date(startDate.getTime()));
                }
                if (endDate != null) {
                    newTask.setDueDate(new java.sql.Date(endDate.getTime()));
                }

                if (!assignees.isEmpty()) {
                    taskService.createTask(newTask, assignees);
                }

                loadProjectTasks(currentProjectId);
                card.showSuccessMessage("Tạo task thành công");
                card.dispose();
            } catch (Exception ex) {
                card.showErrorMessage("Không thể tạo task: " + ex.getMessage());
            }
        });
    }

    private void onTaskClicked(Task task) {
        try {
            List<User> memberList = projectMemberService.getProjectMember(task.getProjectId());

            TaskCard taskCard = new TaskCard(task.getProjectId(), memberList, true);
            taskCard.setTaskData(task);
            new CommentController(mainView, taskCard, task);
            System.out.println(task);
            taskCard.getBtnSave().addActionListener(e -> handleUpdateTask(taskCard, task));
            taskCard.getBtnDelete().addActionListener(e -> handleDeleteTask(taskCard, task));
        } catch (Exception ex) {
            mainView.showErrorMessage("Không thể mở task: " + ex.getMessage());
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

            Date startDate = card.getStartDateChooser().getDate();
            Date endDate = card.getEndDateChooser().getDate();

            Task updatedTask = new Task();
            updatedTask.setTaskId(oldTask.getTaskId());
            updatedTask.setProjectId(oldTask.getProjectId());
            updatedTask.setTitle(title);
            updatedTask.setDescription(description);
            updatedTask.setPriority(priority);
            updatedTask.setStatus(status);
            if (startDate != null) {
                updatedTask.setStartDate(new java.sql.Date(startDate.getTime()));
            }
            if (endDate != null) {
                updatedTask.setDueDate(new java.sql.Date(endDate.getTime()));
            }

            taskService.updateTask(updatedTask, userAssignees);
            loadProjectTasks(updatedTask.getProjectId());
            card.showSuccessMessage("Cập nhật task thành công!");
            card.dispose();
        } catch (Exception ex) {
            card.showErrorMessage("Không thể cập nhật task: " + ex.getMessage());
        }
    }
    private void handleDeleteTask(TaskCard card, Task task) {
        String currentProjectId = projectController.getCurrentProjectId();
        if (currentProjectId == null) return;
        if (!card.showConfirmDialog("Bạn có chắc muốn xóa task này?")) {
            return;
        }
        try {
            taskService.deleteTask(task.getTaskId(), currentProjectId);
            loadProjectTasks(currentProjectId);
            card.showSuccessMessage("Xóa task thành công!");
            card.dispose();
        } catch (Exception ex) {
            card.showErrorMessage("Không thể xóa task: " + ex.getMessage());
        }
    }
}