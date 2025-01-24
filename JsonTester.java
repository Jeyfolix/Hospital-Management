import org.json.JSONObject;
import org.json.JSONException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonTester {

    // Method to validate JSON from a file
    public static void testJsonFile(String filePath) {
        try {
            // Read file content into a String
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            // Parse content as JSON
            new JSONObject(content);
            System.out.println("JSON file is valid.");
            System.out.println("Content:\n" + content);
        } catch (JSONException e) {
            System.out.println("Invalid JSON file. Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Method to validate JSON from a string
    public static void testJsonString(String jsonString) {
        try {
            // Parse string as JSON
            new JSONObject(jsonString);
            System.out.println("JSON string is valid.");
            System.out.println("Content:\n" + jsonString);
        } catch (JSONException e) {
            System.out.println("Invalid JSON string. Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Test JSON file
        String filePath = "example.json"; // Replace with your JSON file path
        testJsonFile(filePath);

        // Test JSON string
        String jsonString = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}"; // Replace with your JSON string
        testJsonString(jsonString);
    }
}

