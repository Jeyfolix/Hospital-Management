import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourceManagement {

    private Map<String, Integer> resources;
    private Map<String, Map<String, Integer>> medications;
    private Map<String, ArrayList<Staff>> staff;
    private ArrayList<WorkflowLog> workflowLog;

    public ResourceManagement() {
        resources = new HashMap<>();
        medications = new HashMap<>();
        staff = new HashMap<>();
        workflowLog = new ArrayList<>();

        resources.put("beds", 0);
        resources.put("ventilators", 0);
        medications.put("medications", new HashMap<>());
        staff.put("doctors", new ArrayList<>());
        staff.put("nurses", new ArrayList<>());
        staff.put("technicians", new ArrayList<>());
        staff.put("administrators", new ArrayList<>());
    }

    public void addResource(String resourceType, int quantity) {
        resources.put(resourceType, resources.getOrDefault(resourceType, 0) + quantity);
    }

    public void addMedication(String name, int quantity) {
        medications.get("medications").put(name, medications.get("medications").getOrDefault(name, 0) + quantity);
    }

    public void assignStaff(String type, String name, String specialty) {
        if (staff.containsKey(type)) {
            staff.get(type).add(new Staff(name, specialty));
        }
    }

    public void recordWorkflow(String action, ArrayList<String> participants, String details) {
        workflowLog.add(new WorkflowLog(action, participants, details));
    }

    public Map<String, Integer> getResources() {
        return resources;
    }

    public ArrayList<WorkflowLog> getWorkflowLog() {
        return workflowLog;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hospital Resource Management");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Resource Type:");
        userLabel.setBounds(10, 20, 100, 25);
        panel.add(userLabel);

        JTextField resourceTypeText = new JTextField(20);
        resourceTypeText.setBounds(120, 20, 165, 25);
        panel.add(resourceTypeText);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(10, 50, 100, 25);
        panel.add(quantityLabel);

        JTextField quantityText = new JTextField(20);
        quantityText.setBounds(120, 50, 165, 25);
        panel.add(quantityText);

        JButton addButton = new JButton("Add Resource");
        addButton.setBounds(10, 80, 150, 25);
        panel.add(addButton);

        JTextArea outputArea = new JTextArea();
        outputArea.setBounds(10, 120, 560, 200);
        outputArea.setEditable(false);
        panel.add(outputArea);

        ResourceManagement hospital = new ResourceManagement();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String resourceType = resourceTypeText.getText();
                int quantity = Integer.parseInt(quantityText.getText());
                hospital.addResource(resourceType, quantity);
                outputArea.setText("Resources Updated: " + hospital.getResources().toString());
            }
        });
    }

    class Staff {
        String name;
        String specialty;

        public Staff(String name, String specialty) {
            this.name = name;
            this.specialty = specialty;
        }
    }

    class WorkflowLog {
        String action;
        ArrayList<String> participants;
        String details;

        public WorkflowLog(String action, ArrayList<String> participants, String details) {
            this.action = action;
            this.participants = participants;
            this.details = details;
        }

        @Override
        public String toString() {
            return "Action: " + action + ", Participants: " + participants + ", Details: " + details;
        }
    }
}

