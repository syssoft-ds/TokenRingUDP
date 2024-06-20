import java.io.IOException;
import java.net.*;
import java.util.LinkedList;


public class TokenRing {

    //check if the endpoint is reachable
    private static boolean isEndpointReachable(DatagramSocket socket, Token token, Token.Endpoint endpoint) {
        try {
            token.send(socket, endpoint);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void loop(DatagramSocket socket, String ip, int port, boolean first) {
        LinkedList<Token.Endpoint> candidates = new LinkedList<>();
        if (first) {
            candidates.add(new Token.Endpoint(ip, port));
        }
        while (true) {
            try {
                if(first){
                    System.out.println("Waiting for token...");
                    //check if all the endpoints in the ring are reachable
                    //only the mother node will do this
                    Token rc = Token.receive(socket);
                    for (Token.Endpoint endpoint : rc.getRing()) {
                        if (!isEndpointReachable(socket, rc, endpoint)) {
                            System.out.printf("Endpoint %s:%d is not reachable\n", endpoint.ip(), endpoint.port());
                            rc.remove(endpoint); // remove the unreachable endpoint from the ring in the token object
                        //doesn't work as expected...
                        }
                    }
                }
                long startTime = System.currentTimeMillis(); // get the start time

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

                long endTime = System.currentTimeMillis(); // get the end time

                // calculate the elapsed time in seconds
                long elapsedTime = (endTime - startTime) / 1000;

                // if the elapsed time is greater than Ring.size * 2 seconds, print a message
                if (elapsedTime > rc.length() * 2) {
                    System.out.println("The Ring takes too long");
                }
            } catch (IOException e) {
                System.out.println("Error receiving packet: " + e.getMessage());
            } catch (Exception e) {
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
                loop(socket, ip, port, true);
            } else if (args.length == 2) {
                Token rc = new Token().append(ip, port);
                rc.send(socket, args[0], Integer.parseInt(args[1]));
                loop(socket, ip, port, false);
            } else {
                System.out.println("Usage: \"java TokenRing\" or \"java TokenRing <ip> <port>\"");
            }
        } catch (SocketException e) {
            System.out.println("Error creating socket: " + e.getMessage());
        } catch (UnknownHostException e) {
            System.out.println("Error while determining IP address: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }
}