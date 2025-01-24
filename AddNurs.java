import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.Random;

public class AddNurs extends JFrame implements ActionListener {
    JTextField tfNurseNo, tfName, tfPhone, tfEmail, tfAddress, tfLicense, tfQualification, tfSupervisor, tfSalary, tfUsername, tfPassword;
    JComboBox cbGender, cbBloodGroup, cbShift, cbDept, cbDesignation;
    JButton submit, reset;
    Random ra = new Random();
    long first4 = Math.abs(ra.nextLong() % 9000L) + 1000L;  // Randomly generated Nurse Number
    Connection conn;

    AddNurs() {
        // Connect to the database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setSize(900, 700);
        setLocation(350, 50);
        setLayout(null);

        JLabel heading = new JLabel("Add Nurse Details");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        // Nurse Number (Randomly Generated)
        JLabel nurseNoLabel = new JLabel("Nurse Number");
        nurseNoLabel.setBounds(50, 150, 200, 30);
        nurseNoLabel.setFont(new Font("serif", Font.BOLD, 20));
        add(nurseNoLabel);

        tfNurseNo = new JTextField("N" + first4);  // Generate unique Nurse No
        tfNurseNo.setBounds(200, 150, 150, 30);
        tfNurseNo.setEditable(false);  // Make it non-editable
        add(tfNurseNo);

        // Nurse Info UI components
        JLabel name = new JLabel("Name");
        name.setBounds(50, 200, 200, 30);
        name.setFont(new Font("serif", Font.BOLD, 20));
        add(name);

        tfName = new JTextField();
        tfName.setBounds(200, 200, 150, 30);
        add(tfName);

        JLabel gender = new JLabel("Gender");
        gender.setBounds(400, 200, 200, 30);
        gender.setFont(new Font("serif", Font.BOLD, 20));
        add(gender);

        String[] genderOptions = {"Male", "Female", "Other"};
        cbGender = new JComboBox<>(genderOptions);
        cbGender.setBounds(600, 200, 150, 30);
        add(cbGender);

        JLabel phone = new JLabel("Phone");
        phone.setBounds(50, 250, 200, 30);
        phone.setFont(new Font("serif", Font.BOLD, 20));
        add(phone);

        tfPhone = new JTextField();
        tfPhone.setBounds(200, 250, 150, 30);
        add(tfPhone);

        JLabel email = new JLabel("Email");
        email.setBounds(400, 250, 200, 30);
        email.setFont(new Font("serif", Font.BOLD, 20));
        add(email);

        tfEmail = new JTextField();
        tfEmail.setBounds(600, 250, 150, 30);
        add(tfEmail);

        JLabel address = new JLabel("Address");
        address.setBounds(50, 300, 200, 30);
        address.setFont(new Font("serif", Font.BOLD, 20));
        add(address);

        tfAddress = new JTextField();
        tfAddress.setBounds(200, 300, 150, 30);
        add(tfAddress);

        JLabel license = new JLabel("License No");
        license.setBounds(400, 300, 200, 30);
        license.setFont(new Font("serif", Font.BOLD, 20));
        add(license);

        tfLicense = new JTextField();
        tfLicense.setBounds(600, 300, 150, 30);
        add(tfLicense);

        JLabel qualification = new JLabel("Qualification");
        qualification.setBounds(50, 350, 200, 30);
        qualification.setFont(new Font("serif", Font.BOLD, 20));
        add(qualification);

        tfQualification = new JTextField();
        tfQualification.setBounds(200, 350, 150, 30);
        add(tfQualification);

        JLabel dept = new JLabel("Department");
        dept.setBounds(400, 350, 200, 30);
        dept.setFont(new Font("serif", Font.BOLD, 20));
        add(dept);

        String[] deptOptions = {"Surgery", "Pediatrics", "ICU", "Emergency", "General Ward"};
        cbDept = new JComboBox<>(deptOptions);
        cbDept.setBounds(600, 350, 150, 30);
        add(cbDept);

        JLabel designation = new JLabel("Designation");
        designation.setBounds(50, 400, 200, 30);
        designation.setFont(new Font("serif", Font.BOLD, 20));
        add(designation);

        String[] designationOptions = {"Registered Nurse", "Senior Nurse", "Nurse Practitioner"};
        cbDesignation = new JComboBox<>(designationOptions);
        cbDesignation.setBounds(200, 400, 150, 30);
        add(cbDesignation);

        JLabel shift = new JLabel("Shift");
        shift.setBounds(400, 400, 200, 30);
        shift.setFont(new Font("serif", Font.BOLD, 20));
        add(shift);

        String[] shiftOptions = {"Day", "Night", "Rotational"};
        cbShift = new JComboBox<>(shiftOptions);
        cbShift.setBounds(600, 400, 150, 30);
        add(cbShift);

        JLabel supervisor = new JLabel("Supervisor");
        supervisor.setBounds(50, 450, 200, 30);
        supervisor.setFont(new Font("serif", Font.BOLD, 20));
        add(supervisor);

        tfSupervisor = new JTextField();
        tfSupervisor.setBounds(200, 450, 150, 30);
        add(tfSupervisor);

        JLabel bloodGroup = new JLabel("Blood Group");
        bloodGroup.setBounds(400, 450, 200, 30);
        bloodGroup.setFont(new Font("serif", Font.BOLD, 20));
        add(bloodGroup);

        String[] bloodGroupOptions = {"A+", "B+", "AB+", "O+"};
        cbBloodGroup = new JComboBox<>(bloodGroupOptions);
        cbBloodGroup.setBounds(600, 450, 150, 30);
        add(cbBloodGroup);

        JLabel salary = new JLabel("Salary");
        salary.setBounds(50, 500, 200, 30);
        salary.setFont(new Font("serif", Font.BOLD, 20));
        add(salary);

        tfSalary = new JTextField();
        tfSalary.setBounds(200, 500, 150, 30);
        add(tfSalary);

        JLabel username = new JLabel("Username");
        username.setBounds(400, 500, 200, 30);
        username.setFont(new Font("serif", Font.BOLD, 20));
        add(username);

        tfUsername = new JTextField();
        tfUsername.setBounds(600, 500, 150, 30);
        add(tfUsername);

        JLabel password = new JLabel("Password");
        password.setBounds(50, 550, 200, 30);
        password.setFont(new Font("serif", Font.BOLD, 20));
        add(password);

        tfPassword = new JTextField();
        tfPassword.setBounds(200, 550, 150, 30);
        add(tfPassword);

        submit = new JButton("Submit");
        submit.setBounds(250, 600, 120, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Tahoma", Font.BOLD, 16));
        submit.addActionListener(this);
        add(submit);

        reset = new JButton("Reset");
        reset.setBounds(450, 600, 120, 30);
        reset.setBackground(Color.BLACK);
        reset.setForeground(Color.WHITE);
        reset.setFont(new Font("Tahoma", Font.BOLD, 16));
        reset.addActionListener(this);
        add(reset);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String nurseNo = tfNurseNo.getText();  // Nurse Number (generated)
            String name = tfName.getText();
            String gender = (String) cbGender.getSelectedItem();
            String phone = tfPhone.getText();
            String email = tfEmail.getText();
            String address = tfAddress.getText();
            String license = tfLicense.getText();
            String qualification = tfQualification.getText();
            String dept = (String) cbDept.getSelectedItem();
            String designation = (String) cbDesignation.getSelectedItem();
            String shift = (String) cbShift.getSelectedItem();
            String supervisor = tfSupervisor.getText();
            String bloodGroup = (String) cbBloodGroup.getSelectedItem();
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

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Nurse details added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error adding nurse details.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (ae.getSource() == reset) {
            tfNurseNo.setText("N" + first4);  // Reset Nurse Number to the original random value
            tfName.setText("");
            tfPhone.setText("");
            tfEmail.setText("");
            tfAddress.setText("");
            tfLicense.setText("");
            tfQualification.setText("");
            tfSupervisor.setText("");
            tfSalary.setText("");
            tfUsername.setText("");
            tfPassword.setText("");
        }
    }

    public static void main(String[] args) {
        new AddNurs();
    }
}

