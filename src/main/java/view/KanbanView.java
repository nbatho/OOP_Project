package main.java.view;

import main.java.model.Task;
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
        // Use a compound border: visible outer line and inner empty border to inset content
        columnPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(style.getCOLOR_BORDER()),
            new EmptyBorder(GlobalStyle.scale(8), GlobalStyle.scale(8), GlobalStyle.scale(8), GlobalStyle.scale(8))
        ));
        // Give each column a comfortable preferred width so they don't appear cramped
        columnPanel.setPreferredSize(new Dimension(columnWidth, 700));
        columnPanel.setMaximumSize(new Dimension(columnWidth, Integer.MAX_VALUE));

        // Header với số lượng task
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

        // Create a cards container that will actually hold the task cards (vertical BoxLayout)
        JPanel cardsPanel = new JPanel();
        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Add a slightly larger top padding so cards don't overlap the column frame/header
        int columnTopPadding = GlobalStyle.scale(32);
        // Use an inner empty border on the cards panel so padding remains even when scrolling
        cardsPanel.setBorder(new EmptyBorder(columnTopPadding, 0, GlobalStyle.scale(12), 0));

        // Simpler centering strategy: use a vertical BoxLayout wrapper with glue above/below
        // the cardsPanel. Glue will consume extra space and keep cardsPanel centered when
        // content is smaller than the viewport; if content is larger, the scroll pane will
        // allow scrolling normally.
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        // Add cardsPanel first so cards stick to the top; add glue below to consume remaining space.
        centerPanel.add(cardsPanel);
        centerPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        columnPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer with create button for this column
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        JButton createBtn = new JButton("Tạo mới");
        createBtn.setFont(GlobalStyle.scaleFont(style.getFONT_NORMAL()));
        createBtn.setBackground(style.getCOLOR_PRIMARY());
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        footer.add(createBtn);
        columnPanel.add(footer, BorderLayout.SOUTH);

        // Store the create button so controller can attach listeners
        columnCreateButtons.put(statusKey, createBtn);

        add(columnPanel);
        // Store the cardsPanel (not the wrapper) so addTaskToBoard/clearAllTasks operate on the actual container
        kanbanColumns.put(statusKey, cardsPanel);
        // Store the count label so we can update counts easily
        columnCountLabels.put(statusKey, countLabel);
    }

    /**
     * Cập nhật tất cả tasks vào Kanban board
     */
    public void updateTasks(List<Task> tasks) {
        // 1. Xóa tất cả tasks cũ
        // Ensure Swing UI updates happen on EDT
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
        // Ensure Swing UI updates happen on EDT
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
            System.out.println("Kanban: added task '" + task.getTitle() + "' to column " + columnKey + ". total comps=" + targetColumn.getComponentCount());
        } else {
            System.err.println("⚠️ Không tìm thấy cột cho status: " + status);
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
                System.err.println("⚠️ Unknown status: " + status + ", defaulting to TODO");
                return "TODO";
        }
    }

    /**
     * Tạo task card component
     */
    private JPanel createTaskCard(Task task) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(style.getCOLOR_BORDER(), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        // center card horizontally inside the task list panel and leave side padding
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        int cardWidth = Math.max(GlobalStyle.scale(300), columnWidth - GlobalStyle.scale(80));
        // Increase default card height so single cards remain visible and not clipped
        int cardHeight = GlobalStyle.scale(220);
        // Prevent card from stretching vertically when there are few cards by capping max height
        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        card.setMaximumSize(new Dimension(cardWidth, cardHeight));
        card.setMinimumSize(new Dimension(cardWidth, cardHeight));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // We'll center the visible content vertically inside the card by using an inner
        // content panel with vertical glue above and below the actual components.
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(Box.createVerticalGlue());

        // Title
        JLabel titleLabel = new JLabel(task.getTitle());
        titleLabel.setFont(GlobalStyle.scaleFont(style.getFONT_BOLD()));
        titleLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);

        contentPanel.add(Box.createVerticalStrut(8));

        // Description (nếu có)
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            // Truncate long descriptions to keep card height reasonable
            String descText = task.getDescription();
            int maxChars = 240; // conservative limit
            if (descText.length() > maxChars) {
                descText = descText.substring(0, maxChars).trim() + "...";
            }
            JTextArea descArea = new JTextArea(descText);
            descArea.setFont(GlobalStyle.scaleFont(style.getFONT_NORMAL()));
            descArea.setForeground(style.getCOLOR_TEXT_MUTED());
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setEditable(false);
            descArea.setOpaque(false);
            descArea.setBorder(null);
            descArea.setMaximumSize(new Dimension(columnWidth - 40, GlobalStyle.scale(140)));
            descArea.setPreferredSize(new Dimension(columnWidth - 40, GlobalStyle.scale(100)));
            descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(descArea);
            contentPanel.add(Box.createVerticalStrut(8));
        }

        // Footer panel (assignee, due date, priority, etc.)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

//        // Assignee
//        if (task.getAssigneeName() != null && !task.getAssigneeName().isEmpty()) {
//            JLabel assigneeLabel = new JLabel("👤 " + task.getAssigneeName());
//            assigneeLabel.setFont(style.getFONT_SMALL());
//            assigneeLabel.setForeground(style.getCOLOR_TEXT_MUTED());
//            footerPanel.add(assigneeLabel);
//        }

        // Due date
        if (task.getDueDate() != null) {
            JLabel dueDateLabel = new JLabel("📅 " + task.getDueDate().toString());
            dueDateLabel.setFont(GlobalStyle.scaleFont(style.getFONT_SMALL()));
            dueDateLabel.setForeground(style.getCOLOR_TEXT_MUTED());
            footerPanel.add(dueDateLabel);
        }

        // Priority badge
        if (task.getPriority() != null && !task.getPriority().isEmpty()) {
            JLabel priorityBadge = createPriorityBadge(task.getPriority());
            footerPanel.add(priorityBadge);
        }

        contentPanel.add(footerPanel);
        contentPanel.add(Box.createVerticalGlue());

        card.add(contentPanel);

        // Thêm click listener để xem chi tiết
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onTaskClicked(task);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(245, 247, 250));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
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
                System.out.println("Kanban: column=" + key + " comps=" + comps + " tasks=" + taskCount);
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