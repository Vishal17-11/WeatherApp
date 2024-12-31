import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Enter Your Location name : ");
        Scanner in = new Scanner(System.in);
        String locationName = in.nextLine();


        // You can change the location name here
        JSONObject weatherData = getWeatherData(locationName);
        if (weatherData != null) {
            System.out.println("Weather Data: " + weatherData.toJSONString());
        } else {
            System.out.println("Failed to fetch weather data.");
        }
    }

    public static JSONObject getWeatherData(String locationName) {
        JSONArray locationData = getlocationData(locationName);
        if (locationData != null && !locationData.isEmpty()) {
            System.out.println("Location data fetched successfully.");
            JSONObject location = (JSONObject) locationData.get(0); // Use the first location result
            double latitude = (double) location.get("latitude");
            double longitude = (double) location.get("longitude");
            return fetchWeatherData(latitude, longitude);
        } else {
            System.out.println("Failed to fetch location data.");
            return null;
        }
    }

    public static JSONArray getlocationData(String locationName) {
        locationName = locationName.replace(" ", "+");

        String apiUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        try {
            HttpURLConnection con = fetchApiResponse(apiUrl);

            if (con == null) {
                System.out.println("Failed to establish connection.");
                return null;
            }

            if (con.getResponseCode() != 200) {
                System.out.println("Error: Response Code " + con.getResponseCode());
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner scn = new Scanner(con.getInputStream());

                while (scn.hasNext()) {
                    resultJson.append(scn.nextLine());
                }

                scn.close();
                con.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(resultJson.toString());

                return (JSONArray) resultsJsonObj.get("results");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject fetchWeatherData(double latitude, double longitude) {
        String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true";

        try {
            HttpURLConnection con = fetchApiResponse(apiUrl);

            if (con == null) {
                System.out.println("Failed ");
                return null;
            }

            if (con.getResponseCode() != 200) {
                System.out.println("Error: Response Code " + con.getResponseCode());
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner scn = new Scanner(con.getInputStream());

                while (scn.hasNext()) {
                    resultJson.append(scn.nextLine());
                }

                scn.close();
                con.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject weatherData = (JSONObject) parser.parse(resultJson.toString());

                return (JSONObject) weatherData.get("current_weather");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            return con;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
