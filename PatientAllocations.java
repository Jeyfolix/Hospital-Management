import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PatientAllocations extends JFrame implements ActionListener {
    private JTextField tfPatientNumber;
    private JButton search, allocate, back, viewAllocatedBeds, refresh, delete;
    private JTable table;
    private Connection conn;
    private DefaultTableModel tableModel;

    public PatientAllocations() {
        // Initialize connection to the database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Setup JFrame
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

        allocate = new JButton("Allocate");
        allocate.setBounds(120, 70, 100, 20);
        allocate.addActionListener(this);
        add(allocate);

        viewAllocatedBeds = new JButton("View Allocated Beds");
        viewAllocatedBeds.setBounds(240, 70, 150, 20);
        viewAllocatedBeds.addActionListener(this);
        add(viewAllocatedBeds);

        back = new JButton("Back");
        back.setBounds(410, 70, 80, 20);
        back.addActionListener(this);
        add(back);

        refresh = new JButton("Refresh");
        refresh.setBounds(500, 70, 100, 20);
        refresh.addActionListener(this);
        add(refresh);

        delete = new JButton("Delete Allocation");
        delete.setBounds(610, 70, 150, 20);
        delete.addActionListener(this);
        add(delete);

        setSize(1000, 800);
        setLocation(300, 100);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            String patientNo = tfPatientNumber.getText();
            if (!patientNo.isEmpty()) {
                fetchAndDisplayPatientDetails(patientNo);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid Patient Number");
            }
        } else if (e.getSource() == allocate) {
            String patientNo = tfPatientNumber.getText();
            if (!patientNo.isEmpty()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
                    int bedNumber = (Integer) tableModel.getValueAt(selectedRow, 2);

                    // Check if the bed is already allocated
                    if (isBedAllocated(roomNumber, bedNumber)) {
                        JOptionPane.showMessageDialog(this, "Sorry, bed already allocated to a patient!");
                    } else {
                        allocatePatientToBed(patientNo, roomNumber, bedNumber);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a room and bed to allocate.");
                }
            }
        } else if (e.getSource() == viewAllocatedBeds) {
            viewAllocatedBeds();
        } else if (e.getSource() == back) {
            setVisible(false);
            // new HospitalDashboard(); // Assuming this is your main dashboard.
        } else if (e.getSource() == refresh) {
            refreshRoomBedTable();  // Refresh the available rooms and beds
        } else if (e.getSource() == delete) {
            deletePatientAllocation();
        }
    }

    private void fetchAndDisplayPatientDetails(String patientNo) {
        try {
            // Fetch patient details
            String query = "SELECT * FROM persondetail WHERE patient_no = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, patientNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Object[][] data = new Object[19][2]; // Updated to 11 rows for all patient details

                // Personal details
                data[0][0] = "Patient No: " + rs.getString("patient_no");
                data[1][0] = "First Name: " + rs.getString("first_name");
data[2][0] = "Middle Name: " + rs.getString("middle_name");
data[3][0] = "Last Name: " + rs.getString("last_name");
data[4][0] = "Date of Birth: " + rs.getString("dob");
data[5][0] = "Gender: " + rs.getString("gender");
data[6][0] = "Address: " + rs.getString("address");
data[7][0] = "Suburb: " + rs.getString("suburb");
data[8][0] = "City: " + rs.getString("city");
data[9][0] = "Country: " + rs.getString("country");
data[10][0] = "Postal Code: " + rs.getString("postal_code");
data[11][0] = "Telephone: " + rs.getString("telephone");
data[12][0] = "Postal Address: " + rs.getString("postal_address");
data[13][0] = "Phone: " + rs.getString("phone");
data[14][0] = "Email: " + rs.getString("email");
data[15][0] = "Emergency Contact: " + rs.getString("emergency_contact");
data[16][0] = "Insurance Provider: " + rs.getString("insurance_provider");
data[17][0] = "Insurance ID: " + rs.getString("insurance_id");
data[18][0] = "Patient Type: " + rs.getString("patient_type");

                // Fetch medical details
                String queryMedical = "SELECT * FROM medicaldetail WHERE patient_no = ?";
                PreparedStatement stmtMedical = conn.prepareStatement(queryMedical);
                stmtMedical.setString(1, patientNo);
                ResultSet rs2 = stmtMedical.executeQuery();

                if (rs2.next()) {
                    // Medical details
                    data[0][1] = "Chronic Condition: " + rs2.getString("chronic_condition");
                    data[1][1] = "Allergies: " + rs2.getString("allergies");
                    data[2][1] = "Vital Signs: " + rs2.getString("vital_sign");
                    data[3][1] = "Reproductive Health: " + rs2.getString("reproductive");
                    data[4][1] = "Surgical History: " + rs2.getString("sha");
                    data[5][1] = "Disabilities: " + rs2.getString("disabilities");
                    data[6][1] = "Custom Data: " + rs2.getString("custom_data");
                    data[7][1] = "Habits: " + rs2.getString("habits");
                    data[8][1] = "Therapies: " + rs2.getString("therapies");
                }

                String[] columnNames = {"Personal Details", "Medical Details"};
                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                table.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "No patient found with the given Patient Number.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching patient details.");
        }
    }

    private void allocatePatientToBed(String patientNo, String roomNumber, int bedNumber) {
        try {
            // Insert allocation data into patient_allocation table
            String query = "INSERT INTO patient_allocations (patient_no, room_number, bed_number) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, patientNo);
            stmt.setString(2, roomNumber);
            stmt.setInt(3, bedNumber);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Patient allocated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error allocating patient.");
        }
    }

    private boolean isBedAllocated(String roomNumber, int bedNumber) {
        try {
            // Check if the bed is already allocated
            String query = "SELECT 1 FROM patient_allocations WHERE room_number = ? AND bed_number = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, roomNumber);
            stmt.setInt(2, bedNumber);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Returns true if the bed is already allocated
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking bed allocation status.");
            return false;
        }
    }

    private void viewAllocatedBeds() {
        try {
            // Fetch allocated beds
            String[] columnNames = {"Patient Number", "Room Number", "Bed Number", "Allocation Date"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            String query = "SELECT patient_no, room_number, bed_number, allocation_date FROM patient_allocations";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String patientNo = rs.getString("patient_no");
                String roomNumber = rs.getString("room_number");
                int bedNumber = rs.getInt("bed_number");
                Timestamp allocationDate = rs.getTimestamp("allocation_date");

                model.addRow(new Object[]{patientNo, roomNumber, bedNumber, allocationDate});
            }

            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching allocated beds.");
        }
    }

    private void refreshRoomBedTable() {
        // Fetch and display the rooms and beds
        try {
            String[] columnNames = {"Room Number", "Room Name", "Bed Number"};
            tableModel = new DefaultTableModel(columnNames, 0);
            table.setModel(tableModel);

            String query = "SELECT r.room_number, r.room_name, b.bed_number FROM rooms r JOIN beds b ON r.room_number = b.room_number";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String roomNumber = rs.getString("room_number");
                String roomName = rs.getString("room_name");
                int bedNumber = rs.getInt("bed_number");
                tableModel.addRow(new Object[]{roomNumber, roomName, bedNumber});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching room and bed data.");
        }
    }

    private void deletePatientAllocation() {
        String patientNo = tfPatientNumber.getText();

        if (!patientNo.isEmpty()) {
            // Prompt for admin credentials
            String username = JOptionPane.showInputDialog(this, "Enter Admin Username:");
            if (username != null && !username.isEmpty()) {
                String password = new String(JOptionPane.showInputDialog(this, "Enter Admin Password:"));
                
                // Check if the credentials are valid
                if (isValidAdminCredentials(username, password)) {
                    try {
                        // Proceed with deletion of the allocation
                        String query = "DELETE FROM patient_allocations WHERE patient_no = ?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, patientNo);
                        int rowsDeleted = stmt.executeUpdate();

                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(this, "Patient allocation deleted successfully.");
                        } else {
                            JOptionPane.showMessageDialog(this, "No allocation found for this patient.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error deleting patient allocation.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid admin credentials. Deletion not allowed.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid Patient Number.");
        }
    }

    private boolean isValidAdminCredentials(String username, String password) {
        try {
            // Assuming you have an 'admin' table that stores the username and password
            String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            // Return true if the admin credentials are correct
            return rs.next(); // If a record is found, credentials are valid
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error validating admin credentials.");
            return false;
        }
    }

    public static void main(String[] args) {
        PatientAllocations app = new PatientAllocations();
        app.refreshRoomBedTable();
    }
}

