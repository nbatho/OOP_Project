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

    // Lưu cột Kanban
    private Map<String, JPanel> kanbanColumns = new HashMap<>();

    // Mapping status từ database sang tên cột hiển thị
    private Map<String, String> statusMapping = new HashMap<>();

    public KanbanView() {
        setLayout(new GridLayout(1, 4, 15, 0));
        setBackground(style.getCOLOR_BACKGROUND());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Khởi tạo mapping
        initStatusMapping();

        // Tạo các cột
        addKanbanColumn("Cần làm", "TODO");
        addKanbanColumn("Đang làm", "IN_PROGRESS");
        addKanbanColumn("Đang review", "REVIEW");
        addKanbanColumn("Hoàn thành", "DONE");
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
        columnPanel.setBorder(new LineBorder(style.getCOLOR_BORDER()));

        // Header với số lượng task
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(style.getFONT_BOLD());
        titleLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());

        JLabel countLabel = new JLabel("0");
        countLabel.setFont(style.getFONT_NORMAL());
        countLabel.setForeground(style.getCOLOR_TEXT_MUTED());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(countLabel, BorderLayout.EAST);

        columnPanel.add(headerPanel, BorderLayout.NORTH);

        // Task list với scroll
        JPanel taskListPanel = new JPanel();
        taskListPanel.setOpaque(false);
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        columnPanel.add(scrollPane, BorderLayout.CENTER);

        add(columnPanel);
        kanbanColumns.put(statusKey, taskListPanel);
    }

    /**
     * Cập nhật tất cả tasks vào Kanban board
     */
    public void updateTasks(List<Task> tasks) {

        clearAllTasks();


        for (Task task : tasks) {
            addTaskToBoard(task);
        }

        updateTaskCounts();
    }

    /**
     * Thêm một task vào board
     */
    public void addTaskToBoard(Task task) {
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
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Task ID badge (góc trên)
        if (task.getTaskId() != null) {
            JLabel idBadge = new JLabel(task.getTaskId());
            idBadge.setFont(new Font("Segoe UI", Font.BOLD, 9));
            idBadge.setForeground(style.getCOLOR_TEXT_MUTED());
            idBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(idBadge);
            card.add(Box.createVerticalStrut(5));
        }

        // Title
        JLabel titleLabel = new JLabel(task.getTitle());
        titleLabel.setFont(style.getFONT_BOLD());
        titleLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);

        card.add(Box.createVerticalStrut(8));

        // Description (nếu có)
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            JTextArea descArea = new JTextArea(task.getDescription());
            descArea.setFont(style.getFONT_NORMAL());
            descArea.setForeground(style.getCOLOR_TEXT_MUTED());
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setEditable(false);
            descArea.setOpaque(false);
            descArea.setBorder(null);
            descArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(descArea);
            card.add(Box.createVerticalStrut(8));
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
            dueDateLabel.setFont(style.getFONT_NORMAL());
            dueDateLabel.setForeground(style.getCOLOR_TEXT_MUTED());
            footerPanel.add(dueDateLabel);
        }

        // Priority badge
        if (task.getPriority() != null && !task.getPriority().isEmpty()) {
            JLabel priorityBadge = createPriorityBadge(task.getPriority());
            footerPanel.add(priorityBadge);
        }

        card.add(footerPanel);

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
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(2, 6, 2, 6));

        // Normalize priority (case-insensitive)
        String normalizedPriority = priority.toUpperCase().trim();

        switch (normalizedPriority) {
            case "HIGH":
            case "URGENT":
            case "CRITICAL":
                badge.setBackground(new Color(255, 230, 230));
                badge.setForeground(new Color(200, 50, 50));
                badge.setText("🔴 HIGH");
                break;
            case "MEDIUM":
            case "NORMAL":
            case "MODERATE":
                badge.setBackground(new Color(255, 245, 220));
                badge.setForeground(new Color(200, 140, 50));
                badge.setText("🟡 MEDIUM");
                break;
            case "LOW":
                badge.setBackground(new Color(230, 245, 230));
                badge.setForeground(new Color(80, 150, 80));
                badge.setText("🟢 LOW");
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
        Component[] columns = getComponents();
        for (Component col : columns) {
            if (col instanceof JPanel) {
                JPanel columnPanel = (JPanel) col;
                Component header = columnPanel.getComponent(0); // Header panel

                if (header instanceof JPanel) {
                    JPanel headerPanel = (JPanel) header;
                    Component countComp = headerPanel.getComponent(1); // Count label

                    if (countComp instanceof JLabel) {
                        JLabel countLabel = (JLabel) countComp;

                        // Đếm số task trong cột
                        JScrollPane scrollPane = (JScrollPane) columnPanel.getComponent(1);
                        JPanel taskListPanel = (JPanel) scrollPane.getViewport().getView();

                        // Chia 2 vì có cả Box.createVerticalStrut
                        int taskCount = (taskListPanel.getComponentCount() + 1) / 2;
                        countLabel.setText(String.valueOf(taskCount));
                    }
                }
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
}