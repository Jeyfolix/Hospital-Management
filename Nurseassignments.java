import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.*;

public class Nurseassignments extends JFrame {
    JTable table;
    JTextField tfPatientNumber;
    JButton loadButton, searchButton, deleteButton;
    Connection conn;

    Nurseassignments() {
        // Initialize connection to the database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Table to display the patient-nurse assignments
        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(50, 150, 900, 500);
        add(jsp);

        // Load Assignments Button
        loadButton = new JButton("Load Assignments");
        loadButton.setBounds(50, 50, 150, 30);
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAssignments();
            }
        });
        add(loadButton);

        // Patient Number Search Label and Text Field
        JLabel patientLabel = new JLabel("Enter Patient Number:");
        patientLabel.setBounds(250, 50, 150, 30);
        add(patientLabel);

        tfPatientNumber = new JTextField();
        tfPatientNumber.setBounds(400, 50, 150, 30);
        add(tfPatientNumber);

        // Search Button
        searchButton = new JButton("Search Patient");
        searchButton.setBounds(570, 50, 150, 30);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchPatient();
            }
        });
        add(searchButton);

        // Delete Button
        deleteButton = new JButton("Delete Allocation");
        deleteButton.setBounds(750, 50, 150, 30);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteAllocation();
            }
        });
        add(deleteButton);

        setSize(1000, 700);
        setLocation(300, 100);
        setVisible(true);
    }

    // Method to load the patient-nurse assignments from the database
    private void loadAssignments() {
        try {
            String query = "SELECT p.patient_no, p.first_name, p.last_name, n.nurse_no, n.name AS nurse_name " +
                           "FROM persondetail p " +
                           "JOIN patient_assignment pa ON p.patient_no = pa.patient_no " +
                           "JOIN nurses n ON pa.nurse_no = n.nurse_no";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
    
            Object[][] data = new Object[50][5]; // Update to 5 columns
            int rowIndex = 0;
            while (rs.next()) {
                data[rowIndex][0] = rs.getString("patient_no");
                data[rowIndex][1] = rs.getString("first_name"); // Patient first name
                data[rowIndex][2] = rs.getString("last_name");  // Patient last name
                data[rowIndex][3] = rs.getString("nurse_no");
                data[rowIndex][4] = rs.getString("nurse_name"); // Nurse name
                rowIndex++;
            }
    
            String[] columnNames = {"Patient No", "First Name", "Last Name", "Nurse No", "Nurse Name"};
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            table.setModel(model);
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient-nurse assignments.");
        }
    }
    

    // Method to search and display a specific patient's details based on patient number
    private void searchPatient() {
        String patientNo = tfPatientNumber.getText().trim();
    
        if (!patientNo.isEmpty()) {
            try {
                // Query to fetch all personal and medical details for the patient
                String query = "SELECT p.patient_no, p.first_name, p.middle_name, p.last_name, p.dob, p.gender, p.address, \n" + //
                                        "       p.suburb, p.city, p.country, p.postal_code, p.telephone, p.postal_address, p.phone, \n" + //
                                        "       p.email, p.emergency_contact, p.insurance_provider, p.insurance_id, p.patient_type, " +
                               "m.chronic_condition, m.allergies, m.vital_sign, m.reproductive, m.sha, " +
                               "m.disabilities, m.custom_data, m.habits, m.therapies " +
                               "FROM persondetail p " +
                               "JOIN medicaldetail m ON p.patient_no = m.patient_no " +
                               "WHERE p.patient_no = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, patientNo);
                ResultSet rs = stmt.executeQuery();
    
                if (rs.next()) {
                    // Create a table model to display the patient details
                    Object[][] data = new Object[28][2]; // 20 rows for attributes and their values
    
                    // Populate personal details
                    data[0][0] = "Patient No"; data[0][1] = rs.getString("patient_no");
data[1][0] = "First Name"; data[1][1] = rs.getString("first_name");
data[2][0] = "Middle Name"; data[2][1] = rs.getString("middle_name");
data[3][0] = "Last Name"; data[3][1] = rs.getString("last_name");
data[4][0] = "Date of Birth"; data[4][1] = rs.getString("dob");
data[5][0] = "Gender"; data[5][1] = rs.getString("gender");
data[6][0] = "Address"; data[6][1] = rs.getString("address");
data[7][0] = "Suburb"; data[7][1] = rs.getString("suburb");
data[8][0] = "City"; data[8][1] = rs.getString("city");
data[9][0] = "Country"; data[9][1] = rs.getString("country");
data[10][0] = "Postal Code"; data[10][1] = rs.getString("postal_code");
data[11][0] = "Telephone"; data[11][1] = rs.getString("telephone");
data[12][0] = "Postal Address"; data[12][1] = rs.getString("postal_address");
data[13][0] = "Phone"; data[13][1] = rs.getString("phone");
data[14][0] = "Email"; data[14][1] = rs.getString("email");
data[15][0] = "Emergency Contact"; data[15][1] = rs.getString("emergency_contact");
data[16][0] = "Insurance Provider"; data[16][1] = rs.getString("insurance_provider");
data[17][0] = "Insurance ID"; data[17][1] = rs.getString("insurance_id");
data[18][0] = "Patient Type"; data[18][1] = rs.getString("patient_type");

                    // Populate medical details
                    data[19][0] = "Chronic Condition"; data[19][1] = rs.getString("chronic_condition");
                    data[20][0] = "Allergies"; data[20][1] = rs.getString("allergies");
                    data[21][0] = "Vital Signs"; data[21][1] = rs.getString("vital_sign");
                    data[22][0] = "Reproductive Health"; data[22][1] = rs.getString("reproductive");
                    data[23][0] = "Surgical History"; data[23][1] = rs.getString("sha");
                    data[24][0] = "Disabilities"; data[24][1] = rs.getString("disabilities");
                    data[25][0] = "Custom Data"; data[25][1] = rs.getString("custom_data");
                    data[26][0] = "Habits"; data[26][1] = rs.getString("habits");
                    data[27][0] = "Therapies"; data[27][1] = rs.getString("therapies");
    
                    // Column names for the table
                    String[] columnNames = {"Attribute", "Details"};
    
                    // Set the data and column names to the table model
                    DefaultTableModel model = new DefaultTableModel(data, columnNames);
                    table.setModel(model);
                    table.setRowHeight(30); // Adjust row height for better readability
                } else {
                    JOptionPane.showMessageDialog(this, "Patient not found.");
                }
    
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error searching for the patient.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a patient number.");
        }
    }
    

    // Method to delete a patient-nurse allocation
    // Method to delete a patient-nurse allocation
private void deleteAllocation() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow != -1) {
        String patientNo = table.getValueAt(selectedRow, 0).toString();
        String nurseNo = table.getValueAt(selectedRow, 2).toString();

        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the allocation for Patient No: " + patientNo + " and Nurse No: " + nurseNo + "?", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Prompt for admin username and password
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();

            Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
            };

            int authResult = JOptionPane.showConfirmDialog(this, message, "Admin Authentication", JOptionPane.OK_CANCEL_OPTION);

            if (authResult == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateAdmin(username, password)) {
                    try {
                        String query = "DELETE FROM patient_assignment WHERE patient_no = ? AND nurse_no = ?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, patientNo);
                        stmt.setString(2, nurseNo);

                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Allocation deleted successfully.");
                            loadAssignments();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error: Allocation not found or already deleted.");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error deleting allocation.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Authentication failed. Deletion cancelled.");
                }
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select a row to delete.");
    }
}

// Method to authenticate admin username and password
private boolean authenticateAdmin(String username, String password) {
    try {
        // Correct table name to 'admins'
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();
        return rs.next(); // Return true if a match is found
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error during authentication.");
        return false;
    }
}

    public static void main(String[] args) {
        new Nurseassignments();
    }
}

