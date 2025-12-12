package gui;

import model.Course;
import model.Student;
import model.UniversityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class CoursePanel extends JPanel {
    private UniversityManager manager;
    private JComboBox<Student> studentBox;
    private JComboBox<Course> courseBox;
    private JTextArea gradeDisplay;
    private JTable courseTable;
    private DefaultTableModel courseTableModel;
    private JTextField cCode, cName, cCredits;
    private String selectedCourseCode = null;

    public CoursePanel(UniversityManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Manage Courses"));

        JPanel inputPanel = new JPanel();
        cCode = new JTextField(5);
        cName = new JTextField(10);
        cCredits = new JTextField(3);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update Selected");

        // - BUTTON STYLnG
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setForeground(Color.RED); // <--- MADE IT RED

        JButton clearBtn = new JButton("Clear");

        inputPanel.add(new JLabel("Code:")); inputPanel.add(cCode);
        inputPanel.add(new JLabel("Name:")); inputPanel.add(cName);
        inputPanel.add(new JLabel("Credits:")); inputPanel.add(cCredits);
        inputPanel.add(addBtn);
        inputPanel.add(updateBtn);
        inputPanel.add(deleteBtn);
        inputPanel.add(clearBtn);

        String[] cols = {"Code", "Name", "Credits"};
        courseTableModel = new DefaultTableModel(cols, 0);
        courseTable = new JTable(courseTableModel);
        JScrollPane tableScroll = new JScrollPane(courseTable);
        tableScroll.setPreferredSize(new Dimension(400, 150));

        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(tableScroll, BorderLayout.CENTER);

        //  BOTTOM: GRADES
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel gradeInputPanel = new JPanel();
        gradeInputPanel.setBorder(BorderFactory.createTitledBorder("Assign Grades"));

        studentBox = new JComboBox<>();
        courseBox = new JComboBox<>();
        JTextField gradeVal = new JTextField(5);
        JButton assignBtn = new JButton("Assign");
        JButton refreshBtn = new JButton("Refresh Lists");

        gradeInputPanel.add(new JLabel("Student:")); gradeInputPanel.add(studentBox);
        gradeInputPanel.add(new JLabel("Course:")); gradeInputPanel.add(courseBox);
        gradeInputPanel.add(new JLabel("Grade:")); gradeInputPanel.add(gradeVal);
        gradeInputPanel.add(assignBtn);
        gradeInputPanel.add(refreshBtn);

        gradeDisplay = new JTextArea();
        gradeDisplay.setEditable(false);
        gradeDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        gradeDisplay.setBorder(BorderFactory.createTitledBorder("Current Student History"));

        bottomPanel.add(gradeInputPanel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(gradeDisplay), BorderLayout.CENTER);

        add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel), BorderLayout.CENTER);

        //  LISTENERS

        // 1 ADD COURSE
        addBtn.addActionListener(e -> {
            try {
                String code = cCode.getText().trim();
                String name = cName.getText().trim();
                int credits = Integer.parseInt(cCredits.getText().trim());
                if(!code.isEmpty() && !name.isEmpty()) {
                    if (manager.addCourse(new Course(code, name, credits))) {
                        refreshCourseTable(); updateDropdowns(); clearInputs();
                    } else JOptionPane.showMessageDialog(this, "Code exists!");
                }
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid Credits"); }
        });

        // TABLE SELECTION
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            int row = courseTable.getSelectedRow();
            if (row != -1) {
                selectedCourseCode = (String) courseTableModel.getValueAt(row, 0);
                cCode.setText(selectedCourseCode);
                cName.setText((String) courseTableModel.getValueAt(row, 1));
                cCredits.setText(courseTableModel.getValueAt(row, 2).toString());
            }
        });

        //  UPDATE COURSE
        updateBtn.addActionListener(e -> {
            if (selectedCourseCode == null) return;
            try {
                int cr = Integer.parseInt(cCredits.getText().trim());
                if (manager.updateCourse(selectedCourseCode, cCode.getText(), cName.getText(), cr)) {
                    refreshCourseTable(); updateDropdowns(); clearInputs();
                } else JOptionPane.showMessageDialog(this, "Update Failed");
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid Credits"); }
        });

        // 4   DELETE COURSE
        deleteBtn.addActionListener(e -> {
            if (selectedCourseCode != null) {
                Course c = manager.getCourseByCode(selectedCourseCode);
                if(c != null) {
                    manager.removeCourse(c);
                    refreshCourseTable(); updateDropdowns(); clearInputs();
                }
            }
        });

        clearBtn.addActionListener(e -> clearInputs());
        refreshBtn.addActionListener(e -> updateDropdowns());

        // 5. ASSIGN GRADE
        assignBtn.addActionListener(e -> {
            Student s = (Student) studentBox.getSelectedItem();
            Course c = (Course) courseBox.getSelectedItem();
            if(s != null && c != null) {
                String g = gradeVal.getText().trim();
                if (!g.isEmpty()) {
                    manager.assignGrade(s, c, g);
                    updateQuickLog(s);
                    gradeVal.setText("");
                }
            }
        });

        studentBox.addActionListener(e -> {
            Student s = (Student) studentBox.getSelectedItem();
            if (s != null) {
                updateQuickLog(s);
            }
        });

        refreshCourseTable();
    }

    private void updateQuickLog(Student s) {
        Map<String, String> grades = manager.getRawGrades(s);
        StringBuilder sb = new StringBuilder();
        sb.append("History for: ").append(s.getName()).append("\n");
        sb.append("----------------------------------\n");

        if (grades.isEmpty()) {
            sb.append("No courses assigned yet.");
        } else {
            for (Map.Entry<String, String> entry : grades.entrySet()) {
                Course c = manager.getCourseByCode(entry.getKey());
                String cName = (c != null) ? c.getName() : entry.getKey();
                sb.append(cName).append(" : ").append(entry.getValue()).append("\n");
            }
        }
        gradeDisplay.setText(sb.toString());
    }

    private void clearInputs() {
        cCode.setText(""); cName.setText(""); cCredits.setText("");
        courseTable.clearSelection(); selectedCourseCode = null;
    }

    private void refreshCourseTable() {
        courseTableModel.setRowCount(0);
        for(Course c : manager.getCourses())
            courseTableModel.addRow(new Object[]{c.getCode(), c.getName(), c.getCredits()});
    }

    private void updateDropdowns() {
        studentBox.removeAllItems();
        courseBox.removeAllItems();
        for(Student s : manager.getStudents()) studentBox.addItem(s);
        for(Course c : manager.getCourses()) courseBox.addItem(c);
    }
}