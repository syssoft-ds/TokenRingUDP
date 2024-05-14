import java.io.IOException; // Importing IOException class for handling exceptions
import java.net.*; // Importing java.net package for networking operations
import java.util.LinkedList; // Importing LinkedList class for using LinkedList data structure

public class TokenRing { // Defining a public class named TokenRing

    private static void loop(DatagramSocket socket, String ip, int port, boolean first){ // Defining a private static method named loop
        LinkedList<Token.Endpoint> candidates = new LinkedList<>(); // Creating a LinkedList of Token.Endpoint objects
        if (first) { // Checking if it's the first node
            candidates.add(new Token.Endpoint(ip, port)); // Adding the first node to the candidates list
        }
        while (true) { // Infinite loop to keep the token ring running
            Token.Endpoint next = null;
            try {
                Token rc = Token.receive(socket); // Receiving a token
                System.out.printf("Token: seq=%d, #members=%d", rc.getSequence(), rc.length()); // Printing the token sequence and number of members
                for (Token.Endpoint endpoint : rc.getRing()) { // Looping through each endpoint in the token ring
                    System.out.printf(" (%s, %d)", endpoint.ip(), endpoint.port()); // Printing the IP and port of each endpoint
                }
                System.out.println(); // Printing a new line
                if (rc.length() == 1) { // Checking if the token ring has only one member
                    candidates.add(rc.poll()); // Adding the member to the candidates list
                    if (!first) { // Checking if it's not the first node
                        continue; // Skipping the rest of the loop
                    }
                }
                first = false; // Setting first to false after the first iteration
                for (Token.Endpoint candidate : candidates) { // Looping through each candidate
                    rc.append(candidate); // Adding the candidate to the token ring
                }
                candidates.clear(); // Clearing the candidates list
                next = rc.poll();
                rc.append(next); // Adding the next endpoint to the token ring



                // attempt to implement timeout
                rc.incrementSequence(); // Incrementing the token sequence
                Thread.sleep(1000); // Pausing the thread for 1 second
                rc.sendWithTimeout(socket, next, 1000); // Sending the token to the next endpoint with a timeout of 5000 milliseconds
                System.out.println("Token sent to " + next.ip() + ":" + next.port()); // Printing a message indicating that the token was sent to the next endpoint
            } catch (SocketTimeoutException e) { // Catching any SocketTimeoutException
                System.out.println("Node " + next + " failed or dropped"); // Printing a message indicating that the node failed or dropped
                // Here you could add code to handle the node failure or drop, such as removing the node from the ring
            } catch (IOException e) { // Catching any IOException
                System.out.println("Error receiving packet: " + e.getMessage()); // Printing the error message
            } catch (Exception e) { // Catching any other exception
                System.out.println("Error: " + e.getMessage()); // Printing the error message
            }
        }
    }

    public static void main(String[] args) { // Defining the main method
        try (DatagramSocket socket = new DatagramSocket()) { // Creating a DatagramSocket
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002); // Connecting the socket to a remote address
            String ip = socket.getLocalAddress().getHostAddress(); // Getting the local IP address
            socket.disconnect(); // Disconnecting the socket
            int port = socket.getLocalPort(); // Getting the local port
            System.out.printf("UDP endpoint is (%s, %d)\n", ip, port); // Printing the local IP and port
            if (args.length == 0) { // Checking if no arguments were passed
                loop(socket,ip,port,true); // Starting the token ring with the local IP and port as the first node
            }
            else if (args.length == 2) { // Checking if two arguments were passed
                Token rc = new Token().append(ip,port); // Creating a new token and adding the local IP and port
                rc.send(socket,args[0],Integer.parseInt(args[1])); // Sending the token to the IP and port passed as arguments
                loop(socket,ip,port,false); // Starting the token ring with the local IP and port as a non-first node
            }
            else { // If any other number of arguments were passed
                System.out.println("Usage: \"java TokenRing\" or \"java TokenRing <ip> <port>\""); // Printing the usage instructions
            }
        }
        catch (SocketException e) { // Catching any SocketException
            System.out.println("Error creating socket: " + e.getMessage()); // Printing the error message
        }
        catch (UnknownHostException e) { // Catching any UnknownHostException
            System.out.println("Error while determining IP address: " + e.getMessage()); // Printing the error message
        }
        catch (IOException e) { // Catching any IOException
            System.out.println("IO error: " + e.getMessage()); // Printing the error message
            System.out.println(e.getStackTrace()); // Printing the stack trace
        }
    }
}