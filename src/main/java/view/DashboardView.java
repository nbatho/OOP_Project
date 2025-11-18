package main.java.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    // Header components
    private JToggleButton kanbanButton;
    private JToggleButton tableButton;
    private JToggleButton calendarButton;
    private JTextField searchField;
    private JButton searchButton;
    private JButton createButton;
    private JButton notifyButton;
    private JButton userButton;

    // Project menu components
    private JLabel titleLabel;
    private JPopupMenu projectMenu;
    private JMenuItem createProjectMenuItem;
    
    // Listener interface cho project selection
    private ProjectSelectionListener projectSelectionListener;

    // Menu items - để Controller truy cập
    private JMenuItem infoMenuItem;
    private JMenuItem logoutMenuItem;
    private JPopupMenu userMenu;

    // Panels
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public DashboardView() {
        setTitle("Quản lý Công việc Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);
        initUI();
        createUserMenu();
        createProjectMenu();
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

        // Tiêu đề - có thể click
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setOpaque(false);
        titlePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        titleLabel = new JLabel("Quản lý");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_PRIMARY);

        JLabel dropdownIcon = new JLabel("▼");
        dropdownIcon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dropdownIcon.setForeground(COLOR_TEXT_MUTED);

        titlePanel.add(titleLabel);
        titlePanel.add(dropdownIcon);

        // Thêm mouse listener cho titlePanel
        titlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (projectMenu != null) {
                    projectMenu.show(titlePanel, 0, titlePanel.getHeight());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                titleLabel.setForeground(COLOR_PRIMARY.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                titleLabel.setForeground(COLOR_PRIMARY);
            }
        });

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
        searchField = new JTextField("", 20);
        topRow.add(searchField, gbc);
        gbc.gridx = 3;
        searchButton = new JButton("Tìm kiếm");
        topRow.add(searchButton, gbc);
        gbc.gridx = 4;
        createButton = new JButton("Tạo mới");
        topRow.add(createButton, gbc);
        gbc.gridx = 5;
        notifyButton = new JButton("🔔");
        topRow.add(notifyButton, gbc);
        gbc.gridx = 6;
        userButton = new JButton("User");

        userButton.addActionListener(e -> {
            if (userMenu != null) {
                userMenu.show(userButton, 0, userButton.getHeight());
            }
        });

        topRow.add(userButton, gbc);

        headerPanel.add(topRow, BorderLayout.CENTER);
        return headerPanel;
    }

    private void createProjectMenu() {
        projectMenu = new JPopupMenu();
        projectMenu.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));

        // Thêm header cho menu
        JLabel headerLabel = new JLabel("  Dự án của bạn");
        headerLabel.setFont(FONT_BOLD);
        headerLabel.setForeground(COLOR_TEXT_MUTED);
        headerLabel.setBorder(new EmptyBorder(8, 5, 5, 5));
        projectMenu.add(headerLabel);
        projectMenu.addSeparator();

        // Danh sách dự án mẫu (có thể thay đổi động sau)
        String[] sampleProjects = {"Dự án A", "Dự án B", "Dự án C"};
        for (String projectName : sampleProjects) {
            JMenuItem projectItem = new JMenuItem(projectName);
            projectItem.setFont(FONT_NORMAL);
            projectItem.addActionListener(e -> {
                setCurrentProjectName(projectName);
                if (projectSelectionListener != null) {
                    projectSelectionListener.onProjectSelected(projectName);
                }
            });
            projectMenu.add(projectItem);
        }

        projectMenu.addSeparator();

        // Nút tạo dự án mới
        createProjectMenuItem = new JMenuItem("+ Tạo dự án mới");
        createProjectMenuItem.setFont(FONT_BOLD);
        createProjectMenuItem.setForeground(COLOR_PRIMARY);
        projectMenu.add(createProjectMenuItem);
    }

    private void createUserMenu() {
        userMenu = new JPopupMenu();

        infoMenuItem = new JMenuItem("Thông tin");
        logoutMenuItem = new JMenuItem("Đăng xuất");

        userMenu.add(infoMenuItem);
        userMenu.add(logoutMenuItem);
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

    // Phương thức để cập nhật tên dự án hiện tại
    public void setCurrentProjectName(String projectName) {
        titleLabel.setText(projectName);
    }
    
    // Phương thức để cập nhật thông tin sidebar
    public void updateSidebarProjectInfo(String projectInfo) {
        // Xóa component cũ
        sidebarPanel.removeAll();
        
        // Thêm thông tin dự án mới
        JPanel projectInfoCard = new JPanel(new BorderLayout());
        projectInfoCard.setOpaque(false);
        projectInfoCard.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDER), "Thông tin dự án",
                0, 0, FONT_BOLD, COLOR_TEXT_PRIMARY
        ));
        
        JTextArea textArea = new JTextArea(projectInfo);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(COLOR_CARD);
        projectInfoCard.add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        sidebarPanel.add(projectInfoCard);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(createPlaceholderCard("Thành viên nhóm"));
        
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }
    
    // Thiết lập listener cho project selection
    public void setProjectSelectionListener(ProjectSelectionListener listener) {
        this.projectSelectionListener = listener;
    }
    
    // Interface để Controller lắng nghe sự kiện chọn dự án
    public interface ProjectSelectionListener {
        void onProjectSelected(String projectName);
    }

    // Phương thức để cập nhật danh sách dự án trong menu
    public void updateProjectList(String[] projects) {
        projectMenu.removeAll();

        // Header
        JLabel headerLabel = new JLabel("  Dự án của bạn");
        headerLabel.setFont(FONT_BOLD);
        headerLabel.setForeground(COLOR_TEXT_MUTED);
        headerLabel.setBorder(new EmptyBorder(8, 5, 5, 5));
        projectMenu.add(headerLabel);
        projectMenu.addSeparator();

        // Thêm các dự án
        for (String projectName : projects) {
            JMenuItem projectItem = new JMenuItem(projectName);
            projectItem.setFont(FONT_NORMAL);
            projectItem.addActionListener(e -> {
                setCurrentProjectName(projectName);
                if (projectSelectionListener != null) {
                    projectSelectionListener.onProjectSelected(projectName);
                }
            });
            projectMenu.add(projectItem);
        }

        projectMenu.addSeparator();

        // Nút tạo dự án mới
        createProjectMenuItem = new JMenuItem("+ Tạo dự án mới");
        createProjectMenuItem.setFont(FONT_BOLD);
        createProjectMenuItem.setForeground(COLOR_PRIMARY);
        projectMenu.add(createProjectMenuItem);
    }

    // Getters
    public JToggleButton getKanbanButton() { return kanbanButton; }
    public JToggleButton getTableButton() { return tableButton; }
    public JToggleButton getCalendarButton() { return calendarButton; }
    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getCreateButton() { return createButton; }
    public JButton getNotifyButton() { return notifyButton; }
    public JButton getUserButton() { return userButton; }
    public JPanel getSidebarPanel() { return sidebarPanel; }
    public JPanel getMainContentPanel() { return mainContentPanel; }
    public CardLayout getCardLayout() { return cardLayout; }

    // Getters cho menu items
    public JMenuItem getInfoMenuItem() { return infoMenuItem; }
    public JMenuItem getLogoutMenuItem() { return logoutMenuItem; }

    // Getters cho project menu
    public JPopupMenu getProjectMenu() { return projectMenu; }
    public JMenuItem getCreateProjectMenuItem() { return createProjectMenuItem; }
    public JLabel getTitleLabel() { return titleLabel; }
}