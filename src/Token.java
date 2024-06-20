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

    public Endpoint first() {
        return ring.peek();
    }

    public Endpoint poll() {
        return ring.poll();
    }

    public int length () {
        return ring.size();
    }

    Endpoint lastsenderendpoint = null;
    public Endpoint getLastSender() { return lastsenderendpoint;}

    public void setLastSender(Endpoint last) {lastsenderendpoint = last;}

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

    public static Token receive(DatagramSocket s, int Timeouttime) throws IOException {
        byte[] buf = new byte[max_buffer_size];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            s.setSoTimeout(Timeouttime);
            s.receive(packet);
        } catch (SocketTimeoutException e) {
            throw e;
        }
        String rc_json = new String(packet.getData(),0,packet.getLength(), StandardCharsets.UTF_8);
        return fromJSON(rc_json);
    }

    public void remove(Endpoint endpoint) {
        ring.remove(endpoint);
    }

    public static void bestaetigungSchicken(DatagramSocket socket, Token.Endpoint endpoint, Token rc) throws IOException {
        Token ackToken = new Token();
        Token.Endpoint confirm = new Token.Endpoint("0.0.0.0.0",0); // Erstellen Sie ein Token mit einem leeren Endpunkt fuer die Bestaetigungsnachricht
        ackToken.append(confirm); // Fuegen Sie den leeren Endpunkt hinzu
        rc.setLastSender(endpoint); // Setzen Sie den letzten Sender auf diesen Knoten
        ackToken.send(socket, new Token.Endpoint(rc.getLastSender().ip(),rc.getLastSender().port())); //verschicke leeren Endpoint an den letzten Sender
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
