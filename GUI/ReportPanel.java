package gui;

import model.Course;
import model.Student;
import model.UniversityManager;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ReportPanel extends JPanel {
    private UniversityManager manager;
    private JComboBox<Student> studentSelector;
    private JTextArea reportArea;

    public ReportPanel(UniversityManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        studentSelector = new JComboBox<>();
        JButton generateBtn = new JButton("Generate Report");
        JButton refreshBtn = new JButton("Refresh List");

        topPanel.add(new JLabel("Student:"));
        topPanel.add(studentSelector);
        topPanel.add(generateBtn);
        topPanel.add(refreshBtn);

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setEditable(false);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);

        generateBtn.addActionListener(e -> generateReport());
        refreshBtn.addActionListener(e -> refreshList());
    }

    public void refreshList() {
        studentSelector.removeAllItems();
        for (Student s : manager.getStudents()) studentSelector.addItem(s);
    }

    private void generateReport() {
        Student s = (Student) studentSelector.getSelectedItem();
        if (s == null) return;

        Map<String, String> grades = manager.getRawGrades(s);
        double average = manager.calculateStudentAverage(s);

        StringBuilder sb = new StringBuilder();
        sb.append("========== ACADEMIC REPORT ==========\n");
        sb.append("Name:      ").append(s.getName()).append("\n");
        sb.append("ID:        ").append(s.getId()).append("\n");
        sb.append("Email:     ").append(s.getEmail()).append("\n");           // NEW
        sb.append("Enrolled:  ").append(s.getEnrollmentDate()).append("\n");  // NEW
        sb.append("-------------------------------------\n");
        sb.append(String.format("%-10s %-20s %-8s %-5s\n", "Code", "Course", "Credits", "Grade"));
        sb.append("-------------------------------------\n");

        for (Map.Entry<String, String> entry : grades.entrySet()) {
            Course c = manager.getCourseByCode(entry.getKey());
            String name = (c != null) ? c.getName() : "Unknown";
            int cred = (c != null) ? c.getCredits() : 0;
            sb.append(String.format("%-10s %-20s %-8d %-5s\n", entry.getKey(), name, cred, entry.getValue()));
        }
        sb.append("-------------------------------------\n");
        sb.append("Average Grade: ").append(String.format("%.2f", average)).append("\n");
        reportArea.setText(sb.toString());
    }
}