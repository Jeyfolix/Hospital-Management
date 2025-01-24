import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DoctorAllocation extends JFrame implements ActionListener {
    private JTextField tfDoctorNumber, tfPatientNumber, tfDisease, tfTimeRequired;
    private JComboBox<String> cbCareType;
    private JButton btnAllocate;
    private JLabel lblResult;
    private Connection conn;

    public DoctorAllocation() {
        setTitle("Allocate Patient to Doctor");
        setSize(600, 500);
        setLocation(300, 150);
        setLayout(null);
        setResizable(false);

        JLabel lblHeading = new JLabel("Allocate Patient to Doctor");
        lblHeading.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblHeading.setBounds(150, 20, 300, 30);
        add(lblHeading);

        JLabel lblPatientNumber = new JLabel("Patient Number:");
        lblPatientNumber.setBounds(50, 80, 150, 30);
        add(lblPatientNumber);

        tfPatientNumber = new JTextField();
        tfPatientNumber.setBounds(200, 80, 300, 30);
        add(tfPatientNumber);

        JLabel lblDoctorNumber = new JLabel("Doctor Number:");
        lblDoctorNumber.setBounds(50, 130, 150, 30);
        add(lblDoctorNumber);

        tfDoctorNumber = new JTextField();
        tfDoctorNumber.setBounds(200, 130, 300, 30);
        add(tfDoctorNumber);

        JLabel lblDisease = new JLabel("Disease:");
        lblDisease.setBounds(50, 180, 150, 30);
        add(lblDisease);

        tfDisease = new JTextField();
        tfDisease.setBounds(200, 180, 300, 30);
        add(tfDisease);

        JLabel lblCareType = new JLabel("Care Type:");
        lblCareType.setBounds(50, 230, 150, 30);
        add(lblCareType);

        cbCareType = new JComboBox<>(new String[]{"Surgery", "Operation", "Treatment"});
        cbCareType.setBounds(200, 230, 300, 30);
        add(cbCareType);

        JLabel lblTimeRequired = new JLabel("Time Required:");
        lblTimeRequired.setBounds(50, 280, 150, 30);
        add(lblTimeRequired);

        tfTimeRequired = new JTextField();
        tfTimeRequired.setBounds(200, 280, 300, 30);
        add(tfTimeRequired);

        btnAllocate = new JButton("Allocate");
        btnAllocate.setBounds(200, 350, 150, 40);
        btnAllocate.addActionListener(this);
        add(btnAllocate);

        lblResult = new JLabel("");
        lblResult.setBounds(50, 400, 500, 30);
        lblResult.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lblResult);

        // Initialize database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hospital", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAllocate) {
            String patientNumber = tfPatientNumber.getText();
            String doctorNumber = tfDoctorNumber.getText();
            String disease = tfDisease.getText();
            String careType = (String) cbCareType.getSelectedItem();
            String timeRequired = tfTimeRequired.getText();

            if (patientNumber.isEmpty() || doctorNumber.isEmpty() || disease.isEmpty() || timeRequired.isEmpty()) {
                lblResult.setText("Please fill all fields.");
                lblResult.setForeground(Color.RED);
                return;
            }

            try {
                // Fetch patient details
                String patientQuery = "SELECT name, patient_number, disease FROM patients WHERE patient_number = ?";
                PreparedStatement patientStmt = conn.prepareStatement(patientQuery);
                patientStmt.setString(1, patientNumber);
                ResultSet patientRs = patientStmt.executeQuery();

                if (!patientRs.next()) {
                    lblResult.setText("Invalid Patient Number.");
                    lblResult.setForeground(Color.RED);
                    return;
                }

                String patientName = patientRs.getString("name");
                String patientDisease = patientRs.getString("disease");

                // Fetch doctor details
                String doctorQuery = "SELECT name, doctor_number, specialization FROM doctors WHERE doctor_number = ?";
                PreparedStatement doctorStmt = conn.prepareStatement(doctorQuery);
                doctorStmt.setString(1, doctorNumber);
                ResultSet doctorRs = doctorStmt.executeQuery();

                if (!doctorRs.next()) {
                    lblResult.setText("Invalid Doctor Number.");
                    lblResult.setForeground(Color.RED);
                    return;
                }

                String doctorName = doctorRs.getString("name");
                String doctorSpecialization = doctorRs.getString("specialization");

                // Allocate patient
                String insertQuery = "INSERT INTO allocations (doctor_number, patient_number, disease, care_type, time_required) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, doctorNumber);
                insertStmt.setString(2, patientNumber);
                insertStmt.setString(3, disease);
                insertStmt.setString(4, careType);
                insertStmt.setString(5, timeRequired);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    lblResult.setText("Patient allocated successfully!\n" +
                            "Patient: " + patientName + " (" + patientNumber + ")\n" +
                            "Disease: " + patientDisease + "\n" +
                            "Doctor: " + doctorName + " (" + doctorNumber + ")\n" +
                            "Specialization: " + doctorSpecialization + "\n" +
                            "Time Required: " + timeRequired);
                    lblResult.setForeground(Color.GREEN);
                } else {
                    lblResult.setText("Failed to allocate patient.");
                    lblResult.setForeground(Color.RED);
                }

            } catch (SQLException ex) {
                lblResult.setText("Database error: " + ex.getMessage());
                lblResult.setForeground(Color.RED);
            }
        }
    }

    public static void main(String[] args) {
        new DoctorAllocation();
    }
}

