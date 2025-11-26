package main.java.component;

import com.toedter.calendar.JDateChooser;
import main.java.model.*;
import main.java.view.GlobalStyle;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.util.List;

public class TaskCard extends JFrame {

    private final GlobalStyle style = new GlobalStyle();


    private String projectId;
    private List<User> users = new ArrayList<>();


    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JComboBox<User> cmbUser;
    private JComboBox<String> cmbPriority;
    private JComboBox<String> cmbStatus;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JButton btnCancel;
    private JButton btnSave;

    private JPanel assignedUserPanel;
    private boolean edited;
    public TaskCard(String projectId, List<User> users,boolean edited) {
        this.projectId = projectId;
        if (users != null) this.users = users;

        this.edited = edited;
        setTitle("Tạo công việc mới");
        setSize(680, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);

        initUI();
        setVisible(true);
    }


    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCCCCCC), 1),
                new EmptyBorder(25, 30, 25, 30)
        ));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Tạo công việc mới");
        lblTitle.setFont(style.getFONT_TITLE());
        lblTitle.setForeground(style.getCOLOR_TEXT_PRIMARY());

        JLabel lblSubtitle = new JLabel("Điền thông tin công việc. Nhấn lưu khi hoàn tất.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(style.getCOLOR_TEXT_MUTED());

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Form Panel với GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);

        int row = 0;

        // Tiêu đề *
        gbc.gridy = row++;
        formPanel.add(createLabel("Tiêu đề *"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtTitle = createTextField();
        txtTitle.setPreferredSize(new Dimension(0, 45));
        formPanel.add(txtTitle, gbc);

        // Mô tả
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(createLabel("Mô tả"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtDescription = createTextArea();
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setPreferredSize(new Dimension(0, 100));
        scrollDesc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(scrollDesc, gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 10, 0);

        assignedUserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        assignedUserPanel.setOpaque(false);
        assignedUserPanel.setPreferredSize(new Dimension(0, 50));
        formPanel.add(assignedUserPanel, gbc);


        // Người thực hiện
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(createLabel("Người thực hiện"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 15, 0);
        cmbUser = new JComboBox<>(users.toArray(new User[0]));
        cmbUser.setRenderer(new UserRenderer());
        cmbUser.setPreferredSize(new Dimension(0, 45));
        cmbUser.setFont(style.getFONT_INPUT());
        formPanel.add(cmbUser, gbc);

        cmbUser.addActionListener(e -> {
            User selected = (User) cmbUser.getSelectedItem();
            if (selected != null) {
                addAssignedUserAvatar(selected);
            }
        });

        gbc.gridy = row++;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 0, 5, 8);
        formPanel.add(createLabel("Trạng thái"), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 8, 5, 0);
        formPanel.add(createLabel("Độ ưu tiên"), gbc);

        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 15, 8);
        cmbStatus = createComboBox(new String[]{"TODO", "IN_PROGRESS", "DONE", "CANCELLED"});
        cmbStatus.setPreferredSize(new Dimension(0, 45));
        formPanel.add(cmbStatus, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 8, 15, 0);
        cmbPriority = createComboBox(new String[]{"HIGH", "MEDIUM", "LOW"});
        cmbPriority.setPreferredSize(new Dimension(0, 45));
        formPanel.add(cmbPriority, gbc);

        // Row 2: Ngày bắt đầu & Ngày kết thúc (2 cột)
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 5, 8);
        formPanel.add(createLabel("Ngày bắt đầu"), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 8, 5, 0);
        formPanel.add(createLabel("Ngày kết thúc"), gbc);

        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 8);
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy/MM/dd");
        startDateChooser.setPreferredSize(new Dimension(0, 45));
        formPanel.add(startDateChooser, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 8, 0, 0);
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy/MM/dd");
        endDateChooser.setPreferredSize(new Dimension(0, 45));
        formPanel.add(endDateChooser, gbc);

        // Buttons Panel
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnCancel = new JButton("Hủy");
        btnCancel.setFont(style.getFONT_NORMAL());
        btnCancel.setForeground(style.getCOLOR_TEXT_PRIMARY());
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());

        if (edited) {
            btnSave = new JButton("Lưu công việc");
        }
        else {
            btnSave = new JButton("Lưu chỉnh sửa");
        }
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(style.getCOLOR_PRIMARY());
        btnSave.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttons.add(btnCancel);
        buttons.add(btnSave);

        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttons, BorderLayout.SOUTH);

        add(mainPanel);
    }
    private void addAssignedUserAvatar(User user) {
        assignedUserPanel.removeAll();

        JLabel avatarLabel = new JLabel(getInitials(user.getFullName()), SwingConstants.CENTER);
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(style.getCOLOR_PRIMARY());
        avatarLabel.setPreferredSize(new Dimension(40, 40));
        avatarLabel.setBorder(new EmptyBorder(4, 4, 4, 4));

        assignedUserPanel.add(avatarLabel);
        assignedUserPanel.revalidate();
        assignedUserPanel.repaint();
    }
    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "--";
        String cleaned = fullName.trim();
        // return the first two non-space characters (e.g., "Nguyễn Minh" -> "NM", "tho" -> "TH")
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cleaned.length() && sb.length() < 2; i++) {
            char c = cleaned.charAt(i);
            if (!Character.isWhitespace(c)) sb.append(Character.toUpperCase(c));
        }
        String initials = sb.toString();
        if (initials.length() == 0) initials = "--";
        return initials;
    }
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(style.getFONT_NORMAL());
        label.setForeground(style.getCOLOR_TEXT_PRIMARY());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(style.getFONT_INPUT());
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(style.getFONT_INPUT());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(style.getFONT_INPUT());
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return combo;
    }


    private static class UserRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof User u) {
                setText(u.getFullName());
            }
            return this;
        }
    }
    public void setTaskData(Task task) {
        txtTitle.setText(task.getTitle());
        txtDescription.setText(task.getDescription());

        // Chọn user
        for (int i = 0; i < cmbUser.getItemCount(); i++) {
            addAssignedUserAvatar(cmbUser.getItemAt(i));

        }

        cmbPriority.setSelectedItem(task.getPriority());
        cmbStatus.setSelectedItem(task.getStatus());

//        startDateChooser.setDate(task.getStartDate());
        endDateChooser.setDate(task.getDueDate());

        // Đổi tiêu đề & nút để người dùng biết đây là update
        setTitle("Cập nhật công việc");
        btnSave.setText("Cập nhật");
    }
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }

    public JTextField getTxtTitle() { return txtTitle; }
    public JTextArea getTxtDescription() { return txtDescription; }
    public JComboBox<User> getCmbUser() { return cmbUser; }
    public JComboBox<String> getCmbPriority() { return cmbPriority; }
    public JComboBox<String> getCmbStatus() { return cmbStatus; }
    public JDateChooser getStartDateChooser() { return startDateChooser; }
    public JDateChooser getEndDateChooser() { return endDateChooser; }

    public boolean isEdited() {
        return edited;
    }

    public String getProjectId() { return projectId; }
}