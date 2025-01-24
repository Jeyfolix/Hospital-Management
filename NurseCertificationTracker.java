import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.*;
import java.text.*;

public class NurseCertificationTracker extends JFrame {
    
    private JTextField txtName, txtCertification, txtCertDate, txtExpiryDate;
    private JTable nurseTable;
    private DefaultTableModel model;
    
    public NurseCertificationTracker() {
        // Set up the frame
        setTitle("Nurse Certification Tracker");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create and set up components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        
        JLabel lblName = new JLabel("Nurse Name:");
        txtName = new JTextField();
        
        JLabel lblCertification = new JLabel("Certification:");
        txtCertification = new JTextField();
        
        JLabel lblCertDate = new JLabel("Certification Date (MM/dd/yyyy):");
        txtCertDate = new JTextField();
        
        JLabel lblExpiryDate = new JLabel("Expiry Date (MM/dd/yyyy):");
        txtExpiryDate = new JTextField();
        
        JButton btnAdd = new JButton("Add Nurse");
        
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblCertification);
        panel.add(txtCertification);
        panel.add(lblCertDate);
        panel.add(txtCertDate);
        panel.add(lblExpiryDate);
        panel.add(txtExpiryDate);
        panel.add(btnAdd);
        
        // Add panel to frame
        add(panel, BorderLayout.NORTH);
        
        // Set up the table model for displaying nurse certifications
        String[] columnNames = {"Nurse Name", "Certification", "Certification Date", "Expiry Date", "Status"};
        model = new DefaultTableModel(columnNames, 0);
        nurseTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(nurseTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add action listener for Add Nurse button
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNurseCertification();
            }
        });
    }
    
    // Method to add nurse details and certification to table
    private void addNurseCertification() {
        String name = txtName.getText();
        String certification = txtCertification.getText();
        String certDateStr = txtCertDate.getText();
        String expiryDateStr = txtExpiryDate.getText();
        
        try {
            // Parse the date fields
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date certDate = sdf.parse(certDateStr);
            Date expiryDate = sdf.parse(expiryDateStr);
            
            // Check if the certification is about to expire within 30 days
            String status = isCertificationExpiring(expiryDate) ? "Expiring Soon" : "Valid";
            
            // Add data to the table
            model.addRow(new Object[]{name, certification, certDate, expiryDate, status});
            
            // Clear text fields
            txtName.setText("");
            txtCertification.setText("");
            txtCertDate.setText("");
            txtExpiryDate.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Invalid input. Please ensure dates are in MM/dd/yyyy format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Method to check if certification is expiring within the next 30 days
    private boolean isCertificationExpiring(Date expiryDate) {
        Date currentDate = new Date();
        long diffInMillis = expiryDate.getTime() - currentDate.getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
        
        return diffInDays <= 30 && diffInDays >= 0;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NurseCertificationTracker().setVisible(true);
            }
        });
    }
}

