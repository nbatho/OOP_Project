package controller;

import view.LoginView;
import view.RegisterView;
import view.Dashboard;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                lview.dispose();
                new Dashboard();
            }
        });
    }



}
