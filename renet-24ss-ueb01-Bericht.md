# Rechnernetze 2024-SS - Übungsblatt 01
`#19D9DA-s4joregn`

## Aufgabe 1
TokenRingUDP Projekt Clonen: (https://github.com/syssoft-ds/TokenRingUDP)

TokenRingUDP Testen

## Aufgabe 2
Möglichst großer Token Ring:

``` 
java -jar TokenRingUDP.jar 
```
``` 
java -jar TokenRingUDP.jar <ip> <port>
```


| Teilnehmer | IP              | Port   |
| ---------- | --------------- | -----  |
| Ring Leader | 136.199.026.020 | 59847 |
| Node 1 | 136.199.200.018 | 58847 |
| Node 2 | 136.199.026.024 | 55588 |



## Aufgabe 3
Wireshark Pakete abfangen


## Aufgabe 4
Zusammenfassung: 
- erste Tests im Heimnetzwerk mit zwei Geräten funktionieren auf anhieb ohne Probleme
- im Uninetzwerk mit drei Geräten stellte sich das ganze etwas schwieriger da. Trotz identischer Einstellungen konnte nur ein Gerät den Ring aufbauen.
- zur Fehlersuche nutzten wir `ping`-Anfragen um zu sehen, ob wir untereinander verbunden waren. 
- tatsächlich konnten wir im Uninetzwerk keinerlei erfolgreiche Pings durchführen, auch wenn der Ring mit zwei Geräten funktionierte.
- mit Wireshark konnten sowohl die ausgehenden `ping` und `udp` Pakete gesehen werden. 

Trotz unserem systematischem Vorgehen und vielen verschieden Test waren wir im Uninetzwerk nicht so erfolgreich wie wir erhofft hatten. 

