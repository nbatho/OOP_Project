package controller;

import model.User;
import view.LoginView;
import view.RegisterView;
import view.DashboardView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class LoginController {
    private final LoginView lview;

    public LoginController(LoginView lview) {
        this.lview = lview;

        // Sự kiện: nhấn "Đăng ký"
        this.lview.getLblRegister().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lview.dispose();
                new RegisterController(new RegisterView());
            }
        });

        // Sự kiện: nhấn "Đăng nhập"
        this.lview.getBtnLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = lview.getTxtUsername().getText();
                String password = new String(lview.getTxtPassword().getPassword());

                if (name.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(lview, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                // Gọi controller xử lý nghiệp vụ đăng nhập
                User user = UserController.checkLogin(name, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(lview, "Đăng nhập thành công! Xin chào ");
                    lview.dispose();
                        new DashboardView();
//                    new DashboardController(new DashboardView(), user);
                } else {
                    JOptionPane.showMessageDialog(lview, "Sai tài khoản hoặc mật khẩu!");
                }
            }
        });
    }
}
