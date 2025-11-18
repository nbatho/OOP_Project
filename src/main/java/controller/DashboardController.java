package main.java.controller;

import main.java.view.CalendarView;
import main.java.view.DashboardView;
import main.java.view.KanbanView;
import main.java.view.TableView;

import javax.swing.*;
import java.awt.*;

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
        view.mainContentPanel.add(kanbanView, "KANBAN");
        view.mainContentPanel.add(tableView, "TABLE");
        view.mainContentPanel.add(calendarView, "CALENDAR");

        // Lắng nghe các nút chuyển view
        view.kanbanButton.addActionListener(e -> showView("KANBAN"));
        view.tableButton.addActionListener(e -> showView("TABLE"));
        view.calendarButton.addActionListener(e -> showView("CALENDAR"));
    }

    private void showView(String name) {
        view.cardLayout.show(view.mainContentPanel, name);
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel p = new JPanel();
        p.add(new JLabel(text));
        return p;
    }

    public KanbanView getKanbanView() {
        return kanbanView;
    }

}
