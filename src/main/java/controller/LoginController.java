package main.java.controller;

import main.java.component.MessageCard;
import main.java.model.User;
import main.java.service.UserService;
import main.java.service.impl.UserServiceImpl;
import main.java.view.LoginView;
import main.java.view.RegisterView;
import main.java.view.DashboardView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class LoginController {
    private final UserService userService;

    public LoginController(LoginView lview) {
        this.userService = UserServiceImpl.getInstance();

        lview.getLblRegister().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lview.dispose();
                new RegisterController(new RegisterView());
            }
        });

        lview.getBtnLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String email = lview.getTxtUsername().getText();
                String password = new String(lview.getTxtPassword().getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    new MessageCard("Vui lòng nhập đầy đủ thông tin!",MessageCard.MessageType.ERROR);
                    return;
                }

                User user = userService.authenticateUser(email, password);
                if (user != null) {
                    lview.dispose();
                    new DashboardController(new DashboardView());
                } else {
                    new MessageCard("Sai tài khoản hoặc mật khẩu!",MessageCard.MessageType.ERROR);
                }
            }
        });
    }
}
