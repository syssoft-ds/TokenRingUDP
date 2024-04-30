Rechnernetzwerke Übung 1 Sascha Schäfer 1545577:

Aufgabe 4; Bericht:

 Der Versuch an einem Tokenring mit anderen Teilnehmern wurde unternommen, der Erfolg war aber nur begrenzt und scheinbar zufällig.
 Ausgeführt auf dem localhost funktionierte die Ausführung aber einwandfrei und mir war es auch möglich mithilfe von Wireshark
 eine Aufzeichnung der gesendeten Pakete zu machen.   
 Die Datei der Aufzeichnung ist auffindbar als TokenRingUDP.pcapng     
 Die kommunikative funktion des Programs ist sichtbar anhand der gesendeten Nachrichten, welche in Wireshark unten einsehbar sind und
 liefert uns genau die Nachrichten die wir in der Konsole auch erwarten. 
 Man sieht dauch das Wireshark nur eines der 3 vorhanden Kanäle anzeigt; nämlich von Port 59292 -> 52170.
 Was fehlt ist der dritte Port welcher auch teil des rings war und die nachricht zurück zu 59292.

Verständniserläuterung vom eigentlichen Programmcode:

 Das Programm startet initial als der main kontaktpunkt und wenn als argument mit einer IP addresse und port gegeben sind
 verbindet es sich im ring und schickt das paket modifiziert kreisrund im 1 sekunden intervall zum nächsten teilnehmer.
 Alle teilnehmer warten auf das Paket und wenn keins reinkommt wird in einer sekunde nochmal gecheckt

