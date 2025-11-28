package main.java.controller;

import main.java.view.RegisterView;
import main.java.view.LoginView;
import main.java.model.User;
import main.java.service.UserService;
import main.java.service.impl.UserServiceImpl;

import javax.swing.*;
import java.awt.event.*;

public class RegisterController {
    private final RegisterView view;
    private final UserService userService;

    public RegisterController(RegisterView view) {
        this.view = view;
        this.userService = new UserServiceImpl();

        this.view.getBtnRegister().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        this.view.getLblLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleLoginRedirect();
            }
        });

        this.view.setVisible(true);
    }

    private void handleRegister() {
        String fullName = view.getTxtUsername().getText().trim();
        String password = new String(view.getTxtPassword().getPassword());
        String repassword = new String(view.getTxtRePassword().getPassword());
        String email = view.getTxtEmail().getText().trim();

        if (fullName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập đầy đủ thông tin!",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        if (!password.equals(repassword)) {
            JOptionPane.showMessageDialog(view,
                    "Mật khẩu nhập lại không khớp!",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!view.getAgree().isSelected()) {
            JOptionPane.showMessageDialog(view,
                    "Bạn cần đồng ý với Điều khoản sử dụng và Chính sách bảo mật trước khi đăng ký!",
                    "Lỗi đăng ký",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userService.isEmailExists(email)) {
            JOptionPane.showMessageDialog(view,
                    "Email đã được sử dụng!",
                    "Lỗi đăng ký",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPasswordHash(password);

        if (userService.createUser(newUser)) {
            JOptionPane.showMessageDialog(view,
                    "Tạo tài khoản thành công!",
                    "Đăng ký thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            handleLoginRedirect();
        } else {
            JOptionPane.showMessageDialog(view,
                    "Đăng ký thất bại! Vui lòng thử lại.",
                    "Lỗi đăng ký",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLoginRedirect() {
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);
        view.dispose();
    }
}
