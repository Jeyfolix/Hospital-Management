import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Main class for Employee Policy
public class Employpol extends JFrame implements ActionListener {
    private static EmployeePolicy policy = new EmployeePolicy();

    // Declare GUI components
    private JTextField typeField, polnumField, cidField, numOfYearsField, startDateField, expiryDateField, nextPremDateField, premAmtField, coverField;
    private JTextArea resultArea;

    // Declare buttons
    private JButton showDetailsButton, payPremiumButton, claimButton;

    public Employpol() {
        // Set up JFrame
        super("Employee Policy");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Initialize the fields
        typeField = new JTextField(20);
        polnumField = new JTextField(20);
        cidField = new JTextField(20);
        numOfYearsField = new JTextField(20);
        startDateField = new JTextField(20);
        expiryDateField = new JTextField(20);
        nextPremDateField = new JTextField(20);
        premAmtField = new JTextField(20);
        coverField = new JTextField(20);

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);

        // Initialize buttons and add action listeners
        showDetailsButton = new JButton("Show Policy Details");
        showDetailsButton.addActionListener(this);

        payPremiumButton = new JButton("Pay Premium");
        payPremiumButton.addActionListener(this);

        claimButton = new JButton("Claim Amount");
        claimButton.addActionListener(this);

        // Add components to JFrame
        add(new JLabel("Policy Type:"));
        add(typeField);
        add(new JLabel("Policy Number:"));
        add(polnumField);
        add(new JLabel("Company ID:"));
        add(cidField);
        add(new JLabel("Number of Years:"));
        add(numOfYearsField);
        add(new JLabel("Start Date:"));
        add(startDateField);
        add(new JLabel("Expiry Date:"));
        add(expiryDateField);
        add(new JLabel("Next Premium Date:"));
        add(nextPremDateField);
        add(new JLabel("Premium Amount:"));
        add(premAmtField);
        add(new JLabel("Cover Amount:"));
        add(coverField);

        add(showDetailsButton);
        add(payPremiumButton);
        add(claimButton);
        add(new JScrollPane(resultArea));

        // Set frame properties
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == showDetailsButton) {
            showPolicyDetails();
        } else if (source == payPremiumButton) {
            payPremium();
        } else if (source == claimButton) {
            claimAmount();
        }
    }

    private void showPolicyDetails() {
        try {
            policy.type = typeField.getText();
            policy.polnum = Integer.parseInt(polnumField.getText());
            policy.cid = Integer.parseInt(cidField.getText());
            policy.num_of_years = Integer.parseInt(numOfYearsField.getText());

            // Parsing dates using SimpleDateFormat
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            policy.start_date = sdf.parse(startDateField.getText());
            policy.expiry_date = sdf.parse(expiryDateField.getText());
            policy.next_prem_date = sdf.parse(nextPremDateField.getText());

            policy.prem_amt = Long.parseLong(premAmtField.getText());
            policy.cover = Long.parseLong(coverField.getText());

            policy.calc_totprem();

            resultArea.setText("Policy Details:\n");
            resultArea.append("Policy ID: " + policy.polnum + "\n");
            resultArea.append("Type: " + policy.type + "\n");
            resultArea.append("Company ID: " + policy.cid + "\n");
            resultArea.append("Number of Years: " + policy.num_of_years + "\n");
            resultArea.append("Start Date: " + policy.start_date + "\n");
            resultArea.append("Expiry Date: " + policy.expiry_date + "\n");
            resultArea.append("Next Premium Date: " + policy.next_prem_date + "\n");
            resultArea.append("Premium Amount: " + policy.prem_amt + "\n");
            resultArea.append("Cover Amount: " + policy.cover + "\n");
            resultArea.append("Total Premium: " + policy.tot_prem + "\n");
        } catch (ParseException ex) {
            resultArea.setText("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private void payPremium() {
        policy.payprem();
        resultArea.append("\nPremium Paid! Remaining Premium: " + policy.tot_prem + "\n");
    }

    private void claimAmount() {
        try {
            long claimAmt = Long.parseLong(JOptionPane.showInputDialog("Enter claim amount:"));
            policy.claim(claimAmt);
            resultArea.append("\nClaim processed. Remaining cover: " + policy.cover + "\n");
        } catch (NumberFormatException ex) {
            resultArea.setText("Invalid claim amount. Please enter a valid number.");
        }
    }

    public static void main(String[] args) {
        new Employpol();
    }
}

// Employee Policy class (Assuming this contains the policy-related logic)
class EmployeePolicy {
    String type;
    int polnum;
    int cid;
    int num_of_years;
    Date start_date;
    Date expiry_date;
    Date next_prem_date;
    long prem_amt;
    long cover;
    long tot_prem;

    public void calc_totprem() {
        // Simple calculation for total premium
        tot_prem = prem_amt * num_of_years;
    }

    public void payprem() {
        // Logic to pay the premium
        tot_prem -= prem_amt;
    }

    public void claim(long claimAmt) {
        // Process the claim
        if (cover >= claimAmt) {
            cover -= claimAmt;
        } else {
            cover = 0;
        }
    }
}
