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

    private final String projectId;
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
    private JButton btnDelete;
    private List<User> assignees = new ArrayList<>();
    private JTextArea txtComment;
    private JButton btnSendComment;
    private JPanel commentListPanel;

    private JPanel assignedUserPanel;
    private final boolean edited;

    public TaskCard(String projectId, List<User> users, boolean edited) {
        this.projectId = projectId;
        if (users != null) this.users = users;

        this.edited = edited;
        setTitle(edited ? "Cập nhật công việc" : "Tạo công việc mới");

        int height = edited ? 900 : 750;
        setSize(680, height);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);

        initUI();
        setVisible(true);
    }

    private void initUI() {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(style.getCOLOR_CARD());
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCCCCCC), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));


        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(edited ? "Cập nhật công việc" : "Tạo công việc mới");
        lblTitle.setFont(style.getFONT_TITLE());
        lblTitle.setForeground(style.getCOLOR_PRIMARY());

        JLabel lblSubtitle = new JLabel("Điền thông tin công việc. Nhấn lưu khi hoàn tất.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(style.getCOLOR_TEXT_MUTED());

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 8, 0);

        int row = 0;

        gbc.gridy = row++;
        formPanel.add(createLabel("Tiêu đề *"), gbc);

        gbc.gridy = row++;
        txtTitle = createTextField();
        txtTitle.setPreferredSize(new Dimension(0, 45));
        formPanel.add(txtTitle, gbc);


        gbc.gridy = row++;
        formPanel.add(createLabel("Mô tả"), gbc);

        gbc.gridy = row++;
        txtDescription = createTextArea();
        JScrollPane scrollDesc = new JScrollPane(txtDescription);


        scrollDesc.getViewport().setBackground(style.getCOLOR_CARD());
        scrollDesc.setBackground(style.getCOLOR_CARD());

        scrollDesc.setPreferredSize(new Dimension(0, 100));
        scrollDesc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(scrollDesc, gbc);


        gbc.gridy = row++;
        formPanel.add(createLabel("Người thực hiện"), gbc);
        gbc.gridy = row++;
        assignedUserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        assignedUserPanel.setOpaque(false);
        formPanel.add(assignedUserPanel, gbc);

        gbc.gridy = row++;
        cmbUser = new JComboBox<>(users.toArray(new User[0]));
        cmbUser.setRenderer(new UserRenderer());
        cmbUser.setFont(style.getFONT_INPUT());
        cmbUser.setPreferredSize(new Dimension(0, 45));
        cmbUser.setBackground(style.getCOLOR_CARD());
        cmbUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));

        formPanel.add(cmbUser, gbc);

        cmbUser.addActionListener(e -> {
            User selectedUser = (User) cmbUser.getSelectedItem();

            if (selectedUser != null && !assignees.contains(selectedUser)) {
                assignees.add(selectedUser);
                refreshAssigneePanel();
                cmbUser.setSelectedIndex(-1);
            }
        });


        gbc.gridy = row++;
        gbc.insets = new Insets(8, 0, 2, 0);

        JPanel dual1 = new JPanel(new GridLayout(1, 2, 10, 0));
        dual1.setOpaque(false);
        dual1.add(createLabel("Trạng thái"));
        dual1.add(createLabel("Độ ưu tiên"));

        formPanel.add(dual1, gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 10, 0);

        JPanel dual2 = new JPanel(new GridLayout(1, 2, 10, 0));
        dual2.setOpaque(false);

        cmbStatus = new JComboBox<>(new String[]{"TODO", "IN_PROGRESS", "DONE", "CANCELLED"});
        cmbStatus.setFont(style.getFONT_INPUT());
        cmbStatus.setPreferredSize(new Dimension(0, 45));
        cmbStatus.setBackground(style.getCOLOR_CARD());
        cmbStatus.setBorder(cmbUser.getBorder());

        cmbPriority = new JComboBox<>(new String[]{"HIGH", "MEDIUM", "LOW"});
        cmbPriority.setFont(style.getFONT_INPUT());
        cmbPriority.setPreferredSize(new Dimension(0, 45));
        cmbPriority.setBackground(style.getCOLOR_CARD());
        cmbPriority.setBorder(cmbUser.getBorder());

        dual2.add(cmbStatus);
        dual2.add(cmbPriority);

        formPanel.add(dual2, gbc);


        gbc.gridy = row++;
        JPanel dual3 = new JPanel(new GridLayout(1, 2, 10, 0));
        dual3.setOpaque(false);
        dual3.add(createLabel("Ngày bắt đầu"));
        dual3.add(createLabel("Ngày kết thúc"));
        formPanel.add(dual3, gbc);

        gbc.gridy = row++;
        JPanel dual4 = new JPanel(new GridLayout(1, 2, 10, 0));
        dual4.setOpaque(false);

        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();

        startDateChooser.setDateFormatString("yyyy/MM/dd");
        endDateChooser.setDateFormatString("yyyy/MM/dd");


        startDateChooser.setBackground(style.getCOLOR_CARD());
        endDateChooser.setBackground(style.getCOLOR_CARD());

        startDateChooser.setBorder(cmbUser.getBorder());
        endDateChooser.setBorder(cmbUser.getBorder());

        dual4.add(startDateChooser);
        dual4.add(endDateChooser);

        formPanel.add(dual4, gbc);


        if (edited) {
            gbc.gridy = row++;
            gbc.insets = new Insets(20, 0, 5, 0);
            formPanel.add(createLabel("Danh sách bình luận"), gbc);

            gbc.gridy = row++;
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;

            commentListPanel = new JPanel();
            commentListPanel.setLayout(new BoxLayout(commentListPanel, BoxLayout.Y_AXIS));
            commentListPanel.setBackground(style.getCOLOR_CARD());

            JScrollPane commentScroll = new JScrollPane(commentListPanel);

            commentScroll.getViewport().setBackground(style.getCOLOR_CARD());
            commentScroll.setBackground(style.getCOLOR_CARD());

            commentScroll.setBorder(BorderFactory.createLineBorder(style.getCOLOR_CARD(), 1, true));
            commentScroll.getVerticalScrollBar().setUnitIncrement(16);

            formPanel.add(commentScroll, gbc);

            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridy = row++;
            formPanel.add(createLabel("Viết bình luận mới"), gbc);

            gbc.gridy = row++;
            txtComment = createTextArea();
            JScrollPane newCommentScroll = new JScrollPane(txtComment);

            newCommentScroll.getViewport().setBackground(style.getCOLOR_CARD());
            newCommentScroll.setBackground(style.getCOLOR_CARD());

            newCommentScroll.setPreferredSize(new Dimension(0, 80));
            newCommentScroll.setBorder(cmbUser.getBorder());
            formPanel.add(newCommentScroll, gbc);

            gbc.gridy = row++;
            gbc.anchor = GridBagConstraints.EAST;
            btnSendComment = new JButton("Gửi bình luận");
            btnSendComment.setFont(style.getFONT_NORMAL());
            btnSendComment.setBackground(new Color(0x4CAF50));
            btnSendComment.setForeground(Color.WHITE);
            btnSendComment.setBorder(new EmptyBorder(8, 16, 8, 16));
            formPanel.add(btnSendComment, gbc);

            gbc.anchor = GridBagConstraints.WEST;
        }

        JScrollPane contentScroll = new JScrollPane(formPanel);
        contentScroll.setBorder(null);
        contentScroll.getVerticalScrollBar().setUnitIncrement(16);

        contentScroll.setBackground(style.getCOLOR_CARD());
        contentScroll.getViewport().setBackground(style.getCOLOR_CARD());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnDelete = new JButton("Xóa");
        btnDelete.setBackground(style.getCOLOR_CARD());
        btnDelete.setBorder(BorderFactory.createLineBorder(style.getCOLOR_BORDER()));
        btnDelete.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnCancel = new JButton("Hủy");
        btnCancel.setBackground(style.getCOLOR_CARD());
        btnCancel.setBorder(BorderFactory.createLineBorder(style.getCOLOR_BORDER()));
        btnCancel.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnCancel.addActionListener(e -> dispose());

        btnSave = new JButton(edited ? "Lưu chỉnh sửa" : "Lưu công việc");
        btnSave.setBackground(style.getCOLOR_PRIMARY());
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorder(new EmptyBorder(10, 20, 10, 20));

        if (isEdited()) {
            footer.add(btnDelete);
        }
        footer.add(btnCancel);
        footer.add(btnSave);

        wrapper.add(headerPanel, BorderLayout.NORTH);
        wrapper.add(contentScroll, BorderLayout.CENTER);
        wrapper.add(footer, BorderLayout.SOUTH);

        add(wrapper);
    }


    private void refreshAssigneePanel() {
        assignedUserPanel.removeAll();
        for (User user : assignees) {
            JLabel avatarLabel = new JLabel(getInitials(user.getFullName()), SwingConstants.CENTER);
            avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            avatarLabel.setForeground(style.getCOLOR_CARD());
            avatarLabel.setOpaque(true);
            avatarLabel.setBackground(style.getCOLOR_PRIMARY());
            avatarLabel.setPreferredSize(new Dimension(35, 35));
            avatarLabel.setBorder(new EmptyBorder(4, 4, 4, 4));

            avatarLabel.setToolTipText(user.getFullName() + " (Click để xóa)");
            avatarLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    assignees.remove(user);
                    refreshAssigneePanel();
                }
                public void mouseEntered(MouseEvent e) {
                    avatarLabel.setBackground(Color.RED);
                    avatarLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(MouseEvent e) {
                    avatarLabel.setBackground(style.getCOLOR_PRIMARY());
                    avatarLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            assignedUserPanel.add(avatarLabel);
        }

        assignedUserPanel.revalidate();
        assignedUserPanel.repaint();
    }

    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "--";
        String cleaned = fullName.trim();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cleaned.length() && sb.length() < 2; i++) {
            char c = cleaned.charAt(i);
            if (!Character.isWhitespace(c)) sb.append(Character.toUpperCase(c));
        }
        String initials = sb.toString();
        if (initials.isEmpty()) initials = "--";
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

        if (task.getAssignedUsers() != null) {
            this.assignees = new ArrayList<>(task.getAssignedUsers());
            refreshAssigneePanel();
        }

        cmbPriority.setSelectedItem(task.getPriority());
        cmbStatus.setSelectedItem(task.getStatus());
        if (task.getDueDate() != null) {
            endDateChooser.setDate(task.getDueDate());
        }
        if (task.getStartDate() != null) {
            startDateChooser.setDate(task.getStartDate());
        }
    }

    // Getters
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }
    public JButton getBtnDelete() {
        return btnDelete;
    }
    public JButton getBtnSendComment() { return btnSendComment; }

    public JTextField getTxtTitle() { return txtTitle; }
    public JTextArea getTxtDescription() { return txtDescription; }
    public JTextArea getTxtComment() { return txtComment; }

    public JComboBox<User> getCmbUser() { return cmbUser; }
    public JComboBox<String> getCmbPriority() { return cmbPriority; }
    public JComboBox<String> getCmbStatus() { return cmbStatus; }

    public JDateChooser getStartDateChooser() { return startDateChooser; }
    public JDateChooser getEndDateChooser() { return endDateChooser; }

    public boolean isEdited() { return edited; }
    public String getProjectId() { return projectId; }


    public void addCommentToList(String userName, String commentText, String timestamp) {
        JPanel commentItem = createCommentItem(userName, commentText, timestamp);
        commentListPanel.add(commentItem);
        commentListPanel.add(Box.createVerticalStrut(8));
        commentListPanel.revalidate();
        commentListPanel.repaint();
    }

    public void clearComments() {
        if (commentListPanel != null) {
            commentListPanel.removeAll();
            commentListPanel.revalidate();
            commentListPanel.repaint();
        }
    }

    private JPanel createCommentItem(String userName, String commentText, String timestamp) {
        JPanel item = new JPanel();
        item.setLayout(new BorderLayout(8, 5));
        item.setBackground(new Color(0xF8F9FA));
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE9ECEF), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));


        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblUserName = new JLabel(userName);
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUserName.setForeground(style.getCOLOR_PRIMARY());

        JLabel lblTimestamp = new JLabel(timestamp);
        lblTimestamp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTimestamp.setForeground(style.getCOLOR_TEXT_MUTED());

        header.add(lblUserName, BorderLayout.WEST);
        header.add(lblTimestamp, BorderLayout.EAST);

        JTextArea txtContent = new JTextArea(commentText);
        txtContent.setFont(style.getFONT_NORMAL());
        txtContent.setForeground(style.getCOLOR_TEXT_PRIMARY());
        txtContent.setBackground(new Color(0xF8F9FA));
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setEditable(false);
        txtContent.setBorder(null);

        item.add(header, BorderLayout.NORTH);
        item.add(txtContent, BorderLayout.CENTER);

        return item;
    }
    public boolean validateComment() {
        if (txtComment.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Bình luận không được để trống!",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    public boolean validateInput() {
        if (txtTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tiêu đề không được để trống!",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (assignees.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Người được giao task không được để trống!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (startDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                    "Ngày bắt đầu không được để trống!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (endDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                    "Ngày kết thúc không được để trống!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    public void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
    public boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    public List<User> getAssignees() {
        return assignees;
    }

}