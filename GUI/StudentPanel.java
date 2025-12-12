package gui;

import model.Student;
import model.UniversityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentPanel extends JPanel {
    private UniversityManager manager;
    private DefaultTableModel tableModel;

    // Inputs
    private JTextField idField, nameField, emailField, dateField, searchField;
    private JTable table;

    public StudentPanel(UniversityManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        //  TOP AREA
        JPanel topContainer = new JPanel(new BorderLayout());

        // Input Area
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Manage Students"));

        idField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        dateField = new JTextField();

        JButton addBtn = new JButton("Add Student");
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setForeground(Color.RED);

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Enrollment Date:"));
        inputPanel.add(dateField);

        inputPanel.add(addBtn);
        inputPanel.add(deleteBtn);


        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search (ID, Name, Email, Date):"));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton clearSearchBtn = new JButton("Show All");

        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearSearchBtn);

        topContainer.add(inputPanel, BorderLayout.CENTER);
        topContainer.add(searchPanel, BorderLayout.SOUTH);

        String[] cols = {"ID", "Name", "Email", "Enrollment Date"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);

        add(topContainer, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        //  LISTENErrs
        addBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String date = dateField.getText().trim();

            if (!id.isEmpty() && !name.isEmpty() && !email.isEmpty() && !date.isEmpty()) {
                // Pass all 4 fields t0o constructr
                if (manager.addStudent(new Student(id, name, email, date))) {
                    refreshTable();
                    clearInputs();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Student ID exists!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);
                for(Student s : manager.getStudents()) {
                    if(s.getId().equals(id)) {
                        manager.removeStudent(s);
                        refreshTable();
                        break;
                    }
                }
            }
        });

        searchBtn.addActionListener(e -> {
            String q = searchField.getText().trim();
            if(!q.isEmpty()) {
                tableModel.setRowCount(0);
                for(Student s : manager.searchStudents(q)) {
                    // Update rows with new data
                    tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getEmail(), s.getEnrollmentDate()});
                }
            }
        });

        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        refreshTable();
    }

    private void clearInputs() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        dateField.setText("");
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : manager.getStudents()) {
            // Update rows with new datas
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getEmail(), s.getEnrollmentDate()});
        }
    }
}