# Einsatzprotokoll: Aufbau & Analyse eines TokenRingUDP-Systems (Testlauf, lokal)

**Projekt:** TokenRingUDP  
**Typ:** Proof-of-Concept / Systemtest  
**Durchgeführt von:** Riber Shamo Elias  
**Umgebung:** Lokales, vertrauenswürdiges Netzwerk (isoliert vom WAN)  
**Datum:** 2025-04-20  
**Ziel:** Verifikation des verteilten Token-Ring-Protokolls via UDP in heterogener Umgebung

---

## 🧪 Testziel & Methodik

Der Test dient der Verifikation eines dezentralen Token-Ring-Kommunikationsmodells mit folgenden Schwerpunkten:

- **Beitritt neuer Knoten**
- **Zyklische Token-Weitergabe**
- **Protokollverständlichkeit (Wireshark)**
- **Plattformunabhängigkeit**
- **Fehlertoleranz**

**Technische Parameter:**

- **Kommunikationsmodell:** UDP (verbindungsfrei, stateless)
- **Tokenstruktur:** JSON-formatiert, sequenziell weitergegeben
- **Join-Mechanismus:** dynamisch, manuell
- **Monitoring:** Logauszüge (macOS) und Wireshark-Auswertung

---

## ⚙️ Testaufbau (Abstrakt)

| Node # | Betriebssystem       | Rolle             | Anmerkung                     |
|--------|----------------------|-------------------|-------------------------------|
| 1      | Linux (Debian)       | Leader-Node       | Startpunkt, Host der Session  |
| 2      | macOS (Apple Silicon)| Peer + Logger     | Beobachtung + Protokollierung |
| 3      | Windows 11           | Dritter Teilnehmer| Test auf Cross-OS-Kompatibilität |

Alle Geräte waren im selben Subnetz `192.168.178.x` verbunden, ohne Zugriff aufs Internet.

---

## 📟 Beobachtung via macOS-Terminal (Node 2)

Nach Start des Clients mit:

```bash
java -jar TokenRingUDP.jar 192.168.178.20 34817
```

folgte die zyklische Übergabe des Tokens. Anbei exemplarisch:

```bash
Token: seq=2, #members=2 (Leader + Self)
...
Token: seq=170, #members=3 (nach Beitritt Windows)
```

Wireshark-Analyse

Capture-Filter:
```bash
udp port 34817
```
```bash
Display-Filter:
udp && ip.addr == 192.168.178.20
```
Ergebnisse:

JSON-Token konnten vollständig und lesbar erfasst werden.

Zyklische Updates inkl. Mitgliedsliste ersichtlich.

Ring wurde dynamisch durch Dritten (Windows-Knoten) erweitert.