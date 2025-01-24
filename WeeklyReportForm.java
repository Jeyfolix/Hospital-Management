import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class WeeklyReportForm extends JFrame implements ActionListener {
    // Form components
    JTextField tfNurseNo, tfPatientsAllocated, tfMedication, tfPatientsReleased, tfPatientsLost, tfShift, tfToolsUsed, tfResourcesUsed;
    JButton submitReport, backButton, verifyNurseNoButton;
    JLabel lblErrorMessage, lblNurseName, lblDate, lblNurseNoLabel;
    Connection conn;
    String nurseNo;
    
    WeeklyReportForm() {
        // Initialize the database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setSize(400, 600);  // Set fixed window size
        setResizable(false); // Disable resizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Add close button
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Nurse Number Input
        JLabel nurseNoLabel = new JLabel("Nurse Number:");
        nurseNoLabel.setBounds(20, 60, 150, 30);
        nurseNoLabel.setFont(new Font("serif", Font.BOLD, 15));
        add(nurseNoLabel);

        tfNurseNo = new JTextField();
        tfNurseNo.setBounds(180, 60, 150, 30);
        add(tfNurseNo);

        verifyNurseNoButton = new JButton("Verify Nurse Number");
        verifyNurseNoButton.setBounds(180, 100, 200, 30);
        verifyNurseNoButton.addActionListener(this);
        add(verifyNurseNoButton);

        lblNurseName = new JLabel("Nurse Name: ");
        lblNurseName.setBounds(20, 140, 350, 30);
        lblNurseName.setFont(new Font("serif", Font.BOLD, 15));
        add(lblNurseName);

        // Weekly Report Fields
        JLabel lblPatientsAllocated = new JLabel("Patients Allocated:");
        lblPatientsAllocated.setBounds(20, 180, 150, 30);
        lblPatientsAllocated.setFont(new Font("serif", Font.BOLD, 15));
        add(lblPatientsAllocated);

        tfPatientsAllocated = new JTextField();
        tfPatientsAllocated.setBounds(180, 180, 150, 30);
        tfPatientsAllocated.setEditable(false);
        add(tfPatientsAllocated);

        JLabel lblMedication = new JLabel("Medication Given:");
        lblMedication.setBounds(20, 220, 150, 30);
        lblMedication.setFont(new Font("serif", Font.BOLD, 15));
        add(lblMedication);

        tfMedication = new JTextField();
        tfMedication.setBounds(180, 220, 150, 30);
        tfMedication.setEditable(false);
        add(tfMedication);

        JLabel lblPatientsReleased = new JLabel("Patients Released:");
        lblPatientsReleased.setBounds(20, 260, 150, 30);
        lblPatientsReleased.setFont(new Font("serif", Font.BOLD, 15));
        add(lblPatientsReleased);

        tfPatientsReleased = new JTextField();
        tfPatientsReleased.setBounds(180, 260, 150, 30);
        tfPatientsReleased.setEditable(false);
        add(tfPatientsReleased);

        JLabel lblPatientsLost = new JLabel("Patients Lost:");
        lblPatientsLost.setBounds(20, 300, 150, 30);
        lblPatientsLost.setFont(new Font("serif", Font.BOLD, 15));
        add(lblPatientsLost);

        tfPatientsLost = new JTextField();
        tfPatientsLost.setBounds(180, 300, 150, 30);
        tfPatientsLost.setEditable(false);
        add(tfPatientsLost);

        JLabel lblShift = new JLabel("Shift (Day/Night):");
        lblShift.setBounds(20, 340, 150, 30);
        lblShift.setFont(new Font("serif", Font.BOLD, 15));
        add(lblShift);

        tfShift = new JTextField();
        tfShift.setBounds(180, 340, 150, 30);
        tfShift.setEditable(false);
        add(tfShift);

        JLabel lblToolsUsed = new JLabel("Hospital Tools Used:");
        lblToolsUsed.setBounds(20, 380, 150, 30);
        lblToolsUsed.setFont(new Font("serif", Font.BOLD, 15));
        add(lblToolsUsed);

        tfToolsUsed = new JTextField();
        tfToolsUsed.setBounds(180, 380, 150, 30);
        tfToolsUsed.setEditable(false);
        add(tfToolsUsed);

        JLabel lblResourcesUsed = new JLabel("Hospital Resources Used:");
        lblResourcesUsed.setBounds(20, 420, 150, 30);
        lblResourcesUsed.setFont(new Font("serif", Font.BOLD, 15));
        add(lblResourcesUsed);

        tfResourcesUsed = new JTextField();
        tfResourcesUsed.setBounds(180, 420, 150, 30);
        tfResourcesUsed.setEditable(false);
        add(tfResourcesUsed);

        // Error Message Label
        lblErrorMessage = new JLabel("");
        lblErrorMessage.setBounds(20, 460, 500, 30);
        lblErrorMessage.setFont(new Font("serif", Font.BOLD, 14));
        lblErrorMessage.setForeground(Color.RED);
        add(lblErrorMessage);

        // Submit Button
        submitReport = new JButton("Submit Report");
        submitReport.setBounds(20, 500, 150, 30);
        submitReport.addActionListener(this);
        submitReport.setEnabled(false);  // Disabled initially
        add(submitReport);

        // Back Button
        backButton = new JButton("Back");
        backButton.setBounds(180, 500, 150, 30);
        backButton.addActionListener(this);
        add(backButton);

        setVisible(true);
    }

    @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == verifyNurseNoButton) {
        // Verify Nurse Number
        String nurseNumber = tfNurseNo.getText();
        if (nurseNumber.isEmpty()) {
            lblErrorMessage.setText("Please enter a valid nurse number.");
            return;
        }

        // Check the nurse number in the database
        try {
            String query = "SELECT name FROM nurses WHERE nurse_no = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nurseNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Display the nurse's name
                lblNurseName.setText("Nurse Name: " + rs.getString("name"));
                nurseNo = nurseNumber;  // Set the global nurseNo to the valid nurse number
                enableReportFields(true);  // Enable the report fields
                lblErrorMessage.setText("");  // Clear any error messages
            } else {
                lblErrorMessage.setText("Invalid Nurse Number.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblErrorMessage.setText("Error verifying nurse number.");
        }
    }

    if (e.getSource() == submitReport) {
        // Validate that it's Saturday
        String dayOfWeek = new java.text.SimpleDateFormat("EEEE").format(new java.util.Date());
        if (!dayOfWeek.equalsIgnoreCase("Saturday")) {
            lblErrorMessage.setText("Reports can only be filled on Saturdays.");
            return;
        }

        // Check if the report has already been submitted this week
        try {
            String checkQuery = "SELECT COUNT(*) AS report_count FROM weekly_reports WHERE nurse_no = ? AND report_date >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, nurseNo);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("report_count") > 0) {
                lblErrorMessage.setText("You have already submitted a report this week.");
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblErrorMessage.setText("Database error while checking report submission.");
            return;
        }

        // Collect report details and submit the report
        String patientsAllocated = tfPatientsAllocated.getText();
        String medication = tfMedication.getText();
        String patientsReleased = tfPatientsReleased.getText();
        String patientsLost = tfPatientsLost.getText();
        String shift = tfShift.getText();
        String toolsUsed = tfToolsUsed.getText();
        String resourcesUsed = tfResourcesUsed.getText();

        // Insert the report into the database
        try {
            String query = "INSERT INTO weekly_reports (nurse_no, patients_allocated, medication, patients_released, patients_lost, shift, tools_used, resources_used, report_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nurseNo);
            stmt.setString(2, patientsAllocated);
            stmt.setString(3, medication);
            stmt.setString(4, patientsReleased);
            stmt.setString(5, patientsLost);
            stmt.setString(6, shift);
            stmt.setString(7, toolsUsed);
            stmt.setString(8, resourcesUsed);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Weekly report submitted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error submitting weekly report.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.");
        }
    }

    if (e.getSource() == backButton) {
        // Navigate back to the previous window (e.g., nurse dashboard)
        this.dispose();  // Close the current window
        new NurseManagement();  // Open the nurse dashboard
    }
}


    private void enableReportFields(boolean enabled) {
        tfPatientsAllocated.setEditable(enabled);
        tfMedication.setEditable(enabled);
        tfPatientsReleased.setEditable(enabled);
        tfPatientsLost.setEditable(enabled);
        tfShift.setEditable(enabled);
        tfToolsUsed.setEditable(enabled);
        tfResourcesUsed.setEditable(enabled);
        submitReport.setEnabled(enabled);
    }

    public static void main(String[] args) {
        // Create the form
        new WeeklyReportForm();
    }
}
