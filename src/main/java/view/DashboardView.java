package main.java.view;

import main.java.model.Project;
import main.java.model.User;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

public class DashboardView extends JFrame {
    private final GlobalStyle style = new GlobalStyle();
    private JToggleButton kanbanButton;
    private JToggleButton calendarButton;
    private JButton createTaskButton;
    private JButton userButton;
    private JLabel titleLabel;
    private JPopupMenu projectMenu;
    private JMenuItem createProjectMenuItem;
    private ProjectSelectionListener projectSelectionListener;

    private JMenuItem logoutMenuItem;
    private JPopupMenu userMenu;

    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private KanbanView kanbanView;
    private JPanel membersCardPanel;
    private JButton addMemberButton;
    private JLabel projectNameLabel;
    private JTextArea projectDescriptionArea;
    private JPanel projectInfoPanel;

    private OnMemberDeleteListener memberDeleteListener;

    public DashboardView() {
        setTitle("Quản lý Công việc Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);
        initUI();
        createUserMenu();
        createProjectMenu();
        showDefaultMainContent();
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

    private void showDefaultMainContent() {
        this.kanbanView = new KanbanView();
        int boardWidth = kanbanView.getColumnWidth() * 4 + GlobalStyle.scale(16) * 3 + GlobalStyle.scale(30);
        kanbanView.setPreferredSize(new Dimension(boardWidth, GlobalStyle.scale(800)));

        JScrollPane kanbanScroll = new JScrollPane(kanbanView,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        kanbanScroll.getHorizontalScrollBar().setUnitIncrement(20);
        kanbanScroll.setBorder(null);

        mainContentPanel.add(kanbanScroll, "KANBAN");
        cardLayout.show(mainContentPanel, "KANBAN");
    }

    public KanbanView getKanbanView() {
        return this.kanbanView;
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
        JPanel titlePanel = createTitlePanel();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        topRow.add(titlePanel, gbc);

        // View toggle buttons
        JPanel viewTogglePanel = createViewTogglePanel();
        gbc.gridx = 1;
        gbc.weightx = 0;
        topRow.add(viewTogglePanel, gbc);

        // Create task button
        gbc.gridx = 2;
        createTaskButton = createStyledButton("Tạo mới", style.getCOLOR_PRIMARY(), Color.WHITE);
        topRow.add(createTaskButton, gbc);

        // User button
        gbc.gridx = 3;
        userButton = new JButton("User");
        userButton.setFont(GlobalStyle.scaleFont(style.getFONT_NORMAL()));
        userButton.setBackground(Color.WHITE);
        userButton.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        userButton.addActionListener(e -> {
            if (userMenu != null) {
                userMenu.show(userButton, 0, userButton.getHeight());
            }
        });
        topRow.add(userButton, gbc);

        headerPanel.add(topRow, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setOpaque(false);
        titlePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        titleLabel = new JLabel("Quản lý");
        titleLabel.setFont(GlobalStyle.scaleFont(style.getFONT_TITLE()));
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

        return titlePanel;
    }

    private JPanel createViewTogglePanel() {
        kanbanButton = new JToggleButton("Kanban", true);
        calendarButton = new JToggleButton("Lịch");

        ButtonGroup viewGroup = new ButtonGroup();
        viewGroup.add(kanbanButton);
        viewGroup.add(calendarButton);

        JPanel viewTogglePanel = new JPanel();
        viewTogglePanel.setOpaque(false);
        viewTogglePanel.add(kanbanButton);
        viewTogglePanel.add(calendarButton);

        return viewTogglePanel;
    }

    private JButton createStyledButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(GlobalStyle.scaleFont(style.getFONT_NORMAL()));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        return button;
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
        logoutMenuItem = new JMenuItem("Đăng xuất");
        userMenu.add(logoutMenuItem);
    }

    private JPanel createSidebarPanel() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(style.getCOLOR_CARD());
        sidebarPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        sidebarPanel.setPreferredSize(new Dimension(GlobalStyle.scale(420), 0));

        // Project info card
        projectInfoPanel = createProjectInfoPanel();
        sidebarPanel.add(projectInfoPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Teams section
        JLabel teamsLabel = new JLabel("Thành viên nhóm");
        teamsLabel.setFont(style.getFONT_BOLD());
        teamsLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());
        teamsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        sidebarPanel.add(teamsLabel);

        // Members panel with scroll
        membersCardPanel = new JPanel();
        membersCardPanel.setLayout(new BoxLayout(membersCardPanel, BoxLayout.Y_AXIS));
        membersCardPanel.setBackground(style.getCOLOR_CARD());

        JScrollPane teamsScrollPane = new JScrollPane(membersCardPanel);
        teamsScrollPane.setPreferredSize(new Dimension(GlobalStyle.scale(360), GlobalStyle.scale(420)));
        teamsScrollPane.setBorder(null);
        teamsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        sidebarPanel.add(teamsScrollPane);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Add member button
        addMemberButton = createStyledButton("+ Thêm thành viên mới", style.getCOLOR_PRIMARY(), Color.WHITE);
        sidebarPanel.add(addMemberButton);

        return sidebarPanel;
    }

    private JPanel createProjectInfoPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(style.getCOLOR_PRIMARY());
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(style.getCOLOR_PRIMARY()),
                new EmptyBorder(16, 16, 16, 16)
        ));

        projectNameLabel = new JLabel("");
        projectNameLabel.setFont(GlobalStyle.scaleFont(style.getFONT_BOLD()));
        projectNameLabel.setForeground(Color.WHITE);
        card.add(projectNameLabel, BorderLayout.NORTH);

        projectDescriptionArea = new JTextArea("");
        projectDescriptionArea.setEditable(false);
        projectDescriptionArea.setOpaque(false);
        projectDescriptionArea.setForeground(new Color(0xDCEFEF));
        projectDescriptionArea.setFont(GlobalStyle.scaleFont(style.getFONT_NORMAL()));
        projectDescriptionArea.setLineWrap(true);
        projectDescriptionArea.setWrapStyleWord(true);
        card.add(projectDescriptionArea, BorderLayout.CENTER);

        return card;
    }

    /**
     * Tính initials từ tên đầy đủ (lấy 2 ký tự đầu không phải khoảng trắng)
     */
    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "--";
        }

        String cleaned = fullName.trim();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cleaned.length() && sb.length() < 2; i++) {
            char c = cleaned.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(Character.toUpperCase(c));
            }
        }

        return !sb.isEmpty() ? sb.toString() : "--";
    }

    private JPanel createMemberCard(User user) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(style.getCOLOR_BORDER(), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, GlobalStyle.scale(90)));

        // Avatar panel
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);

        JLabel avatarLabel = createAvatarLabel(user);
        left.add(avatarLabel, BorderLayout.CENTER);

        // User info panel
        JPanel infoPanel = createUserInfoPanel(user);

        card.add(left, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createAvatarLabel(User user) {
        JLabel avatarLabel = new JLabel(getInitials(user.getFullName()), SwingConstants.CENTER);
        avatarLabel.setFont(GlobalStyle.scaleFont(new Font("Segoe UI", Font.BOLD, 18)));
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(style.getCOLOR_PRIMARY());
        avatarLabel.setPreferredSize(new Dimension(GlobalStyle.scale(56), GlobalStyle.scale(56)));
        avatarLabel.setBorder(new EmptyBorder(6, 6, 6, 6));

        avatarLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        DashboardView.this,
                        "Bạn có chắc muốn xóa user?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION && memberDeleteListener != null) {
                    memberDeleteListener.onDelete(user);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                avatarLabel.setBackground(Color.RED);
                avatarLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                avatarLabel.setToolTipText("Xóa thành viên này");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                avatarLabel.setBackground(style.getCOLOR_PRIMARY());
                avatarLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return avatarLabel;
    }

    private JPanel createUserInfoPanel(User user) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setFont(GlobalStyle.scaleFont(style.getFONT_TITLE()));

        JLabel emailLabel = new JLabel(user.getEmail());
        emailLabel.setFont(GlobalStyle.scaleFont(style.getFONT_NORMAL()));
        emailLabel.setForeground(style.getCOLOR_TEXT_MUTED());

        infoPanel.add(nameLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(Box.createVerticalStrut(8));

        return infoPanel;
    }

    public void showAddMemberPopup(List<User> users, Consumer<User> onUserSelected) {
        JPopupMenu popupMenu = new JPopupMenu();

        DefaultListModel<User> model = new DefaultListModel<>();
        if (users != null) {
            for (User u : users) {
                model.addElement(u);
            }
        }

        JList<User> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                User user = (User) value;
                String txt = user.getFullName() + " (" + user.getEmail() + ")";

                return super.getListCellRendererComponent(
                        list, txt, index, isSelected, cellHasFocus
                );
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    User selected = list.getSelectedValue();
                    if (selected != null && onUserSelected != null) {
                        onUserSelected.accept(selected);
                    }
                    popupMenu.setVisible(false);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(250, 200));

        popupMenu.add(scrollPane);
        popupMenu.show(addMemberButton, 0, addMemberButton.getHeight());
    }


    public void setCurrentProjectName(String projectName) {
        titleLabel.setText(projectName);
        projectNameLabel.setText(projectName);
    }

    public void updateSidebarProjectInfo(Project project) {
        String projectInfo = "Tên: " + project.getName() + "\n" + "Mô tả: " + project.getDescription() + "\n" + "Ngày tạo: " + project.getCreatedAt() + "\n";
        projectDescriptionArea.setText(projectInfo);
        projectInfoPanel.revalidate();
        projectInfoPanel.repaint();
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

        JLabel headerLabel = new JLabel("  Dự án của bạn");
        headerLabel.setFont(style.getFONT_BOLD());
        headerLabel.setForeground(style.getCOLOR_TEXT_MUTED());
        headerLabel.setBorder(new EmptyBorder(8, 5, 5, 5));
        projectMenu.add(headerLabel);
        projectMenu.addSeparator();

        for (String projectName : projects) {
            JMenuItem projectItem = new JMenuItem(projectName);
            projectItem.addActionListener(e -> {
                setCurrentProjectName(projectName);
                if (projectSelectionListener != null) {
                    projectSelectionListener.onProjectSelected(projectName);
                }
            });
            projectMenu.add(projectItem);
        }

        projectMenu.addSeparator();
        projectMenu.add(createProjectMenuItem);
    }

    public void setUserInitials(String fullName) {
        userButton.setText(getInitials(fullName));
    }


    public void setMemberDeleteListener(OnMemberDeleteListener listener) {
        this.memberDeleteListener = listener;
    }

    public void setProjectSelectionListener(ProjectSelectionListener listener) {
        this.projectSelectionListener = listener;
    }

    public interface OnMemberDeleteListener {
        void onDelete(User user);
    }

    public interface ProjectSelectionListener {
        void onProjectSelected(String projectName);
    }
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
    }

    public boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public JToggleButton getKanbanButton() { return kanbanButton; }
    public JToggleButton getCalendarButton() { return calendarButton; }
    public JButton getCreateTaskButton() { return createTaskButton; }
    public JButton getaddMemberButton() { return addMemberButton; }
    public JPanel getMainContentPanel() { return mainContentPanel; }
    public CardLayout getCardLayout() { return cardLayout; }
    public JMenuItem getLogoutMenuItem() { return logoutMenuItem; }
    public JMenuItem getCreateProjectMenuItem() { return createProjectMenuItem; }
}