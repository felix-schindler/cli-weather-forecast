package de.hdm_stuttgart.mi.weather_forecast;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Main class for the Application
 */
public class Main {
    static String city;
    static int id;
    static boolean fromJson;

    /**
     * The application entry
     * @param args String[] - Jar command line arguments
     */
    public static void main(String[] args) {
        handleInput(args);

        WeatherDataParser wp = new WeatherDataParser();
        wp.printWeather(generateFile());
    }

    public static String generateFile(String givenCity) {
        if (givenCity==null)
            return null;

        try {
            id = Integer.parseInt(givenCity);
            fromJson = true;
        } catch (NumberFormatException e) {
            city = givenCity;
            fromJson = false;
            return null;
        }

        return generateFile();
    }

    /**
     * Generating the WeatherData JSON File for the chosen city
     * Directories:
     * - Logs in "logs/cache.log"
     * - Caches in "data/temp/cache/city.weatherData.json"
     * @return String|null - Filepath of the successfully generated File or null, if anythings runs into an error
     */
    public static String generateFile() {
        String fileName, url;
        File logFile = new File("data/temp/logs/cache.log");

        fileName = fromJson ? "data/temp/cache/" + id + ".weatherData.json" : "data/temp/cache/" + city + ".weatherData.json";

        File weatherFile = new File(fileName);
        long timeStampNow = System.currentTimeMillis();
        long cacheFileAge = (timeStampNow-weatherFile.lastModified())/1000;

        // Datei existiert && jünger als 10 Minuten
        if(weatherFile.isFile() && cacheFileAge<=600) {
            try {
                FileUtils.writeStringToFile(logFile, "INFO Re-using cache file "+"data/temp/cache/"+city+".weatherData.json"+" from "+cacheFileAge+" seconds ago\n", "ISO-8859-1", true);
            } catch (IOException e) {
                System.out.println("Fehler beim schreiben des Logs");
            }
        } else {
            try {
                url = fromJson? "https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&id="+id+"&appid=5f54d5225ad6721e8f86112bbfaa6e7b" : "https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&q="+city+"&appid=5f54d5225ad6721e8f86112bbfaa6e7b";
                FileUtils.copyURLToFile(new URL(url), new File(fileName));
            } catch (IOException e) {
                System.out.println("Die von Ihnen eingegebene Stadt existiert in einem Paralleluniversum und ist somit leider außerhalb unserer Reichweite.\nBitte versuchen Sie es erneut und überprüfen Sie Ihre Angaben.");
                return null;
            }
        }
        return fileName;
    }

    /**
     * Handles the command line arguments, requests user input if no arguments, choose city from cities.json
     * @param args String[] - Jar command line arguments
     */
    public static void handleInput(String[] args) {
        String input;
        boolean repeat = false;

        if (args.length > 0) {
            input = args[0];
        } else {
            Scanner scan = new Scanner(System.in);
            do {
                if (repeat)
                    System.out.println("Ungültige Zeichenfolge");

                System.out.print("Stadtname: ");
                input = scan.nextLine().trim();

                repeat = !Pattern.matches("^[a-zA-Z\\s]+$", input) || input.isEmpty();
            } while (repeat);
            scan = null;
        }

        city = input.toLowerCase();
        fromJson = false;

        try {
            String citiesStr = "{ \"cities\": [";
            citiesStr += Files.readString(Path.of("data/cities.json"));
            citiesStr += "]}";
            String cityName, cityCountry;
            int cityID;

            JSONObject citiesJson = new JSONObject(citiesStr);
            JSONArray citiesJsonJSONArray = citiesJson.getJSONArray("cities");
            int citiesJsonJSONArrayLength = citiesJsonJSONArray.length();

            ArrayList<Integer> cityIds = new ArrayList<Integer>();
            ArrayList<String> cityNames = new ArrayList<String>();

            for (int i = 0; i < citiesJsonJSONArrayLength; i++) {
                JSONObject cityJson = citiesJson.getJSONArray("cities").getJSONObject(i);
                cityName = cityJson.getString("name");
                cityID = cityJson.getInt("id");
                cityCountry = cityJson.getString("country");

                // Stadtname enthält irgendwas von eingegebenen Stadtnamen
                if (cityName.toLowerCase().contains(city)) {
                    // Land der zu zeigenden Städte muss "DE" sein
                    if (!cityIds.contains(cityID) && cityCountry.equals("DE")) {
                        cityIds.add(cityID);
                        cityNames.add(cityName);
                    }
                }
            }

            if (cityNames.size()==1) {
                id = cityIds.get(0);
                fromJson = true;
            } else if (cityNames.size()>0) {
                for (int i = 0; i < cityNames.size(); i++)
                    System.out.println(i + 1 + " = " + cityNames.get(i));

                System.out.println("\nBitte gültige Auswahl 1 bis " + cityNames.size() + " treffen");

                Scanner scan = new Scanner(System.in);
                int choice = scan.nextInt();

                while (choice <= 0 || choice > cityIds.size()) {
                    System.out.println("Ungültige Eingabe, bitte versuchen Sie es erneut.");
                    choice = scan.nextInt();
                }
                scan = null;

                id = cityIds.get(--choice);
                fromJson = true;
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Auswahlstädte, die Anfrage wird daher mit der zu Beginn eingegebenen Stadt stattfinden.");
            fromJson = false;
        }
    }
}
