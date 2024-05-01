# Protokoll Übungsblatt 1

## Aufgabe 2
### Versuch:
Im eigenen Home WLAN auf zwei vorhandenen Geräten einen TokenRing erstellen 

### Verbindung:
Home WLAN 
### Geräte:
1. Desktop PC (Windows 10)
2. Chrome Book
### Ergebnis:
Es scheitert daran auf Gerät 2 (Chrome Book) das Repository zum Laufen zu bringen.
### Fazit: 
Da aktuell kein portables Gerät zur Verfügung steht, war die Umsetzung der Aufgabenstellung leider nicht möglich.

## Aufgabe 3:
### Versuch:
1. Installation Wireshark
2. Display Filter auf "UDP only" einstellen
3. Nachrichten eines Token-Rings auf einem Rechner einfangen

### Verbindung & Geräte:
Lokal nur auf einem Rechner (Windows 10 Desktop PC)

### Ausführung:
1. Wireshark starten, Display Filter auf "UDP only" setzen, Aufzeichnung starten
2. Erzeugen eines Token-Rings via TokenRing.java mit IntelliJ
3. via CMD und `java -jar TokenRingUDP.jar <ip> <port>` mit weiteren Knoten dem Ring beitreten
   3. Zunächst Ports 50157 → 65163 verbinden
   4. Port 53810 dazwischen schalten: 50157 → 53810 → 65163
   5. Port 53860 dazwischen schalten: 50157 → 53860 → 53810 → 65163
4. Aufzeichnung 1072s (ca. 18min) beenden 
4. Wireshark Aufzeichnung auswerten

### Beobachtung:
1. Sendezeit der Pakete schwankt minimal zwischen 1,0067s und 1,013s
2. 2 Token: Length = 129, Len = 97 
3. 3 Token: Length = 165, Len = 133
4. 4 Token: Length = 201, Len = 169 
5. Einfügen eines neuen Knotens: Length = 91, Len = 59 
6. Nach 180s Laufzeit mit 4 Token: Length = 202, Len = 170
7. Restliche Laufzeit keine weitere Veränderung

### Fazit: 
Die Beobachtungen zeigen im Wesentlichen, dass TokenRing.java wie erwartet funktioniert. Sollten aber die Schwankungen in der Sendezeit (Beobachtung 1) nicht begründbar sein, könnte man sowohl dieser Sache, als auch der Ursache für Beobachtung 6 nachgehen.


   


 




