package main.java.controller;

import main.java.view.*;

import javax.swing.*;

public class DashboardController {
    private DashboardView view;
    private KanbanView kanbanView;
    private TableView tableView;
    private CalendarView calendarView;

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
    }
    
    private void handleProjectSelected(String projectName) {
        // Cập nhật header với tên dự án
        view.setCurrentProjectName(projectName);
        
        // Lấy thông tin dự án (có thể từ database)
        String projectInfo = getProjectInfo(projectName);
        
        // Cập nhật sidebar
        view.updateSidebarProjectInfo(projectInfo);
    }
    
    private String getProjectInfo(String projectName) {
        // TODO: Lấy thông tin từ database/service
        // Tạm thời trả về dữ liệu mẫu
        return "Tên: " + projectName + "\n" +
               "Mô tả: Đây là một dự án quản lý công việc\n" +
               "Ngày tạo: 01/01/2024\n" +
               "Trạng thái: Hoạt động";
    }

    private void handleCreateProject() {
        ProjectCardView projectCardView = new ProjectCardView();
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

            // TODO: Lưu dự án vào database/model
            System.out.println("Project created: " + title);

            // Cập nhật dashboard với dự án mới tạo
            view.setCurrentProjectName(title);
            String projectInfo = "Tên: " + title + "\n" +
                                "Mô tả: " + description + "\n" +
                                "Ngày tạo: " + java.time.LocalDate.now() + "\n" +
                                "Trạng thái: Hoạt động";
            view.updateSidebarProjectInfo(projectInfo);

            projectCardView.dispose();
        });
    }

    private void handleSearch() {
        System.out.println("Dang Tim kiem");
    }
    private void handleShowCard() {

        TaskCardView taskCardView = new TaskCardView();
        // Có thể thêm listener cho nút Save nếu cần
        taskCardView.getBtnSave().addActionListener(e -> {
            // Xử lý lưu task
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