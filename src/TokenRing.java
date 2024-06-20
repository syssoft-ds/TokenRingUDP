import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.LinkedList;


public class TokenRing {

    //Statische Variable um neu Zeit für hinzugefügte Knoten zu berücksichtigen
    private final static int newNodeBonus = 2;
    private static void loop(DatagramSocket socket, String ip, int port, boolean first){
        LinkedList<Token.Endpoint> candidates = new LinkedList<>();

        int avgRoundTrip = 10000;
        int[] roundTrip = {0};
        long[] startTime = {0};
        LinkedList<Integer> roundTrips = new LinkedList<>(Collections.nCopies(5, avgRoundTrip));
        boolean[] nextDead = {false};

        if (first) {
            Token.Endpoint fistNode = new Token.Endpoint(ip, port);
            candidates.add(fistNode);
        }

        //Timeout Mechanismus prüft ob ein Knoten zu lange braucht um zu antworten
        new Thread(() -> {
            //Prüfe regelmäßig ob die aktuelle Wartezeit, die durchschnittliche Wartezeit übersteigt
            while (Thread.currentThread().isAlive()){
                try {
                    int art = (int)((roundTrips.subList(0,roundTrips.size()-1).stream().reduce(0, Integer::sum) / roundTrips.size()-1) * newNodeBonus);
                    System.out.println("avg: " + art + " rt: " + roundTrip[0]);
                    if (roundTrip[0] > 0 && roundTrip[0] > art) nextDead[0] = true;
                    Thread.sleep(art);
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }).start();

        while (true) {
            try {

                startTime[0] = System.currentTimeMillis();
                Token rc = Token.receive(socket);

                //Aktualisiere Wartezeit
                roundTrip[0] = (int) (System.currentTimeMillis() - startTime[0]);
                roundTrips.add(roundTrip[0]);
                roundTrips.poll();

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
                candidates.clear();
                Token.Endpoint next = rc.poll();
                rc.append(next);
                rc.incrementSequence();
                Thread.sleep(1000);
                rc.send(socket, next);

                //Falls der nächste Knoten nicht antwortet, wird er entfernt
                if (nextDead[0]) {
                    System.out.println("Node is dead, removing node");
                    removeNode(rc, next);
                    nextDead[0] = false;
                    next = rc.poll();
                    System.out.println("Sending token to next node" + next.ip() + ":" + next.port());
                    rc.send(socket, next);
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

    //Entferne Knoten aus Token
    public static void removeNode(Token token, Token.Endpoint endpointToRemove) {
        token.delete(endpointToRemove);
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
                rc.send(socket,args[0],Integer.parseInt(args[1]));
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