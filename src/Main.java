import javax.swing.*;
import view.LoginView;
import controller.LoginController;
import view.UIScale;
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
        UIScale.init();
        LoginView view = new  LoginView();
        new LoginController(view);

        view.setVisible(true);
    }
}
