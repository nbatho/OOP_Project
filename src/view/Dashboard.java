package view;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(UIScale.scale(1020), UIScale.scale(680));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initUI();
        setVisible(true);
    }
    public void initUI() {

    }
}
