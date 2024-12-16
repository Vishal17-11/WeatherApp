
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public  class Weather{


    public static JSONObject getWeatherData(String locationName){

        JSONArray locationData = getlocationData(locationName);

return null;
    }

public static  JSONArray getlocationData(String locationName){

        locationName = locationName.replace("","+");

        String ApiUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" +
        locationName + "&count=10&language=en&format=json";


try{

    HttpURLConnection con = fetchApiResponse(ApiUrl);


    if(con.getResponseCode() != 200){
        System.out.println("Error");
        return null;
    }else{
        StringBuilder resultjson = new StringBuilder();
        Scanner scn = new Scanner(con.getInputStream());

        while(scn .hasNext()){
            resultjson.append(scn.nextLine());
        }

        scn.close();

        con.disconnect();

        JSONParser parser = new JSONParser();

        JSONObject resultsjsonOb = (JSONObject) parser.parse(String.valueOf(resultjson));

        JSONArray locationData = (JSONArray) resultsjsonOb.get("results");
        return locationData;
    }


}catch(Exception e){

    e.printStackTrace();

    }
return null;
}

    private static HttpURLConnection fetchApiResponse(String apiUrl) {
    try{
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();


            con.setRequestMethod("GET");

            con.connect();

            return con;}catch(IOException e){

        e.printStackTrace();
    }
    return null;
    }


}