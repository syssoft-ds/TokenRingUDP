import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Iterator;

public class TokenRing {
    private static LinkedList<Token.Endpoint> candidates = new LinkedList<>();
    private static void loop(DatagramSocket socket, String ip, int port, boolean first){
        //LinkedList<Token.Endpoint> candidates = new LinkedList<>();
        if (first) {
            candidates.add(new Token.Endpoint(ip, port));
        }
        while (true) {
            try {
                Token rc = Token.receive(socket);
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
            }
            catch (IOException e) {
                System.out.println("Error receiving packet: " + e.getMessage());
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void removeNode(String ip, int port) {
        System.out.println("Trying to remove node: " + ip + ", " + port);
        Token.Endpoint endpointToRemove = new Token.Endpoint(ip, port);
        System.out.println("Endpoint to remove: " + endpointToRemove.ip() + ", " + endpointToRemove.port());
        boolean removed = candidates.remove(endpointToRemove);
        if (removed) {
            System.out.println("Node removed: " + ip + ", " + port);
        } else {
            System.out.println("Node not found: " + ip + ", " + port);
        }
    }

    public static void main(String[] args) {
        if (args.length == 3 && args[2].equals("remove")) { // Wenn "remove" als drittes Argument übergeben wird
            new TokenRing().removeNode(args[0], Integer.parseInt(args[1])); // Entferne den Knoten
        } else { // Andernfalls starte den Token-Ring wie zuvor
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                String ip = socket.getLocalAddress().getHostAddress();
                socket.disconnect();
                int port = socket.getLocalPort();
                System.out.printf("UDP endpoint is (%s, %d)\n", ip, port);
                if (args.length == 0) {
                    TokenRing.loop(socket, ip, port, true); // Starte den Token-Ring
                } else if (args.length == 2) {
                    Token rc = new Token().append(ip, port);
                    rc.send(socket, args[0], Integer.parseInt(args[1]));
                    TokenRing.loop(socket, ip, port, false); // Starte den Token-Ring
                } else {
                    System.out.println("Usage: \"java TokenRing\" or \"java TokenRing <ip> <port>\"");
                }
            } catch (SocketException e) {
                System.out.println("Error creating socket: " + e.getMessage());
            } catch (UnknownHostException e) {
                System.out.println("Error while determining IP address: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


}