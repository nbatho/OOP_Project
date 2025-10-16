package view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DashboardView extends JFrame {

    // ===== STYLE CONSTANTS (Tương tự biến CSS) =====
    private static final Color COLOR_BACKGROUND = new Color(0xF4F5F7); // Nền tổng thể sáng hơn
    private static final Color COLOR_CARD = new Color(0xFFFFFF);     // Các card màu trắng
    private static final Color COLOR_BORDER = new Color(0xE0E0E0);     // Đường viền sáng hơn
    private static final Color COLOR_TEXT_PRIMARY = new Color(0x333333); // Màu chữ chính tối hơn
    private static final Color COLOR_TEXT_MUTED = new Color(0x666666);   // Màu chữ phụ
    private static final Color COLOR_PRIMARY = new Color(0x3B82F6);  // Màu xanh chính cho "Dự án Web App" và các button
    private static final Color COLOR_SUCCESS = new Color(0x22C55E);
    private static final Color COLOR_WARNING = new Color(0xF59E0B);
    private static final Color COLOR_DESTRUCTIVE = new Color(0xEF4444);

    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 10);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);

    public DashboardView() {
        setTitle("Quản lý Công việc Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Thay thế UIScale.scale() bằng kích thước cố định để phù hợp với giao diện phức tạp này
        setSize(1600, 900);
        setLocationRelativeTo(null);
        // Layout đã được đặt trong initUI(), nơi getContentPane() được sử dụng
        initUI();
        setVisible(true);
    }

    public void initUI() {
        // Thiết lập layout và màu nền cho content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBackground(COLOR_BACKGROUND);

        // ===== XÂY DỰNG CÁC THÀNH PHẦN GIAO DIỆN =====
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSidebarPanel(), BorderLayout.WEST);
        add(createKanbanBoardPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        // Main panel for header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_CARD);
        headerPanel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(10, 20, 10, 20)
        ));

        // ===== Top Row =====
        JPanel topRow = new JPanel(new GridBagLayout());
        topRow.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Title and Subtitle
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
        gbc.weightx = 1.0; // Pushes other components to the right
        topRow.add(titlePanel, gbc);

        // View Toggles (Kanban, Bảng, Lịch)
        JToggleButton kanbanButton = new JToggleButton("Kanban", true);
        JToggleButton tableButton = new JToggleButton("Bảng");
        JToggleButton calendarButton = new JToggleButton("Lịch");
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

        // Search Bar
        JTextField searchField = new JTextField(" Tìm kiếm công việc...", 20);
        searchField.setBackground(COLOR_BACKGROUND);
        searchField.setForeground(COLOR_TEXT_MUTED);
        searchField.setBorder(new LineBorder(COLOR_BORDER));
        gbc.gridx = 2;
        topRow.add(searchField, gbc);

        // Action Buttons
        JButton filterButton = new JButton("Lọc");
        JButton createButton = new JButton("Tạo mới");
        createButton.setBackground(COLOR_PRIMARY);
        createButton.setForeground(COLOR_TEXT_PRIMARY);

        gbc.gridx = 3;
        topRow.add(filterButton, gbc);
        gbc.gridx = 4;
        topRow.add(createButton, gbc);

        // Separator
        gbc.gridx = 5;
        topRow.add(new JSeparator(SwingConstants.VERTICAL), gbc);


        // User actions (Notifications, Login)
        JButton notificationButton = new JButton("🔔 (3)");
        JButton loginButton = new JButton("Đăng nhập");
        gbc.gridx = 6;
        topRow.add(notificationButton, gbc);
        gbc.gridx = 7;
        topRow.add(loginButton, gbc);

        headerPanel.add(topRow, BorderLayout.CENTER);

        // ===== Bottom Row (Stats) =====
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        bottomRow.setOpaque(false);
        bottomRow.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
                new EmptyBorder(5, 0, 0, 0)
        ));
        bottomRow.add(createStatLabel("Tổng công việc:", "1", Color.GRAY));
        bottomRow.add(createStatLabel("Đang làm:", "1", COLOR_WARNING));
        bottomRow.add(createStatLabel("Hoàn thành:", "0", COLOR_SUCCESS));

        // Member count pushed to the right
        JPanel rightAlignedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightAlignedPanel.setOpaque(false);
        rightAlignedPanel.add(new JLabel("5 thành viên"));

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.add(bottomRow, BorderLayout.WEST);
        bottomContainer.add(rightAlignedPanel, BorderLayout.EAST);

        headerPanel.add(bottomContainer, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(COLOR_CARD);
        sidebarPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        sidebarPanel.setPreferredSize(new Dimension(320, 0));

        // Project Info Card
        sidebarPanel.add(createProjectInfoCard());
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Team Members Card
        sidebarPanel.add(createTeamMembersCard());
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Quick Stats Card
        sidebarPanel.add(createQuickStatsCard());

        return sidebarPanel;
    }

    private JPanel createProjectInfoCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(COLOR_PRIMARY);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Dự án Web App");
        title.setFont(FONT_BOLD);
        title.setForeground(Color.WHITE);
        card.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel progressLabel = new JLabel("Tiến độ hoàn thành");
        progressLabel.setForeground(Color.WHITE);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(72);
        progressBar.setString("72%");
        progressBar.setStringPainted(true);

        content.add(progressLabel);
        content.add(progressBar);
        content.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel stats = new JPanel(new GridLayout(1, 2, 10, 0));
        stats.setOpaque(false);
        stats.add(createStatBlock("24", "Hoàn thành", Color.WHITE));
        stats.add(createStatBlock("8", "Còn lại", Color.WHITE));
        content.add(stats);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTeamMembersCard() {
        JPanel card = new JPanel(new BorderLayout(10, 15));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDER), "Thành viên nhóm",
                0, 0, FONT_BOLD, COLOR_TEXT_PRIMARY
        ));

        JPanel memberList = new JPanel();
        memberList.setLayout(new BoxLayout(memberList, BoxLayout.Y_AXIS));
        memberList.setOpaque(false);

        memberList.add(createMemberPanel("NVA", "Nguyễn Văn A", "Frontend Dev", 80, "8/10", COLOR_SUCCESS));
        memberList.add(Box.createRigidArea(new Dimension(0, 5)));
        memberList.add(createMemberPanel("TTB", "Trần Thị B", "Backend Dev", 75, "6/8", COLOR_WARNING));
        memberList.add(Box.createRigidArea(new Dimension(0, 5)));
        memberList.add(createMemberPanel("LVC", "Lê Văn C", "UI/UX Designer", 80, "12/15", Color.GRAY));

        card.add(new JScrollPane(memberList), BorderLayout.CENTER);

        JButton inviteButton = new JButton("+ Mời thành viên");
        card.add(inviteButton, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createMemberPanel(String initials, String name, String role, int progress, String tasks, Color statusColor) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        panel.add(new AvatarLabel(initials, statusColor), BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(FONT_BOLD);
        nameLabel.setForeground(COLOR_TEXT_PRIMARY);
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(FONT_SMALL);
        roleLabel.setForeground(COLOR_TEXT_MUTED);

        infoPanel.add(nameLabel);
        infoPanel.add(roleLabel);

        JPanel progressPanel = new JPanel(new BorderLayout(5, 0));
        progressPanel.setOpaque(false);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(progress);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(new JLabel(tasks), BorderLayout.EAST);

        infoPanel.add(progressPanel);

        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createQuickStatsCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDER), "Thống kê nhanh",
                0, 0, FONT_BOLD, COLOR_TEXT_PRIMARY
        ));

        JPanel statsGrid = new JPanel(new GridLayout(1, 2, 10, 10));
        statsGrid.setOpaque(false);
        statsGrid.add(createStatBlock("5", "Hôm nay", COLOR_SUCCESS));
        statsGrid.add(createStatBlock("3", "Quá hạn", COLOR_WARNING));

        card.add(statsGrid, BorderLayout.CENTER);
        return card;
    }

    private JScrollPane createKanbanBoardPanel() {
        JPanel kanbanPanel = new JPanel();
        // Dùng BoxLayout theo chiều ngang để xếp các cột
        kanbanPanel.setLayout(new BoxLayout(kanbanPanel, BoxLayout.X_AXIS));
        kanbanPanel.setBackground(COLOR_BACKGROUND);
        kanbanPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Thêm các cột vào
        kanbanPanel.add(createKanbanColumn("Cần làm", 0, new Color(0, 0, 0, 0)));
        kanbanPanel.add(Box.createHorizontalStrut(15));
        kanbanPanel.add(createKanbanColumn("Đang làm", 1, COLOR_WARNING));
        kanbanPanel.add(Box.createHorizontalStrut(15));
        kanbanPanel.add(createKanbanColumn("Đang review", 0, COLOR_PRIMARY));
        kanbanPanel.add(Box.createHorizontalStrut(15));
        kanbanPanel.add(createKanbanColumn("Hoàn thành", 0, COLOR_SUCCESS));

        // Đặt panel vào JScrollPane để có thể cuộn ngang
        JScrollPane scrollPane = new JScrollPane(kanbanPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel createKanbanColumn(String title, int taskCount, Color accentColor) {
        JPanel columnPanel = new JPanel(new BorderLayout(10, 10));
        columnPanel.setBackground(COLOR_CARD);
        columnPanel.setBorder(new LineBorder(COLOR_BORDER));
        columnPanel.setPreferredSize(new Dimension(300, 100)); // Set fixed width for columns

        // Column Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_BOLD);
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        // Accent line
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentColor));

        JLabel countLabel = new JLabel(String.valueOf(taskCount));
        countLabel.setFont(FONT_BOLD);

        header.add(titleLabel, BorderLayout.CENTER);
        header.add(countLabel, BorderLayout.EAST);
        columnPanel.add(header, BorderLayout.NORTH);

        // Task List
        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setOpaque(false);
        taskListPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        // Add sample task to "Đang làm" column
        if ("Đang làm".equals(title)) {
            taskListPanel.add(createTaskCard(
                    "Thiết kế UI Dashboard",
                    "Tạo mockup cho trang chủ",
                    "Cao",
                    COLOR_DESTRUCTIVE,
                    "15/10/2025",
                    "NVA"
            ));
        }

        // Add "Thêm công việc" button
        JButton addTaskButton = new JButton("+ Thêm công việc");

        JPanel buttonContainer = new JPanel(new BorderLayout());
        buttonContainer.setOpaque(false);
        buttonContainer.setBorder(new EmptyBorder(0, 10, 10, 10));
        buttonContainer.add(addTaskButton, BorderLayout.CENTER);

        columnPanel.add(new JScrollPane(taskListPanel), BorderLayout.CENTER); // Make task list scrollable if needed
        columnPanel.add(buttonContainer, BorderLayout.SOUTH);

        return columnPanel;
    }

    private JPanel createTaskCard(String title, String description, String priority, Color priorityColor, String dueDate, String assignee) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_BACKGROUND);
        card.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(10, 10, 10, 10)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT); // Important for BoxLayout
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Constrain height

        // Top row: Title and Priority
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_BOLD);
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        JLabel priorityLabel = new JLabel(priority);
        priorityLabel.setForeground(priorityColor);
        topRow.add(titleLabel, BorderLayout.CENTER);
        topRow.add(priorityLabel, BorderLayout.EAST);

        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(FONT_NORMAL);
        descLabel.setForeground(COLOR_TEXT_MUTED);

        // Bottom row: Due date and Assignee
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);
        JLabel dateLabel = new JLabel("📅 " + dueDate);
        dateLabel.setFont(FONT_SMALL);
        dateLabel.setForeground(COLOR_TEXT_MUTED);

        AvatarLabel assigneeAvatar = new AvatarLabel(assignee, COLOR_PRIMARY);
        assigneeAvatar.setPreferredSize(new Dimension(24, 24));

        bottomRow.add(dateLabel, BorderLayout.CENTER);
        bottomRow.add(assigneeAvatar, BorderLayout.EAST);

        card.add(topRow);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(bottomRow);

        return card;
    }

    // ===== HELPER METHODS & CUSTOM COMPONENTS =====

    private JPanel createStatLabel(String text, String value, Color dotColor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);
        panel.add(new ColorDot(dotColor));
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(COLOR_TEXT_MUTED);
        panel.add(textLabel);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(FONT_BOLD);
        valueLabel.setForeground(COLOR_TEXT_PRIMARY);
        panel.add(valueLabel);
        return panel;
    }

    private JPanel createStatBlock(String value, String label, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(FONT_SMALL);
        textLabel.setForeground(color);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(valueLabel);
        panel.add(textLabel);
        return panel;
    }

    private static class ColorDot extends JComponent {
        private final Color color;

        ColorDot(Color color) {
            this.color = color;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(8, 8);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            g.fillOval(0, 0, 8, 8);
        }
    }

    private static class AvatarLabel extends JLabel {
        private final String initials;
        private final Color statusColor;

        public AvatarLabel(String initials, Color statusColor) {
            this.initials = initials;
            this.statusColor = statusColor;
            setPreferredSize(new Dimension(40, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int diameter = Math.min(getWidth(), getHeight()) - 2;
            int x_offset = (getWidth() - diameter) / 2;
            int y_offset = (getHeight() - diameter) / 2;


            // Draw main avatar circle
            g2d.setColor(COLOR_PRIMARY);
            g2d.fillOval(x_offset, y_offset, diameter, diameter);

            // Draw initials
            g2d.setColor(COLOR_TEXT_PRIMARY);
            g2d.setFont(getFont().deriveFont(Font.BOLD, diameter * 0.4f));
            FontMetrics fm = g2d.getFontMetrics();
            int strX = x_offset + (diameter - fm.stringWidth(initials)) / 2;
            int strY = y_offset + (fm.getAscent() + (diameter - (fm.getAscent() + fm.getDescent())) / 2);
            g2d.drawString(initials, strX, strY);

            // Draw status dot
            if (statusColor != null) {
                int dotDiameter = diameter / 3;
                int dotX = getWidth() - dotDiameter - x_offset;
                int dotY = getHeight() - dotDiameter - y_offset;
                g2d.setColor(statusColor);
                g2d.fillOval(dotX, dotY, dotDiameter, dotDiameter);
                g2d.setColor(COLOR_CARD); // Border for the dot
                g2d.drawOval(dotX, dotY, dotDiameter, dotDiameter);
            }
            g2d.dispose();
        }
    }
}