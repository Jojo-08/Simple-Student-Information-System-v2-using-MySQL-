import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ProgramInformationPanel extends JPanel 
{
    private  int currentPage = 1;
    private  final int pageSize = 20;

    private JButton nextButton;
    private JButton prevButton; 

    private boolean isSearching = false;
    private String searchText = "";

    private static JTable table;
    private static DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    private final ProgramDAO programDAO = new ProgramDAO();
    private final CollegeDAO collegeDAO = new CollegeDAO();

    public  ProgramInformationPanel()
    {

        setLayout(new BorderLayout());
        //title
        JLabel titleLabel = new JLabel("Program Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        //declaring search field
        searchField = new JTextField(20);

        // Table Model
        String[] columnNames = {"Program Code", "Program Name", "College Code"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        
        
        //sorter for table
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        //live search
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
                    currentPage = 1;
                    loadPrograms();
                }
        });
        
                
        //top panel search bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(titleLabel);

        //Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        JButton addProgramButton = new JButton("Add Program");
        JButton editProgramButton = new JButton("Edit Program");
        JButton deleteProgramButton = new JButton("Delete Program");
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(addProgramButton);
        buttonPanel.add(editProgramButton);
        buttonPanel.add(deleteProgramButton);
        
        // add panels and table to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // button actions
        prevButton.addActionListener( e ->  
        {
            if(currentPage > 1)
            {
                currentPage--;
                loadPrograms();
            }
        });
        
        nextButton.addActionListener( e -> 
        {
            currentPage++;
            loadPrograms();
            // code that disables the next button if there are no more students on the next page
        });

        addProgramButton.addActionListener(e ->
        {
            System.out.println("Adding Programs");
            openAddProgramForm();
        });

        editProgramButton.addActionListener( e ->
        {
            System.out.println("Editing Programs");
            openEditProgramForm();
        });

        deleteProgramButton.addActionListener(e->
        {
            System.out.println("Deleting Programs");
            deleteSelectedProgram();
        });

        
        loadPrograms(); //load innital data

    }

    public  void loadPrograms()
    {
        

        List<Program> programs; 
        List<Program> programsNextPage;

        if (isSearching && !searchText.isEmpty())
        {
            programs = programDAO.searchProgramsByPage(searchText, currentPage, pageSize);
            programsNextPage = programDAO.searchProgramsByPage(searchText, currentPage + 1, pageSize);
            System.out.println("loading searched programs...");
        }
        else
        {
            programs = programDAO.getProgramsByPage(currentPage, pageSize);
            programsNextPage = programDAO.getProgramsByPage(currentPage + 1, pageSize);
            System.out.println("loading programs...");
        }

        tableModel.setRowCount(0); // clear current rows
        for (Program program : programs)
        {
            String thiscollegeCode;
            if(program.getCollegeCode() == null)
            {
                thiscollegeCode = "N/A";
            }
            else
            {
                thiscollegeCode = program.getCollegeCode();
            }
            tableModel.addRow(new Object[]{
                    program.getProgramCode(),
                    program.getProgramName(),
                    thiscollegeCode
            });
        }

        if (programsNextPage.isEmpty()) {
            nextButton.setEnabled(false);
        } else {
            nextButton.setEnabled(true);
        }

        System.out.println("programs loaded page: " + currentPage);
        prevButton.setEnabled(currentPage > 1);
    }

    private void openAddProgramForm() {
        JFrame addFrame = new JFrame("Add Program");
        addFrame.setSize(400, 300);
        addFrame.setLayout(new BorderLayout());
    
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, 10px gaps
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
    
        JLabel programCodeLabel = new JLabel("Program Code:");
        JTextField programCodeField = new JTextField();
    
        JLabel programNameLabel = new JLabel("Program Name:");
        JTextField programNameField = new JTextField();
    
        JLabel collegeCodeLabel = new JLabel("College Code:");
        JComboBox<String> collegeCodeCombo = new JComboBox<>();
    
        // Load college codes into the combo box
        List<College> colleges = collegeDAO.getAllColleges();
        for (College college : colleges) {
            collegeCodeCombo.addItem(college.getCollegeCode());
        }
    
        formPanel.add(programCodeLabel);
        formPanel.add(programCodeField);
        formPanel.add(programNameLabel);
        formPanel.add(programNameField);
        formPanel.add(collegeCodeLabel);
        formPanel.add(collegeCodeCombo);
    
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
            String programCode = programCodeField.getText().trim();
            String programName = programNameField.getText().trim();
            String collegeCode = (String) collegeCodeCombo.getSelectedItem();
    
            // Check for duplicate primary key
            if (programDAO.getProgramByProgCode(programCode) != null) {
                JOptionPane.showMessageDialog(addFrame, "A program with this code already exists. Please use a different code.");
                return;
            }
    
            // Validate inputs
            if (programCode.isEmpty() || programName.isEmpty() || collegeCode == null) {
                JOptionPane.showMessageDialog(addFrame, "All fields are required.");
                return;
            }
    
            Program program = new Program(programCode, programName, collegeCode);
            programDAO.addProgram(program);
            addFrame.dispose();
            loadPrograms();
        });
    
        cancelButton.addActionListener(e -> addFrame.dispose());
    
        addFrame.setVisible(true);
    }

    private void openEditProgramForm() {
        int selectedRow = table.getSelectedRow();
    
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a program to edit.");
            return;
        }
    
        String programCode = (String) table.getValueAt(selectedRow, 0);
        Program program = programDAO.getProgramByProgCode(programCode);
    
        if (program == null) {
            JOptionPane.showMessageDialog(null, "Program not found.");
            return;
        }
    
        JFrame editFrame = new JFrame("Edit Program");
        editFrame.setSize(400, 300);
        editFrame.setLayout(new BorderLayout());
    
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, 10px gaps
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
    
        JLabel programCodeLabel = new JLabel("Program Code:");
        JTextField programCodeField = new JTextField(program.getProgramCode());
    
        JLabel programNameLabel = new JLabel("Program Name:");
        JTextField programNameField = new JTextField(program.getProgramName());
    
        JLabel collegeCodeLabel = new JLabel("College Code:");
        JComboBox<String> collegeCodeCombo = new JComboBox<>();
    
        // Load college codes into the combo box
        List<College> colleges = collegeDAO.getAllColleges();
        for (College college : colleges) {
            collegeCodeCombo.addItem(college.getCollegeCode());
        }
        collegeCodeCombo.setSelectedItem(program.getCollegeCode());
    
        formPanel.add(programCodeLabel);
        formPanel.add(programCodeField);
        formPanel.add(programNameLabel);
        formPanel.add(programNameField);
        formPanel.add(collegeCodeLabel);
        formPanel.add(collegeCodeCombo);
    
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
            String newProgramCode = programCodeField.getText().trim();
            String newProgramName = programNameField.getText().trim();
            String newCollegeCode = (String) collegeCodeCombo.getSelectedItem();
    
            // Check for duplicate primary key if the program code is changed
            if (!newProgramCode.equals(programCode) && programDAO.getProgramByProgCode(newProgramCode) != null) {
                JOptionPane.showMessageDialog(editFrame, "A program with this code already exists. Please use a different code.");
                return;
            }
    
            // Validate inputs
            if (newProgramCode.isEmpty() || newProgramName.isEmpty() || newCollegeCode == null) {
                JOptionPane.showMessageDialog(editFrame, "All fields are required.");
                return;
            }
    
            program.setProgramCode(newProgramCode);
            program.setProgramName(newProgramName);
            program.setCollegeCode(newCollegeCode);
    
            programDAO.updateProgram(program, programCode);
            editFrame.dispose();
            loadPrograms();
        });
    
        cancelButton.addActionListener(e -> editFrame.dispose());
    
        editFrame.setVisible(true);
    }
    
    private  void deleteSelectedProgram()
    {
        int[] selectedRows = table.getSelectedRows();
        if(selectedRows.length == 0)
        {
            JOptionPane.showMessageDialog(this,"Please select a program");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                       "Are you sure you want to delete program ?"
                       + " Deleting a program will also affect related students",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION );
    
                        if(confirm == JOptionPane.YES_OPTION)
                        {
                            // Collect all program codes to delete first
                            java.util.List<String> codesToDelete = new java.util.ArrayList<>();
                            for(int viewRow : selectedRows)
                            {
                                int modelRow = table.convertRowIndexToModel(viewRow);
                                String progCode = (String) tableModel.getValueAt(modelRow, 0);
                                codesToDelete.add(progCode);
                            }
                            for(String progCode : codesToDelete)
                            {
                                programDAO.deleteProgram(progCode);
                            }
                            System.out.println("Program(s) successfully deleted");
                            loadPrograms();
                            ((StudentInformationPanel) ((JTabbedPane) getParent()).getComponentAt(0)).loadStudents();
                        }
    
    }


}