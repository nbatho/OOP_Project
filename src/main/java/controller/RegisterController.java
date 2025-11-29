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
            view.showError("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!password.equals(repassword)) {
            view.showError("Mật khẩu nhập lại không khớp!");
            return;
        }
        if (!view.getAgree().isSelected()) {
            view.showError("Bạn cần đồng ý với Điều khoản sử dụng và Chính sách bảo mật trước khi đăng ký!");
            return;
        }

        if (userService.isEmailExists(email)) {
            view.showError("Email đã được sử dụng!");
            return;
        }
        try {
            if (userService.isEmailExists(email)) {
                view.showError("Email đã được sử dụng!");
                return;
            }
            User newUser = new User();
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPasswordHash(password);

            boolean success = userService.createUser(newUser);

            if (success) {
                view.showSuccess("Tạo tài khoản thành công!");
                handleLoginRedirect();
            } else {
                view.showError("Đăng ký thất bại! Vui lòng thử lại.");
            }

        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();

            view.showError("Có lỗi xảy ra trong quá trình đăng ký. Vui lòng thử lại sau.");
        }

    }

    private void handleLoginRedirect() {
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);
        view.dispose();
    }
}
