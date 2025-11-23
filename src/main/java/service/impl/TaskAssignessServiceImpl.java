package main.java.service.impl;

import java.util.List;
import main.java.model.TaskAsigness;
import main.java.service.TaskAssignessService;
import main.java.service.UserService;

public class TaskAssignessServiceImpl implements TaskAssignessService {
    private final UserService userService;

    public TaskAssignessServiceImpl() {
        this.userService = new UserServiceImpl();
    }

    @Override
    public boolean create(TaskAsigness taskAsigness) {
        try {
            if (taskAsigness == null) {
                System.out.println("TaskAsigness không được null");
                return false;
            }

            if (!isValidTaskAssignmentData(taskAsigness)) {
                return false;
            }

            // Kiểm tra task có tồn tại không (cần cả taskId và projectId)
            // Placeholder: cần lấy projectId từ task
            System.out.println("Kiểm tra task tồn tại: " + taskAsigness.getTaskId());

            // Kiểm tra user có tồn tại không
            if (!userService.userExists(taskAsigness.getUserId())) {
                System.out.println("User không tồn tại: " + taskAsigness.getUserId());
                return false;
            }

            // Kiểm tra user đã được assign task chưa
            TaskAsigness existing = getByTaskIdAndUserId(taskAsigness.getTaskId(), taskAsigness.getUserId());
            if (existing != null) {
                System.out.println("User đã được assign task này");
                return false;
            }

            // return taskAssignmentRepository.createTaskAssignment(taskAsigness);
            System.out.println("Assign task thành công: " + taskAsigness.getTaskId() + " -> " + taskAsigness.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi assign task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<TaskAsigness> getAll() {
        try {
            // return taskAssignmentRepository.getAllTaskAssignments();
            System.out.println("Lấy danh sách tất cả task assignments");
            return List.of(); // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách task assignments: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public TaskAsigness getByTaskIdAndUserId(String task_id, String user_id) {
        try {
            if (task_id == null || task_id.trim().isEmpty()) {
                System.out.println("Task ID không được null hoặc rỗng");
                return null;
            }

            if (user_id == null || user_id.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return null;
            }

            // return taskAssignmentRepository.getByTaskAndUser(task_id, user_id);
            System.out.println("Lấy task assignment: " + task_id + " -> " + user_id);
            return null; // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy task assignment: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TaskAsigness> getByTaskId(String task_id) {
        try {
            if (task_id == null || task_id.trim().isEmpty()) {
                System.out.println("Task ID không được null hoặc rỗng");
                return List.of();
            }

            // return taskAssignmentRepository.getAssignmentsByTask(task_id);
            System.out.println("Lấy assignments theo task: " + task_id);
            return List.of(); // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy assignments của task: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<TaskAsigness> getByUserId(String user_id) {
        try {
            if (user_id == null || user_id.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return List.of();
            }

            // return taskAssignmentRepository.getAssignmentsByUser(user_id);
            System.out.println("Lấy assignments theo user: " + user_id);
            return List.of(); // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy assignments của user: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean deleteByTaskIdAndUserId(String task_id, String user_id) {
        try {
            if (getByTaskIdAndUserId(task_id, user_id) == null) {
                System.out.println("Task assignment không tồn tại");
                return false;
            }

            // return taskAssignmentRepository.deleteByTaskAndUser(task_id, user_id);
            System.out.println("Xóa task assignment thành công");
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa task assignment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByTaskId(String task_id) {
        try {
            if (task_id == null || task_id.trim().isEmpty()) {
                return false;
            }

            // return taskAssignmentRepository.deleteByTask(task_id);
            System.out.println("Xóa tất cả assignments của task: " + task_id);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa assignments của task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByUserId(String user_id) {
        try {
            if (user_id == null || user_id.trim().isEmpty()) {
                return false;
            }

            // return taskAssignmentRepository.deleteByUser(user_id);
            System.out.println("Xóa tất cả assignments của user: " + user_id);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa assignments của user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate dữ liệu task assignment
     */
    private boolean isValidTaskAssignmentData(TaskAsigness assignment) {
        if (assignment.getTaskId() == null || assignment.getTaskId().trim().isEmpty()) {
            System.out.println("Task ID không được null hoặc rỗng");
            return false;
        }

        if (assignment.getUserId() == null || assignment.getUserId().trim().isEmpty()) {
            System.out.println("User ID không được null hoặc rỗng");
            return false;
        }

        return true;
    }
}
