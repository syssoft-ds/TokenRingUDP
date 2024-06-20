import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class Token {

    private static final int max_buffer_size = 4096;

    public record Endpoint(String ip, int port) {}

    public Token append(String ip, int port) {
        ring.offer(new Endpoint(ip, port));
        return this;
    }

    public Token append(Endpoint endpoint) {
        ring.offer(endpoint);
        return this;
    }

    public boolean remove(Endpoint endpoint){
        ring.remove(endpoint);
        return true;
    }

    public boolean remove(String ip, int port){
        ring.removeIf(endpoint -> endpoint.ip().equals(ip)&&endpoint.port()==port);
        return true;
    }

    public Endpoint first() {
        return ring.peek();
    }

    public Endpoint poll() {
        return ring.poll();
    }

    public int length () {
        return ring.size();
    }

    private int sequence = 0;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void incrementSequence() {
        sequence++;
    }

    public void send (DatagramSocket s, String ip_address, int port ) throws IOException {
        String rc_json = toJSON();
        byte[] rc_json_bytes = rc_json.getBytes(StandardCharsets.UTF_8);
        InetAddress address = InetAddress.getByName(ip_address);
        DatagramPacket packet = new DatagramPacket(rc_json_bytes, rc_json_bytes.length, address, port);
        // System.out.printf("Sending %s to %s:%d\n", rc_json, ip_address, port);
        s.send(packet);
    }

    public void send (DatagramSocket s, Endpoint endpoint) throws IOException {
        send(s, endpoint.ip(), endpoint.port());
    }

    public static void sendAnswer(DatagramSocket s, InetAddress ip_address, int port) throws IOException {
        String answer = "Received";
        byte[] answer_bytes = answer.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(answer_bytes, answer_bytes.length, ip_address, port);
        s.send(packet);
    }

    public static Token receiveToken(DatagramSocket s) throws IOException {
        byte[] buf = new byte[max_buffer_size];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        s.receive(packet);
        String rc_json = new String(packet.getData(),0,packet.getLength(), StandardCharsets.UTF_8);
        // System.out.printf("Received %s from %s:%d\n", rc_json, packet.getAddress().getHostAddress(), packet.getPort());

        sendAnswer(s,packet.getAddress(), packet.getPort());
        return fromJSON(rc_json);
    }

    public static Boolean receiveAnswer(DatagramSocket s) throws IOException {
        byte[] buf = new byte[max_buffer_size];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        s.setSoTimeout(4000);
        try {
            s.receive(packet);
        } catch (SocketTimeoutException e) {
            // handle the timeout here
            System.out.println("Timeout reached. No packet received.");
            return false;
        }
        String answer = new String(packet.getData(),0,packet.getLength(), StandardCharsets.UTF_8);
        return answer.equals("Received");
    }

    @JsonProperty
    private final Queue<Endpoint> ring = new LinkedList<>();

    public Queue<Endpoint> getRing() {
        return ring;
    }

    private static final ObjectMapper serializer = new ObjectMapper();

    public String toJSON() throws JsonProcessingException {
        return serializer.writeValueAsString(this);
    }

    public static Token fromJSON(String json) throws IOException {
        return serializer.readValue(json, Token.class);
    }
}
