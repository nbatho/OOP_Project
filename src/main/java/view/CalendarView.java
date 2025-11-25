package main.java.view;

// NOTE: com.toedter.calendar.JCalendar requires the JCalendar library (https://toedter.com/jcalendar/)
// To add this library: Download jcalendar-1.4.jar and add to classpath
// import com.toedter.calendar.JCalendar;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class CalendarView extends JPanel {
    private JList<String> taskList;
    private JList<String> upcomingList;
    private JLabel dateLabel;
    private final LocalDate selectedDate = LocalDate.now();

    public CalendarView() {
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createUpcomingPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Lịch công việc", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel desc = new JLabel("Quản lý thời gian và deadline cho các công việc", SwingConstants.LEFT);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(title);
        textPanel.add(desc);
        header.add(textPanel, BorderLayout.WEST);

        return header;
    }


    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Bên trái: Lịch
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new TitledBorder("Chọn ngày"));

        // Thay thế JCalendar bằng JButton đơn giản để chọn ngày
        JButton datePickerButton = new JButton("Chọn ngày: " + selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        datePickerButton.addActionListener(e -> {
            // Đây là nơi có thể tích hợp date picker khác hoặc dialog
            JOptionPane.showMessageDialog(this, "Chức năng chọn ngày sẽ được cập nhật sau");
        });

        leftPanel.add(datePickerButton, BorderLayout.CENTER);
        panel.add(leftPanel);

        // Bên phải: Danh sách công việc theo ngày
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Công việc trong ngày"));

        dateLabel = new JLabel("Công việc ngày " + selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rightPanel.add(dateLabel, BorderLayout.NORTH);

        taskList = new JList<>();
        JScrollPane scroll = new JScrollPane(taskList);
        rightPanel.add(scroll, BorderLayout.CENTER);

        panel.add(rightPanel);
        return panel;
    }


    private JPanel createUpcomingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Công việc sắp đến hạn"));

        upcomingList = new JList<>();
        JScrollPane scroll = new JScrollPane(upcomingList);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
}
