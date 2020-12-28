package de.schindlerfelix;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Main {
    static String city;
    static int id;
    static boolean fromJson;

    /**
     * @param args String[] - City
     */
    /*
     * Link mit Stadtname:  https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&q=Berlin&appid=5f54d5225ad6721e8f86112bbfaa6e7b
     * Link mit id:         https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&id=707860&appid=5f54d5225ad6721e8f86112bbfaa6e7b
     * Letzte Anfrage mit genau dieser Stadt
     *      - kürzer als 10 Minuten her -> Cache -> In Log-File schreiben
     *      - länger: API Request
     *          - URL zusammenbauen
     *              - fromJson? id : stadt;
     *          - Als cache speichern
     * 1. Wetter ausgeben
     *      - Zeit umrechnen von UNIX Timestamp zu Datum & Uhrzeit
     *      - Grad Kelvin -> Celsius
     */
    public static void main(String[] args) {
        handleArguments(args);

        WeatherDataParser wp = new WeatherDataParser();
        wp.printWeather(generateFile());
    }

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
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }

        return fileName;
    }

    /**
     * handles the arguments
     * @param args String[] - jar arguments
     * TODO: Stadt setzen -> !Leerzeichen! (Frankfurt am Main)
     * Gibt's die Stadt in der JSON Datei?
     *      - Liste zur mit Auswahl ausgeben
     *      - Link: &id=id;
     *      - fromJson=true
     * sonst:
     *      - Stadt setzen
     *      - fromJson=false
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

        /*
        String cityString="";
        Scanner scan = null;
        try {
            scan = new Scanner(new File("data/cities.json"));
            while (scan.hasNext())
                cityString = cityString.concat(scan.nextLine());
            scan.close();
            JSONObject city = new JSONObject(cityString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */
    }
}
