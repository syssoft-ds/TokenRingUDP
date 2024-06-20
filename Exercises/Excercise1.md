# Übungsblatt 1 - Nicolas Düro

## Lokaler TokenRing
Das Aufbauen eines TokenRings auf einem lokalen Rechner ist simpel und funktioniert ohne Probleme. Das Filtern der 
Packages mithilfe von Wireshark ist ebenfalls einfach und funktioniert ohne Probleme. Dazu kann ein Display-Filter 
genutzt werden, für das lokale Ausführen reicht "udp and ip.src == 192.168.2.102 and ip.dst == 192.168.2.102", genauso 
wäre die Nutzung eines Aufnahmefilters möglich gewesen [Packet Sniffer lokaler Token Ring](../Pictures/local_TokenRing.png).
Beim lokalen Ausführen kam es zu keinen Schwierigkeiten und das Erstellen eines TokenRings auch mit mehreren Clients war
unproblematisch. Es kam auch nicht zu Abbrüchen bei der Ausführung.
 
## TokenRing in einem Netzwerk
Beim Erstellen eines TokenRings auf mehreren Rechnern, die sich im selben Netzwerk befinden, gab es zunächst 
Schwierigkeiten, so musste zunächst ein Teil der Firewall deaktiviert werden, um die Kommunikation zu ermöglichen.
Auch nachdem die Firewall deaktiviert wurde, gab es Probleme, da die Rechner nach wenigen Tokens die Verbindung abbrachen.
Um die Packages dieses Mal zu filtern, wurde folgender Display-Filter genutzt: "udp and (ip.src == 192.168.2.102 or
ip.src == 192.168.2.103) and (ip.dst == 192.168.2.102 or ip.dst == 192.168.2.103)"
Da ich nur zwei Rechner zur Verfügung hatte, konnte ich nur zwei Rechner in den Ring einbinden, in dem Bild ist der 
TokenRing zwischen zwei Rechnern zu sehen [Packet Sniffer zwei Geräte](../Pictures/Two_devices_TokenRing.png).

## Filtersyntax Wireshark
Die Filtersyntax ist leicht verständlich, es gibt verschiedene Möglichkeiten, um die Packages zu filtern und es gibt nicht
die eine korrekte Lösung. Beispielsweise wäre es auch möglich gewesen über die Ports die Packages zu filtern. Der 
Unterschied zwischen Display und Capture Filter ist einleuchtend, ich entschied mich zunächst nur Display Filter zu nutzen,
da ich so mehr experimentieren konnte und mit derselben Capture Datei arbeiten konnte. 