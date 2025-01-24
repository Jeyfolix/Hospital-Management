import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class NurseReportViewer extends JFrame implements ActionListener {
    // UI Components
    JTextField tfNurseNo;
    JButton btnMonthlyReport, btnWeeklyReport, btnAggregateMonthlyReport, btnBack;
    JTable reportTable;
    JLabel lblErrorMessage;
    Connection conn;

    NurseReportViewer() {
        // Initialize the database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3309/Hosipital", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setSize(600, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel nurseNoLabel = new JLabel("Nurse Number:");
        nurseNoLabel.setBounds(20, 20, 150, 30);
        nurseNoLabel.setFont(new Font("serif", Font.BOLD, 15));
        add(nurseNoLabel);

        tfNurseNo = new JTextField();
        tfNurseNo.setBounds(150, 20, 150, 30);
        add(tfNurseNo);

        btnWeeklyReport = new JButton("Get Weekly Report");
        btnWeeklyReport.setBounds(20, 60, 150, 30);
        btnWeeklyReport.addActionListener(this);
        add(btnWeeklyReport);

        btnMonthlyReport = new JButton("Get Monthly Report");
        btnMonthlyReport.setBounds(200, 60, 150, 30);
        btnMonthlyReport.addActionListener(this);
        add(btnMonthlyReport);

        btnAggregateMonthlyReport = new JButton("Aggregate Monthly Report");
        btnAggregateMonthlyReport.setBounds(380, 60, 200, 30);
        btnAggregateMonthlyReport.addActionListener(this);
        add(btnAggregateMonthlyReport);

        btnBack = new JButton("Back");
        btnBack.setBounds(20, 420, 100, 30);
        btnBack.addActionListener(this);
        add(btnBack);

        lblErrorMessage = new JLabel();
        lblErrorMessage.setBounds(20, 100, 500, 30);
        lblErrorMessage.setFont(new Font("serif", Font.BOLD, 14));
        lblErrorMessage.setForeground(Color.RED);
        add(lblErrorMessage);

        reportTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBounds(20, 140, 560, 260);
        add(scrollPane);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnWeeklyReport) {
            fetchReport("WEEK");
        } else if (e.getSource() == btnMonthlyReport) {
            fetchReport("MONTH");
        } else if (e.getSource() == btnAggregateMonthlyReport) {
            aggregateMonthlyReport();
        } else if (e.getSource() == btnBack) {
            this.dispose();
            new WeeklyReportForm(); // Assuming you want to go back to the main form
        }
    }

    private boolean verifyPatientNumber(String nurseNo) {
        // Check if the nurse number exists in the database
        String query = "SELECT * FROM nurses WHERE nurse_no = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nurseNo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Return true if nurse number exists
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblErrorMessage.setText("Error verifying nurse number.");
            return false;
        }
    }

    private void fetchReport(String period) {
        String nurseNo = tfNurseNo.getText();
        if (nurseNo.isEmpty()) {
            lblErrorMessage.setText("Please enter a nurse number.");
            return;
        }

        // Verify nurse number exists
        if (!verifyPatientNumber(nurseNo)) {
            lblErrorMessage.setText("Nurse number does not exist.");
            return;
        }

        String query;
        if (period.equals("WEEK")) {
            query = "SELECT * FROM weekly_reports WHERE nurse_no = ? AND report_date >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)";
        } else { // MONTH
            query = "SELECT * FROM weekly_reports WHERE nurse_no = ? AND MONTH(report_date) = MONTH(CURDATE()) AND YEAR(report_date) = YEAR(CURDATE())";
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nurseNo);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Nurse No");
            model.addColumn("Patients Allocated");
            model.addColumn("Medication");
            model.addColumn("Patients Released");
            model.addColumn("Patients Lost");
            model.addColumn("Shift");
            model.addColumn("Tools Used");
            model.addColumn("Resources Used");
            model.addColumn("Report Date");

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getString("nurse_no"),
                    rs.getInt("patients_allocated"),
                    rs.getString("medication"),
                    rs.getInt("patients_released"),
                    rs.getInt("patients_lost"),
                    rs.getString("shift"),
                    rs.getString("tools_used"),
                    rs.getString("resources_used"),
                    rs.getDate("report_date")
                });
            }

            reportTable.setModel(model);
            lblErrorMessage.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblErrorMessage.setText("Error fetching report.");
        }
    }

    private void aggregateMonthlyReport() {
        String nurseNo = tfNurseNo.getText();
        if (nurseNo.isEmpty()) {
            lblErrorMessage.setText("Please enter a nurse number.");
            return;
        }

        // Verify nurse number exists
        if (!verifyPatientNumber(nurseNo)) {
            lblErrorMessage.setText("Nurse number does not exist.");
            return;
        }

        String query = "SELECT nurse_no, SUM(patients_allocated) AS total_allocated, SUM(patients_released) AS total_released, " +
                       "SUM(patients_lost) AS total_lost FROM weekly_reports " +
                       "WHERE nurse_no = ? AND MONTH(report_date) = MONTH(CURDATE()) AND YEAR(report_date) = YEAR(CURDATE()) " +
                       "GROUP BY nurse_no";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nurseNo);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Nurse No");
            model.addColumn("Total Patients Allocated");
            model.addColumn("Total Patients Released");
            model.addColumn("Total Patients Lost");

            if (rs.next()) {
                model.addRow(new Object[] {
                    rs.getString("nurse_no"),
                    rs.getInt("total_allocated"),
                    rs.getInt("total_released"),
                    rs.getInt("total_lost")
                });
            } else {
                lblErrorMessage.setText("No reports found for this nurse this month.");
            }

            reportTable.setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblErrorMessage.setText("Error aggregating monthly report.");
        }
    }

    public static void main(String[] args) {
        new NurseReportViewer();
    }
}
