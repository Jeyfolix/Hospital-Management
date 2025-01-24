import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Class for Customer Management
public class Customer extends JFrame implements ActionListener {

    // Customer & Family Member instance
    private CustomerDetails customerObj = new CustomerDetails();
    
    // GUI components
    private JTextField nameField, addressField, dobField, mobField, salaryField;
    private JTextField familyNameField, familyAddressField, familyDobField, familyAgeField, familyMobField;
    private JTextArea resultArea;
    private JButton addCustomerButton, addFamilyMemberButton, addPolicyButton, claimButton, payPremiumButton, viewPolicyButton;

    // Constructor to set up the GUI
    public Customer() {
        setTitle("Customer Management");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Customer Info
        nameField = new JTextField(20);
        addressField = new JTextField(20);
        dobField = new JTextField(20);
        mobField = new JTextField(20);
        salaryField = new JTextField(20);

        // Family Member Info
        familyNameField = new JTextField(20);
        familyAddressField = new JTextField(20);
        familyDobField = new JTextField(20);
        familyAgeField = new JTextField(20);
        familyMobField = new JTextField(20);
        
        // Result area for displaying output
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        
        // Buttons for actions
        addCustomerButton = new JButton("Add Customer");
        addCustomerButton.addActionListener(this);

        addFamilyMemberButton = new JButton("Add Family Member");
        addFamilyMemberButton.addActionListener(this);

        addPolicyButton = new JButton("Add Policy");
        addPolicyButton.addActionListener(this);

        claimButton = new JButton("Claim");
        claimButton.addActionListener(this);

        payPremiumButton = new JButton("Pay Premium");
        payPremiumButton.addActionListener(this);

        viewPolicyButton = new JButton("View Policy Details");
        viewPolicyButton.addActionListener(this);

        // Add components to JFrame
        add(new JLabel("Customer Name:"));
        add(nameField);
        add(new JLabel("Customer Address:"));
        add(addressField);
        add(new JLabel("Customer DOB (dd/mm/yyyy):"));
        add(dobField);
        add(new JLabel("Customer Mobile Number:"));
        add(mobField);
        add(new JLabel("Customer Salary:"));
        add(salaryField);

        add(addCustomerButton);
        
        add(new JLabel("Family Member Name:"));
        add(familyNameField);
        add(new JLabel("Family Member Address:"));
        add(familyAddressField);
        add(new JLabel("Family Member DOB (dd/mm/yyyy):"));
        add(familyDobField);
        add(new JLabel("Family Member Age:"));
        add(familyAgeField);
        add(new JLabel("Family Member Mobile Number:"));
        add(familyMobField);

        add(addFamilyMemberButton);
        
        add(addPolicyButton);
        add(claimButton);
        add(payPremiumButton);
        add(viewPolicyButton);
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
        } else if (source == addFamilyMemberButton) {
            addFamilyMember();
        } else if (source == addPolicyButton) {
            addPolicy();
        } else if (source == claimButton) {
            claim();
        } else if (source == payPremiumButton) {
            payPremium();
        } else if (source == viewPolicyButton) {
            viewPolicyDetails();
        }
    }

    // Add customer details
    private void addCustomer() {
        String name = nameField.getText();
        String address = addressField.getText();
        String dob = dobField.getText();
        long mob = Long.parseLong(mobField.getText());
        long salary = Long.parseLong(salaryField.getText());

        customerObj.setName(name);
        customerObj.setAddress(address);
        customerObj.setDob(dob);
        customerObj.setMobnum(mob);
        customerObj.setSalary(salary);

        resultArea.setText("Customer added successfully!");
    }

    // Add family member details
    private void addFamilyMember() {
        String name = familyNameField.getText();
        String address = familyAddressField.getText();
        String dob = familyDobField.getText();
        int age = Integer.parseInt(familyAgeField.getText());
        long mob = Long.parseLong(familyMobField.getText());

        customerObj.addFamilyMember(name, address, dob, age, mob);
        resultArea.setText("Family member added successfully!");
    }

    // Add policy to the customer
    private void addPolicy() {
        // Sample company for adding the policy
        Company sampleCompany = new Company("001", "Sample Company");
        customerObj.addPolicy(sampleCompany);
        resultArea.setText("Policy added to customer.");
    }

    // Claim policy amount
    private void claim() {
        long claimAmount = Long.parseLong(JOptionPane.showInputDialog(this, "Enter Claim Amount"));
        
        // Sample company for claiming
        Company sampleCompany = new Company("001", "Sample Company");
        
        customerObj.claim(sampleCompany, claimAmount);
        resultArea.setText("Claim processed.");
    }

    // Pay premium for policy
    private void payPremium() {
        // Sample company for paying premium
        Company sampleCompany = new Company("001", "Sample Company");
        
        customerObj.payPremium(sampleCompany);
        resultArea.setText("Premium paid.");
    }

    // View policy details
    private void viewPolicyDetails() {
        // Sample company for viewing details
        Company sampleCompany = new Company("001", "Sample Company");
        
        customerObj.viewPolicyDetails(sampleCompany);
        resultArea.setText("Policy details viewed.");
    }

    public static void main(String[] args) {
        new Customer();
    }
}

// Class for Customer Details
class CustomerDetails {
    String name, address, dob;
    long mobnum, salary;
    FamilyMember fm = new FamilyMember();
    Policy policy = new Policy();

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setDob(String dob) { this.dob = dob; }
    public void setMobnum(long mobnum) { this.mobnum = mobnum; }
    public void setSalary(long salary) { this.salary = salary; }

    public void addFamilyMember(String name, String address, String dob, int age, long mob) {
        fm.addFamilyMember(name, address, dob, age, mob);
    }

    public void addPolicy(Company company) {
        policy.addPolicy(company);
    }

    public void claim(Company company, long claimAmount) {
        policy.claim(claimAmount);
    }

    public void payPremium(Company company) {
        policy.payPremium();
    }

    public void viewPolicyDetails(Company company) {
        policy.viewPolicyDetails();
    }
}

// Class for Family Members
class FamilyMember {
    String[] name = new String[10];
    String[] address = new String[10];
    String[] dob = new String[10];
    int[] age = new int[10];
    long[] mob = new long[10];
    int i = 0;

    public void addFamilyMember(String name, String address, String dob, int age, long mob) {
        this.name[i] = name;
        this.address[i] = address;
        this.dob[i] = dob;
        this.age[i] = age;
        this.mob[i] = mob;
        i++;
    }
}

// Class for Policy
class Policy {
    Company company;

    public void addPolicy(Company company) {
        this.company = company;
    }

    public void claim(long claimAmount) {
        System.out.println("Claim processed for amount: " + claimAmount);
    }

    public void payPremium() {
        System.out.println("Premium paid.");
    }

    public void viewPolicyDetails() {
        System.out.println("Policy details viewed for company: " + company.getName());
    }
}

// Class for Company
class Company {
    String id, name;

    public Company(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
