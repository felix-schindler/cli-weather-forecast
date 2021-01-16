package de.schindlerfelix;

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

public class Main {
    static String city;
    static int id;
    static boolean fromJson;

    /**
     * @param args String[] - Jar command line arguments
     */
    public static void main(String[] args) {
        handleArguments(args);

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
            ArrayList<String> cityCountries = new ArrayList<String>();

            for (int i = 0; i < citiesJsonJSONArrayLength; i++) {
                JSONObject cityJson = citiesJson.getJSONArray("cities").getJSONObject(i);
                cityName = cityJson.getString("name");
                cityID = cityJson.getInt("id");
                cityCountry = cityJson.getString("country");

                // if cityName includes sth from input city
                // TODO: doppelte einträge filtern; zu viele Ergebnisse -> "Bonn"
                if (cityName.length()>=city.length()) {
                    // Comment: Has to begin with the same name as the city e g city="Bonn" => "Bonndorf"==true
                    if (cityName/*.substring(0, city.length())*/.contains(city)) {
                        if (!cityIds.contains(cityID)) {
                            cityIds.add(cityID);
                            cityNames.add(cityName);
                            cityCountries.add(cityCountry);
                        }
                    }
                }
            }

            // TODO: Wenn es nur eine einzige Stadt gibt
            if (cityNames.size()>0) {
                // Bei Goik wurde erst nach der Abfrage aufgefordert eine Auswahlmöglichkeit zu wählen
                // System.out.println("Bitte wählen Sie eine der folgenden Möglichkeiten: ");
                for (int i = 0; i < cityNames.size(); i++) {
                    System.out.println(i + 1 + " = " + cityNames.get(i) + ", " + cityCountries.get(i));
                }

                System.out.println("\nBitte gültige Auswahl 1 bis " + cityNames.size() + " treffen");

                final Scanner sc = new Scanner(System.in);
                int choice = sc.nextInt();
                id = cityIds.get(--choice);
                fromJson = true;
            }
        //TODO: Text wird nicht ausgegeben, wenn die Eingabe Fehlerhaft passiert
        } catch (IOException e) {
            System.out.println("Fehler beim einlesen der Städte");
        }

        WeatherDataParser wp = new WeatherDataParser();
        wp.printWeather(generateFile());
    }

    /**
     * Generating the WeatherData JSON File for the chosen city
     * @return String - Filepath of the generated File
     */
    public static String generateFile() {
        String fileName, url;
        File logFile = new File("data/temp/logs/cache.log");

        fileName = fromJson ? "data/temp/cache/" + id + ".weatherData.json" : "data/temp/cache/" + city.toLowerCase() + ".weatherData.json";

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
                // TODO: Url encode City
                url = fromJson? "https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&id="+id+"&appid=5f54d5225ad6721e8f86112bbfaa6e7b" : "https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&q="+city+"&appid=5f54d5225ad6721e8f86112bbfaa6e7b";
                // Caches in "data/temp/cache/city.weatherData.json"
                FileUtils.copyURLToFile(new URL(url), new File(fileName));
            } catch (IOException e) {
                System.out.println("Die von Ihnen eingegebene Stadt existiert leider in einem Paralleluniversum. \nBitte versuchen Sie es erneut und überprüfen Sie Ihre Angaben.");
            }
        }
        return fileName;
    }

    /**
     * @param args String[] - Jar command line arguments
     */
    public static void handleArguments(String[] args) {
        String input;
        boolean repeat = false;
        //char[] ungültigeZeichen = {'#','!','/','_','?','€','0','1','2','3','4','5','6','7','8','9'};
        String[] ungültigeZeichen = {"#","!","/","_","?","€","0","1","2","3","4","5","6","7","8","9"};
        if (args.length > 0) {
            input = args[0];
        } else {
            Scanner scan = new Scanner(System.in);
            // TODO: Fehlermeldung, wenn Eingabe falsch war
            do {
                if (repeat)
                    System.out.println("Ungültige Zeichenfolge");
                System.out.print("Stadtname: ");
                input = scan.nextLine();
                if (input.trim().isEmpty()) {
                    repeat = true;
                }else {
                    repeat = false;
                }
                for (int i = 0; i < ungültigeZeichen.length; i++) {
                    if (input.contains(ungültigeZeichen[i])) {
                        repeat = false;
                    }else{
                        repeat = true;
                    }
                }
            } while (repeat);
            scan = null;
        }
        city = input;
        fromJson=false;
    }
}
