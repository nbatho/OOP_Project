package main.java.view;

import main.java.model.Task;
import main.java.model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KanbanView extends JPanel {
    GlobalStyle style = new GlobalStyle();

    // desired column width (scaled)
    private final int columnWidth;

    // Lưu cột Kanban
    private Map<String, JPanel> kanbanColumns = new HashMap<>();
    // Lưu label đếm task cho mỗi cột để cập nhật dễ dàng
    private Map<String, JLabel> columnCountLabels = new HashMap<>();
    // Lưu nút Tạo mới cho mỗi cột
    private Map<String, JButton> columnCreateButtons = new HashMap<>();

    // Mapping status từ database sang tên cột hiển thị
    private Map<String, String> statusMapping = new HashMap<>();

    public KanbanView() {
        // Use horizontal BoxLayout so columns can have fixed preferred widths
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(style.getCOLOR_BACKGROUND());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        this.columnWidth = GlobalStyle.scale(420);

        // Khởi tạo mapping
        initStatusMapping();

        // Tạo các cột
        addKanbanColumn("Cần làm", "TODO");
        addRigidArea();
        addKanbanColumn("Đang làm", "IN_PROGRESS");
        addRigidArea();
        addKanbanColumn("Đang review", "REVIEW");
        addRigidArea();
        addKanbanColumn("Hoàn thành", "DONE");
    }

    private void addRigidArea() {
        add(Box.createRigidArea(new Dimension(16, 0)));
    }

    private void initStatusMapping() {
        statusMapping.put("TODO", "Cần làm");
        statusMapping.put("To Do", "Cần làm");

        statusMapping.put("IN_PROGRESS", "Đang làm");
        statusMapping.put("In Progress", "Đang làm");

        statusMapping.put("REVIEW", "Đang review");
        statusMapping.put("Review", "Đang review");

        statusMapping.put("DONE", "Hoàn thành");
        statusMapping.put("Done", "Hoàn thành");
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


        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        JButton createBtn = new JButton("Tạo mới");
        createBtn.setFont(GlobalStyle.scaleFont(style.getFONT_NORMAL()));
        createBtn.setBackground(style.getCOLOR_PRIMARY());
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        footer.add(createBtn);
        columnPanel.add(footer, BorderLayout.SOUTH);


        columnCreateButtons.put(statusKey, createBtn);

        add(columnPanel);

        kanbanColumns.put(statusKey, cardsPanel);

        columnCountLabels.put(statusKey, countLabel);
    }

    /**
     * Cập nhật tất cả tasks vào Kanban board
     */
    public void updateTasks(List<Task> tasks) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> updateTasks(tasks));
            return;
        }

        clearAllTasks();

        // Thêm tasks mới vào các cột tương ứng
        for (Task task : tasks) {
            addTaskToBoard(task);
        }

        // Cập nhật số lượng task cho mỗi cột
        updateTaskCounts();
    }

    /**
     * Thêm một task vào board
     */
    public void addTaskToBoard(Task task) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> addTaskToBoard(task));
            return;
        }

        String status = task.getStatus();

        // Normalize status
        if (status == null || status.isEmpty()) {
            status = "TODO";
        }

        // Map status sang key của column
        String columnKey = normalizeStatus(status);

        JPanel taskCard = createTaskCard(task);

        JPanel targetColumn = kanbanColumns.get(columnKey);
        if (targetColumn != null) {
            targetColumn.add(taskCard);
            targetColumn.add(Box.createVerticalStrut(10));
            targetColumn.revalidate();
            targetColumn.repaint();
//            System.out.println("Kanban: added task '" + task.getTitle() + "' to column " + columnKey + ". total comps=" + targetColumn.getComponentCount());
        } else {
            System.err.println(" Không tìm thấy cột cho status: " + status);
        }
    }

    /**
     * Chuẩn hóa status về format key của kanbanColumns
     */
    private String normalizeStatus(String status) {
        if (status == null) return "TODO";

        // Convert to uppercase và thay space bằng underscore
        String normalized = status.trim().toUpperCase().replace(" ", "_");

        // Map các format khác nhau về standard format
        switch (normalized) {
            case "TODO":
            case "TO_DO":
                return "TODO";
            case "IN_PROGRESS":
            case "INPROGRESS":
            case "IN-PROGRESS":
                return "IN_PROGRESS";
            case "REVIEW":
            case "IN_REVIEW":
                return "REVIEW";
            case "DONE":
            case "COMPLETED":
            case "FINISHED":
                return "DONE";
            default:
                System.err.println(" Unknown status: " + status + ", defaulting to TODO");
                return "TODO";
        }
    }

    /**
     * Tạo task card component
     */
    private JPanel createTaskCard(Task task) {
        // Card chính
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
        headerPanel.setOpaque(false); // thừa hưởng background từ card
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
                avatarLabel.setFont(GlobalStyle.scaleFont(new Font("Segoe UI", Font.BOLD, 18)));
                avatarLabel.setForeground(Color.WHITE);
                avatarLabel.setOpaque(true);
                avatarLabel.setBackground(style.getCOLOR_PRIMARY());
                avatarLabel.setPreferredSize(new Dimension(GlobalStyle.scale(32), GlobalStyle.scale(32)));
                avatarLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
                avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
                avatarLabel.setVerticalAlignment(SwingConstants.CENTER);

                footerPanel.add(avatarLabel);
                footerPanel.add(Box.createHorizontalStrut(4)); // khoảng cách giữa các avatar
            }
        }

        card.add(footerPanel);
        return card;
    }
    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "--";
        String[] parts = fullName.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty() && sb.length() < 2) {
                sb.append(Character.toUpperCase(part.charAt(0)));
            }
        }

        if (sb.length() < 2 && parts[0].length() > 1) {
            sb.append(Character.toUpperCase(parts[0].charAt(1)));
        }

        return sb.length() > 0 ? sb.toString() : "--";
    }


    /**
     * Tạo priority badge
     */
    private JLabel createPriorityBadge(String priority) {
        JLabel badge = new JLabel(priority);
        badge.setFont(GlobalStyle.scaleFont(style.getFONT_SMALL()));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));

        // Normalize priority (case-insensitive)
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
                badge.setText(priority); // Giữ nguyên text nếu không match
        }

        return badge;
    }

    /**
     * Cập nhật số lượng task ở header mỗi cột
     */
    private void updateTaskCounts() {
        // Use our stored maps to compute counts reliably
        for (Map.Entry<String, JLabel> e : columnCountLabels.entrySet()) {
            String key = e.getKey();
            JLabel countLabel = e.getValue();
            JPanel cardsPanel = kanbanColumns.get(key);
            if (cardsPanel != null) {
                // Each card is followed by a vertical strut in addTaskToBoard, so compute accordingly
                int comps = cardsPanel.getComponentCount();
                int taskCount = (comps + 1) / 2; // safe even if no struts
                countLabel.setText(String.valueOf(taskCount));

            }
        }
    }

    /**
     * Xử lý khi click vào task
     */
    private void onTaskClicked(Task task) {
        // TODO: Mở dialog xem/chỉnh sửa chi tiết task
        System.out.println("Clicked task: " + task.getTitle());

        // Có thể trigger event để Controller xử lý
        // if (taskClickListener != null) {
        //     taskClickListener.onTaskClicked(task);
        // }
    }

    /**
     * Xóa tất cả tasks
     */
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

    /**
     * Legacy method - giữ để tương thích
     */
    @Deprecated
    public void addTaskToColumn(String columnName, JComponent taskComponent) {
        JPanel target = kanbanColumns.get(columnName);
        if (target != null) {
            target.add(taskComponent);
            target.revalidate();
            target.repaint();
        }
    }

    // Getter để Controller có thể truy cập
    public Map<String, JPanel> getKanbanColumns() {
        return kanbanColumns;
    }

    public Map<String, JButton> getColumnCreateButtons() {
        return columnCreateButtons;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    // Helper: choose a subtle tint for column headers based on status
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