import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

public class TokenRing {

    private static void loop(DatagramSocket socket, String ip, int port, boolean first) {
        LinkedList<Token.Endpoint> candidates = new LinkedList<>();
        if (first) {
            candidates.add(new Token.Endpoint(ip, port));
        }

        Token lastToken = null;
        Token.Endpoint PrevRecFrom = null;
        Token.Endpoint RecFrom = null;
        DatagramPacket packet;


        while (true) {
            Token rc = null;

            try {
                packet = Token.receive(socket);
                RecFrom = new Token.Endpoint(packet.getAddress().getHostAddress(), packet.getPort());
                rc = Token.fromJSON(Token.genJSON(packet));
                lastToken = rc; // Token aktualisieren
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout: Kein Token empfangen.");

                // Wenn wir einen letzten Token kennen, verwenden wir ihn weiter
                if (lastToken != null && lastToken.length() > 0) {
                    System.out.println("Vermutlich ist der vorherige Knoten ausgefallen. Übernehme Token.");
                    rc = lastToken; // wir nutzen das gespeicherte Token weiter
                } else {
                    System.out.println("Kein Token bekannt. Warte weiter...");
                    continue;
                }
            } catch (IOException e) {
                System.out.println("Fehler beim Empfangen: " + e.getMessage());
                continue;
            }

            // Token-Status ausgeben
            System.out.printf("Token: seq=%d, #members=%d", rc.getSequence(), rc.length());
            for (Token.Endpoint endpoint : rc.getRing()) {
                System.out.printf(" (%s, %d)", endpoint.ip(), endpoint.port());
            }
            System.out.println();
            if (rc.length() == 1) {
                candidates.add(rc.poll());
                if (!first) {
                    continue;
                }
            }
            first = false;
            for (Token.Endpoint candidate : candidates) {
                rc.append(candidate);
            }
            // Wachsende Phase (erstes Mal im Ring)
            if (PrevRecFrom!=null){
                if (!PrevRecFrom.equals(RecFrom)){

                    System.out.printf("Prev: %s, Curr: %s", PrevRecFrom, RecFrom);
                    rc.remove(PrevRecFrom);
                }
            }
            PrevRecFrom = RecFrom;

            // Neue Kandidaten in den Ring einfügen

            boolean sent = false;

            // Versuche, Token weiterzuleiten
            while (!sent && rc.length() > 0) {
                Token.Endpoint next = rc.poll();

                // Nicht an sich selbst zurückschicken (nur wenn allein im Ring)
                if (next.ip().equals(ip) && next.port() == port && rc.length() > 0) {
                    rc.append(next);
                    continue;
                }
                System.out.printf("Next: %s:%d\n", next.ip(), next.port());
                try {
                    rc.append(next);
                    rc.incrementSequence();
                    Thread.sleep(1000);
                    rc.send(socket, next);
                    lastToken = rc; // aktuellen Token speichern
                    sent = true;
                }catch (SocketTimeoutException e) {
                    System.out.printf("Knoten %s:%d nicht erreichbar, entferne aus dem Ring.\n", next.ip(), next.port());
                    // next nicht wieder einfügen → wird entfernt
                }
                catch (IOException e) {
                    System.out.printf("Knoten %s:%d nicht erreichbar, entferne aus dem Ring.\n", next.ip(), next.port());
                    // next nicht wieder einfügen → wird entfernt
                } catch (InterruptedException e) {
                    System.out.println("Thread unterbrochen: " + e.getMessage());
                }
            }

            if (!sent) {
                System.out.println("Kein erreichbarer Knoten mehr im Ring!");
            }
        }
    }

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
            socket.disconnect();
            int port = socket.getLocalPort();
            System.out.printf("UDP endpoint is (%s, %d)\n", ip, port);

            if (args.length == 0) {
                loop(socket, ip, port, true);
            }
            else if (args.length == 2) {
                Token rc = new Token().append(ip, port);
                rc.send(socket, args[0], Integer.parseInt(args[1]));
                loop(socket, ip, port, false);
            }
            else {
                System.out.println("Usage: java TokenRing oder java TokenRing <ip> <port>");
            }
        }
        catch (SocketException e) {
            System.out.println("Fehler beim Erstellen des Sockets: " + e.getMessage());
        }
        catch (UnknownHostException e) {
            System.out.println("Fehler beim Ermitteln der IP-Adresse: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("IO-Fehler: " + e.getMessage());
        }
    }
}
