import com.fasterxml.jackson.annotation.JsonProperty; // Importing JsonProperty annotation for JSON serialization
import com.fasterxml.jackson.core.JsonProcessingException; // Importing JsonProcessingException class for handling exceptions
import com.fasterxml.jackson.databind.ObjectMapper; // Importing ObjectMapper class for JSON serialization
import java.io.IOException; // Importing IOException class for handling exceptions
import java.net.DatagramPacket; // Importing DatagramPacket class for sending and receiving datagrams
import java.net.DatagramSocket; // Importing DatagramSocket class for sending and receiving datagrams
import java.net.InetAddress; // Importing InetAddress class for IP address operations
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets; // Importing StandardCharsets class for character encoding
import java.util.LinkedList; // Importing LinkedList class for using LinkedList data structure
import java.util.Queue; // Importing Queue interface for using queue data structure

public class Token { // Defining a public class named Token

    private static final int max_buffer_size = 4096; // Defining a constant for the maximum buffer size

    public record Endpoint(String ip, int port) {} // Defining a record named Endpoint with two fields: ip and port

    public Token append(String ip, int port) { // Defining a method to add a new endpoint to the token ring
        ring.offer(new Endpoint(ip, port)); // Adding the new endpoint to the token ring
        return this; // Returning the current token
    }

    public Token append(Endpoint endpoint) { // Defining a method to add an existing endpoint to the token ring
        ring.offer(endpoint); // Adding the existing endpoint to the token ring
        return this; // Returning the current token
    }

    public Endpoint first() { // Defining a method to get the first endpoint in the token ring
        return ring.peek(); // Returning the first endpoint in the token ring
    }

    public Endpoint poll() { // Defining a method to remove and return the first endpoint in the token ring
        return ring.poll(); // Removing and returning the first endpoint in the token ring
    }

    public int length () { // Defining a method to get the number of endpoints in the token ring
        return ring.size(); // Returning the number of endpoints in the token ring
    }

    private int sequence = 0; // Defining a private variable for the token sequence

    public int getSequence() { // Defining a getter for the token sequence
        return sequence; // Returning the token sequence
    }

    public void setSequence(int sequence) { // Defining a setter for the token sequence
        this.sequence = sequence; // Setting the token sequence
    }

    public void incrementSequence() { // Defining a method to increment the token sequence
        sequence++; // Incrementing the token sequence
    }

    public void send (DatagramSocket s, String ip_address, int port ) throws IOException { // Defining a method to send the token to a specific IP and port
        String rc_json = toJSON(); // Converting the token to JSON
        byte[] rc_json_bytes = rc_json.getBytes(StandardCharsets.UTF_8); // Converting the JSON string to bytes
        InetAddress address = InetAddress.getByName(ip_address); // Getting the InetAddress object for the IP address
        DatagramPacket packet = new DatagramPacket(rc_json_bytes, rc_json_bytes.length, address, port); // Creating a DatagramPacket with the JSON bytes, IP address, and port
        s.send(packet); // Sending the DatagramPacket
    }

    public void send (DatagramSocket s, Endpoint endpoint) throws IOException { // Defining a method to send the token to a specific endpoint
        send(s, endpoint.ip(), endpoint.port()); // Calling the previous send method with the IP and port of the endpoint
    }

    public static Token receive(DatagramSocket s) throws IOException { // Defining a method to receive a token
        byte[] buf = new byte[max_buffer_size]; // Creating a buffer for the incoming datagram
        DatagramPacket packet = new DatagramPacket(buf, buf.length); // Creating a DatagramPacket for the incoming datagram
        s.receive(packet); // Receiving the datagram
        String rc_json = new String(packet.getData(),0,packet.getLength(), StandardCharsets.UTF_8); // Converting the datagram data to a JSON string
        return fromJSON(rc_json); // Converting the JSON string to a token and returning it
    }

    @JsonProperty // Annotating the ring field for JSON serialization
    private final Queue<Endpoint> ring = new LinkedList<>(); // Defining a queue for the token ring

    public Queue<Endpoint> getRing() { // Defining a getter for the token ring
        return ring; // Returning the token ring
    }

    private static final ObjectMapper serializer = new ObjectMapper(); // Defining an ObjectMapper for JSON serialization

    public String toJSON() throws JsonProcessingException { // Defining a method to convert the token to a JSON string
        return serializer.writeValueAsString(this); // Converting the token to a JSON string and returning it
    }

    public static Token fromJSON(String json) throws IOException { // Defining a method to convert a JSON string to a token
        return serializer.readValue(json, Token.class); // Converting the JSON string to a token and returning it
    }

    public void sendWithTimeout(DatagramSocket s, Endpoint endpoint, int timeout) throws IOException {
    s.setSoTimeout(timeout); // Set the timeout
    try {
        send(s, endpoint); // Try to send the token
    } catch (SocketTimeoutException e) {
        System.out.println("Node " + endpoint + " failed or dropped"); // Handle the timeout
    } finally {
        s.setSoTimeout(0); // Reset the timeout
    }
}
}