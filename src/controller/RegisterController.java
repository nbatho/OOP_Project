package controller;

import view.RegisterView;
import view.LoginView;
import model.User;

import javax.swing.*;
import java.awt.event.*;

public class RegisterController {
    private final RegisterView view;

    public RegisterController(RegisterView view) {
        this.view = view;

        // Khi nhấn nút "Đăng ký"
        this.view.getBtnRegister().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        // Khi nhấn "Đăng nhập"
        this.view.getLblLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleLoginRedirect();
            }
        });
    }

    private void handleRegister() {
        String username = view.getTxtUsername().getText().trim();
        String password = new String(view.getTxtPassword().getPassword());
        String repassword = new String(view.getTxtRePassword().getPassword());
        String email = view.getTxtEmail().getText().trim();
        boolean checkAgree = view.getAgree().isSelected();
        // Kiểm tra dữ liệu nhập vào
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!password.equals(repassword)) {
            JOptionPane.showMessageDialog(view, "Mật khẩu nhập lại không khớp!");
            return;
        }
        if (!view.getAgree().isSelected()) {
            JOptionPane.showMessageDialog(view,
                    "Bạn cần đồng ý với Điều khoản sử dụng và Chính sách bảo mật trước khi đăng ký!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Gọi model User để tạo tài khoản
        if (User.createAccount(username, password, email)) {
            JOptionPane.showMessageDialog(view, "Tạo tài khoản thành công!");
            handleLoginRedirect();
        } else {
            JOptionPane.showMessageDialog(view, "Tên đăng nhập đã tồn tại hoặc lỗi hệ thống!");
        }
    }

    private void handleLoginRedirect() {
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);
        view.dispose();
    }
}
