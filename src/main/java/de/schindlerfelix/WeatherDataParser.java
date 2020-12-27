package de.schindlerfelix;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Scanner;

/**
 * Parsing the weather data
 */
public class WeatherDataParser {
    static String weatherStr;
    static JSONObject weather;

    public static void printWeather() {
        try {
            // Variablen setzen
            String url="https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&q=Xian&appid=5f54d5225ad6721e8f86112bbfaa6e7b";
            weatherStr="";
            SimpleDateFormat simple_date = new SimpleDateFormat("EEEE, dd.MM");
            SimpleDateFormat simple_time = new SimpleDateFormat("H:mm");
            String dateStr, timeStr, oldDateStr="-1", tempStr, dscStr;
            double temp;
            Date date;

            // API request && Create JSONObject
            Scanner scan = new Scanner(new URL(url).openStream());
            while (scan.hasNext())
                weatherStr = weatherStr.concat(scan.nextLine());
            scan.close();
            weather = new JSONObject(weatherStr);

            for (int i = 0; i < weather.getJSONArray("list").length(); i++) {
                JSONObject now = weather.getJSONArray("list").getJSONObject(i);
                JSONObject dsc =  now.getJSONArray("weather").getJSONObject(0);

                date = Date.from(Instant.ofEpochSecond(now.getInt("dt")));
                temp = now.getJSONObject("main").getDouble("temp");
                dscStr = dsc.getString("description");
                dateStr = simple_date.format(date);
                timeStr = simple_time.format(date);

                if (!oldDateStr.equals(dateStr)) {
                    System.out.println("\n"+dateStr);
                }

                if (temp>0)
                    System.out.format("%4s %5s:  %5.2f °C, %s \n", " ", timeStr, temp, dscStr);
                else
                    System.out.format("%4s %5s: %6.2f °C, %s \n", " ", timeStr, temp, dscStr);

                oldDateStr = dateStr;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /*
    public static String parseWeather(final String jsonWeatherDataFilename) {
        return "";
    }
    */
}
