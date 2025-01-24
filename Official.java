import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Official extends JFrame implements ActionListener {

    private static OfficialClass officialObj = new OfficialClass("001", 1000, 2000, 1500, 1200, 1300, 1400);
    private static Policy[] policies = new Policy[100];
    private static Customer[] customers = new Customer[100];
    private static int policyCount = 0;
    
    // Declare GUI components
    private JTextField typeField, numOfYearsField, startDateField, claimAmountField;
    private JTextArea resultArea;
    private JButton addPolicyButton, showPolicyDetailsButton, payPremiumButton, claimButton, removeInvalidButton;

    public Official() {
        // Set up JFrame
        super("Official Policy Management");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Initialize the fields
        typeField = new JTextField(20);
        numOfYearsField = new JTextField(20);
        startDateField = new JTextField(20);
        claimAmountField = new JTextField(20);

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);

        // Initialize buttons and add action listeners
        addPolicyButton = new JButton("Add Policy");
        addPolicyButton.addActionListener(this);

        showPolicyDetailsButton = new JButton("Show Policy Details");
        showPolicyDetailsButton.addActionListener(this);

        payPremiumButton = new JButton("Pay Premium");
        payPremiumButton.addActionListener(this);

        claimButton = new JButton("Claim Amount");
        claimButton.addActionListener(this);

        removeInvalidButton = new JButton("Remove Invalid Policies");
        removeInvalidButton.addActionListener(this);

        // Add components to JFrame
        add(new JLabel("Policy Type (individual, family, employee):"));
        add(typeField);
        add(new JLabel("Number of Years:"));
        add(numOfYearsField);
        add(new JLabel("Start Date (dd/mm/yyyy):"));
        add(startDateField);
        add(new JLabel("Claim Amount:"));
        add(claimAmountField);

        add(addPolicyButton);
        add(showPolicyDetailsButton);
        add(payPremiumButton);
        add(claimButton);
        add(removeInvalidButton);
        add(new JScrollPane(resultArea));

        // Set frame properties
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == addPolicyButton) {
            addPolicy();
        } else if (source == showPolicyDetailsButton) {
            showPolicyDetails();
        } else if (source == payPremiumButton) {
            payPremium();
        } else if (source == claimButton) {
            claimAmount();
        } else if (source == removeInvalidButton) {
            removeInvalidPolicies();
        }
    }

    private void addPolicy() {
        String type = typeField.getText();
        int numOfYears = Integer.parseInt(numOfYearsField.getText());
        String startDate = startDateField.getText();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDateParsed = null;
        try {
            startDateParsed = sdf.parse(startDate);
        } catch (ParseException e) {
            resultArea.setText("Invalid date format! Please use dd/mm/yyyy.");
            return;
        }

        // Example of adding a policy (mocking customer ID 1001)
        String result = officialObj.addPolicy(customers[policyCount], policies, customers, 1001); 
        resultArea.setText("Policy Added!\n" + result);
        policyCount++;
    }

    private void showPolicyDetails() {
        if (policyCount > 0) {
            resultArea.setText("Showing Policy Details:\n");
            policies[0].show();  // Show the first policy's details for demonstration
        } else {
            resultArea.setText("No policies to display.");
        }
    }

    private void payPremium() {
        if (policyCount > 0) {
            officialObj.payPremium(policies[0]);  // Example paying premium for the first policy
            resultArea.setText("Premium paid for the policy.");
        } else {
            resultArea.setText("No policies to pay premium for.");
        }
    }

    private void claimAmount() {
        if (policyCount > 0) {
            try {
                long claimAmount = Long.parseLong(claimAmountField.getText());
                officialObj.claim(policies[0], claimAmount);  // Claim for the first policy
                resultArea.setText("Claim processed for the policy.");
            } catch (NumberFormatException ex) {
                resultArea.setText("Invalid claim amount.");
            }
        } else {
            resultArea.setText("No policies to claim from.");
        }
    }

    private void removeInvalidPolicies() {
        officialObj.removeInvalidPolicies(policies);  // Remove invalid policies
        resultArea.setText("Invalid policies removed.");
    }

    public static void main(String[] args) {
        new Official();
    }
}

// Mock OfficialClass to simulate the official logic
class OfficialClass {
    String companyId;
    long revenue, expenses, profit, premiumCollected, premiumPaid, totalClaimed;

    public OfficialClass(String companyId, long revenue, long expenses, long profit, long premiumCollected, long premiumPaid, long totalClaimed) {
        this.companyId = companyId;
        this.revenue = revenue;
        this.expenses = expenses;
        this.profit = profit;
        this.premiumCollected = premiumCollected;
        this.premiumPaid = premiumPaid;
        this.totalClaimed = totalClaimed;
    }

    public String addPolicy(Customer customer, Policy[] policies, Customer[] customers, int policyId) {
        // Mock policy addition
        policies[policyId] = new Policy("Employee", policyId, customer);
        return "Policy added with ID: " + policyId;
    }

    public void payPremium(Policy policy) {
        policy.payPremium();
    }

    public void claim(Policy policy, long claimAmount) {
        policy.processClaim(claimAmount);
    }

    public void removeInvalidPolicies(Policy[] policies) {
        // Mock invalid policy removal
        for (int i = 0; i < policies.length; i++) {
            if (policies[i] != null && !policies[i].isValid()) {
                policies[i] = null;
            }
        }
    }
}

// Mock Policy class
class Policy {
    String type;
    int policyId;
    Customer customer;
    long premiumAmount;
    long coverAmount;

    public Policy(String type, int policyId, Customer customer) {
        this.type = type;
        this.policyId = policyId;
        this.customer = customer;
        this.premiumAmount = 1000;
        this.coverAmount = 5000;
    }

    public void show() {
        System.out.println("Policy ID: " + policyId + ", Type: " + type + ", Customer ID: " + customer.id);
    }

    public void payPremium() {
        premiumAmount -= 500;
    }

    public void processClaim(long claimAmount) {
        if (claimAmount <= coverAmount) {
            coverAmount -= claimAmount;
        }
    }

    public boolean isValid() {
        return coverAmount > 0;
    }
}

// Mock Customer class
class Customer {
    int id;
    String name;

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
