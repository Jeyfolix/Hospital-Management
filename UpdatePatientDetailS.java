import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class UpdatePatientDetailS extends JFrame implements ActionListener {
    JTextField tfpatientNo, tfFirstName, tfMiddleName, tfLastName, tfDOB, tfAddress, tfSuburb, tfCity, tfCountry, tfPostalCode;
    JTextField tfTelephone, tfPostalAddress, tfPhone, tfEmail, tfEmergencyContact, tfInsuranceId;
    JComboBox<String> cbGender, cbInsuranceProvider, cbPatientType;
    JButton searchBu, updateBu, deleteBu;
    Connection conn;

    UpdatePatientDetailS() {
        // Connect to the database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setSize(900, 700);
        setLocation(350, 50);
        setLayout(null);

        JLabel heading = new JLabel("Personal Information");
        heading.setBounds(320, 30, 500, 40);
        heading.setFont(new Font("serif", Font.BOLD, 24));
        add(heading);

        // Patient Info UI components
        JLabel patientNo = new JLabel("Patient No");
        patientNo.setBounds(50, 120, 150, 25);
        patientNo.setFont(new Font("serif", Font.BOLD, 14));
        add(patientNo);

        tfpatientNo = new JTextField();
        tfpatientNo.setBounds(200, 120, 120, 25);
        add(tfpatientNo);

        JLabel firstName = new JLabel("First Name");
        firstName.setBounds(50, 160, 100, 25);
        firstName.setFont(new Font("serif", Font.BOLD, 14));
        add(firstName);

        tfFirstName = new JTextField();
        tfFirstName.setBounds(200, 160, 120, 25);
        add(tfFirstName);

        JLabel middleName = new JLabel("Middle Name");
        middleName.setBounds(400, 160, 100, 25);
        middleName.setFont(new Font("serif", Font.BOLD, 14));
        add(middleName);

        tfMiddleName = new JTextField();
        tfMiddleName.setBounds(600, 160, 120, 25);
        add(tfMiddleName);

        JLabel lastName = new JLabel("Last Name");
        lastName.setBounds(50, 200, 150, 25);
        lastName.setFont(new Font("serif", Font.BOLD, 14));
        add(lastName);

        tfLastName = new JTextField();
        tfLastName.setBounds(200, 200, 120, 25);
        add(tfLastName);

        JLabel dob = new JLabel("Date of Birth");
        dob.setBounds(400, 200, 150, 25);
        dob.setFont(new Font("serif", Font.BOLD, 14));
        add(dob);

        tfDOB = new JTextField();
        tfDOB.setBounds(600, 200, 120, 25);
        add(tfDOB);

        JLabel gender = new JLabel("Gender");
        gender.setBounds(50, 240, 150, 25);
        gender.setFont(new Font("serif", Font.BOLD, 14));
        add(gender);

        String[] genders = {"Male", "Female", "Other"};
        cbGender = new JComboBox<>(genders);
        cbGender.setBounds(200, 240, 120, 25);
        add(cbGender);

        JLabel address = new JLabel("Address");
        address.setBounds(400, 240, 150, 25);
        address.setFont(new Font("serif", Font.BOLD, 14));
        add(address);

        tfAddress = new JTextField();
        tfAddress.setBounds(600, 240, 120, 25);
        add(tfAddress);

        JLabel suburb = new JLabel("Suburb");
        suburb.setBounds(50, 280, 150, 25);
        suburb.setFont(new Font("serif", Font.BOLD, 14));
        add(suburb);

        tfSuburb = new JTextField();
        tfSuburb.setBounds(200, 280, 120, 25);
        add(tfSuburb);

        JLabel city = new JLabel("City");
        city.setBounds(400, 280, 150, 25);
        city.setFont(new Font("serif", Font.BOLD, 14));
        add(city);

        tfCity = new JTextField();
        tfCity.setBounds(600, 280, 120, 25);
        add(tfCity);

        JLabel country = new JLabel("Country");
        country.setBounds(50, 320, 150, 25);
        country.setFont(new Font("serif", Font.BOLD, 14));
        add(country);

        tfCountry = new JTextField();
        tfCountry.setBounds(200, 320, 120, 25);
        add(tfCountry);

        JLabel postalCode = new JLabel("Postal Code");
        postalCode.setBounds(400, 320, 150, 25);
        postalCode.setFont(new Font("serif", Font.BOLD, 14));
        add(postalCode);

        tfPostalCode = new JTextField();
        tfPostalCode.setBounds(600, 320, 120, 25);
        add(tfPostalCode);

        JLabel telephone = new JLabel("Telephone");
        telephone.setBounds(50, 360, 150, 25);
        telephone.setFont(new Font("serif", Font.BOLD, 14));
        add(telephone);

        tfTelephone = new JTextField();
        tfTelephone.setBounds(200, 360, 120, 25);
        add(tfTelephone);

        JLabel postalAddress = new JLabel("Postal Address");
        postalAddress.setBounds(400, 360, 150, 25);
        postalAddress.setFont(new Font("serif", Font.BOLD, 14));
        add(postalAddress);

        tfPostalAddress = new JTextField();
        tfPostalAddress.setBounds(600, 360, 120, 25);
        add(tfPostalAddress);

        JLabel phone = new JLabel("Phone");
        phone.setBounds(50, 400, 150, 25);
        phone.setFont(new Font("serif", Font.BOLD, 14));
        add(phone);

        tfPhone = new JTextField();
        tfPhone.setBounds(200, 400, 120, 25);
        add(tfPhone);

        JLabel email = new JLabel("Email");
        email.setBounds(400, 400, 150, 25);
        email.setFont(new Font("serif", Font.BOLD, 14));
        add(email);

        tfEmail = new JTextField();
        tfEmail.setBounds(600, 400, 120, 25);
        add(tfEmail);

        JLabel emergencyContact = new JLabel("Emergency Contact");
        emergencyContact.setBounds(50, 440, 150, 25);
        emergencyContact.setFont(new Font("serif", Font.BOLD, 14));
        add(emergencyContact);

        tfEmergencyContact = new JTextField();
        tfEmergencyContact.setBounds(200, 440, 120, 25);
        add(tfEmergencyContact);

        JLabel insuranceProvider = new JLabel("Insurance Provider");
        insuranceProvider.setBounds(400, 440, 150, 25);
        insuranceProvider.setFont(new Font("serif", Font.BOLD, 14));
        add(insuranceProvider);

        String[] providers = {"Provider A", "Provider B", "Provider C"};
        cbInsuranceProvider = new JComboBox<>(providers);
        cbInsuranceProvider.setBounds(600, 440, 120, 25);
        add(cbInsuranceProvider);

        JLabel insuranceId = new JLabel("Insurance ID");
        insuranceId.setBounds(50, 480, 150, 25);
        insuranceId.setFont(new Font("serif", Font.BOLD, 14));
        add(insuranceId);

        tfInsuranceId = new JTextField();
        tfInsuranceId.setBounds(200, 480, 120, 25);
        add(tfInsuranceId);

        JLabel patientType = new JLabel("Patient Type");
        patientType.setBounds(400, 480, 150, 25);
        patientType.setFont(new Font("serif", Font.BOLD, 14));
        add(patientType);

        String[] types = {"Inpatient", "Outpatient"};
        cbPatientType = new JComboBox<>(types);
        cbPatientType.setBounds(600, 480, 120, 25);
        add(cbPatientType);

        // Buttons
        searchBu = new JButton("Search");
        searchBu.setBounds(50, 530, 120, 30);
        searchBu.setBackground(Color.BLACK);
        searchBu.setForeground(Color.WHITE);
        searchBu.setFont(new Font("Tahoma", Font.BOLD, 14));
        searchBu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPatient();
            }
        });
        add(searchBu);

        updateBu = new JButton("Update");
        updateBu.setBounds(200, 530, 120, 30);
        updateBu.setBackground(Color.BLACK);
        updateBu.setForeground(Color.WHITE);
        updateBu.setFont(new Font("Tahoma", Font.BOLD, 14));
        updateBu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePatient();
            }
        });
        add(updateBu);

        deleteBu = new JButton("Delete");
        deleteBu.setBounds(350, 530, 120, 30);
        deleteBu.setBackground(Color.BLACK);
        deleteBu.setForeground(Color.WHITE);
        deleteBu.setFont(new Font("Tahoma", Font.BOLD, 14));
        deleteBu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePatient();
            }
        });
        add(deleteBu);

        setVisible(true);
    }

    public void searchPatient() {
        String patientNo = tfpatientNo.getText();
        try {
            String query = "SELECT * FROM persondetail WHERE patient_no=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, patientNo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tfFirstName.setText(rs.getString("first_name"));
                tfMiddleName.setText(rs.getString("middle_name"));
                tfLastName.setText(rs.getString("last_name"));
                tfDOB.setText(rs.getString("dob"));
                cbGender.setSelectedItem(rs.getString("gender"));
                tfAddress.setText(rs.getString("address"));
                tfSuburb.setText(rs.getString("suburb"));
                tfCity.setText(rs.getString("city"));
                tfCountry.setText(rs.getString("country"));
                tfPostalCode.setText(rs.getString("postal_code"));
                tfTelephone.setText(rs.getString("telephone"));
                tfPostalAddress.setText(rs.getString("postal_address"));
                tfPhone.setText(rs.getString("phone"));
                tfEmail.setText(rs.getString("email"));
                tfEmergencyContact.setText(rs.getString("emergency_contact"));
                cbInsuranceProvider.setSelectedItem(rs.getString("insurance_provider"));
                tfInsuranceId.setText(rs.getString("insurance_id"));
                cbPatientType.setSelectedItem(rs.getString("patient_type"));
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatient() {
        String patientNo = tfpatientNo.getText();
        String firstName = tfFirstName.getText();
        String middleName = tfMiddleName.getText();
        String lastName = tfLastName.getText();
        String dob = tfDOB.getText();
        String gender = cbGender.getSelectedItem().toString();
        String address = tfAddress.getText();
        String suburb = tfSuburb.getText();
        String city = tfCity.getText();
        String country = tfCountry.getText();
        String postalCode = tfPostalCode.getText();
        String telephone = tfTelephone.getText();
        String postalAddress = tfPostalAddress.getText();
        String phone = tfPhone.getText();
        String email = tfEmail.getText();
        String emergencyContact = tfEmergencyContact.getText();
        String insuranceProvider = cbInsuranceProvider.getSelectedItem().toString();
        String insuranceId = tfInsuranceId.getText();
        String patientType = cbPatientType.getSelectedItem().toString();

        try {
            String query = "UPDATE persondetail SET first_name=?, middle_name=?, last_name=?, dob=?, gender=?, address=?, suburb=?, city=?, country=?, postal_code=?, telephone=?, postal_address=?, phone=?, email=?, emergency_contact=?, insurance_provider=?, insurance_id=?, patient_type=? WHERE patient_no=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, firstName);
            stmt.setString(2, middleName);
            stmt.setString(3, lastName);
            stmt.setString(4, dob);
            stmt.setString(5, gender);
            stmt.setString(6, address);
            stmt.setString(7, suburb);
            stmt.setString(8, city);
            stmt.setString(9, country);
            stmt.setString(10, postalCode);
            stmt.setString(11, telephone);
            stmt.setString(12, postalAddress);
            stmt.setString(13, phone);
            stmt.setString(14, email);
            stmt.setString(15, emergencyContact);
            stmt.setString(16, insuranceProvider);
            stmt.setString(17, insuranceId);
            stmt.setString(18, patientType);
            stmt.setString(19, patientNo);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Patient details updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating patient details.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient() {
        String adminUsername = JOptionPane.showInputDialog(this, "Enter Admin Username:");
        String adminPassword = JOptionPane.showInputDialog(this, "Enter Admin Password:");
        
        if (authenticateAdmin(adminUsername, adminPassword)) {
            String patientNo = tfpatientNo.getText();
            try {
                String query = "DELETE FROM persondetail WHERE patient_no=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, patientNo);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Patient details deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting patient details.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Admin verification failed. Patient details not deleted.");
        }
    }

    private boolean authenticateAdmin(String username, String password) {
        try {
            String query = "SELECT * FROM admins WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if admin credentials are valid
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new UpdatePatientDetailS();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}
