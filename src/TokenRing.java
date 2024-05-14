import java.io.IOException;
import java.net.*;
import java.util.LinkedList;

public class TokenRing {

    private static void loop(DatagramSocket socket, String ip, int port, boolean first) {
        LinkedList<Token.Endpoint> candidates = new LinkedList<>();
        LinkedList<Token.Endpoint> ringNodes = new LinkedList<>();
        int sequence = 0; // Initiale Sequenznummer

        if (first) {
            Token.Endpoint currentNode = new Token.Endpoint(ip, port);
            candidates.add(currentNode);
            ringNodes.add(currentNode);
        }

        while (true) {
            try {
                Token rc = Token.receive(socket);
                System.out.printf("Token: seq=%d, #members=%d", rc.getSequence(), rc.length());

                // Clear ringNodes and add the current ring nodes from the received token
                ringNodes.clear();
                ringNodes.addAll(rc.getRing());

                for (Token.Endpoint endpoint : rc.getRing()) {
                    System.out.printf(" (%s, %d)", endpoint.ip(), endpoint.port());
                }
                System.out.println();

                // If current node is the only one in the ring, add itself as a candidate
                if (rc.length() == 1) {
                    candidates.add(rc.poll());
                    if (!first) {
                        continue;
                    }
                }

                first = false;

                // Add candidates to ringNodes and clear candidates list
                ringNodes.addAll(candidates);
                candidates.clear();

                // Create a new token with the updated ring nodes and increment sequence
                Token newToken = new Token();
                for (Token.Endpoint node : ringNodes) {
                    // Check if node already exists in the ring before adding
                    if (!newToken.getRing().contains(node)) {
                        newToken.append(node);
                    }
                }
                newToken.setSequence(sequence); // Setzen der aktualisierten Sequenz
                newToken.incrementSequence(); // Inkrementieren der Sequenz

                // Find the next active node in the ring to send the token
                Token.Endpoint next = null;
                boolean foundCurrent = false;
                for (Token.Endpoint node : newToken.getRing()) {
                    if (foundCurrent) {
                        next = node;
                        break;
                    }
                    if (node.ip().equals(ip) && node.port() == port) {
                        foundCurrent = true;
                    }
                }

                // If next node is null, set it to the first node in the ring
                if (next == null && !newToken.getRing().isEmpty()) {
                    next = newToken.getRing().peek();
                }

                // Send the token to the next active node in the ring
                if (next != null) {
                    Thread.sleep(1000);
                    newToken.send(socket, next);
                }

                // Aktualisieren der lokalen Sequenz
                sequence = newToken.getSequence();
            } catch (IOException e) {
                System.out.println("Error receiving packet: " + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
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
