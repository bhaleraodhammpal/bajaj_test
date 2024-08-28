package bajaj_test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class DestinationHashGenerator {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN Number> <JSON file path>");
            return;
        }

        String prnNumber = args[0].toLowerCase().replaceAll("\\s", ""); 
        String jsonFilePath = args[1];

        try {
            
            String jsonString = readFile(jsonFilePath);
            String destinationValue = findDestinationValue(jsonString);

            if (destinationValue == null) {
                System.out.println("No 'destination' key found in the JSON file.");
                return;
            }

            
            String randomString = generateRandomString(8);

            
            String input = prnNumber + destinationValue + randomString;
            String hashValue = generateMD5Hash(input);

            
            System.out.println(hashValue + ";" + randomString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    private static String findDestinationValue(String jsonString) {
        String searchKey = "\"destination\"";
        int index = jsonString.indexOf(searchKey);

        if (index == -1) {
            return null;
        }

        int startIndex = jsonString.indexOf(":", index) + 1;
        int endIndex = jsonString.indexOf(",", startIndex);

        if (endIndex == -1) {
            endIndex = jsonString.indexOf("}", startIndex);
        }

        String value = jsonString.substring(startIndex, endIndex).trim();

        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        return value;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private static String generateMD5Hash(String input) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        byte[] bytes = input.getBytes();
        int[] md5Bytes = new int[16];

      
        for (int i = 0; i < bytes.length; i++) {
            md5Bytes[i % 16] += bytes[i];
        }

        StringBuilder hexString = new StringBuilder();
        for (int i : md5Bytes) {
            int val = i & 0xff;
            hexString.append(hexArray[val >>> 4]);
            hexString.append(hexArray[val & 0x0f]);
        }
        return hexString.toString();
    }
}
