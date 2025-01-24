import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends JFrame implements Runnable {

    Thread t;
    private Image backgroundImage;

    Welcome() {
        backgroundImage = new ImageIcon("C:\\Users\\User\\Documents\\Downloads\\Desktop\\zoshua-colah-FjHLx1w41lY-unsplash.jpg").getImage(); // Ensure the path is correct

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new ImagePanel());

        setLayout(null);
        t = new Thread(this);
        t.start();

        // Create and configure the welcome button
        JButton welcomeButton = new JButton("Welcome");
        welcomeButton.setBounds(350, 250, 100, 40); // Set button location and size
        welcomeButton.setFont(new Font("Arial", Font.BOLD, 14)); // Optional: Set button font
        welcomeButton.setBackground(Color.CYAN); // Optional: Set button color
        welcomeButton.setForeground(Color.BLACK); // Optional: Set button text color
        welcomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openHosipitalDashboard(); // Open Hospital Dashboard on button click
            }
        });
        add(welcomeButton); // Add button to the frame

        setVisible(true);
        animateWindow();
    }

    private class ImagePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void animateWindow() {
        int x = 1;
        for (int i = 2; i <= 600; i += 4, x += 1) {
            setLocation(600 - ((i + x) / 2), 350 - (i / 2));
            setSize(i + 3 * x, i + x / 2);

            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            Thread.sleep(7000);
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to open HospitalDashboard
    private void openHosipitalDashboard() {
        new HosipitalDashboard(); // Assuming you have a HospitalDashboard class
        dispose(); // Close the current Welcome window
    }

    public static void main(String[] args) {
        new Welcome();
    }
}
