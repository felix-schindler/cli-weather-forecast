# TODOs

### API Calls: 
Link mit Stadtname:  https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&q=Berlin&appid=5f54d5225ad6721e8f86112bbfaa6e7b
Link mit id:         https://api.openweathermap.org/data/2.5/forecast?lang=de&units=metric&id=707860&appid=5f54d5225ad6721e8f86112bbfaa6e7b

1. Stadt eingeben
	- Gibt's die Stadt in der JSON Datei?
		- Liste mit Auswahl ausgeben
		- Stadt auswählen
		- fromJson=true
	- sonst:
		- Stadt setzen
		- fromJson=false

2. Letzte Anfrage mit genau dieser Stadt abfragen
	- kürzer als 10 Minuten her 
		- Aus Cache lesen
		- In Log-File schreiben
	- länger: 
		- API Request
		- URL zusammenbauen
			- fromJson ? id : city;
		- In Cache speichern

3. Wetter ausgeben
	- Zeit umrechnen von UNIX Timestamp zu Datum & Uhrzeit
	- Datum Trennen, Uhrzeit untereinander ausgeben
	- Celsius
