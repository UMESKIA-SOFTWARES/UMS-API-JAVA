import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        String apiUrl = "https://api.umeskiasoftwares.com/api/v1/smsbalance";
        String apiKey = "XXXXXXXXXXXXXXX="; // Replace with your API key
        String email = "example@gmail.com"; // Replace with your email

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String requestBody = "{\"api_key\":\"" + apiKey + "\",\"email\":\"" + email + "\"}";

            OutputStream os = connection.getOutputStream();
            os.write(requestBody.getBytes());
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String responseBody = response.toString();
                JSONObject data = new JSONObject(responseBody);
                String success = data.getString("success");

                if (success.equals("200")) {
                    String creditBalance = data.getString("creditBalance");
                    System.out.println("Sms Balance retrieved successfully, with creditBalance: " + creditBalance);
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
