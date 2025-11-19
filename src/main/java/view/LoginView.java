package main.java.view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblRegister;

    public LoginView() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GlobalStyle.scale(600), GlobalStyle.scale(800));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 5, 0));
        titlePanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Task Manager", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, GlobalStyle.scale(28)));
        lblTitle.setForeground(new Color(0x005B50));

        JLabel lblSubtitle = new JLabel("Quản lý công việc nhóm hiệu quả", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(16)));
        lblSubtitle.setForeground(Color.DARK_GRAY);

        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(GlobalStyle.scale(25), GlobalStyle.scale(20), GlobalStyle.scale(25), GlobalStyle.scale(20))
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(GlobalStyle.scale(10), 0, GlobalStyle.scale(10), 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel lblLoginTitle = new JLabel("Đăng nhập", SwingConstants.CENTER);
        lblLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, GlobalStyle.scale(20)));
        lblLoginTitle.setForeground(new Color(0x005B50));
        gbc.gridy = 0;
        card.add(lblLoginTitle, gbc);

        JLabel lblLoginSub = new JLabel("Đăng nhập vào tài khoản của bạn", SwingConstants.CENTER);
        lblLoginSub.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(14)));
        lblLoginSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        card.add(lblLoginSub, gbc);

        JLabel lblUsername = new JLabel("Tên đăng nhập");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(14)));
        gbc.gridy = 2;
        card.add(lblUsername, gbc);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(14)));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy = 3;
        card.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Mật khẩu");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(14)));
        gbc.gridy = 4;
        card.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(14)));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy = 5;
        card.add(txtPassword, gbc);


        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, GlobalStyle.scale(14)));
        btnLogin.setBackground(new Color(0x00796B));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridy = 7;
        card.add(btnLogin, gbc);


        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setOpaque(false);
        JLabel lblNoAccount = new JLabel("Chưa có tài khoản?");
        lblNoAccount.setFont(new Font("Segoe UI", Font.PLAIN, GlobalStyle.scale(13)));
        lblRegister = new JLabel("Đăng ký ngay");
        lblRegister.setFont(new Font("Segoe UI", Font.BOLD, GlobalStyle.scale(13)));
        lblRegister.setForeground(new Color(0x00796B));
        registerPanel.add(lblNoAccount);
        registerPanel.add(lblRegister);
        gbc.gridy = 8;
        card.add(registerPanel, gbc);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(0xF9F9F9));
        content.setBorder(BorderFactory.createEmptyBorder(0, GlobalStyle.scale(50), 0, GlobalStyle.scale(50))); // Thêm dòng này
        content.add(Box.createVerticalStrut(GlobalStyle.scale(40)));
        content.add(titlePanel);
        content.add(Box.createVerticalStrut(GlobalStyle.scale(30)));
        content.add(card);
        content.add(Box.createVerticalStrut(GlobalStyle.scale(40)));
        add(content, BorderLayout.CENTER);
    }

    // ===== Getter cho Controller =====
    public JButton getBtnLogin() { return btnLogin; }
    public JLabel getLblRegister() { return lblRegister; }
    public JTextField getTxtUsername() { return txtUsername; }
    public JPasswordField getTxtPassword() { return txtPassword; }
}
