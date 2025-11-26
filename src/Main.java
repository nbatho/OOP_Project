import javax.swing.*;
import main.java.view.LoginView;
import main.java.controller.LoginController;
import main.java.view.GlobalStyle;

public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}
        GlobalStyle.init();
        LoginView view = new  LoginView();
        new LoginController(view);

        view.setVisible(true);
    }
}
