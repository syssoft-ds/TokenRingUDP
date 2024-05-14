# Übungsblatt 2 - Nicolas Düro

## Aufgabe 1: Wireshark Scavenger Hunt
Aufgrund des ersten Hinweises habe ich nach allen Packages gesucht, welche ein "password" enthalten. Dazu habe ich den
Display-Filter "frame contains password" genutzt. Das Ergebnis war, dass zwei Packages gefunden wurden, welche das 
"password" dzh20szq enthalten.
Der zweite Hinweis "One HTTP error is different", dazu gab ich testweise einfach als Display Filter "http.response.code"
ein. Dies filterte so viele Packages raus, sodass ich bei reiner Betrachtung den nächsten Code finden konnte. Der 
gesuchte Code war ra6rgv7h.
Der nächste Hinweis "Hiddeninping", um diesen Hinweis zu lösen, stellte ich den Display-Filter "icmp" ein. Dieser Filterte
alle Packages raus, die mit ICMP zu tun haben. Das Ergebnis war, dass ein Package den Code nd7hark3 enthielt und das zum
Lösungcode Q29uZ3JhdHNfVGhpc19Jc190aGVfQm9udXMh führte.

## Tracert zur anu.edu.au

    C:\Users\nicol>tracert www.anu.edu.au
    Routenverfolgung zu terra-web.anu.edu.au [130.56.67.33]
    über maximal 30 Hops:
    1     2 ms     2 ms     2 ms  Speedport.ip [192.168.2.1]
    2    14 ms    13 ms    12 ms  tri0001aihk001.versatel.de [94.134.198.81]
    3    14 ms    17 ms    14 ms  224-221-142-46.pool.kielnet.net [46.142.221.224]
    4    15 ms    16 ms    15 ms  62.67.18.94
    5    16 ms    16 ms    16 ms  lag-108.ear4.Frankfurt1.Level3.net [62.67.18.93]
    6     *        *        *     Zeitüberschreitung der Anforderung.
    7    30 ms    30 ms    38 ms  AARNET-PTY.ear4.London1.Level3.net [217.163.113.74]
    8   505 ms   504 ms     *     et-0-3-0.pe1.actn.act.aarnet.net.au [113.197.15.11]
    9     *        *        *     Zeitüberschreitung der Anforderung.
    10     *        *        *     Zeitüberschreitung der Anforderung.
    11     *        *        *     Zeitüberschreitung der Anforderung.
    12     *        *        *     Zeitüberschreitung der Anforderung.
    13     *        *        *     Zeitüberschreitung der Anforderung.
    14     *        *        *     Zeitüberschreitung der Anforderung.
    15     *        *        *     Zeitüberschreitung der Anforderung.
    16     *        *        *     Zeitüberschreitung der Anforderung.
    17     *        *        *     Zeitüberschreitung der Anforderung.
    18     *        *        *     Zeitüberschreitung der Anforderung.
    19     *        *        *     Zeitüberschreitung der Anforderung.
    20     *        *        *     Zeitüberschreitung der Anforderung.
    21     *        *        *     Zeitüberschreitung der Anforderung.
    22     *        *        *     Zeitüberschreitung der Anforderung.
    23     *        *        *     Zeitüberschreitung der Anforderung.
    24     *        *        *     Zeitüberschreitung der Anforderung.
    25     *        *        *     Zeitüberschreitung der Anforderung.
    26     *        *        *     Zeitüberschreitung der Anforderung.
    27     *        *        *     Zeitüberschreitung der Anforderung.
    28     *        *        *     Zeitüberschreitung der Anforderung.
    29     *        *        *     Zeitüberschreitung der Anforderung.
    30     *        *        *     Zeitüberschreitung der Anforderung.

Die Abfrage startet über meinen Router speedport.ip und geht über versatel und kielnet, welche Router meines Internetanbieters
sind, weiter an level3 ein großer Interprovider, welchen ein Großteil für das Backbone des Internets bereitstellen. Der 
letzte Punkt den mein tracert erreicht ist das Australian Academic and Research Network (AARNet), danach bricht die 
Verbindung ab und es kommt zu Zeitüberschreitungen.

## Filtersyntax Wireshark
    Starting Nmap 7.95 ( https://nmap.org ) at 2024-05-14 16:16 Mitteleuropäische Sommerzeit
    Nmap scan report for scanme.nmap.org (45.33.32.156)
    Host is up (0.17s latency).
    Other addresses for scanme.nmap.org (not scanned): 2600:3c01::f03c:91ff:fe18:bb2f
    Not shown: 996 closed tcp ports (reset)
    PORT      STATE SERVICE
    22/tcp    open  ssh
    80/tcp    open  http
    9929/tcp  open  nping-echo
    31337/tcp open  Elite
    
    Nmap done: 1 IP address (1 host up) scanned in 1049.92 seconds


    Starting Nmap 7.95 ( https://nmap.org ) at 2024-05-14 18:49 Mitteleuropäische Sommerzeit
    Nmap scan report for scanme.nmap.org (45.33.32.156)
    Host is up (0.17s latency).
    Other addresses for scanme.nmap.org (not scanned): 2600:3c01::f03c:91ff:fe18:bb2f
    Not shown: 996 closed tcp ports (reset)
    PORT      STATE SERVICE
    22/tcp    open  ssh
    80/tcp    open  http
    9929/tcp  open  nping-echo
    31337/tcp open  Elite
    
    Nmap done: 1 IP address (1 host up) scanned in 5.80 seconds

Bei UDP dauert die Suche deutlich länger, dass liegt daran das UDP keine Bestätigung der Pakete erwartet und somit nur 
eine Zeitbegrenzung festlegt. Bei TCP hingegen wird auf eine Bestätigung gewartet, wenn diese negativ ausfällt geht die 
Suche direkt weiter.