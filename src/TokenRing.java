import java.io.IOException;
import java.net.*;
import java.util.LinkedList;


public class TokenRing {

    static LinkedList<Token.Endpoint> candidates = new LinkedList<>();

    private static void loop(DatagramSocket socket, String ip, int port, boolean first) throws IOException {
        Token.Endpoint originalEndpoint = new Token.Endpoint(ip, port);
        if (first) {
            candidates.add(originalEndpoint);
        }
        Token rc = null;
        Token.Endpoint next = null;
        while (true) {
            try {
                rc = Token.receive(socket, 100000); // Warten auf Token fuer 100 sec

                System.out.printf("Token: seq=%d, #members=%d", rc.getSequence(), rc.length());
                for (Token.Endpoint endpoint : rc.getRing()) {
                    System.out.printf(" (%s, %d)", endpoint.ip(), endpoint.port());
                }
                System.out.println();

                Thread.sleep(500);
                Token.bestaetigungSchicken(socket, rc.getLastSender(), rc); //Bestaetigungsnachricht an den letzten Sender schicken

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
                candidates.clear();
                next = rc.poll();
                rc.append(next);
                rc.incrementSequence();

                if(next.equals(originalEndpoint)) { //stellt sicher das es nicht an sich selbst schickt, nimmt dann naechsten Knoten
                    next = rc.poll();
                    rc.append(next);
                }

                Thread.sleep(1000);
                rc.setLastSender(originalEndpoint); //setzte diesen Knoten als letzten Sender
                rc.send(socket, next);
                bestaetigungErhalten(socket);
            }
            catch (SocketTimeoutException e) {
                if (rc != null) {
                    System.out.println("Timeout: Removing node from the ring");
                    if (!next.equals(originalEndpoint)) { // ueberpruefen Sie, ob der naechste Knoten nicht der urspruengliche Knoten ist
                        rc.remove(next);
                        next = rc.poll(); // Holen Sie den naechsten Knoten
                        rc.append(next);
                        rc.send(socket, next); // Senden Sie das Token an den naechsten Knoten
                        bestaetigungErhalten(socket);
                    }
                }
            }
            catch (IOException e) {
                System.out.println("Error receiving packet: " + e.getMessage());
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void bestaetigungErhalten(DatagramSocket socket) throws IOException {
        Token nt = Token.receive(socket, 5000); // Warten auf Bestaetigung fuer 5 sec
        Token.Endpoint firstEndpoint = nt.first();
        String ip2 = firstEndpoint.ip();
        int port2 = firstEndpoint.port();
        if ("0.0.0.0.0".equals(ip2) && port2 == 0) {
            System.out.println("Bestaetigungsnachricht ist korrekt");
        } else {
            System.out.println("Bestaetigungsnachricht ist nicht korrekt, also neuer Knoten!");
            if (nt.length() == 1) {
                candidates.add(nt.poll());
                Token.bestaetigungSchicken(socket, nt.lastsenderendpoint, nt);
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
                loop(socket,ip,port,true);
            }
            else if (args.length == 2) {
                Token rc = new Token().append(ip,port);
                rc.setLastSender(new Token.Endpoint(ip,port));
                rc.send(socket,args[0],Integer.parseInt(args[1]));
                bestaetigungErhalten(socket);
                loop(socket,ip,port,false);
            }
            else {
                System.out.println("Usage: \"java TokenRing\" or \"java TokenRing <ip> <port>\"");
            }
        }
        catch (SocketException e) {
            System.out.println("Error creating socket: " + e.getMessage());
        }
        catch (UnknownHostException e) {
            System.out.println("Error while determining IP address: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }
}