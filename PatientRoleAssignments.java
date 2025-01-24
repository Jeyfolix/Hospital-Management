import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRoleAssignments extends JFrame implements ActionListener {
    JTextField tfPatientNumber;
    JButton search, assignRole, back;
    JTable table;
    JComboBox<String> nurseComboBox;
    Connection conn;

    PatientRoleAssignments() {
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

        assignRole = new JButton("Assign Role");
        assignRole.setBounds(120, 70, 120, 20);
        assignRole.addActionListener(this);
        add(assignRole);

        back = new JButton("Back");
        back.setBounds(260, 70, 80, 20);
        back.addActionListener(this);
        add(back);

        // Nurse selection combo box
        nurseComboBox = new JComboBox<>(getNurseNumbers());
        nurseComboBox.setBounds(400, 70, 150, 20);
        add(nurseComboBox);

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
        } else if (e.getSource() == assignRole) {
            String patientNo = tfPatientNumber.getText();
            String nurseNo = (String) nurseComboBox.getSelectedItem();
            if (!patientNo.isEmpty() && nurseNo != null) {
                assignRoleToNurse(patientNo, nurseNo);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a nurse and enter a valid Patient Number");
            }
        } else if (e.getSource() == back) {
            setVisible(false);
            // new HospitalDashboard(); // Assuming this is your main dashboard.
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
                // Preparing data for table in columnar format
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

    private void assignRoleToNurse(String patientNo, String nurseNo) {
        try {
            String query = "INSERT INTO patient_assignment (patient_no, nurse_no) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, patientNo);
            stmt.setString(2, nurseNo);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Role assigned successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error assigning role to nurse.");
        }
    }

    private String[] getNurseNumbers() {
        try {
            String query = "SELECT nurse_no FROM nurses";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            // Use a list to collect the nurse numbers
            List<String> nurseList = new ArrayList<>();
            
            while (rs.next()) {
                nurseList.add(rs.getString("nurse_no"));
            }
            
            // Convert the List to a String[] array
            return nurseList.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    private void applyColorFormatting() {
        TableCellRenderer renderer = table.getDefaultRenderer(Object.class);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.CYAN);
        header.setForeground(Color.BLACK);
        header.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));

        for (int i = 0; i < table.getRowCount(); i++) {
            table.setRowSelectionAllowed(true);
            if (i % 2 == 0) {
                table.setSelectionBackground(Color.LIGHT_GRAY);
            } else {
                table.setSelectionBackground(Color.WHITE);
            }
            table.setBackground(Color.WHITE);
        }

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 0) {
                table.getColumnModel().getColumn(i).setCellRenderer(new TableCellRenderer() {
                    public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
                        java.awt.Component cell = renderer.getTableCellRendererComponent(table, value, isSelected,
                                hasFocus, row, column);
                        cell.setBackground(Color.GREEN);
                        return cell;
                    }
                });
            } else if (i == 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(new TableCellRenderer() {
                    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        java.awt.Component cell = renderer.getTableCellRendererComponent(table, value, isSelected,
                                hasFocus, row, column);
                        cell.setBackground(Color.YELLOW);
                        return cell;
                    }
                });
            }
        }
    }

    public static void main(String[] args) {
        new PatientRoleAssignments();
    }
}

