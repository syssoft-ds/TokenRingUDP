# Rechnernetze 2024-SS - Übungsblatt 02
`#19D9DA-s4joregn`

## Aufgabe 1 - Schnitzeljagd Wireshark


|No.| Result  |  Filter |   
|---|---|---| 
| 251  | https://www.lanwan.ninja/dzh20szq/  | Edit>Find>String>"password"  |   |   |
| 215  | https://www.lanwan.ninja/ra6rgv7h/  | http  |   |   |
| 343  | https://www.lanwan.ninja/nd7hark3/  | icmp  |   |   |
| Solution | Q29uZ3JhdHNfVGhpc19Jc190aGVfQm9udXMh||


## Aufgabe 2 - Traceroute
`Tracerout` ermöglicht den Pfad zu einer bestimmten IP-Adresse/Domain herauszufinden. Die Ausgabe von tracert gibt Zeilenweise  Informationen über den jeweiligen Netzwerkknoten und die Anfragedauer an. In jeder Zeile findet man die ID des jeweiligen Hops, sowie der Hostname mit IP-Adresse. Ebenso werden für jeden Hop drei Round-trip time (RTT) angegeben. Dies ist die Zeit von Afrage zu Andwort (einmal im Kreis).

| Hop # | RTT 1   | RTT 2    | RTT 3| Name/IP Address |
|---|---    |---    |---    |---                                |
```
tracert tracert www.anu.edu.au
```


```
Routenverfolgung zu terra-web.anu.edu.au [130.56.67.33]
über maximal 30 Hops:

  1    30 ms    30 ms    30 ms  vvpn4.uni-trier.de [136.199.4.1]
  2    30 ms    30 ms    30 ms  vzimks2.uni-trier.de [136.199.189.1]
  3    30 ms    30 ms    30 ms  gw.uni-trier.de [136.199.42.2]
  4    33 ms    31 ms    31 ms  g-uni-tr-1.rlp-net.net [217.198.241.193]
  5    32 ms    31 ms    31 ms  g-hbf-tr-1.rlp-net.net [217.198.240.73]
  6    33 ms    33 ms    33 ms  g-hbf-kl-2.rlp-net.net [217.198.240.58]
  7    34 ms    33 ms    34 ms  g-uni-ma-1.rlp-net.net [217.198.240.118]
  8    34 ms    34 ms    35 ms  g-telecity-3.rlp-net.net [217.198.247.21]
  9    34 ms    34 ms    34 ms  g-telecity-4.rlp-net.net [217.198.247.198]
 10    35 ms    34 ms    34 ms  g-telecity-3.rlp-net.net [217.198.247.197]
 11    34 ms    37 ms    34 ms  g-interxion-3.rlp-net.net [217.198.240.5]
 12    33 ms    34 ms    34 ms  g-interxion-4.rlp-net.net [217.198.246.254]
 13    35 ms    34 ms    34 ms  xe-1-2-0.mpr1.fra4.de.above.net [80.81.194.26]
 14     *      166 ms     *     ae12.cs1.fra6.de.eth.zayo.com [64.125.26.172]
 15   169 ms   170 ms   167 ms  ae2.cs1.ams17.nl.eth.zayo.com [64.125.29.59]
 16     *        *        *     Zeitüberschreitung der Anforderung.
 17     *        *        *     Zeitüberschreitung der Anforderung.
 18     *        *        *     Zeitüberschreitung der Anforderung.
 19     *        *        *     Zeitüberschreitung der Anforderung.
 20     *        *        *     Zeitüberschreitung der Anforderung.
 21   167 ms   167 ms   171 ms  ae2.cs1.sea1.us.eth.zayo.com [64.125.29.26]
 22   167 ms   167 ms   167 ms  ae27.mpr1.sea1.us.zip.zayo.com [64.125.29.1]
 23   167 ms   167 ms   166 ms  64.125.193.130.i223.above.net [64.125.193.130]
 24   371 ms   371 ms   371 ms  et-10-0-5.170.pe1.brwy.nsw.aarnet.net.au [113.197.15.62]
 25   370 ms   369 ms   370 ms  et-1-1-0.pe1.rsby.nsw.aarnet.net.au [113.197.15.12]
 26   370 ms   369 ms   370 ms  et-0-3-0.pe1.actn.act.aarnet.net.au [113.197.15.11]
 27     *        *        *     Zeitüberschreitung der Anforderung.
 28     *        *        *     Zeitüberschreitung der Anforderung.
 29     *        *        *     Zeitüberschreitung der Anforderung.
 30     *        *        *     Zeitüberschreitung der Anforderung.

Ablaufverfolgung beendet.
```








## Aufgabe 3 - Nmap

```
nmap -h
```
#### UDP Scan
```
nmap -sU scanme.nmap.org
```
Die UDP-Scans benötigen sehr viel Zeit - aber das Programm läuft. Wireshark mit Display Filter 
`ip.src == 45.33.32.156 and udp`
zeigt den Fortschritt an.

```
Nmap scan report for scanme.nmap.org (45.33.32.156)
Host is up (0.16s latency).
Not shown: 988 closed udp ports (port-unreach)
PORT     STATE         SERVICE
67/udp   open|filtered dhcps
68/udp   open|filtered dhcpc
123/udp  open          ntp
135/udp  open|filtered msrpc
137/udp  open|filtered netbios-ns
138/udp  open|filtered netbios-dgm
139/udp  open|filtered netbios-ssn
161/udp  open|filtered snmp
162/udp  open|filtered snmptrap
445/udp  open|filtered microsoft-ds
1900/udp open|filtered upnp
5353/udp open|filtered zeroconf

Nmap done: 1 IP address (1 host up) scanned in 1092.59 seconds
```
#### TCP Scan
```
nmap -sS scanme.nmap.org
```
```
Nmap scan report for scanme.nmap.org (45.33.32.156)
Host is up (0.17s latency).
Not shown: 996 closed tcp ports (reset)
PORT      STATE SERVICE
22/tcp    open  ssh
80/tcp    open  http
9929/tcp  open  nping-echo
31337/tcp open  Elite

Nmap done: 1 IP address (1 host up) scanned in 11.68 seconds
```


### Geschwindigkeitsunterschiede

#### TCP-Scan (`nmap -sS`)
TCP-Scans sind schneller als UDP-Scans, da TCP-Scans direkt eine SYN-ACK-Antwort des Ziels erhalten, ohne eine vollständige Verbindung herzustellen.

#### UDP-Scan (`nmap -sU`)
Im Gegensatz dazu warten UDP-Scans auf eine Antwort vom Ziel, was mehr Zeit in Anspruch nimmt, da UDP-Anfragen nicht so schnell bestätigt werden können wie TCP-Anfragen.

### Möglichkeiten, den Scan schneller durchzuführen
- Mehrere Scans gleichzeitg ausführen.
- nur bestimmte Ports abfragen 
- Nmap-Optionen  `-T4` (Tempo erhöhen) oder `-F` (kurzer Scan)




## Aufgabe 4 - Programmieren
Verändern Sie den Token Ring derart, dass er auch funktioniert, wenn einer der Knoten ausgefallen ist.  Gehen Sie Schritt für
Schritt vor.
### Ideen zur Umsetzung
1.  Jeder Knoten sendet einen Heartbeat zum Folgeknoten. Kommt von einem Knoten kein Heartbeat in einer bestimmten Zeit an, so wird davon ausgegangen, dass dieser nicht mehr erreichbar ist. Dieser Knoten wird dann aus der Liste der Endpoints entfernt. 
2. Unterschiedliche Pfade
4. Berechnen der zu erwarteten Dauer für einen Ring-Durchlauf, nach überschrittener Zeit neustarten.
5. FDDI

## Abgabe
Pull Request.