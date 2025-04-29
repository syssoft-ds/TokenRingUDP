# Rechnernetze Hausaufgabe 1

## Aufgabe 1

Ich habe das Projekt zuert in ein äquivalentes gradle Projekt geändert.
Um den TokenRing zu starten, führe ich den Befehl `./gradlew run` in einem Terminal im root Verzeichnis des Projektes aus. Anschließend kann ich ein zweites Terminal öffnen und mit dem Befehl `./gradlew run --args="<ip> <port>"` dem Tokenring beitreten. Die beiden Programme schicken sich nun abwechselnd Pakete zu.

Das Programm versucht zuerst eine Verbindung zum Google DNS Sercer 8.8.8.8 auf Port 10002 zu erzeugen.
Da UDP ein verbindungsloses Protokoll ist, wird hier nur sichergestellt, dass Pakete nur von dieser Adresse akzeptiert und nur an diese Adresse gesendet werden können.
Der Hauptgrund für das Aufrufen von `connect()` ist jedoch, dass so das Betriebssystem dem `DatagramSocket` die richtige ip und eine passende Portnummer zuweist.
Die `disconnect()` Methode hebt nun die oben genannte Restriktion auf jedoch bleiben ip und Portnummer erhalten.

Ist das Programm der erste Knoten im TokenRing, wird nun auf diesem Port gewartet, bis ein Paket ankommt.
Ist das Programm nicht der erste Knoten im TokenRing, wird ein Paket an einen bereits beigetretenen Knoten gesendet. Dieser fügt den neuen Knoten in eine Kandidatenliste ein, die später dem `TokenRing` hinzugefügt wird.

Der `TokenRing` ist im Wesentlichen eine `Queue`, deren Kopf auf den Knoten zeigt, an den das nächste Paket gesendet werden soll. Wird der `TokenRing` auf einem `DatagrammSocket` empfangen wird er ausgelesen. Das erste Element, also der nächste Knoten im Ring, wird extrahiert und in einer lokalen Variable gespeichert. Anschließend wird dieser Knoten am Ende der `Queue` eingefügt.
Der `TokenRing` wird in JSON kodiert und an den nächsten Knoten gesendet.

## Aufgabe 2

Wir haben versucht vor der Vorlesung einen TokenRing aufzubauen.
Dies hat leider nicht funktioniert, da wahrscheinlich die Firewall des Kommilitonen keine entsprechende Ausnahme konfiguriert hatte.
Später haben wir versucht den TokenRing aus unseren jeweiligen Heimnetzen aufzubauen.
Trotz richtig konfigurierter Firewall und entsprechendes Port-Forwarding hat dies leider auch nicht wie gewünscht geklappt.
Zu guter Letzt haben sich ein Kommilitone und ich bei mir Zuhause getroffen und den TokenRing im Heimnetz aufgebaut.
Dies hat funktioniert und die folgende Ausgabe produziert:

```
Sending {"sequence":0,"ring":[{"ip":"192.168.0.21","port":57723}]} to 192.168.0.237:39405
Received {"sequence":2,"ring":[{"ip":"192.168.0.237","port":39405},{"ip":"192.168.0.21","port":57723}]} from 192.168.0.237:39405
Token: seq=2, #members=2 (192.168.0.237, 39405) (192.168.0.21, 57723)
```

## Aufgabe 3

Wir können Wireshark dazu nutzen Netzwerktraffic aufzuzeichnen.
Zuerst müssen wir entscheiden welches Netzwerk wir aufzeichnen wollen.
Um Kommunikation auf unterschiedlichen Geräten aufzuzeichnen, müssen wir ein Netzwerkgerät auswählen. Für Kommunikation von Programmen auf demselben Rechner muss das Loopback device gewählt werden.
Diese Aufzeichnung können wir nachher Filtern. Zum Beispiel können wir nach Paketen filtern, die das UDP Prokotoll verwenden. Dies können wir mit folgendem Filter erreichen.
> udp

Dies filtert jedoch nicht Protokolle, die auf udp aufbauen. Um nur reine UDP Protokolle zu erhalten, können wir auf UDP aufbauende Pakte explizit ausschließen. Zum Beispiel mit dem Filter
>udp && !(dns || db-lsp-disc || quic || mdns || ssdp || ntp)

Wollen wir nur bestimmte Pakete aufzeichnen können wir eine sogenannten Capturefilter verwenden. Dieser verwendet jedoch ein anderes Format wie der Displayfilter und ist limitierter. Wir können wieder nach dem UDP Protokoll filtern mit dem Filter
>udp

Wollen wir Protokolle, die auf UDP aufbauen filtern können wir uns eines Tricks bedienen und bestimmte Portnummern aus der Aufzeichnung filtern.
Zum Beispiel mit derm Filter
>udp and not port 53 and not port 5353,

welcher DNS Anfragen auf Port 53 und mDNS Anfragen auf 5353 ausblendet.

Wir können nun die Aufzeichung der Pakete auswerten.
Die Zusammenfassung eines Pakets dieht wie folgt aus.
>76 5.006948038 192.168.0.237 192.168.0.237 UDP 138 60718 → 47275 Len=96

Am Anfang steht die Nummer des Pakets, die von Beginn der Aufzeichnung die Pakete aufzählt `76`.
Dahinter steht die Zeit, die von Beginn der Aufzeichung bis zum Erhalt des Paketes verstrichen ist `5.006948038s`. Es folgen die Adresse des Absenders `192.168.0.237` und die des Empfängers `192.168.0.237`. Anschließend können wir sehen um welches Protokoll es sich handelt. Es folgt die Länge des gesamten Pakets `138 bytes`.
Danach sehen wir von welchem Port auf welchen gesendet wurde `60718 → 47275` und zu guter Letzt die Länge der tatsächlichen Nachricht `96 bytes`

Im Fenster unter der Liste der Pakete können wir detailiertet Informationen über das Paket einsehen. Zum Beispiel können wir das Paket als Hexdump betrachten.

```
0000   00 00 00 00 00 00 00 00 00 00 00 00 08 00 45 00
0010   00 7c 09 69 40 00 40 11 ad dd c0 a8 00 ed c0 a8
0020   00 ed ed 2e b8 ab 00 68 83 a4 7b 22 73 65 71 75
0030   65 6e 63 65 22 3a 38 38 2c 22 72 69 6e 67 22 3a
0040   5b 7b 22 69 70 22 3a 22 31 39 32 2e 31 36 38 2e
0050   30 2e 32 33 37 22 2c 22 70 6f 72 74 22 3a 36 30
0060   37 31 38 7d 2c 7b 22 69 70 22 3a 22 31 39 32 2e
0070   31 36 38 2e 30 2e 32 33 37 22 2c 22 70 6f 72 74
0080   22 3a 34 37 32 37 35 7d 5d 7d
```

Dekodiert ist in diesem Paket die folgende Nachricht.

```
{"sequence":83,"ring":[{"ip":"192.168.0.237","port":47275},{"ip":"192.168.0.237","port":60718}]}
```

## Aufgabe 4

Name des Branches: S25-CN-PS
