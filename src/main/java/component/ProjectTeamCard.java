package main.java.component;

import main.java.model.Project;
import main.java.model.TeamMember;
import main.java.view.GlobalStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class ProjectTeamCard extends JPanel {
    private GlobalStyle style = new GlobalStyle();
    private Project project;
    private List<TeamMember> teamMembers;
    private boolean isExpanded = false;
    
    private JButton expandButton;
    private JPanel membersPanel;
    private JScrollPane scrollPane;

    public ProjectTeamCard(Project project, List<TeamMember> teamMembers) {
        this.project = project;
        this.teamMembers = teamMembers;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(style.getCOLOR_CARD());
        setBorder(new LineBorder(style.getCOLOR_BORDER(), 1, true));

        // Header panel - Project name and expand button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(style.getCOLOR_CARD());
        headerPanel.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Project name label
        JLabel projectNameLabel = new JLabel(project.getName());
        projectNameLabel.setFont(style.getFONT_BOLD());
        projectNameLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());

        // Expand button
        expandButton = new JButton("+");
        expandButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        expandButton.setForeground(style.getCOLOR_PRIMARY());
        expandButton.setBackground(Color.WHITE);
        expandButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_PRIMARY(), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        expandButton.setFocusPainted(false);
        expandButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        expandButton.addActionListener(e -> toggleExpand());

        headerPanel.add(projectNameLabel, BorderLayout.WEST);
        headerPanel.add(expandButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Members panel (initially hidden)
        membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
        membersPanel.setBackground(new Color(0xFAFAFA));
        membersPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        scrollPane = new JScrollPane(membersPanel);
        scrollPane.setBorder(null);
        scrollPane.setVisible(false);

        add(scrollPane, BorderLayout.CENTER);

        setPreferredSize(new Dimension(300, 60));
    }

    private void toggleExpand() {
        isExpanded = !isExpanded;

        if (isExpanded) {
            expandButton.setText("-");
            loadTeamMembers();
            scrollPane.setVisible(true);
            setPreferredSize(new Dimension(300, 300));
        } else {
            expandButton.setText("+");
            scrollPane.setVisible(false);
            setPreferredSize(new Dimension(300, 60));
        }

        revalidate();
        repaint();
        
        // Notify parent to update layout
        Container parent = getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
    }

    private void loadTeamMembers() {
        membersPanel.removeAll();

        if (teamMembers == null || teamMembers.isEmpty()) {
            JLabel noDataLabel = new JLabel("Không có thành viên");
            noDataLabel.setFont(style.getFONT_NORMAL());
            noDataLabel.setForeground(style.getCOLOR_TEXT_MUTED());
            membersPanel.add(noDataLabel);
        } else {
            for (TeamMember member : teamMembers) {
                JPanel memberRow = createMemberRow(member);
                membersPanel.add(memberRow);
                membersPanel.add(Box.createVerticalStrut(8));
            }
        }

        membersPanel.revalidate();
        membersPanel.repaint();
    }

    private JPanel createMemberRow(TeamMember member) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(style.getCOLOR_BORDER(), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));

        // Member info
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 4));
        infoPanel.setOpaque(false);

        JLabel userIdLabel = new JLabel("User ID: " + member.getUserId());
        userIdLabel.setFont(style.getFONT_NORMAL());
        userIdLabel.setForeground(style.getCOLOR_TEXT_PRIMARY());

        JLabel roleIdLabel = new JLabel("Role ID: " + member.getRoleId());
        roleIdLabel.setFont(style.getFONT_NORMAL());
        roleIdLabel.setForeground(style.getCOLOR_TEXT_MUTED());

//        JLabel projectIdLabel = new JLabel("Project ID: " + member.getProject_id());
//        projectIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
//        projectIdLabel.setForeground(style.getCOLOR_TEXT_MUTED());

        infoPanel.add(userIdLabel);
        infoPanel.add(roleIdLabel);
//        infoPanel.add(projectIdLabel);

        row.add(infoPanel, BorderLayout.CENTER);

        return row;
    }

    // Setters
    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
        if (isExpanded) {
            loadTeamMembers();
        }
    }

    public void setProject(Project project) {
        this.project = project;
        repaint();
    }

    // Getters
    public Project getProject() {
        return project;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public JButton getExpandButton() {
        return expandButton;
    }
}
