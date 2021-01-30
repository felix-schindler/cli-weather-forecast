import static org.junit.jupiter.api.Assertions.*;

import de.hdm_stuttgart.mi.weather_forecast.Main;
import de.hdm_stuttgart.mi.weather_forecast.WeatherDataParser;
import org.junit.jupiter.api.*;

public class WeatherTests {

    @Test
    public void creatingWeatherFileShouldNotBeNullByCityName() {
        assertAll("Should return Not Null",
            () -> assertNotNull(Main.generateFile("Stuttgart")),
            () -> assertNotNull(Main.generateFile("parIs")),
            () -> assertNotNull(Main.generateFile("llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch")),
            () -> assertNotNull(Main.generateFile("Winchester-on-the-Severn")),
            () -> assertNotNull(Main.generateFile("Frankfurt am Main")),
            () -> assertNotNull(Main.generateFile("lOndOn")),
            () -> assertNotNull(Main.generateFile("bONn")),
            () -> assertNotNull(Main.generateFile("köLn"))
        );
    }

    @Test
    public void creatingWeatherFileShouldNotBeNullByCityId() {
        assertAll("Should return Not Null",
            () -> assertNotNull(Main.generateFile("384848")),
            () -> assertNotNull(Main.generateFile("803611")),
            () -> assertNotNull(Main.generateFile("690856")),
            () -> assertNotNull(Main.generateFile("2347078")),
            () -> assertNotNull(Main.generateFile("798544")),
            () -> assertNotNull(Main.generateFile("3575514")),
            () -> assertNotNull(Main.generateFile("1857578")),
            () -> assertNotNull(Main.generateFile("546448"))
        );
    }

    @Test
    public void creatingWeatherFileShouldBeNull() {
        assertAll("Should return Null",
            () -> assertNull(Main.generateFile("lazytown")),
            () -> assertNull(Main.generateFile("fjdslacjuidso")),
            () -> assertNull(Main.generateFile("entenhausen")),
            () -> assertNull(Main.generateFile("489650674086954")),
            () -> assertNull(Main.generateFile("zjvgdzugsbwhak")),
            () -> assertNull(Main.generateFile("WW91IGZvdW5kIGl0IQ==")),
            () -> assertNull(Main.generateFile("abradabrakadausen"))
        );
    }

    @Test
    public void printWeatherShouldBeFalse() {
        WeatherDataParser test = new WeatherDataParser();
        assertAll("Should return False",
                () -> assertFalse(test.printWeather(".idea")),
                () -> assertFalse(test.printWeather("src/main/java/de/hdm_stuttgart/mi/weather_forecast")),
                () -> assertFalse(test.printWeather("data/temp/cache/abradabrakadausen.weatherData.json")),
                () -> assertFalse(test.printWeather("data/temp/cache/entenhausen.weatherData.json")),
                () -> assertFalse(test.printWeather("data/temp/cache/lazytown.weatherData.json"))
        );
    }

    @Test
    public void printWeatherShouldBeTrue(){
        creatingWeatherFileShouldNotBeNullByCityName();   // Depends on another Test to work
        creatingWeatherFileShouldNotBeNullByCityId();     // Depends on another Test to work

        WeatherDataParser test = new WeatherDataParser();
        assertAll("Should return False",
                () -> assertTrue(test.printWeather("data/temp/cache/llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/stuttgart.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/384848.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/bonn.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/köln.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/paris.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/london.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/winchester-on-the-severn.weatherData.json")),
                () -> assertTrue(test.printWeather("data/temp/cache/frankfurt am main.weatherData.json"))
        );
    }
}
