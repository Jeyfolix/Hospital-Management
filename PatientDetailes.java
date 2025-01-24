import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.*;

public class PatientDetailes extends JFrame implements ActionListener {
    JTextField tfPatientNumber;
    JButton search, print, update, add, delete, back;
    JTable table;
    Connection conn;

    PatientDetailes() {
        // Initialize connection to the database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("Patient Number");
        heading.setBounds(20, 20, 150, 20);
        add(heading);

        tfPatientNumber = new JTextField();
        tfPatientNumber.setBounds(180, 20, 150, 20);
        add(tfPatientNumber);

        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 1000, 600);
        add(jsp);

        search = new JButton("Search");
        search.setBounds(20, 70, 80, 20);
        search.addActionListener(this);
        add(search);

        print = new JButton("Print");
        print.setBounds(120, 70, 80, 20);
        print.addActionListener(this);
        add(print);

        add = new JButton("Add");
        add.setBounds(220, 70, 80, 20);
        add.addActionListener(this);
        add(add);

        update = new JButton("Update");
        update.setBounds(320, 70, 80, 20);
        update.addActionListener(this);
        add(update);

        delete = new JButton("Delete");
        delete.setBounds(420, 70, 80, 20);
        delete.addActionListener(this);
        add(delete);

        back = new JButton("Back");
        back.setBounds(520, 70, 80, 20);
        back.addActionListener(this);
        add(back);

        setSize(1000, 800);
        setLocation(300, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            String patientNo = tfPatientNumber.getText();
            if (!patientNo.isEmpty()) {
                fetchAndDisplayPatientDetails(patientNo);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid Patient Number");
            }
        } else if (e.getSource() == print) {
            try {
                table.print();
            } catch (Exception ae) {
                ae.printStackTrace();
            }
        } else if (e.getSource() == add) {
            setVisible(false);
            new PersonalDetail();
        } else if (e.getSource() == back) {
            setVisible(false);
           // new HospitalDashboard(); // Assuming this is your main dashboard.
        } else if (e.getSource() == update) {
            // Update logic can be implemented here.
        } else if (e.getSource() == delete) {
            deletePatientDetails();
        }
    }

    // Method to delete patient details from both 'persondetails' and 'medicaldetails' tables
    private void deletePatientDetails() {
        String patientNo = tfPatientNumber.getText();
        if (patientNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Patient Number to delete.");
            return;
        }

        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the details for Patient No: " + patientNo + "?", 
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
                        // Start a transaction
                        conn.setAutoCommit(false); // Disable auto-commit to start a transaction

                        // Delete from medicaldetails table first
                        String medicalQuery = "DELETE FROM medicaldetails WHERE patient_no = ?";
                        PreparedStatement stmt1 = conn.prepareStatement(medicalQuery);
                        stmt1.setString(1, patientNo);
                        stmt1.executeUpdate();

                        // Then delete from persondetails table
                        String personalQuery = "DELETE FROM persondetails WHERE patient_no = ?";
                        PreparedStatement stmt2 = conn.prepareStatement(personalQuery);
                        stmt2.setString(1, patientNo);
                        stmt2.executeUpdate();

                        // Commit the transaction if both deletions are successful
                        conn.commit();
                        JOptionPane.showMessageDialog(this, "Patient details deleted successfully.");
                        table.setModel(new DefaultTableModel()); // Clear the table after deletion
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        try {
                            conn.rollback(); // Rollback if an error occurs
                        } catch (SQLException rollbackEx) {
                            rollbackEx.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(this, "Error deleting patient details.");
                    } finally {
                        try {
                            conn.setAutoCommit(true); // Re-enable auto-commit after transaction
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Authentication failed. Deletion cancelled.");
                }
            }
        }
    }

    // Method to authenticate admin username and password
    private boolean authenticateAdmin(String username, String password) {
        try {
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

    // Method to fetch and display the patient details from the database
    private void fetchAndDisplayPatientDetails(String patientNo) {
        try {
            // Fetching personal details
            String personalQuery = "SELECT * FROM persondetail WHERE patient_no = ?";
            PreparedStatement stmt1 = conn.prepareStatement(personalQuery);
            stmt1.setString(1, patientNo);
            ResultSet rs1 = stmt1.executeQuery();

            // Fetching medical details
            String medicalQuery = "SELECT * FROM medicaldetail WHERE patient_no = ?";
            PreparedStatement stmt2 = conn.prepareStatement(medicalQuery);
            stmt2.setString(1, patientNo);
            ResultSet rs2 = stmt2.executeQuery();

            if (rs1.next() && rs2.next()) {
                // Preparing data for table in columnar format (separating Name and Other Details)
                Object[][] data = new Object[19][2]; // 11 rows for 11 different attributes, 2 columns: one for Personal, one for Medical

                // Personal Details in Column 1
                data[0][0] = "Patient No: " + rs1.getString("patient_no");
                data[1][0] = "First Name: " + rs1.getString("first_name");
data[2][0] = "Middle Name: " + rs1.getString("middle_name");
data[3][0] = "Last Name: " + rs1.getString("last_name");
data[4][0] = "Date of Birth: " + rs1.getString("dob");
data[5][0] = "Gender: " + rs1.getString("gender");
data[6][0] = "Address: " + rs1.getString("address");
data[7][0] = "Suburb: " + rs1.getString("suburb");
data[8][0] = "City: " + rs1.getString("city");
data[9][0] = "Country: " + rs1.getString("country");
data[10][0] = "Postal Code: " + rs1.getString("postal_code");
data[11][0] = "Telephone: " + rs1.getString("telephone");
data[12][0] = "Postal Address: " + rs1.getString("postal_address");
data[13][0] = "Phone: " + rs1.getString("phone");
data[14][0] = "Email: " + rs1.getString("email");
data[15][0] = "Emergency Contact: " + rs1.getString("emergency_contact");
data[16][0] = "Insurance Provider: " + rs1.getString("insurance_provider");
data[17][0] = "Insurance ID: " + rs1.getString("insurance_id");
data[18][0] = "Patient Type: " + rs1.getString("patient_type");

                // Medical Details in Column 2
                data[0][1] = "Chronic Condition: " + rs2.getString("chronic_condition");
                data[1][1] = "Allergies: " + rs2.getString("allergies");
                data[2][1] = "Vital Signs: " + rs2.getString("vital_sign");
                data[3][1] = "Reproductive Health: " + rs2.getString("reproductive");
                data[4][1] = "Surgical History: " + rs2.getString("sha");
                data[5][1] = "Disabilities: " + rs2.getString("disabilities");
                data[6][1] = "Custom Data: " + rs2.getString("custom_data");
                data[7][1] = "Habits: " + rs2.getString("habits");
                data[8][1] = "Therapies: " + rs2.getString("therapies");

                // Column names for the table
                String[] columnNames = { "Personal Details", "Medical Details" };

                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                table.setModel(model);

                // Apply custom colors for better distinction
                applyColorFormatting();
            } else {
                JOptionPane.showMessageDialog(this, "No patient found with the given Patient Number.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching patient details.");
        }
    }

    // Method to apply color formatting to the table
    private void applyColorFormatting() {
        TableCellRenderer renderer = table.getDefaultRenderer(Object.class);

        // Change header background color
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.CYAN);
        header.setForeground(Color.BLACK);
        header.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));

        // Change background color for Personal Details column
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setRowSelectionAllowed(true);
            if (i % 2 == 0) {
                table.setSelectionBackground(Color.LIGHT_GRAY); // Alternate row color
            } else {
                table.setSelectionBackground(Color.WHITE);
            }
            table.setBackground(Color.WHITE);
        }

        // Apply color to the rows (Personal and Medical) for better distinction
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 0) {
                table.getColumnModel().getColumn(i).setCellRenderer(new TableCellRenderer() {
                    public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
                        java.awt.Component cell = renderer.getTableCellRendererComponent(table, value, isSelected,
                                hasFocus, row, column);
                        cell.setBackground(Color.GREEN); // Personal Details with a light green background
                        return cell;
                    }
                });
            } else if (i == 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(new TableCellRenderer() {
                    public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
                        java.awt.Component cell = renderer.getTableCellRendererComponent(table, value, isSelected,
                                hasFocus, row, column);
                        cell.setBackground(Color.YELLOW); // Medical Details with a light yellow background
                        return cell;
                    }
                });
            }
        }
    }

    public static void main(String[] args) {
        new PatientDetailes();
    }
}
