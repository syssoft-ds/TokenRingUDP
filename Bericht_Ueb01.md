# Bericht Ueb1 David Jacobs 1619007

- ausgeführt und getestet unter Windows 10

## A1/A3

- Clonen des Repositories *TokenRingUDP*  und Import des Projekts über IntelliJ IDEA
- Installation von Maven und Java 21 per SDKMAN in Git Bash
- Ausführen des Projekts über GitBash-Instanzen
- 1. Node gestartet
- 2. Node gestartet und an 1. Node angeschlossen über lokale IP und Port
- Token werden erfolgreich weitergegeben (Portfreigabe in Windows Firewall notwendig)
- Wireshark zeigt UDP-Pakete zwischen den Nodes

## A2/A3

- Erste Versuche TokenRing mit Kommilitonen über remote IP aufzubauen misslungen
- Wireshark zeigt keine eingehenden UDP-Pakete

- (Interessehalber): Versuch TokenRing aus 2 Nodes unter Benutzung der eigenen remote IP aufzubauen, die von je einer Instanz von Git Bash am selben Rechner gestartet wurden
  gelingt nach Portfreigabe in Router und Windows Firewall. Wireshark zeigt, dass die Pakete von der remote IP auf die lokale IP umgelenkt werden.
  -> Kommunikation erfolgt scheinbar innerhalb des lokalen NW oder sogar auf dem Rechner selbst(vom OS gemanaged?)
