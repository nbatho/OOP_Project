package main.java.view;

import javax.swing.*;
import java.awt.*;

public class GlobalStyle {
    private final Color COLOR_BACKGROUND = new Color(0xF4F6F6);
    private  final Color COLOR_CARD = new Color(0xFFFFFF);
    private final Color COLOR_BORDER = new Color(0xE6ECEB);
    private final Color COLOR_TEXT_PRIMARY = new Color(0x1F3B3A);
    private final Color COLOR_TEXT_MUTED = new Color(0x6C7C7B);
    private final Color COLOR_PRIMARY = new Color(0x2F7D7A);
    private final Color COLOR_SUCCESS = new Color(0x22C55E);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    private final static int BASE_WIDTH = 1920;
    private final static int BASE_HEIGHT = 1080;


    private static int currentWidth;
    private static int currentHeight;


    private static double SCALE_X;
    private static double SCALE_Y;
    private static double SCALE;


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

    public Font getFONT_SMALL() {
        return FONT_SMALL;
    }

    public Color getCOLOR_SUCCESS() {
        return COLOR_SUCCESS;
    }

    public Color getCOLOR_BACKGROUND() {
        return COLOR_BACKGROUND;
    }

    public Color getCOLOR_CARD() {
        return COLOR_CARD;
    }

    public Color getCOLOR_BORDER() {
        return COLOR_BORDER;
    }

    public Color getCOLOR_TEXT_PRIMARY() {
        return COLOR_TEXT_PRIMARY;
    }

    public Color getCOLOR_TEXT_MUTED() {
        return COLOR_TEXT_MUTED;
    }

    public Color getCOLOR_PRIMARY() {
        return COLOR_PRIMARY;
    }

    public Font getFONT_INPUT() {
        return FONT_INPUT;
    }

    public Font getFONT_BOLD() {
        return FONT_BOLD;
    }

    public Font getFONT_NORMAL() {
        return FONT_NORMAL;
    }

    public Font getFONT_TITLE() {
        return FONT_TITLE;
    }

    public static int getBaseWidth() {
        return BASE_WIDTH;
    }

    public static int getBaseHeight() {
        return BASE_HEIGHT;
    }

    public static int getCurrentWidth() {
        return currentWidth;
    }

    public static int getCurrentHeight() {
        return currentHeight;
    }

    public static double getScaleX() {
        return SCALE_X;
    }

    public static double getScaleY() {
        return SCALE_Y;
    }

    public static double getSCALE() {
        return SCALE;
    }
}