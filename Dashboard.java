import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private Image background;

    public Dashboard() {
        background = new ImageIcon("C:\\Users\\User\\Documents\\Downloads\\Desktop\\jey Image.jpg").getImage(); // Change the path accordingl


        // Frame settingsd
        setTitle("Hosipital Management System");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //setContentPane(new ImagePanel());

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        // Sidebar panel
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(30, 50, 100));
        sidebar.setPreferredSize(new Dimension(150, 0));

        // Dashboard
        addMenuItem(sidebar, "Dashboard", "Patient");

        // Academics section
        addSectionTitle(sidebar, "STAFFS");
        addMenuItem(sidebar, "Nurse");
        addMenuItem(sidebar, "Doctor");
        addMenuItem(sidebar, "Technician");
        addMenuItem(sidebar, "Pharmacist");

        // Financials section
        addSectionTitle(sidebar, "FINANCIALS");
        addMenuItem(sidebar, "Billing and Invoice");
        addMenuItem(sidebar, "Insurance");
        addMenuItem(sidebar, "Report Analysis");
        addMenuItem(sidebar, "Donation");

        // Accommodation section
        addSectionTitle(sidebar, "ACOMMODATION");
        addMenuItem(sidebar, "Bed Management");

        // Examination section
        addSectionTitle(sidebar, "HUMAN REASOURCE");
        addMenuItem(sidebar, "Resource Management");

        // Settings section
        addSectionTitle(sidebar, "SETTINGS");
        addMenuItem(sidebar, "Change Password");


        JPanel titlePanel = new ImagePanel();
       // JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Hospital Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(titlePanel, BorderLayout.CENTER);


        getContentPane().add(mainPanel);
    }

    private void addSectionTitle(JPanel panel, String title) {
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setForeground(Color.CYAN);
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 14));
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        panel.add(sectionTitle);
    }

    private void addMenuItem(JPanel panel, String itemName) {
        JButton button = new JButton(itemName);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBackground(new Color(30, 50, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));


        if (itemName.equals("Patient")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new PatientManagements().setVisible(true); // Open PatientManagement window
                }
            });
        } else {

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "You clicked: " + itemName);
            }
        });
    }

        panel.add(button);
    }

    private void addMenuItem(JPanel panel, String sectionName, String itemName) {
        addSectionTitle(panel, sectionName);
        addMenuItem(panel, itemName);
    }

    private class ImagePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 100); // Adjust height as needed
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }
}
