package de.schindlerfelix;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    static String city;
    static int id;
    static boolean fromJson;

    /**
     * @param args String[] - Jar command line arguments
     */
    public static void main(String[] args) {
        handleArguments(args);
        WeatherDataParser wp = new WeatherDataParser();
        wp.printWeather(generateFile());

        try {
            String citiesStr = Files.readString(Path.of("data/cities.json"));
            String cityName;

            JSONObject citiesJson = new JSONObject(citiesStr);
            for (int i = 0; i < citiesJson.getJSONArray("cities").length(); i++) {
                JSONObject cityJson = citiesJson.getJSONArray("cities").getJSONObject(i);
                cityName = cityJson.getString("name");

                // wenn cityName inclues sth from input city
                // TODO: doppelte filtern; zu viele Ergebnisse -> "Bonn"
                if (cityName.length()>=city.length()) {
                    if (cityName/*.substring(0, city.length())*/.contains(city)) {
                        System.out.println(cityName);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim einlesen der Städte");
        }
    }

    /**
     * Generating the WeatherData JSON File
     * @return String - Filepath of the generated File
     */
    public static String generateFile() {
        File logFile = new File("data/temp/logs/cache.log");
        String fileName = "data/temp/cache/" + city.toLowerCase() + ".weatherData.json", url;
        File weatherFile = new File(fileName);
        long timeStampNow = System.currentTimeMillis();
        long cacheFileAge = (timeStampNow-weatherFile.lastModified())/1000;

        // Datei existiert && jünger als 10 Minuten
        if(weatherFile.isFile() && cacheFileAge<=600) {
            // Logs in "logs/cache.log"
            try {
                FileUtils.writeStringToFile(logFile, "INFO Re-using cache file "+"data/temp/cache/"+city+".weatherData.json"+" from "+cacheFileAge+" seconds ago\n", "ISO-8859-1", true);
            } catch (IOException e) {
                System.out.println("Fehler beim schreiben des Logs");
            }
        } else {
            try {
                if (fromJson) {
                    url =  "https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&id="+id+"&appid=5f54d5225ad6721e8f86112bbfaa6e7b";
                } else {
                    url = "https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&q="+city+"&appid=5f54d5225ad6721e8f86112bbfaa6e7b";
                }
                // Caches in "data/temp/cache/city.weatherData.json"
                FileUtils.copyURLToFile(new URL(url), new File(fileName));
            } catch (IOException e) {
                System.out.println("Stadt wurde nicht gefunden.");
            }
        }

        return fileName;
    }

    /**
     * @param args String[] - Jar command line arguments
     */
    public static void handleArguments(String[] args) {
        String input;
        if (args.length > 0) {
            input = args[0];
        } else {
            Scanner scan = new Scanner(System.in);
            do {
                System.out.print("Stadtname: ");
                input = scan.nextLine();
            } while (input.contains("!") || input.contains("/") || input.contains("_") || input.contains("?") || input.contains("€") || input.contains("0") || input.contains("1") || input.contains("2") || input.contains("3") || input.contains("4") || input.contains("5") || input.contains("6") || input.contains("7") || input.contains("8") || input.contains("9"));
            scan = null;
        }
        city = input;
        fromJson=false;
    }
}
