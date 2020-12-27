package de.schindlerfelix;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {
    static String city="Xian";
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

        File logFile = new File("logs/cache.log");
        File weatherFile = new File("cache/" + city + ".weatherData.json");
        long timeStampNow = System.currentTimeMillis();
        long cacheFileAge = (timeStampNow-weatherFile.lastModified())/1000;

        // Datei existiert && jünger als 10 Minuten
        if(weatherFile.isFile() && cacheFileAge<=600) {
            // Logs in "logs/cache.log"
            // Caches in "cache/city.weatherData.json"
            try {
                FileUtils.writeStringToFile(logFile, "INFO Re-using cache file "+"cache/"+city+".weatherData.json"+" from "+cacheFileAge+" seconds ago\n", "ISO-8859-1", true);
            } catch (IOException e) {
                System.out.println("Failed to write the Log.");
                e.printStackTrace();
            }
        } else {
            System.out.println("creating new file");
            weatherFile.delete(); // eig redundant, da datei auch einf überschrieben werden kann
            try {
                // TODO: set URL q/id (this.fromJsom)
                FileUtils.copyURLToFile(new URL("https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&q="+city+"&appid=5f54d5225ad6721e8f86112bbfaa6e7b"), new File("cache/" + city.toLowerCase() + ".weatherData.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // WeatherDataParser wp = new WeatherDataParser();
        // wp.printWeather();
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

    }
}
