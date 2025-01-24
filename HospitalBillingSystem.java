import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.*;

public class HospitalBillingSystem extends JFrame implements ActionListener {
    private JTextField tfPatientNumber, treatmentCostField, medicationCostField, otherChargesField;
    private JButton search, generateInvoice, print, uploadPhoto, saveToFile;
    private JTable personalTable, medicalTable;
    private JTextArea invoiceArea;
    private JLabel patientPhotoLabel;
    private ImageIcon patientPhoto;
    private Connection conn;
    private File savedPhoto;

    public HospitalBillingSystem() {
        // Initialize database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database!");
            System.exit(1);
        }

        // Set up the main frame
        setTitle("Hospital Billing System");
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Patient number input
        JLabel heading = new JLabel("Enter Patient Number:");
        heading.setBounds(20, 20, 150, 25);
        add(heading);

        tfPatientNumber = new JTextField();
        tfPatientNumber.setBounds(180, 20, 150, 25);
        add(tfPatientNumber);

        search = new JButton("Search");
        search.setBounds(350, 20, 100, 25);
        search.addActionListener(this);
        add(search);

        // Personal details
        JLabel personalDetailsLabel = new JLabel("Personal Details:");
        personalDetailsLabel.setBounds(20, 60, 150, 25);
        add(personalDetailsLabel);

        personalTable = new JTable();
        JScrollPane personalScrollPane = new JScrollPane(personalTable);
        personalScrollPane.setBounds(20, 90, 450, 150);
        add(personalScrollPane);

        // Medical details
        JLabel medicalDetailsLabel = new JLabel("Medical Details:");
        medicalDetailsLabel.setBounds(20, 260, 150, 25);
        add(medicalDetailsLabel);

        medicalTable = new JTable();
        JScrollPane medicalScrollPane = new JScrollPane(medicalTable);
        medicalScrollPane.setBounds(20, 290, 450, 200);
        add(medicalScrollPane);

        // Photo upload
        JLabel photoLabel = new JLabel("Patient Photo:");
        photoLabel.setBounds(500, 60, 150, 25);
        add(photoLabel);

        patientPhotoLabel = new JLabel();
        patientPhotoLabel.setBounds(500, 90, 150, 150);
        patientPhotoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(patientPhotoLabel);

        uploadPhoto = new JButton("Upload Photo");
        uploadPhoto.setBounds(500, 260, 150, 25);
        uploadPhoto.addActionListener(this);
        add(uploadPhoto);

        // Invoice generation
        JLabel costHeading = new JLabel("Enter Costs:");
        costHeading.setBounds(20, 520, 150, 25);
        add(costHeading);

        JLabel treatmentCostLabel = new JLabel("Treatment Cost:");
        treatmentCostLabel.setBounds(20, 560, 150, 25);
        add(treatmentCostLabel);

        treatmentCostField = new JTextField();
        treatmentCostField.setBounds(180, 560, 150, 25);
        add(treatmentCostField);

        JLabel medicationCostLabel = new JLabel("Medication Cost:");
        medicationCostLabel.setBounds(20, 600, 150, 25);
        add(medicationCostLabel);

        medicationCostField = new JTextField();
        medicationCostField.setBounds(180, 600, 150, 25);
        add(medicationCostField);

        JLabel otherChargesLabel = new JLabel("Other Charges:");
        otherChargesLabel.setBounds(20, 640, 150, 25);
        add(otherChargesLabel);

        otherChargesField = new JTextField();
        otherChargesField.setBounds(180, 640, 150, 25);
        add(otherChargesField);

        generateInvoice = new JButton("Generate Invoice");
        generateInvoice.setBounds(20, 680, 150, 25);
        generateInvoice.addActionListener(this);
        add(generateInvoice);

        print = new JButton("Print");
        print.setBounds(200, 680, 80, 25);
        print.addActionListener(this);
        add(print);

        saveToFile = new JButton("Save to File");
        saveToFile.setBounds(300, 680, 120, 25);
        saveToFile.addActionListener(this);
        add(saveToFile);

        invoiceArea = new JTextArea();
        invoiceArea.setEditable(false);
        JScrollPane invoiceScrollPane = new JScrollPane(invoiceArea);
        invoiceScrollPane.setBounds(500, 290, 350, 150);
        add(invoiceScrollPane);
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
        } else if (e.getSource() == uploadPhoto) {
            uploadPatientPhoto();
        } else if (e.getSource() == generateInvoice) {
            generateInvoice();
        } else if (e.getSource() == print) {
            printAllDetails();
        } else if (e.getSource() == saveToFile) {
            saveDetailsToFile();
        }
    }

    private void fetchAndDisplayPatientDetails(String patientNo) {
        try {
            // Fetch personal details
            String personalQuery = "SELECT * FROM persondetail WHERE patient_no = ?";
            PreparedStatement personalStmt = conn.prepareStatement(personalQuery);
            personalStmt.setString(1, patientNo);
            ResultSet personalRs = personalStmt.executeQuery();

            // Populate personal table
            if (personalRs.next()) {
                Object[][] personalData = {
                        { "Name", personalRs.getString("first_name") },
                        { "Patient Number", personalRs.getString("patient_no") },
                        { "Middle Name", personalRs.getString("middle_name") },
                        { "Phone Number", personalRs.getString("phone") },
                        { "Insurance Id", personalRs.getInt("insurance_id") },
                        { "Insurance Provider", personalRs.getString("insurance_provider") }
                };
                String[] personalColumns = { "Attribute", "Value" };
                personalTable.setModel(new DefaultTableModel(personalData, personalColumns));
            }

            // Fetch medical details
            String medicalQuery = "SELECT * FROM medicaldetail WHERE patient_no = ?";
            PreparedStatement medicalStmt = conn.prepareStatement(medicalQuery);
            medicalStmt.setString(1, patientNo);
            ResultSet medicalRs = medicalStmt.executeQuery();

            // Populate medical table
            if (medicalRs.next()) {
                Object[][] medicalData = {
                        { "Chronic Condition", medicalRs.getString("chronic_condition") },
                        { "Allergies", medicalRs.getString("allergies") },
                        { "Vital Signs", medicalRs.getString("vital_sign") },
                        { "Reproductive Health", medicalRs.getString("reproductive") },
                        { "Surgical History", medicalRs.getString("sha") },
                        { "Disabilities", medicalRs.getString("disabilities") },
                        { "Custom Data", medicalRs.getString("custom_data") },
                        { "Habits", medicalRs.getString("habits") },
                        { "Therapies", medicalRs.getString("therapies") }
                };
                String[] medicalColumns = { "Attribute", "Value" };
                medicalTable.setModel(new DefaultTableModel(medicalData, medicalColumns));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching patient details.");
        }
    }

    private void generateInvoice() {
        try {
            double treatmentCost = Double.parseDouble(treatmentCostField.getText());
            double medicationCost = Double.parseDouble(medicationCostField.getText());
            double otherCharges = Double.parseDouble(otherChargesField.getText());
            double total = treatmentCost + medicationCost + otherCharges;

            invoiceArea.setText("Invoice Details:\n");
            invoiceArea.append("Treatment Cost: $" + treatmentCost + "\n");
            invoiceArea.append("Medication Cost: $" + medicationCost + "\n");
            invoiceArea.append("Other Charges: $" + otherCharges + "\n");
            invoiceArea.append("Total: $" + total + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for costs.");
        }
    }

    private void printAllDetails() {
        try {
            StringBuilder details = new StringBuilder("Hospital Billing System Details:\n\n");

            // Personal Details
            details.append("Personal Details:\n");
            for (int i = 0; i < personalTable.getRowCount(); i++) {
                details.append(personalTable.getValueAt(i, 0)).append(": ")
                       .append(personalTable.getValueAt(i, 1)).append("\n");
            }

            // Medical Details
            details.append("\nMedical Details:\n");
            for (int i = 0; i < medicalTable.getRowCount(); i++) {
                details.append(medicalTable.getValueAt(i, 0)).append(": ")
                       .append(medicalTable.getValueAt(i, 1)).append("\n");
            }

            // Invoice Details
            details.append("\nInvoice:\n");
            details.append(invoiceArea.getText());

            // Print the details
            JTextArea printArea = new JTextArea();
            printArea.setText(details.toString());
            printArea.print();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void uploadPatientPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            savedPhoto = new File(filePath);  // Save the file to the PC
            patientPhoto = new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            patientPhotoLabel.setIcon(patientPhoto);
        }
    }

    private void saveDetailsToFile() {
        try {
            String patientNo = tfPatientNumber.getText();
            if (patientNo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Patient Number");
                return;
            }

            // Save the details to a text file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Patient Details");
            fileChooser.setSelectedFile(new File(patientNo + "_details.txt"));
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                FileWriter fileWriter = new FileWriter(saveFile);
                BufferedWriter writer = new BufferedWriter(fileWriter);

                // Write personal details
                writer.write("Personal Details:\n");
                for (int i = 0; i < personalTable.getRowCount(); i++) {
                    writer.write(personalTable.getValueAt(i, 0) + ": " + personalTable.getValueAt(i, 1) + "\n");
                }

                // Write medical details
                writer.write("\nMedical Details:\n");
                for (int i = 0; i < medicalTable.getRowCount(); i++) {
                    writer.write(medicalTable.getValueAt(i, 0) + ": " + medicalTable.getValueAt(i, 1) + "\n");
                }

                // Write invoice details
                writer.write("\nInvoice Details:\n");
                writer.write(invoiceArea.getText());

                // Save the photo
                if (savedPhoto != null) {
                    writer.write("\nPatient Photo: " + savedPhoto.getAbsolutePath() + "\n");
                }

                writer.close();
                JOptionPane.showMessageDialog(this, "Details saved successfully!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving details to file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HospitalBillingSystem frame = new HospitalBillingSystem();
            frame.setVisible(true);
        });
    }
}
