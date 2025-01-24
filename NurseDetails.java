import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class NurseDetails extends JFrame implements ActionListener {
    JTextField tfNurseNoSearch, tfName, tfPhone, tfEmail, tfAddress, tfLicense, tfQualification, tfDept, tfDesignation, tfShift, tfSupervisor, tfBloodGroup, tfSalary, tfUsername, tfPassword;
    JComboBox<String> cbGender;
    JButton search, add, update, delete, back, print;
    JTable table;
    Connection conn;
    String nurseNoToUpdate = "";

    NurseDetails() {
        // Initialize the database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        setResizable(false); // Disable resizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Add close button
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Nurse Number Search
        JLabel searchLabel = new JLabel("Enter Nurse Number:");
        searchLabel.setBounds(20, 20, 150, 30);
        searchLabel.setFont(new Font("serif", Font.BOLD, 20));
        add(searchLabel);

        tfNurseNoSearch = new JTextField();
        tfNurseNoSearch.setBounds(180, 20, 150, 30);
        add(tfNurseNoSearch);

        search = new JButton("Search");
        search.setBounds(340, 20, 100, 30);
        search.addActionListener(this);
        add(search);

        // Print Button
        print = new JButton("Print");
        print.setBounds(450, 20, 100, 30);
        print.addActionListener(this);
        add(print);

        // Table for displaying nurse details
        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 1300, 400);
        add(jsp);

        // Nurse Details Fields (for Add and Update)
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(20, 520, 100, 30);
        lblName.setFont(new Font("serif", Font.BOLD, 15));
        add(lblName);

        tfName = new JTextField();
        tfName.setBounds(120, 520, 200, 30);
        add(tfName);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(20, 560, 100, 30);
        lblGender.setFont(new Font("serif", Font.BOLD, 15));
        add(lblGender);

        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cbGender.setBounds(120, 560, 100, 30);
        add(cbGender);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(20, 600, 100, 30);
        lblPhone.setFont(new Font("serif", Font.BOLD, 15));
        add(lblPhone);

        tfPhone = new JTextField();
        tfPhone.setBounds(120, 600, 200, 30);
        add(tfPhone);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 640, 100, 30);
        lblEmail.setFont(new Font("serif", Font.BOLD, 15));
        add(lblEmail);

        tfEmail = new JTextField();
        tfEmail.setBounds(120, 640, 200, 30);
        add(tfEmail);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(20, 680, 100, 30);
        lblAddress.setFont(new Font("serif", Font.BOLD, 15));
        add(lblAddress);

        tfAddress = new JTextField();
        tfAddress.setBounds(120, 680, 200, 30);
        add(tfAddress);

        JLabel lblLicense = new JLabel("License:");
        lblLicense.setBounds(340, 520, 100, 30);
        lblLicense.setFont(new Font("serif", Font.BOLD, 15));
        add(lblLicense);

        tfLicense = new JTextField();
        tfLicense.setBounds(440, 520, 200, 30);
        add(tfLicense);

        JLabel lblQualification = new JLabel("Qualification:");
        lblQualification.setBounds(340, 560, 100, 30);
        lblQualification.setFont(new Font("serif", Font.BOLD, 15));
        add(lblQualification);

        tfQualification = new JTextField();
        tfQualification.setBounds(440, 560, 200, 30);
        add(tfQualification);

        JLabel lblDept = new JLabel("Department:");
        lblDept.setBounds(340, 600, 100, 30);
        lblDept.setFont(new Font("serif", Font.BOLD, 15));
        add(lblDept);

        tfDept = new JTextField();
        tfDept.setBounds(440, 600, 200, 30);
        add(tfDept);

        JLabel lblDesignation = new JLabel("Designation:");
        lblDesignation.setBounds(340, 640, 100, 30);
        lblDesignation.setFont(new Font("serif", Font.BOLD, 15));
        add(lblDesignation);

        tfDesignation = new JTextField();
        tfDesignation.setBounds(440, 640, 200, 30);
        add(tfDesignation);

        JLabel lblShift = new JLabel("Shift:");
        lblShift.setBounds(340, 680, 100, 30);
        lblShift.setFont(new Font("serif", Font.BOLD, 15));
        add(lblShift);

        tfShift = new JTextField();
        tfShift.setBounds(440, 680, 200, 30);
        add(tfShift);

        JLabel lblSupervisor = new JLabel("Supervisor:");
        lblSupervisor.setBounds(660, 520, 100, 30);
        lblSupervisor.setFont(new Font("serif", Font.BOLD, 15));
        add(lblSupervisor);

        tfSupervisor = new JTextField();
        tfSupervisor.setBounds(760, 520, 200, 30);
        add(tfSupervisor);

        JLabel lblBloodGroup = new JLabel("Blood Group:");
        lblBloodGroup.setBounds(660, 560, 100, 30);
        lblBloodGroup.setFont(new Font("serif", Font.BOLD, 15));
        add(lblBloodGroup);

        tfBloodGroup = new JTextField();
        tfBloodGroup.setBounds(760, 560, 200, 30);
        add(tfBloodGroup);

        JLabel lblSalary = new JLabel("Salary:");
        lblSalary.setBounds(660, 600, 100, 30);
        lblSalary.setFont(new Font("serif", Font.BOLD, 15));
        add(lblSalary);

        tfSalary = new JTextField();
        tfSalary.setBounds(760, 600, 200, 30);
        add(tfSalary);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(660, 640, 100, 30);
        lblUsername.setFont(new Font("serif", Font.BOLD, 15));
        add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(760, 640, 200, 30);
        add(tfUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(660, 680, 100, 30);
        lblPassword.setFont(new Font("serif", Font.BOLD, 15));
        add(lblPassword);

        tfPassword = new JTextField();
        tfPassword.setBounds(760, 680, 200, 30);
        add(tfPassword);

        // Action Buttons
        add = new JButton("Add");
        add.setBounds(980, 520, 80, 30);
        add.addActionListener(this);
        add(add);

        update = new JButton("Update");
        update.setBounds(980, 560, 80, 30);
        update.addActionListener(this);
        add(update);

        delete = new JButton("Delete");
        delete.setBounds(980, 600, 80, 30);
        delete.addActionListener(this);
        add(delete);

        back = new JButton("Back");
        back.setBounds(980, 640, 80, 30);
        back.addActionListener(this);
        add(back);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            String nurseNo = tfNurseNoSearch.getText();
            if (!nurseNo.isEmpty()) {
                fetchAndDisplayNurseDetails(nurseNo);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid Nurse Number");
            }
        } else if (e.getSource() == add) {
             new NurseManagement();
        } else if (e.getSource() == update) {
            updateNurseDetails();
        } else if (e.getSource() == delete) {
            deleteNurseDetails();
        } else if (e.getSource() == back) {
            new NurseManagement();
            // You can navigate back to your main screen or dashboard here.
        } else if (e.getSource() == print) {
            // Print button functionality
            try {
                table.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error printing the table.");
            }
        }
    }

    // Method to fetch and display nurse details from the database
    private void fetchAndDisplayNurseDetails(String nurseNo) {
        try {
            String query = "SELECT * FROM nurses WHERE nurse_no = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nurseNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Object[][] data = new Object[1][16];
                data[0][0] = rs.getString("nurse_no");
                data[0][1] = rs.getString("name");
                data[0][2] = rs.getString("gender");
                data[0][3] = rs.getString("phone");
                data[0][4] = rs.getString("email");
                data[0][5] = rs.getString("address");
                data[0][6] = rs.getString("license");
                data[0][7] = rs.getString("qualification");
                data[0][8] = rs.getString("dept");
                data[0][9] = rs.getString("designation");
                data[0][10] = rs.getString("shift");
                data[0][11] = rs.getString("supervisor");
                data[0][12] = rs.getString("blood_group");
                data[0][13] = rs.getString("salary");
                data[0][14] = rs.getString("username");
                data[0][15] = rs.getString("password");

                String[] columnNames = {"Nurse No", "Name", "Gender", "Phone", "Email", "Address", "License", "Qualification", "Department", "Designation", "Shift", "Supervisor", "Blood Group", "Salary", "Username", "Password"};
                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                table.setModel(model);

                // Store the nurse number for update/delete
                nurseNoToUpdate = rs.getString("nurse_no");

                // Set text fields to nurse details for update
                tfName.setText(rs.getString("name"));
                cbGender.setSelectedItem(rs.getString("gender"));
                tfPhone.setText(rs.getString("phone"));
                tfEmail.setText(rs.getString("email"));
                tfAddress.setText(rs.getString("address"));
                tfLicense.setText(rs.getString("license"));
                tfQualification.setText(rs.getString("qualification"));
                tfDept.setText(rs.getString("dept"));
                tfDesignation.setText(rs.getString("designation"));
                tfShift.setText(rs.getString("shift"));
                tfSupervisor.setText(rs.getString("supervisor"));
                tfBloodGroup.setText(rs.getString("blood_group"));
                tfSalary.setText(rs.getString("salary"));
                tfUsername.setText(rs.getString("username"));
                tfPassword.setText(rs.getString("password"));
            } else {
                JOptionPane.showMessageDialog(this, "No nurse found with the given Nurse Number.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching nurse details.");
        }
    }

    // Method to add nurse details to the database
    private void addNurseDetails() {
        String nurseNo = tfNurseNoSearch.getText();
        String name = tfName.getText();
        String gender = (String) cbGender.getSelectedItem();
        String phone = tfPhone.getText();
        String email = tfEmail.getText();
        String address = tfAddress.getText();
        String license = tfLicense.getText();
        String qualification = tfQualification.getText();
        String dept = tfDept.getText();
        String designation = tfDesignation.getText();
        String shift = tfShift.getText();
        String supervisor = tfSupervisor.getText();
        String bloodGroup = tfBloodGroup.getText();
        String salary = tfSalary.getText();
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        try {
            String query = "INSERT INTO nurses (nurse_no, name, gender, phone, email, address, license, qualification, dept, designation, shift, supervisor, blood_group, salary, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nurseNo);
            stmt.setString(2, name);
            stmt.setString(3, gender);
            stmt.setString(4, phone);
            stmt.setString(5, email);
            stmt.setString(6, address);
            stmt.setString(7, license);
            stmt.setString(8, qualification);
            stmt.setString(9, dept);
            stmt.setString(10, designation);
            stmt.setString(11, shift);
            stmt.setString(12, supervisor);
            stmt.setString(13, bloodGroup);
            stmt.setString(14, salary);
            stmt.setString(15, username);
            stmt.setString(16, password);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Nurse added successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error adding nurse.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding nurse.");
        }
    }

    // Method to update nurse details
    private void updateNurseDetails() {
        String nurseNo = nurseNoToUpdate;
        String name = tfName.getText();
        String gender = (String) cbGender.getSelectedItem();
        String phone = tfPhone.getText();
        String email = tfEmail.getText();
        String address = tfAddress.getText();
        String license = tfLicense.getText();
        String qualification = tfQualification.getText();
        String dept = tfDept.getText();
        String designation = tfDesignation.getText();
        String shift = tfShift.getText();
        String supervisor = tfSupervisor.getText();
        String bloodGroup = tfBloodGroup.getText();
        String salary = tfSalary.getText();
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        try {
            String query = "UPDATE nurses SET name = ?, gender = ?, phone = ?, email = ?, address = ?, license = ?, qualification = ?, dept = ?, designation = ?, shift = ?, supervisor = ?, blood_group = ?, salary = ?, username = ?, password = ? WHERE nurse_no = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, gender);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setString(6, license);
            stmt.setString(7, qualification);
            stmt.setString(8, dept);
            stmt.setString(9, designation);
            stmt.setString(10, shift);
            stmt.setString(11, supervisor);
            stmt.setString(12, bloodGroup);
            stmt.setString(13, salary);
            stmt.setString(14, username);
            stmt.setString(15, password);
            stmt.setString(16, nurseNo);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Nurse details updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating nurse details.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating nurse details.");
        }
    }

    // Method to delete nurse details
    private void deleteNurseDetails() {
        String adminUsername = JOptionPane.showInputDialog(this, "Enter Admin Username:");
        String adminPassword = JOptionPane.showInputDialog(this, "Enter Admin Password:");
    
        if (adminUsername == null || adminPassword == null) {
            JOptionPane.showMessageDialog(this, "Deletion cancelled.");
            return;
        }
    
        try {
            // Validate admin credentials
            String validateQuery = "SELECT * FROM admins WHERE username = ? AND password = ?";
            PreparedStatement validateStmt = conn.prepareStatement(validateQuery);
            validateStmt.setString(1, adminUsername);
            validateStmt.setString(2, adminPassword);
            ResultSet rs = validateStmt.executeQuery();
    
            if (rs.next()) {
                String nurseNo = tfNurseNoSearch.getText();
                if (nurseNo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a Nurse Number.");
                    return;
                }
    
                // Check if the nurse has any active patient assignments
                String checkAssignmentQuery = "SELECT COUNT(*) AS assignment_count FROM patient_assignments WHERE nurse_no = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkAssignmentQuery);
                checkStmt.setString(1, nurseNo);
                ResultSet checkRs = checkStmt.executeQuery();
    
                if (checkRs.next() && checkRs.getInt("assignment_count") > 0) {
                    JOptionPane.showMessageDialog(this,
                        "This nurse has active patient assignments. Please disallocate all patients from this nurse before deletion.");
                    return;
                }
    
                // Proceed with deletion
                String deleteQuery = "DELETE FROM nurses WHERE nurse_no = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setString(1, nurseNo);
                int rowsAffected = deleteStmt.executeUpdate();
    
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Nurse deleted successfully.");
                    tfNurseNoSearch.setText("");
                    // Clear any other fields if necessary
                } else {
                    JOptionPane.showMessageDialog(this, "No nurse found with the given Nurse Number.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials. Deletion not allowed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error validating admin credentials or deleting nurse details.");
        }
    }
    
    public static void main(String[] args) {
        new NurseDetails();
    }
}
