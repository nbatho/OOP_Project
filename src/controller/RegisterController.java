package controller;

import view.RegisterView;
import view.LoginView;
import view.Dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterController {
    private final RegisterView view;

    public RegisterController(RegisterView view) {
        this.view = view;

        // Khi nhấn nút "Đăng ký"
        this.view.getBtnRegister().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Giả sử đăng ký thành công
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
                view.dispose();
            }
        });

        // Khi nhấn "Đăng nhập"
        this.view.getLblLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginView loginView = new LoginView();
                new LoginController(loginView);
                loginView.setVisible(true);
                view.dispose();
            }
        });
    }
}
