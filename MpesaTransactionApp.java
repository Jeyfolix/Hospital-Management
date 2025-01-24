import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URI;
import java.net.http.*;
import java.util.Base64;
import org.json.JSONObject;

public class MpesaTransactionApp {

    private static final String CONSUMER_KEY = "liNeDD3Zybrl4F2liSRqNA9x6YzyRGXxBeppK1sopav8emFF";
    private static final String CONSUMER_SECRET = "TyGtaaDAnRw8UpcI5SFs3NpAq6rGJvngAvCAewaF7eXcehGxjEYIGO6LKlJBaCB9";
    private static final String API_BASE_URL = "https://sandbox.safaricom.co.ke";
    private static final String BUSINESS_SHORT_CODE = "YOUR_BUSINESS_SHORT_CODE"; // Replace with your Paybill or Till number
    private static final String PASSKEY = "YOUR_PASSKEY";
    private static final String ADMIN_PHONE_NUMBER = "254701603497"; // Admin phone number to receive payments
    private static JLabel statusLabel;

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("M-Pesa Transaction App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 1));

        // Phone number input panel
        JPanel phonePanel = new JPanel();
        phonePanel.add(new JLabel("Client Phone Number:"));
        JTextField phoneField = new JTextField(15);
        phonePanel.add(phoneField);

        // Amount input panel
        JPanel amountPanel = new JPanel();
        amountPanel.add(new JLabel("Enter Amount:"));
        JTextField amountField = new JTextField(10);
        amountPanel.add(amountField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton sendButton = new JButton("Send Payment Request");
        buttonPanel.add(sendButton);

        // Status label panel
        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel("Status: Waiting for input...");
        statusPanel.add(statusLabel);

        // Add all panels to the frame
        frame.add(phonePanel);
        frame.add(amountPanel);
        frame.add(buttonPanel);
        frame.add(statusPanel);

        // Add action listener to the send button
        sendButton.addActionListener(e -> {
            String clientPhoneNumber = phoneField.getText().trim();
            String amount = amountField.getText().trim();

            if (isValidPhoneNumber(clientPhoneNumber) && isValidAmount(amount)) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        try {
                            statusLabel.setText("Status: Generating access token...");
                            String accessToken = generateAccessToken();
                            if (accessToken != null) {
                                statusLabel.setText("Status: Sending payment request...");
                                boolean success = initiateStkPush(accessToken, clientPhoneNumber, amount);
                                if (success) {
                                    statusLabel.setText("Status: Payment request sent successfully!");
                                    JOptionPane.showMessageDialog(frame, "Payment Request Sent Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    statusLabel.setText("Status: Payment request failed.");
                                    JOptionPane.showMessageDialog(frame, "Payment Request Failed.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                statusLabel.setText("Status: Failed to generate access token.");
                                JOptionPane.showMessageDialog(frame, "Failed to generate access token.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            statusLabel.setText("Status: An error occurred.");
                            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter valid phone number and amount.", "Error", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("Status: Invalid input.");
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("07\\d{8}");
    }

    private static boolean isValidAmount(String amount) {
        try {
            double val = Double.parseDouble(amount);
            return val > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String generateAccessToken() throws Exception {
        String credentials = CONSUMER_KEY + ":" + CONSUMER_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/oauth/v1/generate?grant_type=client_credentials"))
                .header("Authorization", "Basic " + encodedCredentials)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JSONObject json = new JSONObject(response.body());
            return json.getString("access_token");
        }
        return null;
    }

    private static boolean initiateStkPush(String accessToken, String clientPhoneNumber, String amount) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        JSONObject payload = new JSONObject();
        payload.put("BusinessShortCode", BUSINESS_SHORT_CODE);
        payload.put("Password", generatePassword());
        payload.put("Timestamp", getTimestamp());
        payload.put("TransactionType", "CustomerPayBillOnline");
        payload.put("Amount", amount);
        payload.put("PartyA", clientPhoneNumber);
        payload.put("PartyB", BUSINESS_SHORT_CODE);
        payload.put("PhoneNumber", clientPhoneNumber);
        payload.put("CallBackURL", "https://your-callback-url.com");
        payload.put("AccountReference", "Admin Payment");
        payload.put("TransactionDesc", "Payment to Admin");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/mpesa/stkpush/v1/processrequest"))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    private static String generatePassword() {
        String timestamp = getTimestamp();
        String rawPassword = BUSINESS_SHORT_CODE + PASSKEY + timestamp;
        return Base64.getEncoder().encodeToString(rawPassword.getBytes());
    }

    private static String getTimestamp() {
        return new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
    }
}
