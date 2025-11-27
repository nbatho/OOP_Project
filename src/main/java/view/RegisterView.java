package main.java.view;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtRePassword;
    private JCheckBox chkAgree;
    private JButton btnRegister;
    private JLabel lblLoginLink;

    public RegisterView() {
        setTitle("Đăng ký");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GlobalStyle.scale(600), GlobalStyle.scale(800));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JLabel lblTitle = new JLabel("Task Manager", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, GlobalStyle.scale(28)));
        lblTitle.setForeground(new Color(0x005B50));

        JLabel lblSubtitle = new JLabel("Quản lý công việc nhóm hiệu quả", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(15)));
        lblSubtitle.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, GlobalStyle.scale(5)));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        // --- Card chính ---
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(GlobalStyle.scale(25), GlobalStyle.scale(20), GlobalStyle.scale(25), GlobalStyle.scale(20))
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(GlobalStyle.scale(6), 0, GlobalStyle.scale(6), 0); // Giảm từ 8 xuống 6
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        // --- Tiêu đề card ---
        JLabel lblFormTitle = new JLabel("Đăng ký", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, GlobalStyle.scale(20)));
        lblFormTitle.setForeground(new Color(0x005B50));
        gbc.gridy = 0;
        card.add(lblFormTitle, gbc);

        JLabel lblFormSub = new JLabel("Tạo tài khoản mới để bắt đầu", SwingConstants.CENTER);
        lblFormSub.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(13)));
        lblFormSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        card.add(lblFormSub, gbc);

        // --- Họ và tên ---
        JLabel lblName = new JLabel("Họ và tên");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(13)));
        gbc.gridy = 2;
        card.add(lblName, gbc);

        txtFullName = new JTextField("");
        styleInput(txtFullName);
        gbc.gridy = 3;
        card.add(txtFullName, gbc);

        // --- Email ---
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(13)));
        gbc.gridy = 4;
        card.add(lblEmail, gbc);

        txtEmail = new JTextField("");
        styleInput(txtEmail);
        gbc.gridy = 5;
        card.add(txtEmail, gbc);

        // --- Mật khẩu ---
        JLabel lblPassword = new JLabel("Mật khẩu");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(13)));
        gbc.gridy = 6;
        card.add(lblPassword, gbc);

        txtPassword = new JPasswordField("");
        styleInput(txtPassword);
        gbc.gridy = 7;
        card.add(txtPassword, gbc);

        // --- Xác nhận mật khẩu ---
        JLabel lblRePassword = new JLabel("Xác nhận mật khẩu");
        lblRePassword.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(13)));
        gbc.gridy = 8;
        card.add(lblRePassword, gbc);

        txtRePassword = new JPasswordField("");
        styleInput(txtRePassword);
        gbc.gridy = 9;
        card.add(txtRePassword, gbc);

        // --- Checkbox ---
        chkAgree = new JCheckBox("Tôi đồng ý với Điều khoản sử dụng và Chính sách bảo mật");
        chkAgree.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(12)));
        chkAgree.setOpaque(false);
        gbc.gridy = 10;
        card.add(chkAgree, gbc);

        // --- Nút & đăng nhập (layout dọc, căn giữa) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        // --- Nút Đăng ký ---
        btnRegister = new JButton("Đăng ký");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, GlobalStyle.scale(14)));
        btnRegister.setBackground(new Color(0x00796B));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, GlobalStyle.scale(45)));
        bottomPanel.add(btnRegister);
        bottomPanel.add(Box.createVerticalStrut(GlobalStyle.scale(10))); // Giảm từ 15 xuống 10

        // --- Link Đăng nhập ---
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setOpaque(false);
        JLabel lblHaveAcc = new JLabel("Đã có tài khoản?");
        lblHaveAcc.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(12)));
        lblLoginLink = new JLabel("Đăng nhập");
        lblLoginLink.setFont(new Font("Segoe UI", Font.BOLD, GlobalStyle.scale(12)));
        lblLoginLink.setForeground(new Color(0x00796B));
        loginPanel.add(lblHaveAcc);
        loginPanel.add(lblLoginLink);
        bottomPanel.add(loginPanel);

        gbc.gridy = 11;
        card.add(bottomPanel, gbc);

        // --- Layout tổng ---
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(0xF8FDFD));
        content.setBorder(BorderFactory.createEmptyBorder(
                GlobalStyle.scale(30),  // top - giảm từ 40
                GlobalStyle.scale(50),  // left - padding 2 bên
                GlobalStyle.scale(30),  // bottom - giảm từ 40
                GlobalStyle.scale(50)   // right - padding 2 bên
        ));
        content.add(Box.createVerticalStrut(GlobalStyle.scale(20))); // Giảm từ 40 xuống 20
        content.add(titlePanel);
        content.add(Box.createVerticalStrut(GlobalStyle.scale(20))); // Giảm từ 40 xuống 20
        content.add(card);
        content.add(Box.createVerticalStrut(GlobalStyle.scale(20))); // Giảm từ 40 xuống 20

        add(content, BorderLayout.CENTER);
    }


    private void styleInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(14)));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
    }
    public JTextField getTxtUsername() {
        return txtFullName;
    }
    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JPasswordField getTxtRePassword() {
        return txtRePassword;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }
    public JCheckBox getAgree() {
        return chkAgree;
    }
    public JButton getBtnRegister() {
        return btnRegister;
    }
    public JLabel getLblLogin() {
        return lblLoginLink;
    }
}
