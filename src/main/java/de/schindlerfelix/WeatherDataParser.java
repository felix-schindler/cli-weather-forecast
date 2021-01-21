package de.schindlerfelix;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * Parsing the weather data
 */
public class WeatherDataParser {
    String weatherStr;
    JSONObject weather;

    /**
     *
     * @param jsonWeatherDataFilename String - JsonWeatherDataFilepath
     * @return true if the weather was printed successfully, false otherwise
     */
    public boolean printWeather(final String jsonWeatherDataFilename) {
        if (jsonWeatherDataFilename==null)
            return false;

        // Variablen setzen
        SimpleDateFormat simple_date = new SimpleDateFormat("EEEE, dd.MM");
        SimpleDateFormat simple_time = new SimpleDateFormat("HH:mm");
        String dateStr, oldDateStr = "-1", timeStr, dscStr;
        double temp;
        Date date;

        // API request && Create JSONObject
        try {
            weatherStr = Files.readString(java.nio.file.Path.of(jsonWeatherDataFilename));
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
            return true;
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Wetterdaten.");
            return false;
        }
    }
}
