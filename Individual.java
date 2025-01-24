import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// The Individual class
public class Individual extends JFrame implements ActionListener {
    private IndividualPolicy policy;

    // Declare GUI components
    private JTextField typeField, polnumField, cidField, numOfYearsField, startDateField, expiryDateField, nextPremDateField, premAmtField, coverField;
    private JTextArea resultArea;

    private JButton showDetailsButton, payPremiumButton, claimButton;

    public Individual() {
        super("Individual Policy");

        policy = new IndividualPolicy();

        // Set layout to GridBagLayout for better positioning
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize components
        typeField = new JTextField(15);
        polnumField = new JTextField(15);
        cidField = new JTextField(15);
        numOfYearsField = new JTextField(15);
        startDateField = new JTextField(15);
        expiryDateField = new JTextField(15);
        nextPremDateField = new JTextField(15);
        premAmtField = new JTextField(15);
        coverField = new JTextField(15);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        showDetailsButton = new JButton("Show Policy Details");
        showDetailsButton.addActionListener(this);

        payPremiumButton = new JButton("Pay Premium");
        payPremiumButton.addActionListener(this);

        claimButton = new JButton("Claim Amount");
        claimButton.addActionListener(this);

        // Add components to the frame
        int row = 0;

        addLabelAndField("Policy Type:", typeField, gbc, row++);
        addLabelAndField("Policy Number:", polnumField, gbc, row++);
        addLabelAndField("Company ID:", cidField, gbc, row++);
        addLabelAndField("Number of Years:", numOfYearsField, gbc, row++);
        addLabelAndField("Start Date (dd/MM/yyyy):", startDateField, gbc, row++);
        addLabelAndField("Expiry Date (dd/MM/yyyy):", expiryDateField, gbc, row++);
        addLabelAndField("Next Premium Date (dd/MM/yyyy):", nextPremDateField, gbc, row++);
        addLabelAndField("Premium Amount:", premAmtField, gbc, row++);
        addLabelAndField("Cover Amount:", coverField, gbc, row++);

        // Add buttons
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(showDetailsButton);
        buttonPanel.add(payPremiumButton);
        buttonPanel.add(claimButton);
        add(buttonPanel, gbc);

        // Add result area
        gbc.gridy = ++row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        // Set frame properties
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addLabelAndField(String labelText, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(field, gbc);
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

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
        } catch (NumberFormatException | ParseException ex) {
            resultArea.setText("Error: Please enter valid input values.\n" + ex.getMessage());
        }
    }

    private void payPremium() {
        if (policy.tot_prem > 0) {
            policy.payprem();
            resultArea.append("\nPremium Paid! Remaining Premium: " + policy.tot_prem + "\n");
        } else {
            resultArea.append("\nNo premium left to pay!\n");
        }
    }

    private void claimAmount() {
        try {
            long claimAmt = Long.parseLong(JOptionPane.showInputDialog("Enter claim amount:"));
            if (policy.cover >= claimAmt) {
                policy.claim(claimAmt);
                resultArea.append("\nClaim processed. Remaining cover: " + policy.cover + "\n");
            } else {
                resultArea.append("\nClaim amount exceeds the remaining cover!\n");
            }
        } catch (NumberFormatException ex) {
            resultArea.setText("Error: Please enter a valid claim amount.");
        }
    }

    public static void main(String[] args) {
        new Individual();
    }
}

// The IndividualPolicy class
class IndividualPolicy {
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
        tot_prem = prem_amt * num_of_years;
    }

    public void payprem() {
        if (tot_prem >= prem_amt) {
            tot_prem -= prem_amt;
        }
    }

    public void claim(long claimAmt) {
        if (cover >= claimAmt) {
            cover -= claimAmt;
        }
    }
}
