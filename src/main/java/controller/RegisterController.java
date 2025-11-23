package main.java.controller;

import main.java.view.RegisterView;
import main.java.view.LoginView;
import main.java.model.User;
import main.java.service.UserService;
import main.java.service.impl.UserServiceImpl;
import main.java.component.MessageCard;

import javax.swing.*;
import java.awt.event.*;

public class RegisterController {
    private final RegisterView view;
    private final UserService userService;

    public RegisterController(RegisterView view) {
        this.view = view;
        this.userService = new UserServiceImpl();

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

        this.view.setVisible(true);
    }

    private void handleRegister() {
        String fullName = view.getTxtUsername().getText().trim();
        String password = new String(view.getTxtPassword().getPassword());
        String repassword = new String(view.getTxtRePassword().getPassword());
        String email = view.getTxtEmail().getText().trim();

        if (fullName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            new MessageCard("Vui lòng nhập đầy đủ thông tin!", MessageCard.MessageType.ERROR);
            return;
        }

        if (!password.equals(repassword)) {
            new MessageCard("Mật khẩu nhập lại không khớp!", MessageCard.MessageType.ERROR);
            return;
        }

        if (!view.getAgree().isSelected()) {
            new MessageCard("Bạn cần đồng ý với Điều khoản sử dụng và Chính sách bảo mật trước khi đăng ký!", MessageCard.MessageType.ERROR);
            return;
        }

        // Kiểm tra email đã tồn tại
        if (userService.isEmailExists(email)) {
            new MessageCard("Email đã được sử dụng!", MessageCard.MessageType.ERROR);
            return;
        }

        // Tạo user mới
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPasswordHash(password);

        // Lưu user vào database
        if (userService.createUser(newUser)) {
            new MessageCard("Tạo tài khoản thành công!", MessageCard.MessageType.INFO);
            handleLoginRedirect();
        } else {
            new MessageCard("Đăng ký thất bại! Vui lòng thử lại.", MessageCard.MessageType.ERROR);
        }
    }

    private void handleLoginRedirect() {
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);
        view.dispose();
    }
}
