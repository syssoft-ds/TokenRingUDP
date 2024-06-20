import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;


public class TokenRing {

    private static Token expecting = null;

    private synchronized static void receivedAnswer(Token t) {
        if (expecting.acceptAnswer(t)) expecting = null;
    }

    private static void loop(DatagramSocket socket, String ip, int port, boolean first){
        LinkedList<Token.Endpoint> candidates = new LinkedList<>();
        int lastSequence = 0;
        while (true) {
            try {
                Token rc = Token.receive(socket);
                if (rc.getSequence() < 0) {
                    receivedAnswer(rc);
                    continue;
                }
                if (rc.getSequence() > 0 && rc.getSequence() <= lastSequence) continue;
                if (rc.getSequence() > 0) lastSequence = rc.getSequence();
                System.out.printf("Token: seq=%d, #members=%d", rc.getSequence(), rc.length());
                for (Token.Endpoint endpoint : rc.getRing()) {
                    System.out.printf(" (%s, %d)", endpoint.ip(), endpoint.port());
                }
                System.out.println();
                if (rc.length() == 1) {
                    candidates.offer(rc.poll());
                    if (!first) {
                        if (candidates.peek().equals(new Token.Endpoint(ip,port))) {
                            first = true;
                        }
                        continue;
                    } else {
                        candidates.offer(new Token.Endpoint(ip, port));
                    }
                }
                first = false;
                while (!candidates.isEmpty()) {
                    rc.append(candidates.poll());
                }
                Token.Endpoint next = rc.poll();
                rc.append(next);
                rc.incrementSequence();
                Thread.sleep(1000);
                rc.send(socket, next);
                rc.expectAnswer(socket, next);
                expecting=rc;
                if (rc.getSequence() > 1) rc.reply(socket);
            }
            catch (IOException e) {
                System.out.println("Error receiving packet: " + e.getMessage());
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
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