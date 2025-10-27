package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class DashboardView extends JFrame {

    // ===================== STYLE CONSTANTS =====================
    private static final Color COLOR_BACKGROUND = new Color(0xF4F5F7);
    private static final Color COLOR_CARD = new Color(0xFFFFFF);
    private static final Color COLOR_BORDER = new Color(0xE0E0E0);
    private static final Color COLOR_TEXT_PRIMARY = new Color(0x333333);
    private static final Color COLOR_TEXT_MUTED = new Color(0x666666);
    private static final Color COLOR_PRIMARY = new Color(0x3B82F6);

    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);

    // ===================== UI COMPONENTS =====================
    // Header components
    public JToggleButton kanbanButton;
    public JToggleButton tableButton;
    public JToggleButton calendarButton;
    public JTextField searchField;
    public JButton filterButton;
    public JButton createButton;
    public JButton notifyButton;
    public JButton loginButton;

    // Sidebar & Kanban Panels
    private JPanel sidebarPanel;
    private JPanel kanbanPanel;

    // Lưu tham chiếu các cột Kanban theo tên
    public Map<String, JPanel> kanbanColumns = new HashMap<>();


    // ===================== CONSTRUCTOR =====================
    public DashboardView() {
        setTitle("Quản lý Công việc Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);
        initUI();
        setVisible(true);
    }


    // ===================== MAIN UI LAYOUT =====================
    private void initUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBackground(COLOR_BACKGROUND);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSidebarPanel(), BorderLayout.WEST);
        add(createKanbanBoardPanel(), BorderLayout.CENTER);
    }


    // ===================== HEADER PANEL =====================
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_CARD);
        headerPanel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(10, 20, 10, 20)
        ));

        JPanel topRow = new JPanel(new GridBagLayout());
        topRow.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Tiêu đề Dashboard ---
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Quản lý Công việc");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_PRIMARY);
        JLabel subtitleLabel = new JLabel("Dashboard quản lý dự án và công việc nhóm");
        subtitleLabel.setFont(FONT_NORMAL);
        subtitleLabel.setForeground(COLOR_TEXT_MUTED);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        topRow.add(titlePanel, gbc);

        // ---  Chọn chế độ hiển thị (Kanban / Bảng / Lịch) ---
        kanbanButton = new JToggleButton("Kanban", true);
        tableButton = new JToggleButton("Bảng");
        calendarButton = new JToggleButton("Lịch");
        ButtonGroup viewGroup = new ButtonGroup();
        viewGroup.add(kanbanButton);
        viewGroup.add(tableButton);
        viewGroup.add(calendarButton);
        JPanel viewTogglePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        viewTogglePanel.setOpaque(false);
        viewTogglePanel.add(kanbanButton);
        viewTogglePanel.add(tableButton);
        viewTogglePanel.add(calendarButton);
        gbc.gridx = 1;
        gbc.weightx = 0;
        topRow.add(viewTogglePanel, gbc);

        // ---  Thanh tìm kiếm và nút hành động ---
        gbc.gridx = 2;
        searchField = new JTextField(" Tìm kiếm...", 20);
        searchField.setBackground(COLOR_BACKGROUND);
        searchField.setForeground(COLOR_TEXT_MUTED);
        searchField.setBorder(new LineBorder(COLOR_BORDER));
        topRow.add(searchField, gbc);

        gbc.gridx = 3;
        filterButton = new JButton("Lọc");
        topRow.add(filterButton, gbc);
        gbc.gridx = 4;
        createButton = new JButton("Tạo mới");
        topRow.add(createButton, gbc);
        gbc.gridx = 5;
        notifyButton = new JButton("🔔");
        topRow.add(notifyButton, gbc);
        gbc.gridx = 6;
        loginButton = new JButton("Đăng nhập");
        topRow.add(loginButton, gbc);

        headerPanel.add(topRow, BorderLayout.CENTER);
        return headerPanel;
    }


    // ===================== SIDEBAR PANEL =====================
    private JPanel createSidebarPanel() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(COLOR_CARD);
        sidebarPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        sidebarPanel.setPreferredSize(new Dimension(320, 0));

        // Các phần trong sidebar được để trống, Controller sẽ bổ sung nội dung
        sidebarPanel.add(createPlaceholderCard("Thông tin dự án"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(createPlaceholderCard("Thành viên nhóm"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(createPlaceholderCard("Thống kê nhanh"));

        return sidebarPanel;
    }

    private JPanel createPlaceholderCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDER), title,
                0, 0, FONT_BOLD, COLOR_TEXT_PRIMARY
        ));

        JLabel label = new JLabel("(Chưa có dữ liệu)");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(COLOR_TEXT_MUTED);
        card.add(label, BorderLayout.CENTER);
        return card;
    }


    // ===================== KANBAN BOARD PANEL =====================
    private JPanel createKanbanBoardPanel() {
        kanbanPanel = new JPanel();
        kanbanPanel.setLayout(new GridLayout(1, 4, 15, 0));
        kanbanPanel.setBackground(COLOR_BACKGROUND);
        kanbanPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        addKanbanColumn("Cần làm");
        addKanbanColumn("Đang làm");
        addKanbanColumn("Đang review");
        addKanbanColumn("Hoàn thành");

        return kanbanPanel;
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


        kanbanPanel.add(columnPanel);
        kanbanColumns.put(title, taskListPanel);
    }

    // ===================== PUBLIC METHODS =====================


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
