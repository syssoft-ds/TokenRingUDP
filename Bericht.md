# Uebung 1 Rechnernetze

- Ich habe den Bericht als Markdown erstellt, da wie ich finde Github diese gut darstellen kann.
- Alle Dateipfade und öffentliche IP-Adressen wurden zensiert.


## Versuch 1: Erster TokenRing mit einem Rechner

Dieser erste Versuch lief problemlos.

    C:\xxxxx\Rechnernetze\Uebung>java -jar TokenRingUDP.jar
    UDP endpoint is (192.168.178.20, 49774)
    Token: seq=0, #members=1 (192.168.178.20, 61520)
    Token: seq=1, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=3, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=5, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=7, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=9, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=11, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=13, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=15, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=17, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=19, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=21, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=23, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=25, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=27, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=29, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=31, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=33, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=35, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=37, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=39, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=41, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=43, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=45, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=47, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=49, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=51, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
    Token: seq=53, #members=2 (192.168.178.20, 61520) (192.168.178.20, 49774)
_

    C:\xxxxx\Rechnernetze\Uebung>java -jar TokenRingUDP.jar 192.168.178.20 49774
    UDP endpoint is (192.168.178.20, 61520)
    Token: seq=2, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=4, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=6, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=8, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=10, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=12, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=14, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=16, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=18, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=20, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=22, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=24, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=26, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=28, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=30, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=32, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=34, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=36, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=38, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=40, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=42, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=44, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=46, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=48, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=50, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=52, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)
    Token: seq=54, #members=2 (192.168.178.20, 49774) (192.168.178.20, 61520)


## Versuch 2: Aufbau eines TokenRing mit einem Kommilitonen.

Ich habe versucht einen TokenRing mit einem Kommilitonen zu starten, allerdings konnten wir selbst nach
freigabe der Ports nur erreichen, dass ein Packet bei mir ankam, allerdings nicht bei ihm.
Höchstwahrscheinlich liegt es daran, dass er über keine öffentliche IP-Adresse verfügt.

### Versuch den Rechner des Kommilitonen über einen Ping zu erreichen
Er konnte meinen Rechner über einen Ping erreichen, ich seinen allerdings nicht.\
Ping auf seinen Rechner:

    C:\xxxxx\Rechnernetze\Uebung>ping xxx.xxx.xxx.xxx
    
    Ping wird ausgeführt für xxx.xxx.xxx.xxx mit 32 Bytes Daten:
    Zeitüberschreitung der Anforderung.
    Zeitüberschreitung der Anforderung.
    Zeitüberschreitung der Anforderung.
    Zeitüberschreitung der Anforderung.
    
    Ping-Statistik für xxx.xxx.xxx.xxx:
    Pakete: Gesendet = 4, Empfangen = 0, Verloren = 4
    (100% Verlust),

### Kommilitone verbindet sich mit meinem TokenRing
Hierbei kam bei mir nur ein Packet an, da mein Rechner erreichbar war, allerdings brach dann der Ring auch ab, da er
wie bereits vermutet nicht über eine Public IP verfügt.

    C:\xxxxx\Rechnernetze\Uebung>java -jar TokenRingUDP.jar
    UDP endpoint is (192.168.178.20, 50412)
    Token: seq=0, #members=1 (192.168.0.53, 58330)
    Token: seq=1, #members=2 (192.168.0.53, 58330) (192.168.178.20, 50412)

Um überhaupt ein Packet empfangen zu können musste ich den Port meines Rings in den Einstellungen meines Routers öffnen
(gezeigt auf Screenshot_1).
![Screenshot_1.jpg](Screenshot_1.jpg)
### Ich verbinde mich mit dem TokenRing des Kommilitonen
Mein Versuch seinem Ring beizutreten ist gescheitert.

    C:\xxxxx\Rechnernetze\Uebung>java -jar TokenRingUDP.jar xxx.xxx.xxx.xxx 61845
    UDP endpoint is (192.168.178.20, 52363)


## Versuch 3: TokenRing mit einem Rechner und Wireshark

### Ergebniss in der Konsole
    C:\xxxxx\Rechnernetze\Uebung>java -jar TokenRingUDP.jar 192.168.178.20 59169
    UDP endpoint is (192.168.178.20, 61180)
    Token: seq=2, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=4, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=6, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=8, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=10, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=12, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=14, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=16, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=18, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=20, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=22, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    Token: seq=24, #members=2 (192.168.178.20, 59169) (192.168.178.20, 61180)
    C:\xxxxx\Rechnernetze\Uebung>


    C:\xxxxx\Rechnernetze\Uebung>java -jar TokenRingUDP.jar
    UDP endpoint is (192.168.178.20, 59169)
    Token: seq=0, #members=1 (192.168.178.20, 61180)
    Token: seq=1, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=3, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=5, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=7, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=9, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=11, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=13, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=15, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=17, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=19, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=21, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    Token: seq=23, #members=2 (192.168.178.20, 61180) (192.168.178.20, 59169)
    C:\xxxxx\Rechnernetze\Uebung>

### Ergebnisse in Wireshark
Ich konnte die UPD-Pakete zunächst nicht finden, erst nachdem ich als Schnittstelle "Adapter for
loopback traffic capture" anstelle von "Ethernet" gewählt hatte (Veranschaulicht auf Screenshot_2).
![Screenshot_2.jpg](Screenshot_2.jpg)

Dort wurden nur sehr wenige Pakete angezeigt so, dass ich keinen Filter benötigt habe um zu identifizieren welche zum
TokenRing gehören. Hätte ich einen gebraucht hätte ich folgenden genutzt:\
**udp.port == 59169**

Die Ergebnisse aus Wireshark werden auf Screenshot_3 gezeigt
![Screenshot_3.jpg](Screenshot_3.jpg)

Ebenfalls konnte man in Wireshark den Inhalt des Pakets sehen. Aus Screenshot_4 gezeigt die gesendeten Daten im JSON-Format.
![Screenshot_4.jpg](Screenshot_4.jpg)