import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class DoctorDetails extends JFrame implements ActionListener {
    JTextField tfDoctorNo, tfName, tfDob, tfContact, tfAddress, tfEmergencyContact,
               tfExperience, tfLicense, tfAffiliation, tfDegree, tfPostgraduate,
               tfFellowships, tfCME, tfPosition, tfDepartment, tfBankAccount,
               tfTaxId, tfInsurance, tfHealthHistory, tfAccountNumber;
    JLabel currentDateLabel;
    JButton submit, clear;
    JComboBox<String> cbGender, cbSpecialization, cbBankName, cbDegree, cbPostgraduate;
    Random ra = new Random();
    Connection conn;

    public DoctorDetails() {
        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle("Doctor Details Entry");
        setSize(1000, 800);
        setLocation(200, 50);
        setLayout(null);
        setResizable(false);  // Disable maximize button

        // Submit button positioned closer to health history and insurance
        submit = new JButton("Submit");
        submit.setBounds(400, 600, 100, 35);  // Reduced size and positioned closer
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Tahoma", Font.BOLD, 14));
        submit.addActionListener(this);
        add(submit);

        // Clear button positioned closer to health history and insurance
        clear = new JButton("Clear");
        clear.setBounds(520, 600, 100, 35);  // Reduced size and positioned closer
        clear.setBackground(Color.RED);
        clear.setForeground(Color.WHITE);
        clear.setFont(new Font("Tahoma", Font.BOLD, 14));
        clear.addActionListener(this);
        add(clear);

        JLabel heading = new JLabel("Doctor Details");
        heading.setBounds(400, 20, 400, 50);
        heading.setFont(new Font("serif", Font.BOLD, 24));
        add(heading);

        // Current Date
        LocalDate currentDate = LocalDate.now();
        JLabel currentDateLabelHeader = new JLabel("Current Date:");
        currentDateLabelHeader.setBounds(50, 80, 200, 30);
        currentDateLabelHeader.setFont(new Font("serif", Font.BOLD, 16));
        add(currentDateLabelHeader);

        currentDateLabel = new JLabel(currentDate.toString());
        currentDateLabel.setBounds(250, 80, 200, 30);
        currentDateLabel.setFont(new Font("serif", Font.PLAIN, 14));
        add(currentDateLabel);

        // Doctor Number (auto-generated)
        JLabel doctorNoLabel = new JLabel("Doctor No:");
        doctorNoLabel.setBounds(50, 120, 200, 30);
        doctorNoLabel.setFont(new Font("serif", Font.BOLD, 16));
        add(doctorNoLabel);

        tfDoctorNo = new JTextField("D" + generateDoctorNumber());
        tfDoctorNo.setBounds(250, 120, 180, 30);
        tfDoctorNo.setEditable(false);
        add(tfDoctorNo);

        // Fields arranged in two columns side by side
        int leftX = 50, rightX = 400, yStart = 170, yStep = 40;

        addField("Name", leftX, yStart);
        tfName = addTextField(leftX + 150, yStart);

        addField("Date of Birth", rightX, yStart);
        tfDob = addTextField(rightX + 150, yStart);

        addField("Gender", leftX, yStart += yStep);
        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cbGender.setBounds(leftX + 150, yStart, 180, 30);
        add(cbGender);

        addField("Contact", rightX, yStart);
        tfContact = addTextField(rightX + 150, yStart);

        addField("Emergency Contact", leftX, yStart += yStep);
        tfEmergencyContact = addTextField(leftX + 150, yStart);

        addField("Address", rightX, yStart);
        tfAddress = addTextField(rightX + 150, yStart);

        addField("Specialization", leftX, yStart += yStep);
        cbSpecialization = new JComboBox<>(new String[]{"Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Surgery", "OB/GYN", "Internal Medicine", "Psychiatry", "Anesthesiology", "Radiology", "Pathology", "Oncology", "Darmatology"});
        cbSpecialization.setBounds(leftX + 150, yStart, 180, 30);
        add(cbSpecialization);

        addField("Experience", rightX, yStart);
        tfExperience = addTextField(rightX + 150, yStart);

        addField("Medical License", leftX, yStart += yStep);
        tfLicense = addTextField(leftX + 150, yStart);

        addField("Affiliations", rightX, yStart);
        tfAffiliation = addTextField(rightX + 150, yStart);

        addField("Medical Degree", leftX, yStart += yStep);
        cbDegree = new JComboBox<>(new String[]{"MBBS", "BDS", "BAMS", "BHMS", "BVSc"});
        cbDegree.setBounds(leftX + 150, yStart, 180, 30);
        add(cbDegree);

        addField("Postgraduate Degree", rightX, yStart);
        cbPostgraduate = new JComboBox<>(new String[]{"MD", "MS", "DNB", "MCh", "DM", "MPH", "MHA", "MBA in Healthcare"});
        cbPostgraduate.setBounds(rightX + 150, yStart, 180, 30);
        add(cbPostgraduate);

        addField("Fellowships", leftX, yStart += yStep);
        tfFellowships = addTextField(leftX + 150, yStart);

        addField("CME", rightX, yStart);
        tfCME = addTextField(rightX + 150, yStart);

        addField("Position", leftX, yStart += yStep);
        tfPosition = addTextField(leftX + 150, yStart);

        addField("Department", rightX, yStart);
        tfDepartment = addTextField(rightX + 150, yStart);

        addField("Bank Name", leftX, yStart += yStep);
        cbBankName = new JComboBox<>(new String[]{"Select Bank", "Central Bank", "Commercial Bank", "Equity Bank", "Cooperative Bank", "Development Bank"});
        cbBankName.setBounds(leftX + 150, yStart, 180, 30);
        add(cbBankName);

        addField("Account Number", rightX, yStart);
        tfAccountNumber = addTextField(rightX + 150, yStart);

        addField("Tax ID", leftX, yStart += yStep);
        tfTaxId = addTextField(leftX + 150, yStart);

        addField("Insurance", rightX, yStart);
        tfInsurance = addTextField(rightX + 150, yStart);

        addField("Health History", leftX, yStart += yStep);
        tfHealthHistory = addTextField(leftX + 150, yStart);

        setVisible(true);
    }

    private void addField(String labelText, int x, int y) {
        JLabel label = new JLabel(labelText + ":");
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("serif", Font.BOLD, 16));
        add(label);
    }

    private JTextField addTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 180, 30);
        add(field);
        return field;
    }

    private String generateDoctorNumber() {
        long first4 = Math.abs(ra.nextLong() % 9000L) + 1000L;
        char prefix = (char) (ra.nextInt(26) + 'A');
        return prefix + String.valueOf(first4);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String details =
                "Doctor Number: " + tfDoctorNo.getText() + "\n" +
                "Current Date: " + currentDateLabel.getText() + "\n" +
                "Name: " + tfName.getText() + "\n" +
                "Date of Birth: " + tfDob.getText() + "\n" +
                "Gender: " + cbGender.getSelectedItem() + "\n" +
                "Contact: " + tfContact.getText() + "\n" +
                "Address: " + tfAddress.getText() + "\n" +
                "Emergency Contact: " + tfEmergencyContact.getText() + "\n" +
                "Specialization: " + cbSpecialization.getSelectedItem() + "\n" +
                "Experience: " + tfExperience.getText() + "\n" +
                "Medical License: " + tfLicense.getText() + "\n" +
                "Affiliations: " + tfAffiliation.getText() + "\n" +
                "Medical Degree: " + cbDegree.getSelectedItem() + "\n" +
                "Postgraduate Degree: " + cbPostgraduate.getSelectedItem() + "\n" +
                "Fellowships: " + tfFellowships.getText() + "\n" +
                "CME: " + tfCME.getText() + "\n" +
                "Position: " + tfPosition.getText() + "\n" +
                "Department: " + tfDepartment.getText() + "\n" +
                "Bank Name: " + cbBankName.getSelectedItem() + "\n" +
                "Account Number: " + tfAccountNumber.getText() + "\n" +
                "Tax ID: " + tfTaxId.getText() + "\n" +
                "Insurance: " + tfInsurance.getText() + "\n" +
                "Health History: " + tfHealthHistory.getText();

            // Insert into database
            String query = "INSERT INTO doctors (doctor_number, curreDate, name, dob, gender, contact, address, emergency_contact, specialization, experience, medical_license, affiliations, degree, postgraduate_degree, fellowships, cme, position, department, bank_name, account_number, tax_id, insurance, health_history) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, tfDoctorNo.getText());
                stmt.setString(2, currentDateLabel.getText());
                stmt.setString(3, tfName.getText());
                stmt.setString(4, tfDob.getText());
                stmt.setString(5, (String) cbGender.getSelectedItem());
                stmt.setString(6, tfContact.getText());
                stmt.setString(7, tfAddress.getText());
                stmt.setString(8, tfEmergencyContact.getText());
                stmt.setString(9, (String) cbSpecialization.getSelectedItem());
                stmt.setString(10, tfExperience.getText());
                stmt.setString(11, tfLicense.getText());
                stmt.setString(12, tfAffiliation.getText());
                stmt.setString(13, (String) cbDegree.getSelectedItem());
                stmt.setString(14, (String) cbPostgraduate.getSelectedItem());
                stmt.setString(15, tfFellowships.getText());
                stmt.setString(16, tfCME.getText());
                stmt.setString(17, tfPosition.getText());
                stmt.setString(18, tfDepartment.getText());
                stmt.setString(19, (String) cbBankName.getSelectedItem());
                stmt.setString(20, tfAccountNumber.getText());
                stmt.setString(21, tfTaxId.getText());
                stmt.setString(22, tfInsurance.getText());
                stmt.setString(23, tfHealthHistory.getText());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Doctor details saved successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save doctor details.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            }
        } else if (ae.getSource() == clear) {
            // Clear all fields for new entry
            tfName.setText("");
            tfDob.setText("");
            tfContact.setText("");
            tfAddress.setText("");
            tfEmergencyContact.setText("");
            tfExperience.setText("");
            tfLicense.setText("");
            tfAffiliation.setText("");
            tfFellowships.setText("");
            tfCME.setText("");
            tfPosition.setText("");
            tfDepartment.setText("");
            tfAccountNumber.setText("");
            tfTaxId.setText("");
            tfInsurance.setText("");
            tfHealthHistory.setText("");
        }
    }

    public static void main(String[] args) {
        new DoctorDetails();
    }
}
