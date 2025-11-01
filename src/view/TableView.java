package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

/** Bảng công việc – responsive theo độ rộng màn hình */
public class TableView extends JPanel {
    // ===== Theme =====
    private static final Color COLOR_BACKGROUND   = new Color(0xF4F5F7);
    private static final Color COLOR_CARD         = Color.WHITE;
    private static final Color COLOR_BORDER       = new Color(0xE0E0E0);
    private static final Color COLOR_TEXT_PRIMARY = new Color(0x333333);
    private static final Color COLOR_TEXT_MUTED   = new Color(0x666666);
    private static final Color COLOR_PRIMARY      = new Color(0x3B82F6);
    private static final Color COLOR_SUCCESS      = new Color(0x22C55E);

    private static final Font FONT_BOLD   = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 24);

    // ===== UI refs =====
    private DefaultTableModel model;
    private JTable table;

    private JToggleButton btnFrontend, btnBackend, btnDevOps, btnTesting;
    private JLabel catTitleLabel, catStatsLabel, donePillLabel;

    // dữ liệu theo hạng mục
    private final Map<String, java.util.List<Object[]>> dataByCat = new LinkedHashMap<>();

    // tỉ lệ responsive cho 5 cột: Công việc, Người thực hiện, Trạng thái, Độ ưu tiên, Tiến độ
    private static final double[] COL_RATIO = {0.35, 0.20, 0.15, 0.15, 0.15};
    private static final int[]    COL_MIN   = {220,   140,  110,  110,  120}; // tối thiểu

    public TableView() {
        setLayout(new BorderLayout(8,8));
        setOpaque(true);
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10,10,10,10));

        /* ===== Header nhỏ ===== */
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Bảng công việc theo hạng mục");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_PRIMARY);
        JLabel sub = new JLabel("Quản lý chi tiết công việc theo từng lĩnh vực");
        sub.setFont(FONT_NORMAL);
        sub.setForeground(COLOR_TEXT_MUTED);
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(sub);
        add(header, BorderLayout.NORTH);

        /* ===== Chips: 4 nút chia đều ===== */
        JPanel chipsRow = new JPanel(new GridLayout(1,4,0,0));
        chipsRow.setOpaque(true);
        chipsRow.setBackground(new Color(0xEEF2F5));
        chipsRow.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(10,12,10,12)));

        ButtonGroup chipsGroup = new ButtonGroup();
        btnFrontend = chipTab("🎨  Frontend", true,  chipsGroup);
        btnBackend  = chipTab("⚙️  Backend",  false, chipsGroup);
        btnDevOps   = chipTab("🔧  DevOps",   false, chipsGroup);
        btnTesting  = chipTab("🧪  Testing",  false, chipsGroup);

        chipsRow.add(btnFrontend);
        chipsRow.add(btnBackend);
        chipsRow.add(btnDevOps);
        chipsRow.add(btnTesting);

        /* ===== Section card (tiêu đề + % hoàn thành) ===== */
        JPanel sectionCard = new JPanel(new BorderLayout());
        sectionCard.setBackground(COLOR_CARD);
        sectionCard.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER),
                new EmptyBorder(16,16,16,16)));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        catTitleLabel = new JLabel("Frontend");
        catTitleLabel.setFont(FONT_BOLD.deriveFont(16f));
        catTitleLabel.setForeground(COLOR_TEXT_PRIMARY);

        catStatsLabel = new JLabel("—");
        catStatsLabel.setFont(FONT_NORMAL);
        catStatsLabel.setForeground(COLOR_TEXT_MUTED);

        left.add(catTitleLabel);
        left.add(Box.createVerticalStrut(4));
        left.add(catStatsLabel);

        // pill % hoàn thành
        JPanel pill = new JPanel(new FlowLayout(FlowLayout.CENTER,10,4));
        pill.setBackground(new Color(0xE6F9F0));
        pill.setBorder(new LineBorder(new Color(0xE6F9F0), 0, true));
        donePillLabel = new JLabel("—");
        donePillLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        donePillLabel.setForeground(COLOR_SUCCESS);
        pill.add(donePillLabel);

        sectionCard.add(left, BorderLayout.WEST);
        sectionCard.add(pill, BorderLayout.EAST);

        // stack chips + card
        JPanel topStack = new JPanel();
        topStack.setOpaque(false);
        topStack.setLayout(new BoxLayout(topStack, BoxLayout.Y_AXIS));
        topStack.add(chipsRow);
        topStack.add(Box.createVerticalStrut(8));
        topStack.add(sectionCard);

        /* ===== Vùng giữa (nền trắng) ===== */
        JPanel middle = new JPanel(new BorderLayout(0,12));
        middle.setBackground(COLOR_CARD);
        middle.setOpaque(true);
        middle.add(topStack, BorderLayout.NORTH);

        /* ===== JTable ===== */
        String[] cols = {"Công việc","Người thực hiện","Trạng thái","Độ ưu tiên","Tiến độ"};
        model = new DefaultTableModel(cols, 0){
            @Override public boolean isCellEditable(int r, int c){ return false; }
            @Override public Class<?> getColumnClass(int c){ return c==4 ? Integer.class : Object.class; }
        };

        table = new JTable(model){
            // giữ nền trắng cho ô rỗng
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col){
                Component c = super.prepareRenderer(r, row, col);
                if (!isCellSelected(row, col) && c instanceof JComponent jc) {
                    jc.setOpaque(true);
                    jc.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        table.setFillsViewportHeight(true);
        table.setRowHeight(44);
        table.setGridColor(new Color(0xF1F3F5));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0,1));
        table.setBackground(COLOR_CARD);
        table.setFont(FONT_NORMAL);

        // Header & auto-resize responsive
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN); // để setPreferredWidth theo tỉ lệ hoạt động mượt
        JTableHeader h = table.getTableHeader();
        h.setReorderingAllowed(false);
        h.setBackground(new Color(0xEEF2F5));
        h.setForeground(new Color(0x6B7280));
        h.setFont(FONT_BOLD);
        ((DefaultTableCellRenderer) h.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // renderer các cột
        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(1).setCellRenderer(new AssigneeRenderer());
        cm.getColumn(2).setCellRenderer(new BadgeRenderer("Hoàn thành", new Color(0xEAF6F3), new Color(0x0F766E)));
        cm.getColumn(3).setCellRenderer(new BadgeRenderer("Cao", new Color(0xFDECEC), new Color(0xB91C1C)));
        cm.getColumn(4).setCellRenderer(new ProgressRenderer());

        // ScrollPane
        JScrollPane sp = new JScrollPane(table);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.getViewport().setBackground(COLOR_CARD);
        sp.setBorder(BorderFactory.createMatteBorder(1,0,0,0, COLOR_BORDER));

        middle.add(sp, BorderLayout.CENTER);
        add(middle, BorderLayout.CENTER);

        // ===== Responsive widths theo tỉ lệ =====
        // set lần đầu sau khi bảng có size
        SwingUtilities.invokeLater(this::applyResponsiveWidths);
        // cập nhật khi panel thay đổi kích thước
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                applyResponsiveWidths();
            }
        });

        /* ===== DỮ LIỆU & SỰ KIỆN NÚT ===== */
        seedData();
        loadCategory("Frontend");

        btnFrontend.addActionListener(e -> loadCategory("Frontend"));
        btnBackend .addActionListener(e -> loadCategory("Backend"));
        btnDevOps  .addActionListener(e -> loadCategory("DevOps"));
        btnTesting.addActionListener(e -> loadCategory("Testing"));
    }

    /** Áp dụng độ rộng cột theo tỉ lệ màn hình */
    private void applyResponsiveWidths(){
        if (table == null || table.getColumnModel().getColumnCount() < 5) return;
        int w = table.getVisibleRect().width;
        if (w <= 0) w = getWidth();

        TableColumnModel cm = table.getColumnModel();
        for (int i = 0; i < 5; i++){
            int pref = Math.max((int)(w * COL_RATIO[i]), COL_MIN[i]);
            cm.getColumn(i).setPreferredWidth(pref);
            cm.getColumn(i).setMinWidth(COL_MIN[i]);
        }
        table.doLayout();
    }

    // ====== Helpers UI ======
    private JToggleButton chipTab(String text, boolean selected, ButtonGroup group){
        JToggleButton b = new JToggleButton(text, selected);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setFont(FONT_BOLD);
        b.setForeground(new Color(0x6B7280));
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setBorder(new EmptyBorder(6, 0, 6, 0));
        b.addChangeListener(e -> {
            if (b.isSelected()){
                b.setForeground(COLOR_TEXT_PRIMARY);
                b.setBorder(new MatteBorder(0,0,3,0, COLOR_PRIMARY));
            } else {
                b.setForeground(new Color(0x6B7280));
                b.setBorder(new EmptyBorder(6,0,6,0));
            }
        });
        group.add(b);
        return b;
    }

    private static Object multiline(String title, String sub){
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title);
        t.setFont(FONT_BOLD);
        t.setForeground(COLOR_TEXT_PRIMARY);
        JLabel s = new JLabel(sub);
        s.setFont(FONT_NORMAL);
        s.setForeground(COLOR_TEXT_MUTED);
        p.add(t); p.add(s);
        return p;
    }

    // ====== Data & actions ======
    private void seedData(){
        // Sử dụng kiểu ArrayList để tương thích mọi JDK
        java.util.List<Object[]> frontend = new ArrayList<>();
        frontend.add(new Object[]{
            multiline("Thiết kế UI Dashboard", "Tạo mockup cho trang chủ"),
            new Assignee("NVA","Nguyễn Văn A"),
            "Hoàn thành", "Cao", 60
        });
        dataByCat.put("Frontend", frontend);

        java.util.List<Object[]> backend = new ArrayList<>();
        backend.add(new Object[]{
            multiline("Thiết kế API v1", "Chuẩn hoá DTO & error"),
            new Assignee("TTB","Trần Thị B"),
            "Đang làm", "Trung bình", 30
        });
        dataByCat.put("Backend", backend);

        java.util.List<Object[]> devops = new ArrayList<>();
        devops.add(new Object[]{
            multiline("Viết pipeline CI", "Build + test + sonar"),
            new Assignee("NVA","Nguyễn Văn A"),
            "Chờ", "Thấp", 0
        });
        dataByCat.put("DevOps", devops);

        java.util.List<Object[]> testing = new ArrayList<>();
        testing.add(new Object[]{
            multiline("Viết test Dashboard", "JUnit + AssertJ"),
            new Assignee("LVC","Lê Văn C"),
            "Hoàn thành", "Cao", 100
        });
        dataByCat.put("Testing", testing);
    }

    private void loadCategory(String cat){
        model.setRowCount(0);
        java.util.List<Object[]> rows = dataByCat.getOrDefault(cat, Collections.emptyList());
        for (Object[] r : rows) model.addRow(r);

        catTitleLabel.setText(cat);
        long done = rows.stream().filter(r -> "Hoàn thành".equals(String.valueOf(r[2]))).count();
        catStatsLabel.setText(rows.size() + " công việc • " + done + " hoàn thành");
        int percent = rows.isEmpty() ? 0 : (int)Math.round(done * 100.0 / rows.size());
        donePillLabel.setText(percent + "% hoàn thành");

        // update widths sau khi dữ liệu thay đổi
        SwingUtilities.invokeLater(this::applyResponsiveWidths);
    }

    // ====== Renderers ======
    private static class Assignee {
        final String code; final String name;
        Assignee(String c, String n){ code=c; name=n; }
    }
    private static class AssigneeRenderer extends DefaultTableCellRenderer{
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean sel, boolean foc, int row, int col){
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
            p.setOpaque(true);
            p.setBackground(Color.WHITE);

            JLabel avatar = new JLabel();
            avatar.setOpaque(true);
            avatar.setBackground(new Color(0x2563EB));
            avatar.setPreferredSize(new Dimension(24,24));

            String name = (value instanceof Assignee a) ? a.name : String.valueOf(value);
            JLabel lb = new JLabel(name);
            lb.setFont(FONT_NORMAL);
            lb.setForeground(COLOR_TEXT_PRIMARY);

            p.add(avatar); p.add(lb);
            return p;
        }
    }
    private static class BadgeRenderer extends DefaultTableCellRenderer{
        private final String text; private final Color bg; private final Color fg;
        BadgeRenderer(String text, Color bg, Color fg){ this.text=text; this.bg=bg; this.fg=fg; }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c){
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER,10,4));
            p.setOpaque(true); p.setBackground(bg);
            JLabel lb = new JLabel(text);
            lb.setFont(FONT_BOLD); lb.setForeground(fg);
            p.add(lb); return p;
        }
    }
    private static class ProgressRenderer extends JProgressBar implements TableCellRenderer{
        ProgressRenderer(){ super(0,100); setStringPainted(true); setBorder(new EmptyBorder(2,8,2,8)); }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c){
            int val = (v instanceof Number n) ? n.intValue() : 0;
            setValue(val); setString(val + "%"); setBackground(COLOR_CARD);
            return this;
        }
    }
}
