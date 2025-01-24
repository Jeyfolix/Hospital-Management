import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class PatientViewerApp extends JFrame {
    private JList<String> patientList;
    private JTable patientDetailsTable;
    private DefaultListModel<String> listModel;
    private Connection conn;

    public PatientViewerApp() {
        // Initialize database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Set up the GUI
        setTitle("Patient Viewer");
        setLayout(new BorderLayout());

        // Left side: Patient details table
        patientDetailsTable = new JTable();
        JScrollPane detailsScrollPane = new JScrollPane(patientDetailsTable);
        detailsScrollPane.setPreferredSize(new Dimension(500, 600));
        add(detailsScrollPane, BorderLayout.CENTER);

        // Right side: Patient names list
        listModel = new DefaultListModel<>();
        patientList = new JList<>(listModel);
        patientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(patientList);
        listScrollPane.setPreferredSize(new Dimension(200, 600));
        add(listScrollPane, BorderLayout.EAST);

        // Load patient names
        loadPatientNames();

        // Add list selection listener
        patientList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedPatient = patientList.getSelectedValue();
                if (selectedPatient != null) {
                    fetchAndDisplayPatientDetails(selectedPatient);
                }
            }
        });

        // Window settings
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadPatientNames() {
        try {
            String query = "SELECT name FROM persondetails";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                listModel.addElement(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient names: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchAndDisplayPatientDetails(String patientName) {
        try {
            String personalQuery = "SELECT * FROM persondetails WHERE name = ?";
            PreparedStatement stmt1 = conn.prepareStatement(personalQuery);
            stmt1.setString(1, patientName);
            ResultSet rs1 = stmt1.executeQuery();

            String medicalQuery = "SELECT * FROM medicaldetails WHERE patient_no = ?";
            PreparedStatement stmt2 = null;
            ResultSet rs2 = null;

            if (rs1.next()) {
                String patientNo = rs1.getString("patient_no");

                // Fetch medical details
                stmt2 = conn.prepareStatement(medicalQuery);
                stmt2.setString(1, patientNo);
                rs2 = stmt2.executeQuery();

                // Combine personal and medical details into a table
                Object[][] data = {
                    { "Patient Number", rs1.getString("patient_no") },
                    { "Name", rs1.getString("name") },
                    { "ID Number", rs1.getString("id_number") },
                    { "Address", rs1.getString("address") },
                    { "Age", rs1.getInt("age") },
                    { "Email", rs1.getString("email") },
                    { "Location", rs1.getString("location") },
                    { "Origin", rs1.getString("origin") },
                    { "Marital Status", rs1.getString("marital_status") },
                    { "Occupation", rs1.getString("occupation") },
                    { "Insurance Provider", rs1.getString("insurance_provider") }
                };

                if (rs2 != null && rs2.next()) {
                    Object[][] extendedData = new Object[data.length + 7][2];
                    System.arraycopy(data, 0, extendedData, 0, data.length);

                    extendedData[data.length] = new Object[] { "Chronic Condition", rs2.getString("chronic_condition") };
                    extendedData[data.length + 1] = new Object[] { "Allergies", rs2.getString("allergies") };
                    extendedData[data.length + 2] = new Object[] { "Vital Signs", rs2.getString("vital_sign") };
                    extendedData[data.length + 3] = new Object[] { "Reproductive Health", rs2.getString("reproductive") };
                    extendedData[data.length + 4] = new Object[] { "Surgical History", rs2.getString("sha") };
                    extendedData[data.length + 5] = new Object[] { "Disabilities", rs2.getString("disabilities") };
                    extendedData[data.length + 6] = new Object[] { "Therapies", rs2.getString("therapies") };

                    data = extendedData;
                }

                String[] columnNames = { "Attribute", "Value" };
                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                patientDetailsTable.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "No details found for the selected patient.", "Info", JOptionPane.INFORMATION_MESSAGE);
                patientDetailsTable.setModel(new DefaultTableModel());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching patient details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new PatientViewerApp();
    }
}
