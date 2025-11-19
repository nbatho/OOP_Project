package main.java.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

public class TableView extends JPanel {
    // ====== UI components public để controller truy cập ======
    public JTable table;
    public DefaultTableModel model;
    public JLabel catTitleLabel, catStatsLabel, donePillLabel;
    public JToggleButton btnFrontend, btnBackend, btnDevOps, btnTesting;

    // ===== Theme =====
    GlobalStyle style = new GlobalStyle();


    public TableView() {
        setLayout(new BorderLayout(8, 8));
        setOpaque(true);
        setBackground(style.getCOLOR_BACKGROUND());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Bảng công việc theo hạng mục");
        title.setFont(style.getFONT_TITLE());
        title.setForeground(style.getCOLOR_TEXT_PRIMARY());
        JLabel sub = new JLabel("Quản lý chi tiết công việc theo từng lĩnh vực");
        sub.setFont(style.getFONT_NORMAL());
        sub.setForeground(style.getCOLOR_TEXT_MUTED());
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(sub);
        add(header, BorderLayout.NORTH);

        // Tabs (chips)
        JPanel chipsRow = new JPanel(new GridLayout(1, 4, 0, 0));
        chipsRow.setOpaque(true);
        chipsRow.setBackground(new Color(0xEEF2F5));
        chipsRow.setBorder(new CompoundBorder(new LineBorder(style.getCOLOR_BORDER()),
                new EmptyBorder(10, 12, 10, 12)));

        ButtonGroup chipsGroup = new ButtonGroup();
        btnFrontend = chipTab("Frontend", true, chipsGroup);
        btnBackend  = chipTab("Backend",  false, chipsGroup);
        btnDevOps   = chipTab("DevOps",   false, chipsGroup);
        btnTesting  = chipTab("Testing",  false, chipsGroup);

        chipsRow.add(btnFrontend);
        chipsRow.add(btnBackend);
        chipsRow.add(btnDevOps);
        chipsRow.add(btnTesting);

        // Section card
        JPanel sectionCard = new JPanel(new BorderLayout());
        sectionCard.setBackground(style.getCOLOR_CARD());
        sectionCard.setBorder(new CompoundBorder(new LineBorder(style.getCOLOR_BORDER()),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        catTitleLabel = new JLabel("Frontend");
        catTitleLabel.setFont(style.getFONT_BOLD().deriveFont(16f));
        catTitleLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());
        catStatsLabel = new JLabel("—");
        catStatsLabel.setFont(style.getFONT_NORMAL());
        catStatsLabel.setForeground(style.getCOLOR_TEXT_MUTED());
        left.add(catTitleLabel);
        left.add(Box.createVerticalStrut(4));
        left.add(catStatsLabel);

        JPanel pill = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        pill.setBackground(new Color(0xE6F9F0));
        donePillLabel = new JLabel("—");
        donePillLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        donePillLabel.setForeground(style.getCOLOR_SUCCESS());
        pill.add(donePillLabel);

        sectionCard.add(left, BorderLayout.WEST);
        sectionCard.add(pill, BorderLayout.EAST);

        JPanel topStack = new JPanel();
        topStack.setOpaque(false);
        topStack.setLayout(new BoxLayout(topStack, BoxLayout.Y_AXIS));
        topStack.add(chipsRow);
        topStack.add(Box.createVerticalStrut(8));
        topStack.add(sectionCard);

        // Table
        String[] cols = {"Công việc", "Người thực hiện", "Trạng thái", "Độ ưu tiên", "Tiến độ"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) { return c == 4 ? Integer.class : Object.class; }
        };
        table = new JTable(model);
        table.setRowHeight(44);
        table.setFont(style.getFONT_NORMAL());
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(0xF1F3F5));
        table.setBackground(style.getCOLOR_CARD());

        JTableHeader h = table.getTableHeader();
        h.setBackground(new Color(0xEEF2F5));
        h.setFont(style.getFONT_BOLD());
        ((DefaultTableCellRenderer) h.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(style.getCOLOR_CARD());
        sp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, style.getCOLOR_BORDER()));

        JPanel middle = new JPanel(new BorderLayout());
        middle.setBackground(style.getCOLOR_CARD());
        middle.add(topStack, BorderLayout.NORTH);
        middle.add(sp, BorderLayout.CENTER);

        add(middle, BorderLayout.CENTER);
    }

    private JToggleButton chipTab(String text, boolean selected, ButtonGroup group) {
        JToggleButton b = new JToggleButton(text, selected);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setFont(style.getFONT_BOLD());
        b.setForeground(new Color(0x6B7280));
        b.addChangeListener(e -> {
            if (b.isSelected()) {
                b.setForeground(style.getCOLOR_TEXT_PRIMARY());
                b.setBorder(new MatteBorder(0, 0, 3, 0, style.getCOLOR_PRIMARY()));
            } else {
                b.setForeground(new Color(0x6B7280));
                b.setBorder(new EmptyBorder(6, 0, 6, 0));
            }
        });
        group.add(b);
        return b;
    }
}
