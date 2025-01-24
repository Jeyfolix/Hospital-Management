import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HospitalAppointmentCalendar extends JFrame implements ActionListener {

    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, idField, contactField, dateField, timeField, doctorField, departmentField, purposeField, paymentField;

    public HospitalAppointmentCalendar() {
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Hospital Appointment Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JLabel headerLabel = new JLabel("Hospital Appointment Calendar", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);

        String[] columns = {"Patient Name", "Patient ID", "Contact", "Date", "Time", "Doctor", "Department", "Purpose", "Payment Method"};
        tableModel = new DefaultTableModel(columns, 0);
        appointmentTable = new JTable(tableModel);
        add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(10, 2, 10, 10));

        nameField = createInputField(inputPanel, "Patient Name:");
        idField = createInputField(inputPanel, "Patient ID:");
        contactField = createInputField(inputPanel, "Contact:");
        dateField = createInputField(inputPanel, "Date (YYYY-MM-DD):", LocalDate.now().toString());
        timeField = createInputField(inputPanel, "Time:", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeField.setEditable(false);
        doctorField = createInputField(inputPanel, "Doctor:");
        departmentField = createInputField(inputPanel, "Department:");
        purposeField = createInputField(inputPanel, "Purpose:");
        paymentField = createInputField(inputPanel, "Payment Method:");

        JButton addButton = new JButton("Add Appointment");
        addButton.addActionListener(this);
        inputPanel.add(new JLabel(""));
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private JTextField createInputField(JPanel panel, String labelText) {
        return createInputField(panel, labelText, "");
    }

    private JTextField createInputField(JPanel panel, String labelText, String defaultValue) {
        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField(defaultValue);
        panel.add(label);
        panel.add(textField);
        return textField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String id = idField.getText();
        String contact = contactField.getText();
        String date = dateField.getText();
        String time = timeField.getText();
        String doctor = doctorField.getText();
        String department = departmentField.getText();
        String purpose = purposeField.getText();
        String payment = paymentField.getText();

        if (validateInput(name, id, contact, date, time, doctor, department, purpose, payment)) {
            addAppointmentToTable(name, id, contact, date, time, doctor, department, purpose, payment);
            clearFields();
        }
    }

    private boolean validateInput(String... inputs) {
        for (String input : inputs) {
            if (input == null || input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void addAppointmentToTable(String name, String id, String contact, String date, String time, String doctor, String department, String purpose, String payment) {
        tableModel.addRow(new Object[]{name, id, contact, date, time, doctor, department, purpose, payment});
        JOptionPane.showMessageDialog(this, "Appointment added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        contactField.setText("");
        doctorField.setText("");
        departmentField.setText("");
        purposeField.setText("");
        paymentField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HospitalAppointmentCalendar::new);
    }
}
