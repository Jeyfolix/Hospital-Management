import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NurseManagement extends JFrame implements ActionListener {
    
    private Image background;

    // Constructor to initialize the NurseManagement window
    public NurseManagement() {
        // Load the background image
        background = new ImageIcon("C:\\Users\\User\\Documents\\Downloads\\Desktop\\istockphoto-532523451-1024x1024.jpg").getImage(); // Change the path accordingly

        // Set up the main window
        setSize(1540, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new ImagePanel());

        // Set up the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Nurse Information Menu
        JMenu nurseInfoMenu = new JMenu("Nurse Information");
        nurseInfoMenu.setForeground(Color.BLUE);
        menuBar.add(nurseInfoMenu);

        JMenuItem nurseDetailsItem = new JMenuItem("Nurse Details");
        nurseDetailsItem.setBackground(Color.WHITE);
        nurseDetailsItem.addActionListener(this);
        nurseInfoMenu.add(nurseDetailsItem);

        JMenuItem nurseCertificationItem = new JMenuItem("Nurse Certifications");
        nurseCertificationItem.setBackground(Color.WHITE);
        nurseCertificationItem.addActionListener(this);
        nurseInfoMenu.add(nurseCertificationItem);

        JMenuItem addNurse = new JMenuItem("Add Nurse");
        addNurse.setBackground(Color.WHITE);
        addNurse.addActionListener(this);
        nurseInfoMenu.add(addNurse);

        // Roles and Responsibilities Menu
        JMenu rolesMenu = new JMenu("Roles and Responsibilities");
        rolesMenu.setForeground(Color.RED);
        menuBar.add(rolesMenu);

        JMenuItem viewRolesItem = new JMenuItem("View Roles");
        viewRolesItem.setBackground(Color.WHITE);
        viewRolesItem.addActionListener(this);
        rolesMenu.add(viewRolesItem);

        // Nurse Schedule Menu
        JMenu scheduleMenu = new JMenu("Nurse Schedule");
        scheduleMenu.setForeground(Color.GREEN);
        menuBar.add(scheduleMenu);

        JMenuItem viewScheduleItem = new JMenuItem("View Schedule");
        viewScheduleItem.setBackground(Color.WHITE);
        viewScheduleItem.addActionListener(this);
        scheduleMenu.add(viewScheduleItem);

        // Billing and Finance Menu
        JMenu billingMenu = new JMenu("Nurse Billing and Finance");
        billingMenu.setForeground(Color.BLUE);
        menuBar.add(billingMenu);

        JMenuItem viewPaymentsItem = new JMenuItem("View Payments");
        viewPaymentsItem.setBackground(Color.WHITE);
        viewPaymentsItem.addActionListener(this);
        billingMenu.add(viewPaymentsItem);

        // Utility Menu
        JMenu utilityMenu = new JMenu("Utility");
        utilityMenu.setForeground(Color.RED);
        menuBar.add(utilityMenu);

        JMenuItem notepadItem = new JMenuItem("Notepad");
        notepadItem.setBackground(Color.WHITE);
        notepadItem.addActionListener(this);
        utilityMenu.add(notepadItem);

        JMenuItem calcItem = new JMenuItem("Calculator");
        calcItem.setBackground(Color.WHITE);
        calcItem.addActionListener(this);
        utilityMenu.add(calcItem);

        // Exit Menu
        JMenu exitMenu = new JMenu("Exit");
        exitMenu.setForeground(Color.RED);
        menuBar.add(exitMenu);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setBackground(Color.WHITE);
        exitItem.addActionListener(this);
        exitMenu.add(exitItem);

        // Set the menu bar for the JFrame
        setJMenuBar(menuBar);

        // Make the window visible
        setVisible(true);
    }

    // Inner class for painting the background image
    private class ImagePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Action listener method to handle menu item clicks
    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();

        // Handle Exit action
        if (actionCommand.equals("Exit")) {
            setVisible(false);
        }
        // Handle Notepad action (implement functionality as needed)
        else if (actionCommand.equals("Notepad")) {
            // Implement Notepad functionality
        }
        // Handle Calculator action (implement functionality as needed)
        else if (actionCommand.equals("Calculator")) {
            // Implement Calculator functionality
        }
        // Handle Nurse Details action
        else if (actionCommand.equals("Add Nurse")) {
            setVisible(false);
            new AddNurs(); // Open the NurseDetails window (you need to define this class)
        }
        // Handle Nurse Certifications action
        else if (actionCommand.equals("Nurse Details")) {
            setVisible(true);
            new NurseDetails(); // Open the NurseCertifications window (you need to define this class)
        }
        // Handle View Roles action
        else if (actionCommand.equals("View Roles")) {
            setVisible(true);
           new PatientRoleAssignments(); // Open the NurseRoles window (you need to define this class)
        }
        // Handle View Schedule action
        else if (actionCommand.equals("View Schedule")) {
            setVisible(true);
            new Nurseassignments(); // Open the NurseSchedule window (you need to define this class)
        }
        // Handle View Payments action
        else if (actionCommand.equals("View Payments")) {
            setVisible(true);
            //new NurseAssignment(); // Open the NursePayments window (you need to define this class)
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        new NurseManagement(); // Create and display the Nurse Management window
    }
}
