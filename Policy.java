import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Policy extends JFrame implements ActionListener {
    private JTextField dateField, monthsField, yearsField, resultField;
    private JButton addMonthsButton, addYearsButton;
    private PolicyExample policy;

    public Policy() {
        // JFrame properties
        setTitle("Policy Management System");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        // Labels and input fields
        add(new JLabel("Enter Date (yyyy-MM-dd):"));
        dateField = new JTextField();
        add(dateField);

        add(new JLabel("Add Months:"));
        monthsField = new JTextField();
        add(monthsField);

        add(new JLabel("Add Years:"));
        yearsField = new JTextField();
        add(yearsField);

        add(new JLabel("Resulting Date:"));
        resultField = new JTextField();
        resultField.setEditable(false);
        add(resultField);

        // Buttons
        addMonthsButton = new JButton("Add Months");
        addMonthsButton.addActionListener(this);
        add(addMonthsButton);

        addYearsButton = new JButton("Add Years");
        addYearsButton.addActionListener(this);
        add(addYearsButton);

        // Initialize a sample policy
        policy = new PolicyExample(1, 500000, "Life", 101, 5, new Date(), 10000);
    }

    // Action listener for buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Parse the date input
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateField.getText());

            if (e.getSource() == addMonthsButton) {
                int numMonths = Integer.parseInt(monthsField.getText());
                addMonths(date, numMonths);
            } else if (e.getSource() == addYearsButton) {
                int numYears = Integer.parseInt(yearsField.getText());
                addYears(date, numYears);
            }

            // Display the result
            SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy-MM-dd");
            resultField.setText(resultFormat.format(date));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please try again.");
        }
    }

    // Method to add months to a date
    public void addMonths(Date date, int numMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, numMonths);
        date.setTime(calendar.getTimeInMillis());
    }

    // Method to add years to a date
    public void addYears(Date date, int numYears) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, numYears);
        date.setTime(calendar.getTimeInMillis());
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Policy frame = new Policy();
            frame.setVisible(true);
        });
    }
}

// Abstract base class for policies
abstract class AbstractPolicy {
    abstract int getcid();
    abstract String getpolnum();
    abstract int getnum_of_years();
    abstract Date getnpd();
    abstract Date getsd();
    abstract Date geted();
    abstract long gettp();
    abstract long getcov();
    abstract String gettype();
    abstract void calc_totprem();
    abstract long getpremamt();
    abstract void setnum_of_years(int num_of_years);
    abstract void setnpd(Date next_prem_date);
    abstract void setcov(long cover);
    abstract void setsd(Date start_date);
    abstract void seted(Date expiry_date);
    abstract void setpremamt(long prem_amt);
    abstract void payprem();
    abstract void show();
    abstract void claim(long amount);
}

// Concrete implementation of the abstract policy class
class PolicyExample extends AbstractPolicy {
    private int cid;
    private long cover;
    private String type;
    private int polnum;
    private int num_of_years;
    private Date next_prem_date, start_date, expiry_date;
    private long prem_amt, tot_prem;

    public PolicyExample(int cid, long cover, String type, int polnum, int num_of_years, Date start_date, long prem_amt) {
        this.cid = cid;
        this.cover = cover;
        this.type = type;
        this.polnum = polnum;
        this.num_of_years = num_of_years;
        this.start_date = start_date;
        this.prem_amt = prem_amt;
    }

    @Override
    int getcid() {
        return cid;
    }

    @Override
    String getpolnum() {
        return String.valueOf(polnum);
    }

    @Override
    int getnum_of_years() {
        return num_of_years;
    }

    @Override
    Date getnpd() {
        return next_prem_date;
    }

    @Override
    Date getsd() {
        return start_date;
    }

    @Override
    Date geted() {
        return expiry_date;
    }

    @Override
    long gettp() {
        return tot_prem;
    }

    @Override
    long getcov() {
        return cover;
    }

    @Override
    String gettype() {
        return type;
    }

    @Override
    void calc_totprem() {
        tot_prem = num_of_years * prem_amt;
    }

    @Override
    long getpremamt() {
        return prem_amt;
    }

    @Override
    void setnum_of_years(int num_of_years) {
        this.num_of_years = num_of_years;
    }

    @Override
    void setnpd(Date next_prem_date) {
        this.next_prem_date = next_prem_date;
    }

    @Override
    void setcov(long cover) {
        this.cover = cover;
    }

    @Override
    void setsd(Date start_date) {
        this.start_date = start_date;
    }

    @Override
    void seted(Date expiry_date) {
        this.expiry_date = expiry_date;
    }

    @Override
    void setpremamt(long prem_amt) {
        this.prem_amt = prem_amt;
    }

    @Override
    void payprem() {
        System.out.println("Premium paid!");
    }

    @Override
    void show() {
        System.out.println("Policy Number: " + polnum);
        System.out.println("Policy Type: " + type);
        System.out.println("Cover Amount: " + cover);
    }

    @Override
    void claim(long amount) {
        if (amount <= cover) {
            cover -= amount;
            System.out.println("Claim approved for amount: " + amount);
        } else {
            System.out.println("Claim amount exceeds cover!");
        }
    }
}
