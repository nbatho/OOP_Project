package main.java.controller;

import main.java.model.User;
import main.java.service.UserService;
import main.java.service.impl.UserServiceImpl;
import main.java.view.DashboardView;
import main.java.view.LoginView;

public class DashboardController {
    private final DashboardView view;
    private final UserService userService;
    private TaskController taskController;

    public DashboardController(DashboardView view) {
        this.userService = UserServiceImpl.getInstance();
        this.view = view;
        initUserInfo();
        setupControllers();
        initMainNavigation();
        this.view.setVisible(true);
    }

    private void initUserInfo() {
        try {
            User user = userService.getCurrentUser();
            if (user != null) view.setUserInitials(user.getFullName());
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void setupControllers() {
        ProjectController projectController = new ProjectController(view, (newProjectId) -> {
            if (taskController != null) {
                taskController.loadProjectTasks(newProjectId);
            }
        });
        taskController = new TaskController(view, projectController);
        projectController.loadProjectList();
    }

    private void initMainNavigation() {
        view.getKanbanButton().addActionListener(e -> showView("KANBAN"));
        view.getCalendarButton().addActionListener(e -> showView("CALENDAR"));
        view.getLogoutMenuItem().addActionListener(e -> handleLogout());
    }

    private void showView(String name) {
        view.getCardLayout().show(view.getMainContentPanel(), name);
    }

    private void handleLogout() {
        if (view.showConfirmDialog("Bạn có chắc muốn đăng xuất?")) {
            view.dispose();
            LoginView loginView = new LoginView();
            new LoginController(loginView);
        }
    }
}