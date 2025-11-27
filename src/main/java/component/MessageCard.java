package main.java.component;

import main.java.view.GlobalStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MessageCard extends JFrame {
    GlobalStyle style =  new GlobalStyle();
    private final JLabel lblMessage;

    public enum MessageType {
        ERROR,
        INFO
    }


    public MessageCard(String message, MessageType type) {
        setTitle(type == MessageType.ERROR ? "Error" : "Message");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        lblMessage = new JLabel(message, SwingConstants.CENTER);
        lblMessage.setFont(style.getFONT_BOLD());
        if (type == MessageType.ERROR) {
            lblMessage.setForeground(Color.RED);
        } else {
            lblMessage.setForeground(Color.BLACK);
        }


        JButton btnOK = new JButton("OK");
        btnOK.addActionListener((ActionEvent e) -> dispose());

        JPanel panelButton = new JPanel();
        panelButton.add(btnOK);


        add(lblMessage, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);

        setVisible(true);
    }

}
