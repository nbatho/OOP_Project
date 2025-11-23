package main.java.view;

import main.java.model.User;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    // Menu items
    private JMenuItem infoMenuItem;
    private JMenuItem logoutMenuItem;
    private JPopupMenu userMenu;

    // Panels
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Teams card panel
    private JPanel membersCardPanel;
    private JScrollPane teamsScrollPane;
    private JButton addMemberButton;

    // Lưu reference đến projectInfoPanel để update sau
    private JPanel projectInfoPanel;

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

        // Title panel with dropdown
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

        titlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (projectMenu != null) {
                    projectMenu.show(titlePanel, 0, titlePanel.getHeight());
                }
            }
        });

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        topRow.add(titlePanel, gbc);

        // Toggle view buttons
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

        // Search and action buttons
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

        JLabel headerLabel = new JLabel("  Dự án của bạn");
        headerLabel.setFont(style.getFONT_BOLD());
        headerLabel.setForeground(style.getCOLOR_TEXT_MUTED());
        headerLabel.setBorder(new EmptyBorder(8, 5, 5, 5));
        projectMenu.add(headerLabel);
        projectMenu.addSeparator();

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

        // Project info placeholder
        projectInfoPanel = createPlaceholderCard("Thông tin dự án");
        sidebarPanel.add(projectInfoPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Teams section header
        JLabel teamsLabel = new JLabel("Thành viên nhóm");
        teamsLabel.setFont(style.getFONT_BOLD());
        teamsLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());
        teamsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        sidebarPanel.add(teamsLabel);

        // Teams card panel with scroll
        membersCardPanel = new JPanel();
        membersCardPanel.setLayout(new BoxLayout(membersCardPanel, BoxLayout.Y_AXIS));
        membersCardPanel.setBackground(style.getCOLOR_CARD());

        teamsScrollPane = new JScrollPane(membersCardPanel);
        teamsScrollPane.setPreferredSize(new Dimension(320, 300));
        teamsScrollPane.setBorder(null);
        teamsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        sidebarPanel.add(teamsScrollPane);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Add member button
        addMemberButton = new JButton("+ Thêm thành viên mới");
        addMemberButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addMemberButton.setForeground(Color.WHITE);
        addMemberButton.setBackground(style.getCOLOR_PRIMARY());
        addMemberButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addMemberButton.setFocusPainted(false);
        addMemberButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMemberButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addMemberButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(addMemberButton);

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

    private JPanel createMemberCard(User user) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(style.getCOLOR_BORDER(), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(320, 80));

        // Avatar
        JLabel avatarLabel = new JLabel("X");
        avatarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        avatarLabel.setPreferredSize(new Dimension(50, 50));

        // User info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setFont(style.getFONT_BOLD());

        JLabel emailLabel = new JLabel(user.getEmail());
        emailLabel.setFont(style.getFONT_NORMAL());
        emailLabel.setForeground(style.getCOLOR_TEXT_MUTED());

        infoPanel.add(nameLabel);
        infoPanel.add(emailLabel);

        card.add(avatarLabel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    public void setCurrentProjectName(String projectName) {
        titleLabel.setText(projectName);
    }

    public void updateSidebarProjectInfo(String projectInfo) {
        JPanel newProjectInfoCard = new JPanel(new BorderLayout());
        newProjectInfoCard.setOpaque(false);
        newProjectInfoCard.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(style.getCOLOR_BORDER()), "Thông tin dự án",
                0, 0, style.getFONT_BOLD(), style.getCOLOR_TEXT_PRIMARY()
        ));

        JTextArea textArea = new JTextArea(projectInfo);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(style.getCOLOR_CARD());
        newProjectInfoCard.add(new JScrollPane(textArea), BorderLayout.CENTER);

        Component[] components = sidebarPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] == projectInfoPanel) {
                sidebarPanel.remove(i);
                sidebarPanel.add(newProjectInfoCard, i);
                projectInfoPanel = newProjectInfoCard;
                break;
            }
        }

        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }

    public void updateMembersList(List<User> users) {
        membersCardPanel.removeAll();

        for (User user : users) {
            JPanel memberCard = createMemberCard(user);
            membersCardPanel.add(memberCard);
            membersCardPanel.add(Box.createVerticalStrut(10));
        }

        membersCardPanel.revalidate();
        membersCardPanel.repaint();
    }

    public void updateProjectList(String[] projects) {
        projectMenu.removeAll();

        // Header
        JLabel headerLabel = new JLabel("  Dự án của bạn");
        headerLabel.setFont(style.getFONT_BOLD());
        headerLabel.setForeground(style.getCOLOR_TEXT_MUTED());
        headerLabel.setBorder(new EmptyBorder(8, 5, 5, 5));
        projectMenu.add(headerLabel);
        projectMenu.addSeparator();

        // Add project items
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

        // Create project button
        createProjectMenuItem = new JMenuItem("+ Tạo dự án mới");
        createProjectMenuItem.setFont(style.getFONT_BOLD());
        createProjectMenuItem.setForeground(style.getCOLOR_PRIMARY());
        projectMenu.add(createProjectMenuItem);
    }

    public void clearMembersList() {
        membersCardPanel.removeAll();
        membersCardPanel.revalidate();
        membersCardPanel.repaint();
    }


    public void setProjectSelectionListener(ProjectSelectionListener listener) {
        this.projectSelectionListener = listener;
    }

    public interface ProjectSelectionListener {
        void onProjectSelected(String projectName);
    }


    public JToggleButton getKanbanButton() { return kanbanButton; }
    public JToggleButton getTableButton() { return tableButton; }
    public JToggleButton getCalendarButton() { return calendarButton; }
    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getCreateButton() { return createButton; }
    public JButton getNotifyButton() { return notifyButton; }
    public JButton getUserButton() { return userButton; }
    public JButton getaddMemberButton() { return addMemberButton; }
    public JPanel getMainContentPanel() { return mainContentPanel; }
    public CardLayout getCardLayout() { return cardLayout; }
    public JMenuItem getInfoMenuItem() { return infoMenuItem; }
    public JMenuItem getLogoutMenuItem() { return logoutMenuItem; }
    public JMenuItem getCreateProjectMenuItem() { return createProjectMenuItem; }
    public JLabel getTitleLabel() { return titleLabel; }
}