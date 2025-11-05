package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class KanbanView extends JPanel {

    private static final Color COLOR_BACKGROUND = new Color(0xF4F5F7);
    private static final Color COLOR_CARD = new Color(0xFFFFFF);
    private static final Color COLOR_BORDER = new Color(0xE0E0E0);
    private static final Color COLOR_TEXT_PRIMARY = new Color(0x333333);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    // Lưu cột Kanban
    public Map<String, JPanel> kanbanColumns = new HashMap<>();

    public KanbanView() {
        setLayout(new GridLayout(1, 4, 15, 0));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        addKanbanColumn("Cần làm");
        addKanbanColumn("Đang làm");
        addKanbanColumn("Đang review");
        addKanbanColumn("Hoàn thành");
    }

    private void addKanbanColumn(String title) {
        JPanel columnPanel = new JPanel(new BorderLayout(10, 10));
        columnPanel.setBackground(COLOR_CARD);
        columnPanel.setBorder(new LineBorder(COLOR_BORDER));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_BOLD);
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        columnPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel taskListPanel = new JPanel();
        taskListPanel.setOpaque(false);
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        columnPanel.add(taskListPanel, BorderLayout.CENTER);

        add(columnPanel);
        kanbanColumns.put(title, taskListPanel);
    }

    public void clearAllTasks() {
        for (JPanel listPanel : kanbanColumns.values()) {
            listPanel.removeAll();
            listPanel.revalidate();
            listPanel.repaint();
        }
    }

    public void addTaskToColumn(String columnName, JComponent taskComponent) {
        JPanel target = kanbanColumns.get(columnName);
        if (target != null) {
            target.add(taskComponent);
            target.revalidate();
            target.repaint();
        }
    }
}
