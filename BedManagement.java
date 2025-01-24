import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BedManagement {

    private Connection conn;
    private JTable bedTable;
    private DefaultTableModel tableModel;

    public BedManagement() {
        // Initialize the database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the main frame for the GUI
        JFrame frame = new JFrame("Hospital Room and Bed Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);  // Disable window resizing (maximization)
        
        // Create the table to display room and bed information
        String[] columnNames = {"Room Number", "Room Name", "Bed Number"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bedTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(bedTable);

        // Populate the table with data from the database
        refreshTable();

        // Admin panel for adding rooms and beds
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField roomNumberField = new JTextField();
        JTextField roomNameField = new JTextField(); // For naming the room (e.g., ICU, General)
        JTextField numBedsField = new JTextField();
        JButton addRoomButton = new JButton("Add Room");
        JButton deleteBedButton = new JButton("Delete Bed");

        addRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNumber = roomNumberField.getText();
                String roomName = roomNameField.getText();
                int numBeds = Integer.parseInt(numBedsField.getText());
                addRoomAndBeds(roomNumber, roomName, numBeds);
            }
        });

        deleteBedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedBed();
            }
        });

        adminPanel.add(new JLabel("Room Number:"));
        adminPanel.add(roomNumberField);
        adminPanel.add(new JLabel("Room Name:"));
        adminPanel.add(roomNameField); // Input field for room name (ICU, General)
        adminPanel.add(new JLabel("Number of Beds:"));
        adminPanel.add(numBedsField);
        adminPanel.add(addRoomButton);
        adminPanel.add(deleteBedButton);

        // Add components to the frame
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(adminPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void refreshTable() {
        // Refresh the table to display rooms and beds
        tableModel.setRowCount(0);
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT r.room_number, r.room_name, b.bed_number FROM rooms r JOIN beds b ON r.room_number = b.room_number");
            while (rs.next()) {
                String roomNumber = rs.getString("room_number");
                String roomName = rs.getString("room_name");
                int bedNumber = rs.getInt("bed_number");
                tableModel.addRow(new Object[]{roomNumber, roomName, bedNumber});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRoomAndBeds(String roomNumber, String roomName, int numBeds) {
        // Add room and beds to the database
        try {
            // Insert room into the 'rooms' table with a name (e.g., ICU, General)
            String roomQuery = "INSERT INTO rooms (room_number, room_name) VALUES (?, ?)";
            PreparedStatement roomStmt = conn.prepareStatement(roomQuery);
            roomStmt.setString(1, roomNumber);
            roomStmt.setString(2, roomName);
            roomStmt.executeUpdate();

            // Insert beds into the 'beds' table for the room
            for (int i = 1; i <= numBeds; i++) {
                String bedQuery = "INSERT INTO beds (room_number, bed_number) VALUES (?, ?)";
                PreparedStatement bedStmt = conn.prepareStatement(bedQuery);
                bedStmt.setString(1, roomNumber);
                bedStmt.setInt(2, i);
                bedStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Room and beds added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable(); // Refresh the table to show the new room and beds

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBed() {
        // Get the selected row in the table
        int selectedRow = bedTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a bed to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the room number and bed number from the selected row
        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        int bedNumber = (Integer) tableModel.getValueAt(selectedRow, 2);

        try {
            // Delete the selected bed from the 'beds' table
            String deleteQuery = "DELETE FROM beds WHERE room_number = ? AND bed_number = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setString(1, roomNumber);
            deleteStmt.setInt(2, bedNumber);
            deleteStmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Bed deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable(); // Refresh the table to reflect the deletion

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting bed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BedManagement());
    }
}
