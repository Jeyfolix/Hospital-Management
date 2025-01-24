import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class MedicalDetail extends JFrame {
    JTextField tfcondition, tfall, tffname, tfrep, tfsha, tfdis, tfc, tfh, tft, tfpatientNo;
    JComboBox<String> cblood;
    JButton submit;
    Connection conn;

    public MedicalDetail() {
        // Connect to the database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle("Medical Details");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel heading = new JLabel("Provide Medical Details");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        // Medical Details UI components
        JLabel patientNoLabel = new JLabel("Patient Number");
        patientNoLabel.setBounds(50, 100, 200, 30);
        patientNoLabel.setFont(new Font("serif", Font.BOLD, 20));
        add(patientNoLabel);

        tfpatientNo = new JTextField();
        tfpatientNo.setBounds(200, 100, 150, 30);
        add(tfpatientNo);

        JLabel name = new JLabel("Chronic Condition");
        name.setBounds(50, 150, 200, 30);
        name.setFont(new Font("serif", Font.BOLD, 20));
        add(name);

        tfcondition = new JTextField();
        tfcondition.setBounds(200, 150, 150, 30);
        add(tfcondition);

        JLabel fname = new JLabel("Allergies");
        fname.setBounds(400, 150, 100, 30);
        fname.setFont(new Font("serif", Font.BOLD, 20));
        add(fname);

        tfall = new JTextField();
        tfall.setBounds(600, 150, 150, 30);
        add(tfall);

        JLabel vital = new JLabel("Vital Signs");
        vital.setBounds(50, 200, 200, 30);
        vital.setFont(new Font("serif", Font.BOLD, 20));
        add(vital);

        tffname = new JTextField();
        tffname.setBounds(200, 200, 150, 30);
        add(tffname);

        JLabel rep = new JLabel("Reproductive Health");
        rep.setBounds(400, 200, 200, 30);
        rep.setFont(new Font("serif", Font.BOLD, 20));
        add(rep);

        tfrep = new JTextField();
        tfrep.setBounds(600, 200, 150, 30);
        add(tfrep);

        JLabel sha = new JLabel("Surgical History");
        sha.setBounds(50, 250, 200, 30);
        sha.setFont(new Font("serif", Font.BOLD, 20));
        add(sha);

        tfsha = new JTextField();
        tfsha.setBounds(200, 250, 150, 30);
        add(tfsha);

        JLabel dis = new JLabel("Disabilities");
        dis.setBounds(400, 250, 200, 30);
        dis.setFont(new Font("serif", Font.BOLD, 20));
        add(dis);

        tfdis = new JTextField();
        tfdis.setBounds(600, 250, 150, 30);
        add(tfdis);

        JLabel c = new JLabel("Custom Data");
        c.setBounds(50, 300, 200, 30);
        c.setFont(new Font("serif", Font.BOLD, 20));
        add(c);

        tfc = new JTextField();
        tfc.setBounds(200, 300, 150, 30);
        add(tfc);

        JLabel habits = new JLabel("Habits");
        habits.setBounds(400, 300, 200, 30);
        habits.setFont(new Font("serif", Font.BOLD, 20));
        add(habits);

        tfh = new JTextField();
        tfh.setBounds(600, 300, 150, 30);
        add(tfh);

        JLabel therapies = new JLabel("Therapies");
        therapies.setBounds(50, 350, 200, 30);
        therapies.setFont(new Font("serif", Font.BOLD, 20));
        add(therapies);

        tft = new JTextField();
        tft.setBounds(200, 350, 150, 30);
        add(tft);

        JLabel bloodgroup = new JLabel("Blood Group");
        bloodgroup.setBounds(400, 350, 200, 30);
        bloodgroup.setFont(new Font("serif", Font.BOLD, 20));
        add(bloodgroup);

        cblood = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        cblood.setBounds(600, 350, 150, 30);
        add(cblood);

        submit = new JButton("Submit");
        submit.setBounds(250, 450, 120, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Tahoma", Font.BOLD, 16));
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String patientNo = tfpatientNo.getText();
                if (validatePatientNo(patientNo)) {
                    // Insert Medical Data to the database
                    try {
                        String query = "INSERT INTO medicaldetail (patient_no, chronic_condition, allergies, vital_sign, reproductive, sha, disabilities, custom_data, habits, therapies, blood_group) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, patientNo);  // Patient No entered by the user
                        stmt.setString(2, tfcondition.getText());
                        stmt.setString(3, tfall.getText());
                        stmt.setString(4, tffname.getText());
                        stmt.setString(5, tfrep.getText());
                        stmt.setString(6, tfsha.getText());
                        stmt.setString(7, tfdis.getText());
                        stmt.setString(8, tfc.getText());
                        stmt.setString(9, tfh.getText());
                        stmt.setString(10, tft.getText());
                        stmt.setString(11, (String) cblood.getSelectedItem());

                        int rows = stmt.executeUpdate();
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(MedicalDetail.this, "Medical details added successfully!");
                        } else {
                            JOptionPane.showMessageDialog(MedicalDetail.this, "Error adding medical details.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(MedicalDetail.this, "Invalid Patient Number. Please ensure it exists in the system.");
                }
            }
        });
        add(submit);

        setVisible(true);
    }

    // Method to validate the patient number against the persondetails table
    private boolean validatePatientNo(String patientNo) {
        try {
            String query = "SELECT COUNT(*) FROM persondetail WHERE patient_no = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, patientNo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;  // Patient number exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Patient number does not exist
    }

    public static void main(String[] args) {
        new MedicalDetail();
    }
}

