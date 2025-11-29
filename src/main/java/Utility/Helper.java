package main.java.Utility;

import main.java.view.GlobalStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Helper {
    private final GlobalStyle style = new GlobalStyle();
    public String formatTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(timestamp);
    }
    public LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public LocalDate convertSqlDateToLocalDate(java.util.Date date) {
        if (date == null) return null;

        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "--";
        String cleaned = fullName.trim();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cleaned.length() && sb.length() < 2; i++) {
            char c = cleaned.charAt(i);
            if (!Character.isWhitespace(c)) sb.append(Character.toUpperCase(c));
        }
        String initials = sb.toString();
        if (initials.isEmpty()) initials = "--";
        return initials;
    }
    public String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    public String formatDateFull(LocalDate date) {
        if (date == null) return "";

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
        String formattedDate = date.format(formatter);

        return capitalizeFirst(formattedDate);
    }
    public JLabel createPriorityBadge(String priority) {
        JLabel badge = new JLabel(priority);
        badge.setFont(style.getFONT_SMALL());
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));

        String normalizedPriority = priority.toUpperCase().trim();

        switch (normalizedPriority) {
            case "HIGH":
                badge.setBackground(new Color(255, 230, 230));
                badge.setForeground(new Color(200, 50, 50));
                badge.setText("Cao");
                break;
            case "MEDIUM":
                badge.setBackground(new Color(255, 245, 220));
                badge.setForeground(new Color(200, 140, 50));
                badge.setText("Trung bình");
                break;
            case "LOW":
                badge.setBackground(new Color(230, 245, 230));
                badge.setForeground(new Color(80, 150, 80));
                badge.setText("Thấp");
                break;
            default:
                badge.setBackground(new Color(240, 240, 240));
                badge.setForeground(Color.GRAY);
                badge.setText(priority);
        }

        return badge;
    }
}
