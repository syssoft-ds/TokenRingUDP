### Setup verlief so weit hoffentlich richtig

## Aufgabe 2:
Ähnlich zur Vorlesung habe ich zunächst einen lokalen Token-Ring "geschaffen", 
indem ich die bereitgestellte .jar wie in der README erklärt, mehrmals in Windows 
PowerShell mit den jeweiligen Eigenschaften aufgerufen habe.
#### java -jar "TokenRingUDP.jar"

Und als Ausgabe z.B. unter anderem:
#### Token: seq=350, #members=2 (IP, 63364) (IP, 63365)


## Aufgabe 3:
Auf WireShark habe ich dann lange nach den Nachrichten des Rings gesucht, 
bin mir aber unsicher wirklich was Brauchbares gefunden zu haben. Displayfilterbefehle wie:

#### tcp.port== / udp.port==56101 
#### ip.src==192.168.0.0/16 and ip.dst==192.168.0.0/16

waren hier nicht hilfreich. Entweder, ich habe die Nachrichten des Rings nicht angezeigt bekommen, 
oder dessen Ports und IP weichen ab von jenen, die auf WireShark angezeigt werden. 
So habe ich überhaupt keine Nachricht gefunden, welche die Ports hatte, die die .jar zurückgegeben hatte.
Hier kam ich also nicht sehr weit.