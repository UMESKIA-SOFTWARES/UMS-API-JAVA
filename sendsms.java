import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        String apiUrl = "https://api.umeskiasoftwares.com/api/v1/sms";
        String apiKey = "XXXXXXXXXXXXXXXXXX="; // Replace with your API key
        String email = "example@gmail.com"; // Replace with your email
        String senderId = "23107"; // If you have a custom sender ID, use it here OR use the default sender ID: 23107
        String message = "UMS SMS Api Test Message";
        String phoneNumber = "0768XXXXX60"; // Phone number should be in the format: 0768XXXXX60 OR 254768XXXXX60 OR 254168XXXXX60

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String requestBody = "{\"api_key\":\"" + apiKey + "\",\"email\":\"" + email + "\",\"Sender_Id\":\"" +
                    senderId + "\",\"message\":\"" + message + "\",\"phone\":\"" + phoneNumber + "\"}";

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(requestBody);
            wr.flush();
            wr.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String newResponse = response.toString().replace("[", "").replace("]", "");
                JSONObject data = new JSONObject(newResponse);
                String success = data.getString("success");

                if (success.equals("200")) {
                    String requestId = data.getString("request_id");
                    String responseMessage = data.getString("message");
                    System.out.println("Sms sent successfully, with request_id: " + requestId + " and message: " + responseMessage);
                } else {
                    String resultCode = data.getString("ResultCode");
                    String errorMessage = data.getString("errorMessage");
                    System.out.println("Sms not sent, with ResultCode: " + resultCode + " and errorMessage: " + errorMessage);
                }
            } else {
                System.out.println("Request failed with status code " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
