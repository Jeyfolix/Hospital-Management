import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePassword extends JFrame {
    Connection conn;

    // GUI Components
    JTextField tfUsername;
    JPasswordField pfCurrentPassword, pfNewPassword;
    JButton btnUpdatePassword;
    JComboBox<String> cbRole;

    public ChangePassword() {
        // Initialize database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed.");
            System.exit(1);
        }

        // Set up the GUI
        setTitle("Change Password");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Role Selection
        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(50, 30, 100, 30);
        add(lblRole);

        cbRole = new JComboBox<>(new String[]{"Nurse", "Admin"});
        cbRole.setBounds(150, 30, 150, 30);
        add(cbRole);

        // Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 70, 100, 30);
        add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(150, 70, 150, 30);
        add(tfUsername);

        // Current Password
        JLabel lblCurrentPassword = new JLabel("Current Password:");
        lblCurrentPassword.setBounds(50, 110, 150, 30);
        add(lblCurrentPassword);

        pfCurrentPassword = new JPasswordField();
        pfCurrentPassword.setBounds(150, 110, 150, 30);
        add(pfCurrentPassword);

        // New Password
        JLabel lblNewPassword = new JLabel("New Password:");
        lblNewPassword.setBounds(50, 150, 150, 30);
        add(lblNewPassword);

        pfNewPassword = new JPasswordField();
        pfNewPassword.setBounds(150, 150, 150, 30);
        add(pfNewPassword);

        // Update Password Button
        btnUpdatePassword = new JButton("Update Password");
        btnUpdatePassword.setBounds(100, 200, 200, 30);
        btnUpdatePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePassword();
            }
        });
        add(btnUpdatePassword);

        setVisible(true);
        setLocationRelativeTo(null); // Center the window
    }

    private void updatePassword() {
        String role = cbRole.getSelectedItem().toString();
        String username = tfUsername.getText().trim();
        String currentPassword = new String(pfCurrentPassword.getPassword()).trim();
        String newPassword = new String(pfNewPassword.getPassword()).trim();

        if (username.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.");
            return;
        }

        try {
            // Verify current password and role
            String query = "SELECT * FROM loginn WHERE username = ? AND password = ? AND role = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, currentPassword);
            stmt.setString(3, role);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Update to new password
                String updateQuery = "UPDATE loginn SET password = ? WHERE username = ? AND role = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, username);
                updateStmt.setString(3, role);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Password updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update password.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username, password, or role.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.");
        }
    }

    public static void main(String[] args) {
        new ChangePassword();
    }
}
