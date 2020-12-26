package de.schindlerfelix;

public class Main {
    // TODO: Classes:
    String city;
    int id;
    boolean fromJson;

    /**
     * @param args String[] - City
     */
    public static void main(String[] args) {
        handleArguments(args);
        /* Bsp Link: https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&q=Berlin&appid=5f54d5225ad6721e8f86112bbfaa6e7b
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
    }

    /**
     * handles the arguments
     * @param args String[] - jar arguments
     * 1. Stadt setzen -> !Leerzeichen
     * Gibt's die Stadt in der JSON Datei
     *      - Liste zur mit Auswahl ausgeben
     *      - Link: &id=id;
     *      - fromJson=true
     */
    public static void handleArguments(String[] args) {
        
    }
}
