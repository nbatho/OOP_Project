package main.java.view;

import com.toedter.calendar.JCalendar;
import main.java.model.Task;
import main.java.model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class CalendarView extends JPanel {

    private final GlobalStyle style = new GlobalStyle();
    private final JCalendar calendar;
    private final JPanel taskListPanel;
    private final JPanel upcomingListPanel;
    private final JLabel selectedDateLabel;

    private TaskClickListener taskClickListener;
    private List<Task> allTasks = new java.util.ArrayList<>(); // Lưu tất cả tasks để lọc khi đổi ngày

    public CalendarView() {
        setLayout(new BorderLayout(10, 10));

        // HEADER
        add(createHeader(), BorderLayout.NORTH);

        // MAIN BODY (calendar + task of day)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // LEFT — JCalendar
        calendar = new JCalendar();
        calendar.setBorder(new TitledBorder("Lịch"));
        calendar.getDayChooser().setAlwaysFireDayProperty(true);
        mainPanel.add(calendar);

        // RIGHT — TaskCard of selected day
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Công việc trong ngày"));

        // Label hiển thị ngày được chọn
        selectedDateLabel = new JLabel();
        selectedDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        selectedDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectedDateLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        selectedDateLabel.setOpaque(true);
        selectedDateLabel.setBackground(new Color(230, 240, 255));
        updateSelectedDateLabel(LocalDate.now());

        rightPanel.add(selectedDateLabel, BorderLayout.NORTH);

        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(Color.WHITE);

        JScrollPane taskScrollPane = new JScrollPane(taskListPanel);
        taskScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        taskScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        taskScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        taskScrollPane.setBorder(null); // Bỏ border của scroll pane

        rightPanel.add(taskScrollPane, BorderLayout.CENTER);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Upcoming section (bottom) - GIỚI HẠN CHIỀU CAO
        JPanel upcomingPanel = new JPanel(new BorderLayout());
        upcomingPanel.setBorder(new TitledBorder("Công việc sắp đến hạn"));
        upcomingPanel.setPreferredSize(new Dimension(0, 250)); // Giới hạn chiều cao 250px
        upcomingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        upcomingListPanel = new JPanel();
        upcomingListPanel.setLayout(new BoxLayout(upcomingListPanel, BoxLayout.Y_AXIS));
        upcomingListPanel.setBackground(Color.WHITE);

        JScrollPane upcomingScrollPane = new JScrollPane(upcomingListPanel);
        upcomingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        upcomingScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        upcomingScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling

        upcomingPanel.add(upcomingScrollPane, BorderLayout.CENTER);
        add(upcomingPanel, BorderLayout.SOUTH);

        // EVENT — pick date
        calendar.addPropertyChangeListener("calendar", evt -> {
            LocalDate selected = convertToLocalDate(calendar.getDate());
            updateSelectedDateLabel(selected);

            // Tự động cập nhật lại tasks khi chọn ngày mới
            if (!allTasks.isEmpty()) {
                updateTasksForSelectedDate(selected);
            }

            if (onDateSelectedListener != null) {
                onDateSelectedListener.onDateSelected(selected);
            }
        });
    }

    private void updateSelectedDateLabel(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy");
        String formattedDate = date.format(formatter);
        formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
        selectedDateLabel.setText("📅 " + formattedDate);
    }

    public void updateTasks(List<Task> tasks) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> updateTasks(tasks));
            return;
        }

        // Lưu lại để dùng khi user chọn ngày khác
        this.allTasks = new java.util.ArrayList<>(tasks);

        // Lấy ngày hiện tại đang được chọn trên calendar
        LocalDate selectedDate = convertToLocalDate(calendar.getDate());
        updateTasksForSelectedDate(selectedDate);
    }

    private void updateTasksForSelectedDate(LocalDate selectedDate) {
        // Lọc tasks của ngày được chọn
        List<Task> tasksOfDay = allTasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> {
                    LocalDate taskDate = convertSqlDateToLocalDate(task.getDueDate());
                    return taskDate.equals(selectedDate);
                })
                .toList();

        // Lọc tasks sắp đến hạn (7 ngày tới, không tính ngày đã chọn)
        LocalDate weekLater = selectedDate.plusDays(7);

        List<Task> upcomingTasks = allTasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> {
                    LocalDate taskDate = convertSqlDateToLocalDate(task.getDueDate());
                    return taskDate.isAfter(selectedDate) && !taskDate.isAfter(weekLater);
                })
                .sorted((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()))
                .toList();

        // Cập nhật UI
        setTasksOfDay(tasksOfDay, null);
        setUpcomingTasks(upcomingTasks, null);
    }

    public void setTasksOfDay(List<Task> tasks, List<User> projectUsers) {
        taskListPanel.removeAll();

        if (tasks.isEmpty()) {
            JLabel emptyLabel = new JLabel("Không có công việc nào trong ngày này");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            taskListPanel.add(emptyLabel);
        } else {
            for (Task task : tasks) {
                JPanel taskCard = createTaskCard(task);
                taskListPanel.add(taskCard);
                taskListPanel.add(Box.createVerticalStrut(10));
            }
        }

        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    public void setUpcomingTasks(List<Task> tasks, List<User> projectUsers) {
        upcomingListPanel.removeAll();

        if (tasks.isEmpty()) {
            JLabel emptyLabel = new JLabel("Không có công việc sắp đến hạn");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            upcomingListPanel.add(emptyLabel);
        } else {
            for (Task task : tasks) {
                JPanel taskCard = createTaskCard(task);
                upcomingListPanel.add(taskCard);
                upcomingListPanel.add(Box.createVerticalStrut(10));
            }
        }

        upcomingListPanel.revalidate();
        upcomingListPanel.repaint();
    }

    private JPanel createTaskCard(Task task) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(true);
        card.setBackground(style.getCOLOR_CARD());
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0xE0E0E0), 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        int cardHeight = 120; // Chiều cao cố định
        card.setPreferredSize(new Dimension(Integer.MAX_VALUE, cardHeight));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
        card.setMinimumSize(new Dimension(0, cardHeight));

        // Header: Title + Priority
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(task.getTitle());
        titleLabel.setFont(style.getFONT_BOLD());
        titleLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalGlue());

        if (task.getPriority() != null && !task.getPriority().isEmpty()) {
            JLabel priorityBadge = createPriorityBadge(task.getPriority());
            headerPanel.add(priorityBadge);
        }

        card.add(headerPanel);
        card.add(Box.createVerticalStrut(8));

        // Description
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            String desc = task.getDescription();
            if (desc.length() > 100) {
                desc = desc.substring(0, 100) + "...";
            }
            JLabel descLabel = new JLabel("<html>" + desc + "</html>");
            descLabel.setFont(style.getFONT_SMALL());
            descLabel.setForeground(style.getCOLOR_TEXT_MUTED());
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(descLabel);
            card.add(Box.createVerticalStrut(8));
        }

        // Footer: Due date + Avatars
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.X_AXIS));
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (task.getDueDate() != null) {
            JLabel dueDateLabel = new JLabel("📅 " + task.getDueDate().toString());
            dueDateLabel.setFont(style.getFONT_SMALL());
            dueDateLabel.setForeground(style.getCOLOR_TEXT_MUTED());
            footerPanel.add(dueDateLabel);
        }

        footerPanel.add(Box.createHorizontalGlue());

        // Avatar người được assign
        if (task.getAssignedUsers() != null && !task.getAssignedUsers().isEmpty()) {
            List<User> users = task.getAssignedUsers();
            for (User user : users) {
                JLabel avatarLabel = new JLabel(getInitials(user.getFullName()), SwingConstants.CENTER);
                avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                avatarLabel.setForeground(Color.WHITE);
                avatarLabel.setOpaque(true);
                avatarLabel.setBackground(style.getCOLOR_PRIMARY());
                avatarLabel.setPreferredSize(new Dimension(32, 32));
                avatarLabel.setBorder(new LineBorder(Color.WHITE, 2));

                footerPanel.add(avatarLabel);
                footerPanel.add(Box.createHorizontalStrut(4));
            }
        }

        card.add(footerPanel);

        // Mouse events
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Gọi cả 2 listeners để tương thích
                if (taskClickListener != null) {
                    taskClickListener.onTaskClicked(task);
                }
                if (taskCardClickListener != null) {
                    taskCardClickListener.onTaskCardClicked(task.getProjectId(), task.getTaskId());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(0xF5F5F5));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(style.getCOLOR_CARD());
            }
        });

        return card;
    }

    private JLabel createPriorityBadge(String priority) {
        JLabel badge = new JLabel(priority);
        badge.setFont(style.getFONT_SMALL());
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));

        String normalizedPriority = priority.toUpperCase().trim();

        switch (normalizedPriority) {
            case "HIGH":
            case "URGENT":
            case "CRITICAL":
                badge.setBackground(new Color(255, 230, 230));
                badge.setForeground(new Color(200, 50, 50));
                badge.setText("Cao");
                break;
            case "MEDIUM":
            case "NORMAL":
            case "MODERATE":
                badge.setBackground(new Color(255, 245, 220));
                badge.setForeground(new Color(200, 140, 50));
                badge.setText("Trung bình");
                break;
            case "LOW":
                badge.setBackground(new Color(230, 245, 230));
                badge.setForeground(new Color(80, 150, 80));
                badge.setText("Thấp");
                break;
            default:
                badge.setBackground(new Color(240, 240, 240));
                badge.setForeground(Color.GRAY);
                badge.setText(priority);
        }

        return badge;
    }


    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "--";
        String cleaned = fullName.trim();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cleaned.length() && sb.length() < 2; i++) {
            char c = cleaned.charAt(i);
            if (!Character.isWhitespace(c)) sb.append(Character.toUpperCase(c));
        }
        String initials = sb.toString();
        if (initials.length() == 0) initials = "--";
        return initials;
    }

    public interface OnDateSelectedListener {
        void onDateSelected(LocalDate date);
    }

    public interface TaskClickListener {
        void onTaskClicked(Task task);
    }

    public interface TaskCardClickListener {
        void onTaskCardClicked(String projectId, String taskId);
    }

    private OnDateSelectedListener onDateSelectedListener;
    private TaskCardClickListener taskCardClickListener;

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.onDateSelectedListener = listener;
    }

    public void setTaskClickListener(TaskClickListener listener) {
        this.taskClickListener = listener;
    }

    public void setTaskCardClickListener(TaskCardClickListener listener) {
        this.taskCardClickListener = listener;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDate convertSqlDateToLocalDate(java.util.Date date) {
        if (date == null) return null;

        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Lịch công việc");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel sub = new JLabel("Theo dõi công việc theo deadline");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(title);
        textPanel.add(sub);

        header.add(textPanel, BorderLayout.WEST);
        return header;
    }
}