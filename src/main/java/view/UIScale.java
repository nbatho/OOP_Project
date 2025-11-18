package main.java.view;

import javax.swing.*;
import java.awt.*;

public class UIScale {

    public static final int BASE_WIDTH = 1920;
    public static final int BASE_HEIGHT = 1080;


    public static int currentWidth;
    public static int currentHeight;


    public static double SCALE_X;
    public static double SCALE_Y;
    public static double SCALE;


    public static void init() {

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        currentWidth = screen.width;
        currentHeight = screen.height;


        SCALE_X = currentWidth / (double) BASE_WIDTH;
        SCALE_Y = currentHeight / (double) BASE_HEIGHT;

        SCALE = (SCALE_X + SCALE_Y) / 2.0;

        applyGlobalScale();
    }


    public static int scale(int value) {
        return (int) Math.round(value * SCALE);
    }

    public static Font scaleFont(Font font) {
        float newSize = (float) (font.getSize2D() * SCALE);
        return font.deriveFont(newSize);
    }

    public static void applyGlobalScale() {
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, scale(12));

        UIManager.put("Button.font", scaleFont(defaultFont));
        UIManager.put("Label.font", scaleFont(defaultFont));
        UIManager.put("TextField.font", scaleFont(defaultFont));
        UIManager.put("ComboBox.font", scaleFont(defaultFont));
        UIManager.put("Table.font", scaleFont(defaultFont));
        UIManager.put("Menu.font", scaleFont(defaultFont));
        UIManager.put("CheckBox.font", scaleFont(defaultFont));

        UIManager.put("Button.margin", new Insets(scale(5), scale(10), scale(5), scale(10)));
        UIManager.put("Panel.background", new Color(0xF7F7F7));
    }
}