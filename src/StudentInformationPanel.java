import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class StudentInformationPanel extends JPanel{

    private  int currentPage = 1;
    private  final int pageSize = 5;

    private JButton prevButton;
    private JButton nextButton;

    private boolean isSearching = false;
    private String searchText = "";

    private  JTable table;
    private  DefaultTableModel tableModel;
    private  JTextField searchField;
    private  TableRowSorter<DefaultTableModel> sorter;

    private  StudentDAO studentDAO = new StudentDAO();
    private  ProgramDAO programDAO = new ProgramDAO();

    public StudentInformationPanel() 
    {
        
        setLayout(new BorderLayout());
        // title label
        JLabel titleLabel = new JLabel("Student Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        //table model
        String[] columnNames = {"ID Number","First Name", "Last Name", "Year", "Gender",  "Program Code"};
        tableModel = new DefaultTableModel(columnNames,0);
        table = new JTable(tableModel);

        table.setFont(new Font("Segoe UI", Font.PLAIN,14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD,16));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        //sorter for table
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        //live search
        // declaring searchField
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener()
        {
                public void insertUpdate(DocumentEvent e) { search(); }
                public void removeUpdate(DocumentEvent e) { search(); }
                public void changedUpdate(DocumentEvent e) { search(); }

                private void search() {
                    String text = searchField.getText().trim();
                    if (text.length() == 0) {
                        isSearching = false;
                        searchText = "";
                    } else {
                        isSearching = true;
                        searchText = text;
                    }
                    currentPage = 1; // Reset to first page
                    loadStudents(); // or loadStudents(), depending on where you are
                }
        });

        //top panel search bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(titleLabel);

        //button panels
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        JButton addButton = new JButton("Add Student");
        JButton editButton = new JButton("Edit Student");
        JButton deleteButton = new JButton("Delete Student");
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // add panel to main panels
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        prevButton.addActionListener( e -> 
        {
            if(currentPage > 1)
            {
                currentPage--;
                loadStudents();
            }
        });

        nextButton.addActionListener( e -> 
        {
            currentPage++;
            loadStudents();
        });

        
        addButton.addActionListener( e ->
        {
           System.out.println("Adding student");
            openAddStudentForm();
        } );

        
        deleteButton.addActionListener( e ->
            {
                System.out.println("deleting student");
                deleteSelectedStudent();
            }
        );

        editButton.addActionListener(e ->
        {
            System.out.println("Editing a student");
            openEditStudentForm();
        });

        loadStudents(); //load initial list
    }

    public void loadStudents()
    {
        
        List<Student> students;
        List<Student> studentsNextPage;

        if (isSearching && !searchText.isEmpty())
        {
            students = studentDAO.searchStudentsByPage(searchText, currentPage, pageSize);
            studentsNextPage = studentDAO.searchStudentsByPage(searchText, currentPage + 1, pageSize);
            System.out.println("loading searched students...");
        }
        else
        {
            students = studentDAO.getStudentsByPage(currentPage, pageSize);
            studentsNextPage = studentDAO.getStudentsByPage(currentPage + 1, pageSize);
            System.out.println("loading students...");
        }

        tableModel.setRowCount(0); //clear current rows
        for (Student student : students)
        {
           String programCode;
            if(student.getProgramCode() == null)
            {
                programCode = "Unenrolled";
            }
            else
            {
                programCode = student.getProgramCode();
            }
            tableModel.addRow(new Object[]
            {
                    student.getStudentId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getYearLevel(),
                    student.getGender(),
                    programCode
            });
        }
        if (studentsNextPage.isEmpty()) {
            nextButton.setEnabled(false);
        } else {
            nextButton.setEnabled(true);
        }
        
        prevButton.setEnabled(currentPage > 1);
        System.out.println("Students loaded");
    }

    private void openAddStudentForm() {
        JFrame addFrame = new JFrame("Add Student");
        addFrame.setSize(400, 400);
        addFrame.setLayout(new BorderLayout());
    
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10)); // 6 rows, 2 columns, 10px gaps
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
    
        JLabel studentIdLabel = new JLabel("Student ID (YYYY-NNNN):");
        JTextField studentIdField = new JTextField();
    
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
    
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
    
        JLabel yearLevelLabel = new JLabel("Year Level:");
        JTextField yearLevelField = new JTextField();
    
        JLabel genderLabel = new JLabel("Gender:");
        JTextField genderField = new JTextField();
    
        JLabel programCodeLabel = new JLabel("Program Code:");
        JComboBox<String> programCodeCombo = new JComboBox<>();
    
        // Load program codes into the combo box
        List<Program> programs = programDAO.getAllPrograms();
        for (Program program : programs) {
            programCodeCombo.addItem(program.getProgramCode());
        }
    
        formPanel.add(studentIdLabel);
        formPanel.add(studentIdField);
        formPanel.add(firstNameLabel);
        formPanel.add(firstNameField);
        formPanel.add(lastNameLabel);
        formPanel.add(lastNameField);
        formPanel.add(yearLevelLabel);
        formPanel.add(yearLevelField);
        formPanel.add(genderLabel);
        formPanel.add(genderField);
        formPanel.add(programCodeLabel);
        formPanel.add(programCodeCombo);
    
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
    
        // Add panels to the frame
        addFrame.add(formPanel, BorderLayout.CENTER);
        addFrame.add(buttonPanel, BorderLayout.SOUTH);
    
        // Button Actions
        saveButton.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String yearLevelText = yearLevelField.getText().trim();
            String gender = genderField.getText().trim();
            String programCode = (String) programCodeCombo.getSelectedItem();
    
            // Check for duplicate primary key
            if (studentDAO.getStudentById(studentId) != null) {
                JOptionPane.showMessageDialog(addFrame, "A student with this ID already exists. Please use a different ID.");
                return;
            }
    
            // Validate inputs
            if (studentId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || yearLevelText.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "All fields are required.");
                return;
            }
    
            // Validate ID format
            if (!studentId.matches("^\\d{4}-\\d{4}$")) {
                JOptionPane.showMessageDialog(addFrame, "Invalid ID format! Use YYYY-NNNN (e.g., 2025-0001).");
                return;
            }
    
            // Validate year level
            int yearLevel;
            try {
                yearLevel = Integer.parseInt(yearLevelText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addFrame, "Year Level must be a valid number.");
                return;
            }
    
            Student newStudent = new Student(studentId, firstName, lastName, yearLevel, gender, programCode);
            studentDAO.addStudent(newStudent);
            addFrame.dispose();
            loadStudents(); // Refresh the table
        });
    
        cancelButton.addActionListener(e -> addFrame.dispose());
    
        addFrame.setVisible(true);
    }

    private void deleteSelectedStudent()
    {
        int[] selectedRow = table.getSelectedRows();

        if(selectedRow.length == 0)
        {
            JOptionPane.showMessageDialog(this, "Please select a student to delete. ");
            return;
        }

       

        int confirm = JOptionPane.showConfirmDialog(this, 
                        "Are you sure you want to delete student ?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if(confirm == JOptionPane.YES_OPTION)
        {
            for(int row : selectedRow)
            {
             
                String idNumber = (String) tableModel.getValueAt(row,0);                    
                studentDAO.deleteStudent(idNumber);
                    
            }
            System.out.println("Students successfully deleted");
            loadStudents();
        }
    }

    private void openEditStudentForm() {
        int selectedRow = table.getSelectedRow();
    
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a student to edit.");
            return;
        }
    
        String studentId = (String) table.getValueAt(selectedRow, 0);
        Student student = studentDAO.getStudentById(studentId);
    
        if (student == null) {
            JOptionPane.showMessageDialog(null, "Student not found.");
            return;
        }
    
        JFrame editFrame = new JFrame("Edit Student");
        editFrame.setSize(400, 400);
        editFrame.setLayout(new BorderLayout());
    
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10)); // 6 rows, 2 columns, 10px gaps
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
    
        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField(student.getStudentId());
        studentIdField.setEditable(false); // Make ID non-editable
    
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField(student.getFirstName());
    
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField(student.getLastName());
    
        JLabel yearLevelLabel = new JLabel("Year Level:");
        JTextField yearLevelField = new JTextField(String.valueOf(student.getYearLevel()));
    
        JLabel genderLabel = new JLabel("Gender:");
        JTextField genderField = new JTextField(student.getGender());
    
        JLabel programCodeLabel = new JLabel("Program Code:");
        JComboBox<String> programCodeCombo = new JComboBox<>();
    
        // Load program codes into the combo box
        List<Program> programs = programDAO.getAllPrograms();
        for (Program program : programs) {
            programCodeCombo.addItem(program.getProgramCode());
        }
        programCodeCombo.setSelectedItem(student.getProgramCode());
    
        formPanel.add(studentIdLabel);
        formPanel.add(studentIdField);
        formPanel.add(firstNameLabel);
        formPanel.add(firstNameField);
        formPanel.add(lastNameLabel);
        formPanel.add(lastNameField);
        formPanel.add(yearLevelLabel);
        formPanel.add(yearLevelField);
        formPanel.add(genderLabel);
        formPanel.add(genderField);
        formPanel.add(programCodeLabel);
        formPanel.add(programCodeCombo);
    
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
    
        // Add panels to the frame
        editFrame.add(formPanel, BorderLayout.CENTER);
        editFrame.add(buttonPanel, BorderLayout.SOUTH);
    
        // Button Actions
        saveButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String yearLevelText = yearLevelField.getText().trim();
            String gender = genderField.getText().trim();
            String programCode = (String) programCodeCombo.getSelectedItem();
    
            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty() || yearLevelText.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "All fields are required.");
                return;
            }
    
            // Validate year level
            int yearLevel;
            try {
                yearLevel = Integer.parseInt(yearLevelText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editFrame, "Year Level must be a valid number.");
                return;
            }
    
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setYearLevel(yearLevel);
            student.setGender(gender);
            student.setProgramCode(programCode);
    
            studentDAO.updateStudent(student, studentId);
            editFrame.dispose();
            loadStudents(); // Refresh the table
        });
    
        cancelButton.addActionListener(e -> editFrame.dispose());
    
        editFrame.setVisible(true);
    }
}
