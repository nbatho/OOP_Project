package main.java.view;

import main.java.model.Task;
import main.java.model.User;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TableView extends JPanel {

    private final JTable table;
    private final DefaultTableModel model;
    GlobalStyle style = new GlobalStyle();
    private TaskRowClickListener taskRowClickListener;
    public TableView() {
        setLayout(new BorderLayout(8, 8));
        setOpaque(true);
        setBackground(style.getCOLOR_BACKGROUND());
        setBorder(new EmptyBorder(0, 10, 10, 10));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Bảng công việc theo hạng mục");
        title.setFont(style.getFONT_TITLE());
        title.setForeground(style.getCOLOR_TEXT_PRIMARY());
        JLabel sub = new JLabel("Quản lý chi tiết công việc theo từng lĩnh vực");
        sub.setFont(style.getFONT_NORMAL());
        sub.setForeground(style.getCOLOR_TEXT_MUTED());
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(sub);
        add(header, BorderLayout.NORTH);

        String[] cols = {"ID","Công việc", "Người thực hiện", "Trạng thái", "Độ ưu tiên", "Tiến độ"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override
            public Class<?> getColumnClass(int c) {
                if (c == 1) return List.class;
                if (c == 4) return Integer.class;
                return Object.class;
            }
        };

        table = new JTable(model);
        table.setRowHeight(44);
        table.setFont(style.getFONT_NORMAL());
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(0xF1F3F5));
        table.setBackground(style.getCOLOR_CARD());
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setWidth(0);

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof List<?> list) {
                    List<String> names = new ArrayList<>();
                    for (Object o : list) {
                        if (o instanceof User u) {
                            names.add(u.getFullName());
                        }
                    }
                    setText(String.join(", ", names));
                } else {
                    super.setValue(value);
                }
            }
        });
        JTableHeader h = table.getTableHeader();
        h.setBackground(new Color(0xEEF2F5));
        h.setFont(style.getFONT_BOLD());
        ((DefaultTableCellRenderer) h.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(style.getCOLOR_CARD());
        sp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, style.getCOLOR_BORDER()));

        JPanel middle = new JPanel(new BorderLayout());
        middle.setBackground(style.getCOLOR_CARD());
        middle.add(sp, BorderLayout.CENTER);

        add(middle, BorderLayout.CENTER);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        if (taskRowClickListener != null) {
                            String projectId = (String) model.getValueAt(row, 0);
                            String taskId = (String) model.getValueAt(row, 1);
                            taskRowClickListener.onTaskRowClicked(projectId, taskId);
                        }
                    }
                }
            }
        });
    }

    public interface TaskRowClickListener {
        void onTaskRowClicked(String projectId, String taskId);
    }

    public void setTaskRowClickListener(TaskRowClickListener listener) {
        this.taskRowClickListener = listener;
    }
    public void updateTasks(List<Task> tasks) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> updateTasks(tasks));
            return;
        }

         clearAllTasks();

        for (Task task : tasks) {
            addTaskTable(
                    task.getProjectId(),
                    task.getTaskId(),
                    task.getTitle(),
                    task.getAssignedUsers(),
                    task.getStatus(),
                    task.getPriority()
            );
        }
    }
    public void addTaskTable(String projectId,String taskId ,String title, List<User> users, String status, String priority) {
        model.addRow(new Object[]{projectId,taskId ,title, users, status, priority, 0});
    }
    public void clearAllTasks() {
        model.setRowCount(0);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
