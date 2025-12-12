package gui;

import model.UniversityManager;
import util.FileManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MainFrame extends JFrame {
    private UniversityManager manager;
    private JTabbedPane tabbedPane;

    // I keep references to panels so we can swap them out
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private ReportPanel reportPanel;

    public MainFrame() {
        // 1. Load default data on startup
        try {
            manager = FileManager.load();
        } catch (Exception e) {
            manager = new UniversityManager();
        }

        // 2. Frame Setup
        setTitle("Student Management System");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 3. Menu Bar
        setupMenu();

        // 4. Initialize Tabs
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        // Populate the tabs with the current manager
        setupTabs();
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open Custom File...");
        JMenuItem saveItem = new JMenuItem("Save (Default)");
        JMenuItem exitItem = new JMenuItem("Exit");

        // --- OPEN FILE LOGIC ---
        openItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // Optional: Filter to show only .ser files
            fileChooser.setFileFilter(new FileNameExtensionFilter("Java Data Files (*.ser)", "ser"));

            // Open the file dialog
            int option = fileChooser.showOpenDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    // Load the NEW manager
                    manager = FileManager.load(selectedFile);

                    // CRITICAL: Re-create the tabs with the NEW data
                    setupTabs();

                    JOptionPane.showMessageDialog(this, "Loaded: " + selectedFile.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- SAVE LOGIC ---
        saveItem.addActionListener(e -> {
            try {
                FileManager.save(manager);
                JOptionPane.showMessageDialog(this, "Saved to default location!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem); // Add the Open button
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    // help method to refresh tabs
    private void setupTabs() {

        tabbedPane.removeAll();


        studentPanel = new StudentPanel(manager);
        coursePanel = new CoursePanel(manager);
        reportPanel = new ReportPanel(manager);


        tabbedPane.addTab("Students", studentPanel);
        tabbedPane.addTab("Courses", coursePanel);
        tabbedPane.addTab("Reports", reportPanel);

        for (var l : tabbedPane.getChangeListeners()) {
            tabbedPane.removeChangeListener(l);
        }

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == reportPanel) {
                reportPanel.refreshList();
            }
        });
    }
}