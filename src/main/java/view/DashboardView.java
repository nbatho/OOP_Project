package main.java.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DashboardView extends JFrame {

    // Style constants
    private static final Color COLOR_BACKGROUND = new Color(0xF4F5F7);
    private static final Color COLOR_CARD = new Color(0xFFFFFF);
    private static final Color COLOR_BORDER = new Color(0xE0E0E0);
    private static final Color COLOR_TEXT_PRIMARY = new Color(0x333333);
    private static final Color COLOR_TEXT_MUTED = new Color(0x666666);
    private static final Color COLOR_PRIMARY = new Color(0x3B82F6);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);

    // Header components (public để Controller truy cập)
    public JToggleButton kanbanButton;
    public JToggleButton tableButton;
    public JToggleButton calendarButton;
    public JTextField searchField;
    public JButton filterButton;
    public JButton createButton;
    public JButton notifyButton;
    public JButton loginButton;

    // Panels
    private JPanel sidebarPanel;
    public JPanel mainContentPanel;
    public CardLayout cardLayout;

    public DashboardView() {
        setTitle("Quản lý Công việc Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBackground(COLOR_BACKGROUND);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSidebarPanel(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        add(mainContentPanel, BorderLayout.CENTER);
    }

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

        // Tiêu đề
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
        gbc.weightx = 1.0;
        topRow.add(titlePanel, gbc);

        // Toggle view
        kanbanButton = new JToggleButton("Kanban", true);
        tableButton = new JToggleButton("Bảng");
        calendarButton = new JToggleButton("Lịch");
        ButtonGroup viewGroup = new ButtonGroup();
        viewGroup.add(kanbanButton);
        viewGroup.add(tableButton);
        viewGroup.add(calendarButton);
        JPanel viewTogglePanel = new JPanel();
        viewTogglePanel.setOpaque(false);
        viewTogglePanel.add(kanbanButton);
        viewTogglePanel.add(tableButton);
        viewTogglePanel.add(calendarButton);
        gbc.gridx = 1;
        gbc.weightx = 0;
        topRow.add(viewTogglePanel, gbc);

        // Search and buttons
        gbc.gridx = 2;
        searchField = new JTextField(" Tìm kiếm...", 20);
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

    private JPanel createSidebarPanel() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(COLOR_CARD);
        sidebarPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        sidebarPanel.setPreferredSize(new Dimension(320, 0));

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
}
