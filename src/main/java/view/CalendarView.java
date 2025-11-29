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
import main.java.Utility.Helper;
import java.util.List;
public class CalendarView extends JPanel {
    private final GlobalStyle style = new GlobalStyle();
    private final JCalendar calendar;
    private final JPanel taskListPanel;
    private final JPanel upcomingListPanel;
    private final JLabel selectedDateLabel;
    private OnDateSelectedListener onDateSelectedListener;
    private TaskCardClickListener taskCardClickListener;
    private final Helper helper;
    private TaskClickListener taskClickListener;
    private List<Task> allTasks = new java.util.ArrayList<>();

    public CalendarView() {
        this.helper = new Helper();
        setLayout(new BorderLayout(10, 10));
        add(createHeader(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        calendar = new JCalendar();
        calendar.setBorder(new TitledBorder("Lịch"));
        calendar.getDayChooser().setAlwaysFireDayProperty(true);
        mainPanel.add(calendar);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Công việc trong ngày"));
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
        taskScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        taskScrollPane.setBorder(null);
        rightPanel.add(taskScrollPane, BorderLayout.CENTER);

        mainPanel.add(rightPanel);
        add(mainPanel, BorderLayout.CENTER);

        JPanel upcomingPanel = new JPanel(new BorderLayout());
        upcomingPanel.setBorder(new TitledBorder("Công việc sắp đến hạn"));
        upcomingPanel.setPreferredSize(new Dimension(0, 250));
        upcomingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        upcomingListPanel = new JPanel();
        upcomingListPanel.setLayout(new BoxLayout(upcomingListPanel, BoxLayout.Y_AXIS));
        upcomingListPanel.setBackground(Color.WHITE);

        JScrollPane upcomingScrollPane = new JScrollPane(upcomingListPanel);
        upcomingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        upcomingScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        upcomingScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        upcomingPanel.add(upcomingScrollPane, BorderLayout.CENTER);
        add(upcomingPanel, BorderLayout.SOUTH);

        calendar.addPropertyChangeListener("calendar", evt -> {
            LocalDate selected = helper.convertToLocalDate(calendar.getDate());
            updateSelectedDateLabel(selected);
            if (!allTasks.isEmpty()) {
                updateTasksForSelectedDate(selected);
            }
            if (onDateSelectedListener != null) {
                onDateSelectedListener.onDateSelected(selected);
            }
        });
    }

    private void updateSelectedDateLabel(LocalDate date) {
        String formattedDate = helper.formatDateFull(date);
        selectedDateLabel.setText(formattedDate);
    }

    public void updateTasks(List<Task> tasks) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> updateTasks(tasks));
            return;
        }
        this.allTasks = new java.util.ArrayList<>(tasks);
        LocalDate selectedDate = helper.convertToLocalDate(calendar.getDate());
        updateTasksForSelectedDate(selectedDate);
    }

    private void updateTasksForSelectedDate(LocalDate selectedDate) {
        List<Task> tasksOfDay = allTasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> {
                    LocalDate taskDate = helper.convertSqlDateToLocalDate(task.getDueDate());
                    return taskDate.equals(selectedDate);
                })
                .toList();

        LocalDate weekLater = selectedDate.plusDays(7);

        List<Task> upcomingTasks = allTasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> {
                    LocalDate taskDate = helper.convertSqlDateToLocalDate(task.getDueDate());
                    return taskDate.isAfter(selectedDate) && !taskDate.isAfter(weekLater);
                })
                .sorted((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()))
                .toList();


        setTasksOfDay(tasksOfDay);
        setUpcomingTasks(upcomingTasks);
    }

    public void setTasksOfDay(List<Task> tasks) {
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

    public void setUpcomingTasks(List<Task> tasks) {
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

        int cardHeight = 120;
        card.setPreferredSize(new Dimension(Integer.MAX_VALUE, cardHeight));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
        card.setMinimumSize(new Dimension(0, cardHeight));

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
            JLabel priorityBadge = helper.createPriorityBadge(task.getPriority());
            headerPanel.add(priorityBadge);
        }

        card.add(headerPanel);
        card.add(Box.createVerticalStrut(8));

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

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.X_AXIS));
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (task.getDueDate() != null) {
            JLabel dueDateLabel = new JLabel(task.getDueDate().toString());
            dueDateLabel.setFont(style.getFONT_SMALL());
            dueDateLabel.setForeground(style.getCOLOR_TEXT_MUTED());
            footerPanel.add(dueDateLabel);
        }

        footerPanel.add(Box.createHorizontalGlue());

        if (task.getAssignedUsers() != null && !task.getAssignedUsers().isEmpty()) {
            List<User> users = task.getAssignedUsers();
            for (User user : users) {
                JLabel avatarLabel = new JLabel(helper.getInitials(user.getFullName()), SwingConstants.CENTER);
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

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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

    public interface OnDateSelectedListener { void onDateSelected(LocalDate date); }

    public interface TaskClickListener { void onTaskClicked(Task task); }

    public interface TaskCardClickListener { void onTaskCardClicked(String projectId, String taskId); }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) { this.onDateSelectedListener = listener; }

    public void setTaskClickListener(TaskClickListener listener) { this.taskClickListener = listener; }

    public void setTaskCardClickListener(TaskCardClickListener listener) { this.taskCardClickListener = listener; }



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