# Installation 

## Command line
Download the ZIP Archive and unzip it:<br>
`foo@bar:~$ wget https://cdn.schindlerfelix.de/weather-forecast.zip && unzip weather-forecast.zip -d weather-forecast`

### Usage 
Change directory to the unzipped Folder<br>
`cd weather-forecast`

If you just run the jar file, you will be required to enter the city name later.<br>
`foo@bar:~$ java -jar weather-forecast.jar`

You can also request a city via command line arguments.<br>
`foo@bar:~$ java -jar weather-forecast.jar <City name>`


## IntelliJ
Clone git project:<br>
`foo@bar:~$ git clone git@gitlab.mi.hdm-stuttgart.de:fs146/weather-forecast.git`

### Usage 
Open the just cloned project in IntelliJ, now you should be able to run the main function.<br>
Follow the instructions for user input.
