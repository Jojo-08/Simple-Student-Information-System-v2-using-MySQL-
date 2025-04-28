import javax.swing.*;

public class StudentInformationSystem extends JFrame{
    
    private JTabbedPane tabbedPane;

    public StudentInformationSystem()
    {
        setTitle("Student Information System");
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        
        //add tabs

        tabbedPane.addTab("Students", new StudentInformationPanel());
        tabbedPane.addTab("Programs", new ProgramInformationPanel());
        tabbedPane.addTab("Colleges", new CollegeInformationPanel());

        add(tabbedPane);

    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()->
            {
                new StudentInformationSystem().setVisible(true);
            });
    }
}
