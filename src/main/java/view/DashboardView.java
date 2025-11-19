package main.java.view;

import main.java.component.ProjectTeamCard;
import main.java.model.Project;
import main.java.model.TeamMember;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DashboardView extends JFrame {
    GlobalStyle style = new GlobalStyle();

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
    
    // Teams card panel
    private JPanel teamsCardPanel;
    private JScrollPane teamsScrollPane;
    private JButton addTeamButton;

    public DashboardView() {
        setTitle("Quản lý Công việc Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);
        initUI();
        createUserMenu();
        createProjectMenu();
        loadSampleData();
        setVisible(true);
    }

    private void initUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBackground(style.getCOLOR_BACKGROUND());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSidebarPanel(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(style.getCOLOR_CARD());
        headerPanel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, style.getCOLOR_BORDER()),
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
        titleLabel.setFont(style.getFONT_TITLE());
        titleLabel.setForeground(style.getCOLOR_PRIMARY());

        JLabel dropdownIcon = new JLabel("▼");
        dropdownIcon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dropdownIcon.setForeground(style.getCOLOR_TEXT_MUTED());

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
                titleLabel.setForeground(style.getCOLOR_PRIMARY().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                titleLabel.setForeground(style.getCOLOR_PRIMARY());
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
        projectMenu.setBorder(BorderFactory.createLineBorder(style.getCOLOR_BORDER()));

        // Thêm header cho menu
        JLabel headerLabel = new JLabel("  Dự án của bạn");
        headerLabel.setFont(style.getFONT_BOLD());
        headerLabel.setForeground(style.getCOLOR_TEXT_MUTED());
        headerLabel.setBorder(new EmptyBorder(8, 5, 5, 5));
        projectMenu.add(headerLabel);
        projectMenu.addSeparator();

        // Danh sách dự án mẫu (có thể thay đổi động sau)
        String[] sampleProjects = {"Dự án A", "Dự án B", "Dự án C"};
        for (String projectName : sampleProjects) {
            JMenuItem projectItem = new JMenuItem(projectName);
            projectItem.setFont(style.getFONT_NORMAL());
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
        createProjectMenuItem.setFont(style.getFONT_BOLD());
        createProjectMenuItem.setForeground(style.getCOLOR_PRIMARY());
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
        sidebarPanel.setBackground(style.getCOLOR_CARD());
        sidebarPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        sidebarPanel.setPreferredSize(new Dimension(350, 0));

        sidebarPanel.add(createPlaceholderCard("Thông tin dự án"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Header cho danh sách dự án
        JLabel teamsLabel = new JLabel("Danh sách các dự án");
        teamsLabel.setFont(style.getFONT_BOLD());
        teamsLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());
        teamsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        sidebarPanel.add(teamsLabel);
        
        // Teams card panel with scroll
        teamsCardPanel = new JPanel();
        teamsCardPanel.setLayout(new BoxLayout(teamsCardPanel, BoxLayout.Y_AXIS));
        teamsCardPanel.setBackground(style.getCOLOR_CARD());
        
        teamsScrollPane = new JScrollPane(teamsCardPanel);
        teamsScrollPane.setPreferredSize(new Dimension(320, 300));
        teamsScrollPane.setBorder(null);
        teamsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        
        sidebarPanel.add(teamsScrollPane);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Nút thêm team mới ở dưới
        addTeamButton = new JButton("+ Thêm team mới");
        addTeamButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addTeamButton.setForeground(Color.WHITE);
        addTeamButton.setBackground(style.getCOLOR_PRIMARY());
        addTeamButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addTeamButton.setFocusPainted(false);
        addTeamButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addTeamButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addTeamButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sidebarPanel.add(addTeamButton);

        return sidebarPanel;
    }

    private JPanel createPlaceholderCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(style.getCOLOR_BORDER()), title,
                0, 0, style.getFONT_BOLD(), style.getCOLOR_TEXT_PRIMARY()
        ));

        JLabel label = new JLabel("(Chưa có dữ liệu)");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(style.getCOLOR_TEXT_MUTED());
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
                new LineBorder(style.getCOLOR_BORDER()), "Thông tin dự án",
                0, 0, style.getFONT_BOLD(), style.getCOLOR_TEXT_PRIMARY()
        ));
        
        JTextArea textArea = new JTextArea(projectInfo);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(style.getCOLOR_CARD());
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
        headerLabel.setFont(style.getFONT_BOLD());
        headerLabel.setForeground(style.getCOLOR_TEXT_MUTED());
        headerLabel.setBorder(new EmptyBorder(8, 5, 5, 5));
        projectMenu.add(headerLabel);
        projectMenu.addSeparator();

        // Thêm các dự án
        for (String projectName : projects) {
            JMenuItem projectItem = new JMenuItem(projectName);
            projectItem.setFont(style.getFONT_NORMAL());
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
        createProjectMenuItem.setFont(style.getFONT_BOLD());
        createProjectMenuItem.setForeground(style.getCOLOR_PRIMARY());
        projectMenu.add(createProjectMenuItem);
    }

    // Phương thức để thêm ProjectTeamCard vào danh sách
    public void addProjectTeamCard(Project project, List<TeamMember> teamMembers) {
        ProjectTeamCard card = new ProjectTeamCard(project, teamMembers);
        card.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));
        teamsCardPanel.add(card);
        teamsCardPanel.add(Box.createVerticalStrut(10));
        
        teamsCardPanel.revalidate();
        teamsCardPanel.repaint();
    }

    // Phương thức để cập nhật ProjectTeamCard với dữ liệu mới
    public void addProjectTeamCards(List<Project> projects, List<List<TeamMember>> teamMembersLists) {
        teamsCardPanel.removeAll();
        
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            List<TeamMember> members = (i < teamMembersLists.size()) ? teamMembersLists.get(i) : new ArrayList<>();
            
            ProjectTeamCard card = new ProjectTeamCard(project, members);
            card.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));
            teamsCardPanel.add(card);
            teamsCardPanel.add(Box.createVerticalStrut(10));
        }
        
        teamsCardPanel.revalidate();
        teamsCardPanel.repaint();
    }

    // Phương thức để xóa tất cả ProjectTeamCard
    public void clearProjectTeamCards() {
        teamsCardPanel.removeAll();
        teamsCardPanel.revalidate();
        teamsCardPanel.repaint();
    }

    // Phương thức load sample data
    private void loadSampleData() {
        // Tạo dữ liệu mẫu
        List<Project> sampleProjects = new ArrayList<>();
        List<List<TeamMember>> sampleTeamMembers = new ArrayList<>();

        // Project 1
        Project project1 = new Project("proj001", "team001", "Website Redesign", "Thiết kế lại giao diện website");
        List<TeamMember> team1Members = new ArrayList<>();
        team1Members.add(new TeamMember("proj001", "user001", "role001"));
        team1Members.add(new TeamMember("proj001", "user002", "role002"));
        team1Members.add(new TeamMember("proj001", "user003", "role001"));
        
        sampleProjects.add(project1);
        sampleTeamMembers.add(team1Members);

        // Project 2
        Project project2 = new Project("proj002", "team001", "Mobile App Development", "Phát triển ứng dụng di động");
        List<TeamMember> team2Members = new ArrayList<>();
        team2Members.add(new TeamMember("proj002", "user004", "role002"));
        team2Members.add(new TeamMember("proj002", "user005", "role003"));
        
        sampleProjects.add(project2);
        sampleTeamMembers.add(team2Members);

        // Project 3
        Project project3 = new Project("proj003", "team002", "API Gateway", "Xây dựng API gateway");
        List<TeamMember> team3Members = new ArrayList<>();
        team3Members.add(new TeamMember("proj003", "user001", "role002"));
        team3Members.add(new TeamMember("proj003", "user006", "role001"));
        team3Members.add(new TeamMember("proj003", "user007", "role003"));
        team3Members.add(new TeamMember("proj003", "user008", "role001"));
        
        sampleProjects.add(project3);
        sampleTeamMembers.add(team3Members);

        // Thêm các card vào sidebar
        addProjectTeamCards(sampleProjects, sampleTeamMembers);
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
    public JButton getAddTeamButton() { return addTeamButton; }
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