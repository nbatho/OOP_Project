package controller;

import view.LoginView;
import view.RegisterView;
import view.DashboardView;
import model.User;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;


public class LoginController {
    private final LoginView lview;
    public LoginController(LoginView lview) {
        this.lview = lview;
        this.lview.getLblRegister().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lview.dispose();
                new RegisterController(new RegisterView());
            }
        });

        this.lview.getBtnLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String username = lview.getTxtUsername().getText();
                String password = new String(lview.getTxtPassword().getPassword());
                boolean remember = lview.getChkRemember().isSelected();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(lview, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                if (User.checkLogin(username, password,remember)) {
                    JOptionPane.showMessageDialog(lview, "Đăng nhập thành công!");
                    lview.dispose();
                    new DashboardView();
                } else {
                    JOptionPane.showMessageDialog(lview, "Sai tài khoản hoặc mật khẩu!");
                }
            }
        });
    }



}
