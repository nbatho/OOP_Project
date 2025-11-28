package main.java.view;

import main.java.model.Task;
import main.java.model.User;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class KanbanView extends JPanel {
    GlobalStyle style = new GlobalStyle();


    private final int columnWidth;


    private final Map<String, JPanel> kanbanColumns = new HashMap<>();
    private final Map<String, JLabel> columnCountLabels = new HashMap<>();

    private final Map<String, String> statusMapping = new HashMap<>();

    private TaskClickListener taskClickListener;
    public KanbanView() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(style.getCOLOR_BACKGROUND());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        this.columnWidth = GlobalStyle.scale(420);
        initStatusMapping();

        addKanbanColumn("Cần làm", "TODO");
        addRigidArea();
        addKanbanColumn("Đang làm", "IN_PROGRESS");
        addRigidArea();
        addKanbanColumn("Hoàn thành", "DONE");
        addRigidArea();
        addKanbanColumn("Hủy", "CANCELLED");
    }

    private void addRigidArea() {
        add(Box.createRigidArea(new Dimension(16, 0)));
    }

    private void initStatusMapping() {
        statusMapping.put("To do", "Cần làm");
        statusMapping.put("IN_PROGRESS", "Đang làm");
        statusMapping.put("In Progress", "Đang làm");

        statusMapping.put("DONE", "Hoàn thành");
        statusMapping.put("Done", "Hoàn thành");

        statusMapping.put("CANCELLED", "Hủy");
        statusMapping.put("Cancelled", "Hủy");

    }

    private void addKanbanColumn(String title, String statusKey) {
        JPanel columnPanel = new JPanel(new BorderLayout(10, 10));
        columnPanel.setBackground(style.getCOLOR_CARD());

        columnPanel.setBorder(new EmptyBorder(
                GlobalStyle.scale(8),
                GlobalStyle.scale(8),
                GlobalStyle.scale(8),
                GlobalStyle.scale(8)
        ));

        columnPanel.setPreferredSize(new Dimension(columnWidth, 700));
        columnPanel.setMaximumSize(new Dimension(columnWidth, Integer.MAX_VALUE));


        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(true);
        headerPanel.setBackground(getColumnTint(statusKey));
        headerPanel.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(GlobalStyle.scaleFont(style.getFONT_BOLD()));
        titleLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());

        JLabel countLabel = new JLabel("0");
        countLabel.setFont(GlobalStyle.scaleFont(style.getFONT_SMALL()));
        countLabel.setForeground(Color.WHITE);
        countLabel.setOpaque(true);
        countLabel.setBackground(style.getCOLOR_PRIMARY());
        countLabel.setBorder(new EmptyBorder(4,8,4,8));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(countLabel, BorderLayout.EAST);

        columnPanel.add(headerPanel, BorderLayout.NORTH);


        JPanel cardsPanel = new JPanel();
        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int columnTopPadding = GlobalStyle.scale(32);
        cardsPanel.setBorder(new EmptyBorder(columnTopPadding, 0, GlobalStyle.scale(12), 0));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        centerPanel.add(cardsPanel);
        centerPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        columnPanel.add(scrollPane, BorderLayout.CENTER);





        add(columnPanel);

        kanbanColumns.put(statusKey, cardsPanel);

        columnCountLabels.put(statusKey, countLabel);
    }

    public interface TaskClickListener {
        void onTaskClicked(Task task);
    }

    public void setTaskClickListener(TaskClickListener listener) {
        this.taskClickListener = listener;
    }
    public void updateTasks(List<Task> tasks) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> updateTasks(tasks));
            return;
        }

        clearAllTasks();

        for (Task task : tasks) {
            addTaskToBoard(task);
        }
        updateTaskCounts();
    }
    public void addTaskToBoard(Task task) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> addTaskToBoard(task));
            return;
        }

        String status = task.getStatus();

        if (status == null || status.isEmpty()) {
            status = "TODO";
        }

        String columnKey = normalizeStatus(status);

        JPanel taskCard = createTaskCard(task);

        JPanel targetColumn = kanbanColumns.get(columnKey);
        if (targetColumn != null) {
            targetColumn.add(taskCard);
            targetColumn.add(Box.createVerticalStrut(10));
            targetColumn.revalidate();
            targetColumn.repaint();
        } else {
            System.err.println(" Không tìm thấy cột cho status: " + status);
        }
    }


    private String normalizeStatus(String status) {
        if (status == null) return "TODO";
        String normalized = status.trim().toUpperCase().replace(" ", "_");
        switch (normalized) {
            case "TODO":
            case "TO_DO":
                return "TODO";
            case "IN_PROGRESS":
            case "INPROGRESS":
            case "IN-PROGRESS":
                return "IN_PROGRESS";
            case "CANCELLED":
            case "DONE":
            case "FINISHED":
                return "DONE";
            default:
                System.err.println(" Unknown status: " + status + ", defaulting to TODO");
                return "TODO";
        }
    }

    private JPanel createTaskCard(Task task) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(true);
        card.setBackground(style.getCOLOR_CARD());
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0xE0E0E0), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        int cardWidth = Math.max(GlobalStyle.scale(300), columnWidth - GlobalStyle.scale(80));
        int cardHeight = GlobalStyle.scale(150);
        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        card.setMaximumSize(new Dimension(cardWidth, cardHeight));
        card.setMinimumSize(new Dimension(cardWidth, cardHeight));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

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


        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            JLabel descLabel = new JLabel("<html><body style='width:" + (cardWidth - 20) + "px'>" + task.getDescription() + "</body></html>");
            descLabel.setFont(GlobalStyle.scaleFont(style.getFONT_SMALL()));
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
            dueDateLabel.setFont(GlobalStyle.scaleFont(style.getFONT_SMALL()));
            dueDateLabel.setForeground(style.getCOLOR_TEXT_MUTED());
            footerPanel.add(dueDateLabel);
        }

        footerPanel.add(Box.createHorizontalGlue());

        if (task.getAssignedUsers() != null && !task.getAssignedUsers().isEmpty()) {
            List<User> users = task.getAssignedUsers();
            for (User user : users) {
                JLabel avatarLabel = new JLabel(getInitials(user.getFullName()), SwingConstants.CENTER);
                avatarLabel.setFont(GlobalStyle.scaleFont(new Font("Segoe UI", Font.BOLD, 14)));
                avatarLabel.setForeground(Color.WHITE);
                avatarLabel.setOpaque(true);
                avatarLabel.setBackground(style.getCOLOR_PRIMARY());
                avatarLabel.setPreferredSize(new Dimension(GlobalStyle.scale(40), GlobalStyle.scale(40)));
                avatarLabel.setBorder(new EmptyBorder(4,4,4,4));

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
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(0xF5F5F5)); // Hover effect
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(style.getCOLOR_CARD()); // Reset
            }
        });


        return card;
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
        if (initials.isEmpty()) initials = "--";
        return initials;
    }


    private JLabel createPriorityBadge(String priority) {
        JLabel badge = new JLabel(priority);
        badge.setFont(GlobalStyle.scaleFont(style.getFONT_SMALL()));
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

    private void updateTaskCounts() {

        for (Map.Entry<String, JLabel> e : columnCountLabels.entrySet()) {
            String key = e.getKey();
            JLabel countLabel = e.getValue();
            JPanel cardsPanel = kanbanColumns.get(key);
            if (cardsPanel != null) {

                int comps = cardsPanel.getComponentCount();
                int taskCount = (comps + 1) / 2;
                countLabel.setText(String.valueOf(taskCount));

            }
        }
    }

    public void clearAllTasks() {
        // Ensure Swing UI updates happen on EDT
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::clearAllTasks);
            return;
        }

        for (JPanel listPanel : kanbanColumns.values()) {
            listPanel.removeAll();
            listPanel.revalidate();
            listPanel.repaint();
        }
    }

    @Deprecated
    public void addTaskToColumn(String columnName, JComponent taskComponent) {
        JPanel target = kanbanColumns.get(columnName);
        if (target != null) {
            target.add(taskComponent);
            target.revalidate();
            target.repaint();
        }
    }
    public int getColumnWidth() {
        return columnWidth;
    }


    private Color getColumnTint(String statusKey) {
        switch (statusKey) {
            case "TODO":
                return new Color(0xF1F8F9); // light teal/blue
            case "IN_PROGRESS":
                return new Color(0xFFF7F1); // light peach
            case "REVIEW":
                return new Color(0xF6F8F9); // light gray
            case "DONE":
                return new Color(0xF2FBF2); // light green
            default:
                return style.getCOLOR_CARD();
        }
    }
}