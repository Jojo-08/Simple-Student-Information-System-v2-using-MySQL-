import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CollegeInformationPanel extends JPanel 
{
    private  int currentPage = 1;
    private  final int pageSize = 5;

    private JButton nextButton;
    private JButton prevButton;

    private boolean isSearching = false;
    private String searchText = "";

    private  JTable table;
    private  DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    private  CollegeDAO collegeDAO = new CollegeDAO();

    public CollegeInformationPanel()
    {
        setLayout(new BorderLayout());

        // title label
        JLabel titleLabel = new JLabel("College Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
       
        //Table setup
        String[] columnNames = {"College Code", "College Name"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        table.setFont(new Font("Segoe UI", Font.PLAIN,14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD,16));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);


        //sorter for college table
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        //live search
        //declaring searchField
        searchField =  new JTextField(20);

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
                    loadColleges();
                }
        });

        //top panel search bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);

        //button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,10));
        prevButton = new JButton ("Previous");
        nextButton = new JButton ("Next");
        JButton addCollegeButton = new JButton ("Add College");
        JButton editCollegeButton = new JButton ("Edit College");
        JButton deleteCollegeButton = new JButton ("Delete College");

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(addCollegeButton);
        buttonPanel.add(editCollegeButton);
        buttonPanel.add(deleteCollegeButton);
        
        //adding panels and table to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        prevButton.addActionListener( e ->  
        {
            if(currentPage > 1)
            {
                currentPage--;
                loadColleges();
            }
        });
        
        nextButton.addActionListener( e -> 
        {
            currentPage++;
            loadColleges();
            // code that disables the next button if there are no more students on the next page
        });
    
        addCollegeButton.addActionListener(e ->
        {
            System.out.println("Adding College");
            openAddCollegeForm();
        });
    
        editCollegeButton.addActionListener( e ->
        {
            System.out.println("Editing College");
            openEditCollegeForm();
        });
    
        deleteCollegeButton.addActionListener(e->
        {
            System.out.println("Deleting Colleges");
            deleteSelectedColleges();
        });
    
        loadColleges(); // load inital data

    }

    private void loadColleges()
    {
        List<College> colleges; 
        List<College> collegesNextPage;

        if (isSearching && !searchText.isEmpty())
        {
            colleges = collegeDAO.searchCollegesByPage(searchText, currentPage, pageSize);
            collegesNextPage = collegeDAO.searchCollegesByPage(searchText, currentPage + 1, pageSize);
            System.out.println("loading searched colleges...");
        }
        else
        {
            colleges = collegeDAO.getCollegesByPage(currentPage, pageSize);
            collegesNextPage = collegeDAO.getCollegesByPage(currentPage + 1, pageSize);
            System.out.println("loading colleges...");
        }

        tableModel.setRowCount(0); // clear current rows


        for (College college : colleges)
        {
            tableModel.addRow(new Object[]{
                    college.getCollegeCode(),
                    college.getCollegeName()
            });
        }

        if (collegesNextPage.isEmpty()) {
            nextButton.setEnabled(false);
        } else {
            nextButton.setEnabled(true);
        }
        
        prevButton.setEnabled(currentPage > 1);
        

        System.out.println("colleges loaded, page: " + currentPage);
    }

    private void openAddCollegeForm() {
        JFrame addFrame = new JFrame("Add College");
        addFrame.setSize(400, 200);
        addFrame.setLayout(new BorderLayout());
    
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 rows, 2 columns, 10px gaps
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
    
        JLabel collegeCodeLabel = new JLabel("College Code:");
        JTextField collegeCodeField = new JTextField();
    
        JLabel collegeNameLabel = new JLabel("College Name:");
        JTextField collegeNameField = new JTextField();
    
        formPanel.add(collegeCodeLabel);
        formPanel.add(collegeCodeField);
        formPanel.add(collegeNameLabel);
        formPanel.add(collegeNameField);
    
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
            String collegeCode = collegeCodeField.getText().trim();
            String collegeName = collegeNameField.getText().trim();
    
            // Check for duplicate primary key
            if (collegeDAO.getCollegeByCode(collegeCode) != null) {
                JOptionPane.showMessageDialog(addFrame, "A college with this code already exists. Please use a different code.");
                return;
            }
    
            // Validate inputs
            if (collegeCode.isEmpty() || collegeName.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "All fields are required.");
                return;
            }
    
            College college = new College(collegeCode, collegeName);
            collegeDAO.addCollege(college);
            addFrame.dispose();
            loadColleges();
        });
    
        cancelButton.addActionListener(e -> addFrame.dispose());
    
        addFrame.setVisible(true);
    }

    private void openEditCollegeForm() {
        int selectedRow = table.getSelectedRow();
    
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a college to edit.");
            return;
        }
    
        String collegeCode = (String) table.getValueAt(selectedRow, 0);
        College college = collegeDAO.getCollegeByCode(collegeCode);
    
        if (college == null) {
            JOptionPane.showMessageDialog(null, "College not found.");
            return;
        }
    
        JFrame editFrame = new JFrame("Edit College");
        editFrame.setSize(400, 200);
        editFrame.setLayout(new BorderLayout());
    
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 rows, 2 columns, 10px gaps
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
    
        JLabel collegeCodeLabel = new JLabel("College Code:");
        JTextField collegeCodeField = new JTextField(college.getCollegeCode());
    
        JLabel collegeNameLabel = new JLabel("College Name:");
        JTextField collegeNameField = new JTextField(college.getCollegeName());
    
        formPanel.add(collegeCodeLabel);
        formPanel.add(collegeCodeField);
        formPanel.add(collegeNameLabel);
        formPanel.add(collegeNameField);
    
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
            String newCollegeCode = collegeCodeField.getText().trim();
            String newCollegeName = collegeNameField.getText().trim();
    
            // Check for duplicate primary key if the college code is changed
            if (!newCollegeCode.equals(collegeCode) && collegeDAO.getCollegeByCode(newCollegeCode) != null) {
                JOptionPane.showMessageDialog(editFrame, "A college with this code already exists. Please use a different code.");
                return;
            }
    
            // Validate inputs
            if (newCollegeCode.isEmpty() || newCollegeName.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "All fields are required.");
                return;
            }
    
            college.setCollegeCode(newCollegeCode);
            college.setCollegeName(newCollegeName);
            collegeDAO.updateCollege(college, collegeCode);
    
            editFrame.dispose();
            loadColleges();
        });
    
        cancelButton.addActionListener(e -> editFrame.dispose());
    
        editFrame.setVisible(true);
    }

    private  void deleteSelectedColleges()
    {
        int[] selectedRow = table.getSelectedRows();
        if(selectedRow.length == 0)
        {
            JOptionPane.showMessageDialog(this,"Please select a college");
            return;
        }
    
        int confirm = JOptionPane.showConfirmDialog(this,
                       "Are you sure you want to delete the selected colleges?" 
                       + " Deleting a college will also affect related programs and students.",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION );
    
        if(confirm == JOptionPane.YES_OPTION)
        {
            
            for(int row : selectedRow)
            {
                String collegeCode = (String) tableModel.getValueAt(row, 0);
                collegeDAO.deleteCollege(collegeCode);
                
            }
            System.out.println("Colleges successfully deleted");
            loadColleges();
            ((ProgramInformationPanel) ((JTabbedPane) getParent()).getComponentAt(1)).loadPrograms(); // Reload programs
            ((StudentInformationPanel) ((JTabbedPane) getParent()).getComponentAt(0)).loadStudents(); // Reload students
        }
    
    }
}
