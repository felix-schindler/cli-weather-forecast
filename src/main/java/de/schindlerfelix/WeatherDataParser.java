package de.schindlerfelix;

import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Scanner;

/**
 * Parsing the weather data
 */
public class WeatherDataParser {
    String weatherStr;
    JSONObject weather;

    // TODO: Print weather from given JSONFile
    public void printWeather(final String jsonWeatherDataFilename) {
        try {
            // Variablen setzen
            SimpleDateFormat simple_date = new SimpleDateFormat("EEEE, dd.MM");
            SimpleDateFormat simple_time = new SimpleDateFormat("H:mm");
            String dateStr, timeStr, oldDateStr="-1", tempStr, dscStr;
            double temp;
            Date date;

            // API request && Create JSONObject
            // TODO: do this in with given file
            Scanner scan = new Scanner(new File(jsonWeatherDataFilename));
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
            // TODO: Fancy Fehlerausgabe
            e.printStackTrace();
        }
    }
}
