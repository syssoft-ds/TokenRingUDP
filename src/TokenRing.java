import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class TokenRing {

    private static Set<Token.Endpoint> removedNodes = new HashSet<>();

    private static void loop(DatagramSocket socket, String ip, int port, boolean first){
        LinkedList<Token.Endpoint> candidates = new LinkedList<>();
        if (first) {
            candidates.add(new Token.Endpoint(ip, port));
        }
        Token rc = null;
        while (true) {
            try {
                rc = Token.receive(socket);
                System.out.printf("Token: seq=%d, #members=%d", rc.getSequence(), rc.length());
                for (Token.Endpoint endpoint : rc.getRing()) {
                    System.out.printf(" (%s, %d)", endpoint.ip(), endpoint.port());
                }
                System.out.println();
                if (rc.length() == 1) {
                    Token.Endpoint next = rc.poll();
                    while(isRemoved(next) && !rc.getRing().isEmpty()) {
                        next = rc.poll();
                    }
                    if (isRemoved(next) && rc.getRing().isEmpty()) {
                        System.out.println("No more nodes in the ring");
                        continue;
                    }
                    candidates.add(next);
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
                while (isRemoved(next) && !rc.getRing().isEmpty()){
                    next = rc.poll();
                }
                if (isRemoved(next) && rc.getRing().isEmpty()){
                    System.out.println("No more nodes in the ring");
                    continue;
                }
                rc.append(next);
                rc.incrementSequence();
                Thread.sleep(1000);
                rc.send(socket, next);
            }
            // The ides is to catch the exception and try to send the token to the next node in the ring.
            catch (SocketTimeoutException e) {
                System.out.println("Timeout occurred, the previous node may have stopped sending the token");
                if (rc != null) {
                    Token.Endpoint next = rc.poll();
                    while (isRemoved(next) && !rc.getRing().isEmpty()) {
                        next = rc.poll();
                    }
                    if (isRemoved(next) && rc.getRing().isEmpty()) {
                        System.out.println("No more nodes in the ring");
                        continue;
                    }
                    candidates.add(next);
                }
            } catch (IOException e) {
                System.out.println("Error receiving packet: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static boolean isRemoved(Token.Endpoint endpoint) {
        return removedNodes.contains(endpoint);
    }

    public static void removeNode(Token.Endpoint endpoint) {
        removedNodes.add(endpoint);
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