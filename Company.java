import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Main class for Company Management
public class Company extends JFrame implements ActionListener {

    // GUI Components
    private JTextField cidField, addressField, descField;
    private JTextField pol1Field, pol2Field, pol3Field, cov1Field, cov2Field, cov3Field;
    private JTextArea resultArea;
    private JButton addCustomerButton, claimButton, viewPolicyButton, payPremiumButton;
    
    private CompanyDetails companyObj;
    private Customer customerObj;

    public Company() {
        // Set up the JFrame
        setTitle("Company Management");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Create the fields for company details
        cidField = new JTextField(20);
        addressField = new JTextField(20);
        descField = new JTextField(20);
        pol1Field = new JTextField(20);
        pol2Field = new JTextField(20);
        pol3Field = new JTextField(20);
        cov1Field = new JTextField(20);
        cov2Field = new JTextField(20);
        cov3Field = new JTextField(20);

        // Result Area for displaying output
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);

        // Buttons for actions
        addCustomerButton = new JButton("Add Customer");
        addCustomerButton.addActionListener(this);

        claimButton = new JButton("Claim");
        claimButton.addActionListener(this);

        viewPolicyButton = new JButton("View Policy Details");
        viewPolicyButton.addActionListener(this);

        payPremiumButton = new JButton("Pay Premium");
        payPremiumButton.addActionListener(this);

        // Add components to JFrame
        add(new JLabel("Company ID:"));
        add(cidField);
        add(new JLabel("Company Address:"));
        add(addressField);
        add(new JLabel("Company Description:"));
        add(descField);
        add(new JLabel("Policy 1 Name:"));
        add(pol1Field);
        add(new JLabel("Policy 2 Name:"));
        add(pol2Field);
        add(new JLabel("Policy 3 Name:"));
        add(pol3Field);
        add(new JLabel("Policy 1 Coverage:"));
        add(cov1Field);
        add(new JLabel("Policy 2 Coverage:"));
        add(cov2Field);
        add(new JLabel("Policy 3 Coverage:"));
        add(cov3Field);

        add(addCustomerButton);
        add(claimButton);
        add(viewPolicyButton);
        add(payPremiumButton);
        add(new JScrollPane(resultArea));

        // Set frame properties
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == addCustomerButton) {
            addCustomer();
        } else if (source == claimButton) {
            claim();
        } else if (source == viewPolicyButton) {
            viewPolicyDetails();
        } else if (source == payPremiumButton) {
            payPremium();
        }
    }

    // Create the company object with the data from the GUI
    private void createCompany() {
        int cid = Integer.parseInt(cidField.getText());
        String address = addressField.getText();
        String description = descField.getText();
        String pol1 = pol1Field.getText();
        String pol2 = pol2Field.getText();
        String pol3 = pol3Field.getText();
        long cov1 = Long.parseLong(cov1Field.getText());
        long cov2 = Long.parseLong(cov2Field.getText());
        long cov3 = Long.parseLong(cov3Field.getText());
        long p1 = 1000, p2 = 1500, p3 = 2000; // Sample premiums for policies

        companyObj = new CompanyDetails(cid, address, description, pol1, pol2, pol3, cov1, cov2, cov3, p1, p2, p3);
        resultArea.setText("Company created successfully!");
    }

    // Add a customer to the company
    private void addCustomer() {
        if (companyObj == null) {
            resultArea.setText("Please create a company first!");
            return;
        }

        // Create customer
        customerObj = new Customer();
        customerObj.add(); // Assume this method gathers the necessary customer data
        
        // Add the customer to the company
        companyObj.addCustomer(customerObj);
        resultArea.setText("Customer added successfully to company!");
    }

    // Claim a policy for a customer
    private void claim() {
        if (companyObj == null || customerObj == null) {
            resultArea.setText("Please create a company and add a customer first!");
            return;
        }

        long claimAmount = Long.parseLong(JOptionPane.showInputDialog(this, "Enter Claim Amount"));

        companyObj.claim(customerObj, claimAmount);
        resultArea.setText("Claim processed.");
    }

    // View the policy details for the customer
    private void viewPolicyDetails() {
        if (companyObj == null || customerObj == null) {
            resultArea.setText("Please create a company and add a customer first!");
            return;
        }

        companyObj.showPolicyDetails(customerObj);
        resultArea.setText("Policy details viewed.");
    }

    // Pay premium for the customer's policy
    private void payPremium() {
        if (companyObj == null || customerObj == null) {
            resultArea.setText("Please create a company and add a customer first!");
            return;
        }

        companyObj.payPremium(customerObj);
        resultArea.setText("Premium paid.");
    }

    public static void main(String[] args) {
        new Company();
    }
}

// Class for Company Details
class CompanyDetails {
    private int companyId;
    private String address, description;
    private String policy1, policy2, policy3;
    private long coverage1, coverage2, coverage3;
    private long premium1, premium2, premium3;
    private List<Customer> customers;

    public CompanyDetails(int companyId, String address, String description, String policy1, String policy2, String policy3,
                          long coverage1, long coverage2, long coverage3, long premium1, long premium2, long premium3) {
        this.companyId = companyId;
        this.address = address;
        this.description = description;
        this.policy1 = policy1;
        this.policy2 = policy2;
        this.policy3 = policy3;
        this.coverage1 = coverage1;
        this.coverage2 = coverage2;
        this.coverage3 = coverage3;
        this.premium1 = premium1;
        this.premium2 = premium2;
        this.premium3 = premium3;
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void claim(Customer customer, long claimAmount) {
        // Process the claim
        System.out.println("Claim processed for customer: " + customer.getName() + " Amount: " + claimAmount);
    }

    public void showPolicyDetails(Customer customer) {
        // Display policy details
        System.out.println("Policy details for customer: " + customer.getName());
    }

    public void payPremium(Customer customer) {
        // Process premium payment
        System.out.println("Premium paid for customer: " + customer.getName());
    }
}

// Class for Customer
class Customer {
    private String name;

    public void add() {
        // Assume this method gathers the necessary customer data
        name = JOptionPane.showInputDialog("Enter customer name");
    }

    public String getName() {
        return name;
    }
}
