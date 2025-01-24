import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

public class Login extends JFrame implements ActionListener {
    JTextField tfuser;
    JPasswordField fpass;

    // Database Connection Details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3309/Hosipital"; // Adjust port and database name
    private static final String USERNAME = "root"; // MySQL username
    private static final String PASSWORD = ""; // MySQL password

    JButton login, cancel;

    Login() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel username = new JLabel("Username");
        username.setBounds(40, 20, 100, 20);
        add(username);

        tfuser = new JTextField();
        tfuser.setBounds(150, 20, 150, 20);
        add(tfuser);

        JLabel pass = new JLabel("Password");
        pass.setBounds(40, 70, 100, 20);
        add(pass);

        fpass = new JPasswordField();
        fpass.setBounds(150, 70, 150, 20);
        add(fpass);

        login = new JButton("Login");
        login.setBounds(40, 140, 120, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Tahoma", Font.BOLD, 16));
        login.addActionListener(this);
        add(login);

        cancel = new JButton("Cancel");
        cancel.setBounds(180, 140, 120, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 16));
        cancel.addActionListener(this);
        add(cancel);

        setSize(600, 300);
        setLocation(500, 250);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String enteredUsername = tfuser.getText();
            String enteredPassword = new String(fpass.getPassword());

            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                // Query to check login credentials along with their role
                String checkUserQuery = "SELECT password, role FROM loginn WHERE username = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(checkUserQuery)) {
                    preparedStatement.setString(1, enteredUsername);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        String role = resultSet.getString("role");

                        if (storedPassword.equals(enteredPassword)) {
                            // Login successful, open the corresponding window based on the role
                            JOptionPane.showMessageDialog(
                                null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE
                            );
                            setVisible(false); // Close the login window

                            // Open different windows based on the user's role
                            if (role.equals("")) {
                               // new AdminDashboard();  // Open Admin Dashboard
                            } else if (role.equals("nurse")) {
                                new NurseManagement();  // Open Nurse Management
                            } else if (role.equals("admin")) {
                                new PatientManagements();  // Open Patient Management
                            } else {
                                JOptionPane.showMessageDialog(null, "Role not recognized!", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            // Incorrect password
                            JOptionPane.showMessageDialog(
                                null, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } else {
                        // Username not found
                        JOptionPane.showMessageDialog(
                            null, "Username not registered!", "Error", JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    null, "Database connection error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        } else if (ae.getSource() == cancel) {
            setVisible(false); // Close the application
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}

