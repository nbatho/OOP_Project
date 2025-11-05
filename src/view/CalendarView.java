package view;

import com.toedter.calendar.JCalendar;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import controller.CalendarController;
import model.Task;

public class CalendarView extends JPanel {
    private JList<String> taskList;
    private JList<String> upcomingList;
    private JLabel dateLabel;
    private LocalDate selectedDate = LocalDate.now();
    private CalendarController controller;
    private List<Task> tasks = new ArrayList<>();

    public CalendarView() {
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createUpcomingPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Lịch công việc", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel desc = new JLabel("Quản lý thời gian và deadline cho các công việc", SwingConstants.LEFT);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(title);
        textPanel.add(desc);
        header.add(textPanel, BorderLayout.WEST);

        return header;
    }


    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Bên trái: Lịch
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new TitledBorder("Chọn ngày"));

        JCalendar calendar = new JCalendar();
        calendar.setWeekOfYearVisible(false); // ✅ Ẩn cột số tuần (18,19,...)
        calendar.addPropertyChangeListener("calendar", evt -> {
            Calendar cal = (Calendar) evt.getNewValue();
            if (cal != null) {
                selectedDate = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                updateTaskList();
                if (controller != null) controller.onDateSelected(selectedDate);
            }
        });

        leftPanel.add(calendar, BorderLayout.CENTER);
        panel.add(leftPanel);

        // Bên phải: Danh sách công việc theo ngày
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Công việc trong ngày"));

        dateLabel = new JLabel("Công việc ngày " + selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rightPanel.add(dateLabel, BorderLayout.NORTH);

        taskList = new JList<>();
        JScrollPane scroll = new JScrollPane(taskList);
        rightPanel.add(scroll, BorderLayout.CENTER);

        panel.add(rightPanel);
        return panel;
    }


    private JPanel createUpcomingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Công việc sắp đến hạn"));

        upcomingList = new JList<>();
        JScrollPane scroll = new JScrollPane(upcomingList);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }


    private void updateTaskList() {
        List<String> todayTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getDeadline() != null && t.getDeadline().isEqual(selectedDate)) {
                todayTasks.add(t.getTitle() + " - " + t.getStatus());
            }
        }

        if (todayTasks.isEmpty()) {
            todayTasks.add("Không có công việc nào trong ngày này");
        }

        taskList.setListData(todayTasks.toArray(new String[0]));
        dateLabel.setText("Công việc ngày " + selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }


    public void setController(CalendarController controller) {
        this.controller = controller;
    }

    public void updateTaskList(List<Task> tasks, LocalDate date) {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.getTitle() + " - " + t.getStatus() + " (" + t.getPriority() + ")");
        }
        if (lines.isEmpty()) lines.add("Không có công việc trong ngày này");

        taskList.setListData(lines.toArray(new String[0]));
        dateLabel.setText("Công việc ngày " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }


    public void updateUpcomingList(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.getTitle() + " - hạn: " + t.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM")));
        }
        if (lines.isEmpty()) lines.add("Không có công việc nào sắp đến hạn");
        upcomingList.setListData(lines.toArray(new String[0]));
    }
}
