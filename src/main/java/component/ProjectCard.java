package main.java.component;

import main.java.view.GlobalStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class ProjectCard extends JFrame {
    GlobalStyle style = new GlobalStyle();

    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JButton btnCancel;
    private JButton btnSave;

    public ProjectCard() {
        setTitle("Tạo công việc mới");
        setSize(680, 500);
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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Tạo dự án mới");
        lblTitle.setFont(style.getFONT_TITLE());
        lblTitle.setForeground(style.getCOLOR_TEXT_MUTED());

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
                BorderFactory.createEmptyBorder(4, 10, 8, 10)
        ));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(scrollDesc);

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
        label.setBorder(new EmptyBorder(0, 0, 4, 0));
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

    public JTextField getTxtTitle() {
        return txtTitle;
    }

    public JTextArea getTxtDescription() {
        return txtDescription;
    }

    public JButton getBtnSave() {
        return btnSave;
    }
}
