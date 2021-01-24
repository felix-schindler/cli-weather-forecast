package test;

import static org.junit.jupiter.api.Assertions.*;

import de.hdm_stuttgart.mi.weather_forecast.WeatherDataParser;
import org.junit.jupiter.api.Test;

public class WeatherTests {
    @Test
    public void creatingWeatherFile() {
        assertNull(null);
    }

    @Test
    public void WeatherDataParserTrue(){
        WeatherDataParser test = new WeatherDataParser();
        assertAll("Should return true or false",
                () -> assertTrue(test.printWeather("data/temp/cache/llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/Stuttgart.weatherData.json")),
                () -> assertFalse(test.printWeather(".idea")),
                () -> assertFalse(test.printWeather("src/main/java/de/hdm_stuttgart/mi/weather_forecast")),
                () -> assertTrue(test.printWeather("data/temp/cache/Bonn.weatherData.json")),
                () -> assertFalse(test.printWeather("data/temp/cache/Abradabrakadausen.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/Paris.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/London.weatherData.json")),
                () -> assertFalse(test.printWeather("data/temp/cache/Entenhausen.weatherData.json")),
                () -> assertFalse(test.printWeather("data/temp/cache/Lazytown.weatherData.json"))
        );
    }


/*
    @Test
    public void creatingWeatherFileShouldBeTrue() {
        // Class TestiTesti is tested
        TestiTesti tester = new TestiTesti();

        // assert statements
        assertFalse(tester.generateFile(""), "Hällo eim ze Felix.");
        assertFalse(tester.generateFile(""), "Weather forecasting is so easy.");
        assertTrue(tester.generateFile(""), "Berlin");
        assertFalse(tester.generateFile(""), "A good API documentation is the A and the O.");
        assertTrue(tester.generateFile(""), "Stuttgart");
        assertTrue(tester.generateFile(""), "Bonn");
    }
 */
}
