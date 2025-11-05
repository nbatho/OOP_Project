package controller;

import view.TableView;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

public class TableController {
    private final TableView view;
    private final Map<String, java.util.List<Object[]>> dataByCat = new LinkedHashMap<>();

    public TableController(TableView view) {
        this.view = view;
        seedData();
        loadCategory("Frontend");
        setupListeners();
    }

    private void setupListeners() {
        view.btnFrontend.addActionListener(e -> loadCategory("Frontend"));
        view.btnBackend.addActionListener(e -> loadCategory("Backend"));
        view.btnDevOps.addActionListener(e -> loadCategory("DevOps"));
        view.btnTesting.addActionListener(e -> loadCategory("Testing"));
    }

    private void seedData() {
        java.util.List<Object[]> frontend = new ArrayList<>();
        frontend.add(new Object[]{"Thiết kế UI Dashboard", "Nguyễn Văn A", "Hoàn thành", "Cao", 60});
        dataByCat.put("Frontend", frontend);

        java.util.List<Object[]> backend = new ArrayList<>();
        backend.add(new Object[]{"Thiết kế API v1", "Trần Thị B", "Đang làm", "Trung bình", 30});
        dataByCat.put("Backend", backend);

        java.util.List<Object[]> devops = new ArrayList<>();
        devops.add(new Object[]{"Viết pipeline CI", "Nguyễn Văn A", "Chờ", "Thấp", 0});
        dataByCat.put("DevOps", devops);

        java.util.List<Object[]> testing = new ArrayList<>();
        testing.add(new Object[]{"Viết test Dashboard", "Lê Văn C", "Hoàn thành", "Cao", 100});
        dataByCat.put("Testing", testing);
    }

    public void loadCategory(String cat) {
        DefaultTableModel model = view.model;
        model.setRowCount(0);
        java.util.List<Object[]> rows = dataByCat.getOrDefault(cat, Collections.emptyList());
        for (Object[] r : rows) model.addRow(r);

        view.catTitleLabel.setText(cat);
        long done = rows.stream().filter(r -> "Hoàn thành".equals(String.valueOf(r[2]))).count();
        view.catStatsLabel.setText(rows.size() + " công việc • " + done + " hoàn thành");
        int percent = rows.isEmpty() ? 0 : (int)Math.round(done * 100.0 / rows.size());
        view.donePillLabel.setText(percent + "% hoàn thành");
    }
}
