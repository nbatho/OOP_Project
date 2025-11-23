package main.java.component;

import main.java.view.GlobalStyle;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class TaskCard extends JFrame {
    GlobalStyle style = new GlobalStyle();
    // Components
    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JTextField txtAssignee;
    private JComboBox<String> cmbCategory;
    private JComboBox<String> cmbPriority;
    private JComboBox<String> cmbStatus;
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JButton btnCancel;
    private JButton btnSave;

    public TaskCard() {
        setTitle("Tạo công việc mới");
        setSize(680, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);


        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                dispose();
            }
        });

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

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Tạo công việc mới");
        lblTitle.setFont(style.getFONT_TITLE());
        lblTitle.setForeground(style.getCOLOR_TEXT_PRIMARY());

        JLabel lblSubtitle = new JLabel("Điền thông tin công việc. Nhấn lưu khi hoàn tất.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(style.getCOLOR_TEXT_MUTED());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Tiêu đề *
        formPanel.add(createLabel("Tiêu đề *"));
        txtTitle = createTextField();
        formPanel.add(txtTitle);
        formPanel.add(Box.createVerticalStrut(15));

        // Mô tả
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

        // Người thực hiện
        formPanel.add(createLabel("Người thực hiện"));
        txtAssignee = createTextField();
        formPanel.add(txtAssignee);
        formPanel.add(Box.createVerticalStrut(15));

        // Hạng mục và Độ ưu tiên (2 cột)
        JPanel row1 = new JPanel(new GridLayout(1, 2, 15, 0));
        row1.setOpaque(false);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setOpaque(false);
        categoryPanel.add(createLabel("Hạng mục"));
        cmbCategory = createComboBox(new String[]{"Frontend", "Backend", "Design", "Testing"});
        categoryPanel.add(cmbCategory);

        JPanel priorityPanel = new JPanel();
        priorityPanel.setLayout(new BoxLayout(priorityPanel, BoxLayout.Y_AXIS));
        priorityPanel.setOpaque(false);
        priorityPanel.add(createLabel("Độ ưu tiên"));
        cmbPriority = createComboBox(new String[]{"Trung bình", "Thấp", "Cao", "Khẩn cấp"});
        priorityPanel.add(cmbPriority);

        row1.add(categoryPanel);
        row1.add(priorityPanel);
        formPanel.add(row1);
        formPanel.add(Box.createVerticalStrut(15));

        // Trạng thái
        formPanel.add(createLabel("Trạng thái"));
        cmbStatus = createComboBox(new String[]{"Cần làm", "Đang làm", "Hoàn thành"});
        formPanel.add(cmbStatus);
        formPanel.add(Box.createVerticalStrut(15));

        // Ngày bắt đầu và Ngày hết hạn (2 cột)
        JPanel row2 = new JPanel(new GridLayout(1, 2, 15, 0));
        row2.setOpaque(false);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel startDatePanel = new JPanel();
        startDatePanel.setLayout(new BoxLayout(startDatePanel, BoxLayout.Y_AXIS));
        startDatePanel.setOpaque(false);
        startDatePanel.add(createLabel("Ngày bắt đầu"));
        txtStartDate = createDateField();
        startDatePanel.add(txtStartDate);

        JPanel endDatePanel = new JPanel();
        endDatePanel.setLayout(new BoxLayout(endDatePanel, BoxLayout.Y_AXIS));
        endDatePanel.setOpaque(false);
        endDatePanel.add(createLabel("Ngày hết hạn"));
        txtEndDate = createDateField();
        endDatePanel.add(txtEndDate);

        row2.add(startDatePanel);
        row2.add(endDatePanel);
        formPanel.add(row2);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnCancel = new JButton("Hủy");
        btnCancel.setFont(style.getFONT_NORMAL());
        btnCancel.setForeground(style.getCOLOR_TEXT_PRIMARY());
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());

        btnSave = new JButton("Lưu công việc");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(style.getCOLOR_PRIMARY());
        btnSave.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);

        // Add all to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(style.getFONT_NORMAL());
        label.setForeground(style.getCOLOR_TEXT_PRIMARY());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(style.getFONT_INPUT());
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(style.getFONT_INPUT());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        return area;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(style.getFONT_INPUT());
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return combo;
    }

    private JTextField createDateField() {
        JTextField field = new JTextField();
        field.setFont(style.getFONT_INPUT());
        field.setForeground(style.getCOLOR_TEXT_PRIMARY());
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    // Getters
    public JTextField getTxtTitle() {
        return txtTitle;
    }

    public JTextArea getTxtDescription() {
        return txtDescription;
    }

    public JTextField getTxtAssignee() {
        return txtAssignee;
    }

    public JComboBox<String> getCmbPriority() {
        return cmbPriority;
    }

    public JComboBox<String> getCmbStatus() {
        return cmbStatus;
    }

    public JTextField getTxtStartDate() {
        return txtStartDate;
    }

    public JTextField getTxtEndDate() {
        return txtEndDate;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

}