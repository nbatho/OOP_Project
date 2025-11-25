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


    public TaskCard(String projectId, List<User> users) {
        this.projectId = projectId;
        if (users != null) this.users = users;

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


        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 0, 0, 0));


        formPanel.add(createLabel("Tiêu đề *"));
        txtTitle = createTextField();
        formPanel.add(txtTitle);
        formPanel.add(Box.createVerticalStrut(15));


        formPanel.add(createLabel("Mô tả"));
        txtDescription = createTextArea();
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setPreferredSize(new Dimension(0, 100));
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scrollDesc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(scrollDesc);
        formPanel.add(Box.createVerticalStrut(15));


        formPanel.add(createLabel("Người thực hiện"));
        cmbUser = new JComboBox<>(users.toArray(new User[0]));
        cmbUser.setRenderer(new UserRenderer());
        cmbUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cmbUser.setFont(style.getFONT_INPUT());
        formPanel.add(cmbUser);
        formPanel.add(Box.createVerticalStrut(15));


        JPanel row1 = new JPanel(new GridLayout(1, 2, 15, 0));
        row1.setOpaque(false);

        // Trạng thái
        JPanel stPanel = new JPanel(new GridLayout(2, 1));
        stPanel.setOpaque(false);
        stPanel.add(createLabel("Trạng thái"));
        cmbStatus = createComboBox(new String[]{"Todo", "In Progress", "Done"});
        stPanel.add(cmbStatus);

        // Ưu tiên
        JPanel prPanel = new JPanel(new GridLayout(2, 1));
        prPanel.setOpaque(false);
        prPanel.add(createLabel("Độ ưu tiên"));
        cmbPriority = createComboBox(new String[]{"Low", "Medium", "High", "Urgent"});
        prPanel.add(cmbPriority);

        row1.add(stPanel);
        row1.add(prPanel);
        formPanel.add(row1);
        formPanel.add(Box.createVerticalStrut(15));

        // ===== Ngày bắt đầu & Ngày kết thúc =====
        JPanel row2 = new JPanel(new GridLayout(1, 2, 15, 0));
        row2.setOpaque(false);

        JPanel startPanel = new JPanel(new GridLayout(2, 1));
        startPanel.setOpaque(false);
        startPanel.add(createLabel("Ngày bắt đầu"));
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy/MM/dd");
        startPanel.add(startDateChooser);

        JPanel endPanel = new JPanel(new GridLayout(2, 1));
        endPanel.setOpaque(false);
        endPanel.add(createLabel("Ngày kết thúc"));
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy/MM/dd");
        endPanel.add(endDateChooser);

        row2.add(startPanel);
        row2.add(endPanel);
        formPanel.add(row2);

        // ===== BUTTONS =====
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);

        btnCancel = new JButton("Hủy");
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());

        btnSave = new JButton("Lưu công việc");
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttons.add(btnCancel);
        buttons.add(btnSave);

        // Add to main
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttons, BorderLayout.SOUTH);

        add(mainPanel);
    }


    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(style.getFONT_NORMAL());
        label.setForeground(style.getCOLOR_TEXT_PRIMARY());
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

    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }

    public JTextField getTxtTitle() { return txtTitle; }
    public JTextArea getTxtDescription() { return txtDescription; }
    public JComboBox<User> getCmbUser() { return cmbUser; }
    public JComboBox<String> getCmbPriority() { return cmbPriority; }
    public JComboBox<String> getCmbStatus() { return cmbStatus; }
    public JDateChooser getStartDateChooser() { return startDateChooser; }
    public JDateChooser getEndDateChooser() { return endDateChooser; }

    public String getProjectId() { return projectId; }
}