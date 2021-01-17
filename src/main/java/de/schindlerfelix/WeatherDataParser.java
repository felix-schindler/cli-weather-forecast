package de.schindlerfelix;

import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * Parsing the weather data
 */
public class WeatherDataParser {
    String weatherStr="";
    JSONObject weather;
    private Object Path;

    /**
     * @param jsonWeatherDataFilename String - JsonWeatherDataFilepath
     */
    public void printWeather(final String jsonWeatherDataFilename) {
        // Variablen setzen
        SimpleDateFormat simple_date = new SimpleDateFormat("EEEE, dd.MM");
        SimpleDateFormat simple_time = new SimpleDateFormat("HH:mm");
        String dateStr, oldDateStr = "-1", timeStr, dscStr;
        double temp;
        Date date;

        // API request && Create JSONObject
        // TODO: Read complete File at once, not line for line
        Path = Paths.get(URI.create(jsonWeatherDataFilename));
        try {
            String content = Files.readString((java.nio.file.Path) Path);

            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
        Scanner sc = new Scanner(new File(jsonWeatherDataFilename));
        while (sc.hasNext())
            weatherStr = weatherStr.concat(sc.nextLine());
        sc = null;

        weather = new JSONObject(weatherStr);
         **/

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
    }
}
