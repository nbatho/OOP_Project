package main.java.service.impl;

import main.java.model.Task;
import main.java.model.TaskAssignees;
import main.java.model.User;
import main.java.repository.TaskRepository;
import main.java.service.TaskService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskAssigneesServiceImpl taskAssigneesService;
    private final UserServiceImpl  userService;
    private final String[] VALID_STATUSES = {"TODO", "IN_PROGRESS", "DONE", "CANCELLED"};
    private final String[] VALID_PRIORITIES = {"HIGH", "MEDIUM", "LOW"};

    public TaskServiceImpl() {
        this.taskRepository = new TaskRepository();
        this.taskAssigneesService = new TaskAssigneesServiceImpl();
        this.userService = new UserServiceImpl();
    }

    @Override
    public boolean createTask(Task task, List<User> assignees) {
        try {
            if (task == null) {
                System.out.println("Task không được null");
                return false;
            }
            if (assignees.isEmpty()) {
                System.out.println("assignees không được rỗng");
                return false;
            }
            if (!isValidTaskData(task)) {
                return false;
            }
            if (task.getTaskId() == null || task.getTaskId().trim().isEmpty()) {
                task.setTaskId(UUID.randomUUID().toString());
            }
            if (task.getProjectId() == null) {
                System.out.println("ProjectId không được null");
                return false;
            }
            if (taskRepository.findByTaskId(task.getTaskId(), task.getProjectId()) != null) {
                System.out.println("Task ID đã tồn tại: " + task.getTaskId());
                return false;
            }

            // Set default values
            if (task.getStatus() == null) task.setStatus("Todo");
            if (task.getPriority() == null) task.setPriority("Normal");

            // 1. Tạo task
            boolean created = taskRepository.createTask(task);
            if (!created) {
                System.out.println("Không thể tạo task");
                return false;
            }

            // 2. Tạo assignee

            boolean assigned = false;
            for (User assignee : assignees) {
                taskAssigneesService.create(new TaskAssignees(task.getTaskId(), assignee.getUserId()));
                assigned = true;
            }
            if (!assigned) {
                taskRepository.deleteByTaskId(task.getTaskId(), task.getProjectId());
                System.out.println("Không thể lưu assignee, task đã rollback");
                return false;
            }

            // 3. Nếu cả hai đều thành công
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi tạo task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Task> getTasksByProjectId(String projectId) {
        try {
            if (projectId == null || projectId.trim().isEmpty()) {
                System.out.println("Project ID không được null hoặc rỗng");
                return List.of();
            }

            return taskRepository.findAll(projectId);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy tasks theo project ID: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<Task> getTasksWithAssignees(String projectId) {
        try {
            if (projectId == null || projectId.trim().isEmpty()) {
                System.out.println("Project ID không được null hoặc rỗng");
                return List.of();
            }

            List<Task> listTasks = getTasksByProjectId(projectId);

            for (Task task : listTasks) {
                List<TaskAssignees> taskAssignees = taskAssigneesService.getByTaskId(task.getTaskId());
                List<User> listAssignees = new ArrayList<>();

                for (TaskAssignees taskAssigned : taskAssignees) {
                    User user = userService.getUserById(taskAssigned.getUserId());
                    if (user != null) {
                        listAssignees.add(user);
                    }
                }
                task.setAssignedUsers(listAssignees);
            }

            return listTasks;

        } catch (Exception e) {
            System.out.println("Lỗi khi lấy tasks với assignees: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Task getTaskById(String taskId, String projectId) {
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                System.out.println("Task ID không được null hoặc rỗng");
                return null;
            }

            if (projectId == null || projectId.trim().isEmpty()) {
                System.out.println("Project ID không được null hoặc rỗng");
                return null;
            }

            return taskRepository.findByTaskId(taskId, projectId);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy task theo ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateTask(Task task,List<User> newAssignees) {
        try {
            if (task == null) {
                System.out.println("Task không được null");
                return false;
            }

            if (!isValidTaskData(task)) {
                return false;
            }

            // Kiểm tra task có tồn tại không
            if (!taskExists(task.getTaskId(), task.getProjectId())) {
                System.out.println("Task không tồn tại");
                return false;
            }
            taskAssigneesService.deleteByTaskId(task.getTaskId());
            if (newAssignees != null && !newAssignees.isEmpty()) {
                for (User user : newAssignees) {
                    TaskAssignees ta = new TaskAssignees(task.getTaskId(), user.getUserId());
                    boolean assigned = taskAssigneesService.create(ta);
                    if (!assigned) {
                        throw new SQLException("Lỗi khi thêm assignee: " + user.getFullName());
                    }
                }
            }

            return taskRepository.updateTask(
                task.getTaskId(),
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate()
            );
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateTaskStatus(String taskId, String projectId, String status) {
        try {
            if (!isValidStatus(status)) {
                System.out.println("Status không hợp lệ: " + status);
                return false;
            }

            if (!taskExists(taskId, projectId)) {
                System.out.println("Task không tồn tại");
                return false;
            }

            return taskRepository.updateTaskStatus(taskId, projectId, status);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật status task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateTaskPriority(String taskId, String projectId, String priority) {
        try {
            if (!isValidPriority(priority)) {
                System.out.println("Priority không hợp lệ: " + priority);
                return false;
            }

            if (!taskExists(taskId, projectId)) {
                System.out.println("Task không tồn tại");
                return false;
            }

            return taskRepository.updateTaskPriority(taskId, projectId, priority);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật priority task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateTaskDueDate(String taskId, String projectId, Date dueDate) {
        try {
            if (!taskExists(taskId, projectId)) {
                System.out.println("Task không tồn tại");
                return false;
            }

            return taskRepository.updateTaskDueDate(taskId, projectId, dueDate);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật due date task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteTask(String taskId, String projectId) {
        try {
            if (!taskExists(taskId, projectId)) {
                System.out.println("Task không tồn tại");
                return false;
            }
            return taskRepository.deleteByTaskId(taskId, projectId);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean taskExists(String taskId, String projectId) {
        try {
            Task task = getTaskById(taskId, projectId);
            return task != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra task tồn tại: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isValidStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }

        for (String validStatus : VALID_STATUSES) {
            if (validStatus.equalsIgnoreCase(status.trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isValidPriority(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            return false;
        }

        for (String validPriority : VALID_PRIORITIES) {
            if (validPriority.equalsIgnoreCase(priority.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate dữ liệu task
     */
    private boolean isValidTaskData(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            System.out.println("Title task không được null hoặc rỗng");
            return false;
        }

        if (task.getTitle().length() > 255) {
            System.out.println("Title task quá dài (tối đa 255 ký tự)");
            return false;
        }

        if (task.getProjectId() == null || task.getProjectId().trim().isEmpty()) {
            System.out.println("Project ID không được null hoặc rỗng");
            return false;
        }

        if (task.getStatus() != null && !isValidStatus(task.getStatus())) {
            System.out.println("Status không hợp lệ: " + task.getStatus());
            return false;
        }

        if (task.getPriority() != null && !isValidPriority(task.getPriority())) {
            System.out.println("Priority không hợp lệ: " + task.getPriority());
            return false;
        }

        return true;
    }
}
