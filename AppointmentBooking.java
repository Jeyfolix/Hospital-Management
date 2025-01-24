import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.Date;
import com.twilio.Twilio;`
import com.twilio.rest.api.v2010.account.Message;

public class AppointmentBooking extends JFrame implements ActionListener {
    JLabel lblPatientName, lblContact, lblSuffering, lblOperation, lblHospital, lblDate, lblTime, lblInsurance, lblAppointmentType;
    JTextField tfPatientName, tfContact, tfSuffering, tfInsurance;
    JComboBox<String> cbOperation, cbHospital, cbAppointmentType;
    JSpinner spDate, spTime;
    JButton btnBookAppointment, btnBack;
    Connection conn;

    // Twilio credentials
    public static final String ACCOUNT_SID = "your_account_sid";
    public static final String AUTH_TOKEN = "your_auth_token";
    public static final String TWILIO_PHONE_NUMBER = "your_twilio_phone_number";

    AppointmentBooking() {
        // Initialize connection to the database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hospital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle("Appointment Booking");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Patient Name
        lblPatientName = new JLabel("Patient Name");
        lblPatientName.setBounds(20, 20, 150, 20);
        add(lblPatientName);

        tfPatientName = new JTextField();
        tfPatientName.setBounds(180, 20, 200, 20);
        add(tfPatientName);

        // Contact Number
        lblContact = new JLabel("Contact Number");
        lblContact.setBounds(20, 60, 150, 20);
        add(lblContact);

        tfContact = new JTextField();
        tfContact.setBounds(180, 60, 200, 20);
        add(tfContact);

        // Suffering
        lblSuffering = new JLabel("Suffering");
        lblSuffering.setBounds(20, 100, 150, 20);
        add(lblSuffering);

        tfSuffering = new JTextField();
        tfSuffering.setBounds(180, 100, 200, 20);
        add(tfSuffering);

        // Operation
        lblOperation = new JLabel("Operation Required");
        lblOperation.setBounds(20, 140, 150, 20);
        add(lblOperation);

        cbOperation = new JComboBox<>(new String[] { "Select Operation", "Surgery", "Physiotherapy", "General Consultation", "Diagnostics" });
        cbOperation.setBounds(180, 140, 200, 20);
        add(cbOperation);

        // Hospital
        lblHospital = new JLabel("Hospital");
        lblHospital.setBounds(20, 180, 150, 20);
        add(lblHospital);

        cbHospital = new JComboBox<>(new String[] { "Select Hospital", "City Hospital", "Green Valley Hospital", "Hope Medical Center", "Healing Touch Hospital" });
        cbHospital.setBounds(180, 180, 200, 20);
        add(cbHospital);

        // Appointment Date
        lblDate = new JLabel("Appointment Date");
        lblDate.setBounds(20, 220, 150, 20);
        add(lblDate);

        spDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spDate, "yyyy-MM-dd");
        spDate.setEditor(dateEditor);
        spDate.setBounds(180, 220, 200, 20);
        add(spDate);

        // Appointment Time
        lblTime = new JLabel("Appointment Time");
        lblTime.setBounds(20, 260, 150, 20);
        add(lblTime);

        spTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spTime, "HH:mm");
        spTime.setEditor(timeEditor);
        spTime.setBounds(180, 260, 200, 20);
        add(spTime);

        // Appointment Type
        lblAppointmentType = new JLabel("Appointment Type");
        lblAppointmentType.setBounds(20, 300, 150, 20);
        add(lblAppointmentType);

        cbAppointmentType = new JComboBox<>(new String[] { "Select Type", "In-person", "Online/Telemedicine" });
        cbAppointmentType.setBounds(180, 300, 200, 20);
        add(cbAppointmentType);

        // Insurance Provider
        lblInsurance = new JLabel("Insurance Provider");
        lblInsurance.setBounds(20, 340, 150, 20);
        add(lblInsurance);

        tfInsurance = new JTextField();
        tfInsurance.setBounds(180, 340, 200, 20);
        add(tfInsurance);

        // Book Appointment Button
        btnBookAppointment = new JButton("Book Appointment");
        btnBookAppointment.setBounds(180, 380, 200, 30);
        btnBookAppointment.addActionListener(this);
        add(btnBookAppointment);

        // Back Button
        btnBack = new JButton("Back");
        btnBack.setBounds(20, 380, 100, 30);
        btnBack.addActionListener(this);
        add(btnBack);

        setSize(420, 470);
        setLocation(300, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            setVisible(false);
        } else if (e.getSource() == btnBookAppointment) {
            bookAppointment();
        }
    }

    private void bookAppointment() {
        String patientName = tfPatientName.getText();
        String contact = tfContact.getText();
        String suffering = tfSuffering.getText();
        String operation = cbOperation.getSelectedItem().toString();
        String hospital = cbHospital.getSelectedItem().toString();
        Date appointmentDate = (Date) spDate.getValue();
        Date appointmentTime = (Date) spTime.getValue();
        String appointmentType = cbAppointmentType.getSelectedItem().toString();
        String insurance = tfInsurance.getText();

        if (patientName.isEmpty() || contact.isEmpty() || suffering.isEmpty() || operation.equals("Select Operation")
                || hospital.equals("Select Hospital") || appointmentType.equals("Select Type")) {
            JOptionPane.showMessageDialog(this, "Please fill all the required fields.");
            return;
        }

        try {
            String query = "INSERT INTO appointments (patient_name, contact, suffering, operation_required, hospital, appointment_date, appointment_time, appointment_type, insurance_provider) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, patientName);
            stmt.setString(2, contact);
            stmt.setString(3, suffering);
            stmt.setString(4, operation);
            stmt.setString(5, hospital);
            stmt.setTimestamp(6, new java.sql.Timestamp(appointmentDate.getTime()));
            stmt.setTimestamp(7, new java.sql.Timestamp(appointmentTime.getTime()));
            stmt.setString(8, appointmentType);
            stmt.setString(9, insurance);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Appointment booked successfully.");
                sendConfirmationMessage(contact, patientName, appointmentDate, appointmentTime);
            } else {
                JOptionPane.showMessageDialog(this, "Error booking appointment.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in database connection.");
        }
    }

    private void sendConfirmationMessage(String contact, String patientName, Date appointmentDate, Date appointmentTime) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String messageBody = String.format("Hello %s, your appointment is booked successfully for %s at %s.", 
                                            patientName, appointmentDate.toString(), appointmentTime.toString());

        try {
            Message.creator(
                    new com.twilio.type.PhoneNumber(contact),
                    new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                    messageBody
            ).create();

            JOptionPane.showMessageDialog(this, "Confirmation message sent to " + contact);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send confirmation message.");
        }
    }

    public static void main(String[] args) {
        new AppointmentBooking();
    }
}
