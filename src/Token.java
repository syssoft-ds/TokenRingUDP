import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class Token implements Runnable {

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

    public void reply(DatagramSocket s) {
        setSequence(-getSequence() + 1);
        try {
            send(s, replyTo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSequence(-getSequence() + 1);
    }

    public Endpoint sentTo;
    public Endpoint sentFrom;
    public Endpoint replyTo;

    public boolean remove(Endpoint ep) {
        System.out.println("removed " + ep.port);
        return ring.remove(ep);
    }

    private Endpoint expectingAnswer = null;
    private DatagramSocket repetitionSocket = null;

    public boolean acceptAnswer(Token t) {
        if (expectingAnswer == null) return false;
        if (t.getSequence() == - getSequence()) {
            expectingAnswer = null;
            System.out.println("accepted " + t.getSequence());
            return true;
        } else {
            System.out.println("not accepted " + t.getSequence() + "  " + getSequence());
            return false;
        }
    }

    public void expectAnswer(DatagramSocket s, Endpoint ep) {
        if (length() <= 2) return;
        repetitionSocket = s;
        expectingAnswer = ep;
        System.out.println("expecting: " + getSequence());
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("awake");
        if (expectingAnswer != null) {
            remove(expectingAnswer);
            Endpoint next = poll();
            append(next);
            try {
                send(repetitionSocket, next);
                expectAnswer(repetitionSocket, next);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send (DatagramSocket s, String ip_address, int port ) throws IOException {
        replyTo = sentFrom;
        if (getSequence() != 0) {
            sentFrom = sentTo;
            sentTo = ring.getLast();
        } else {
            sentFrom = ring.getLast();
            sentTo = new Endpoint(ip_address, port);
        }
        String rc_json = toJSON();
        byte[] rc_json_bytes = rc_json.getBytes(StandardCharsets.UTF_8);
        InetAddress address = InetAddress.getByName(ip_address);
        DatagramPacket packet = new DatagramPacket(rc_json_bytes, rc_json_bytes.length, address, port);
        // System.out.printf("Sending %s to %s:%d\n", rc_json, ip_address, port);
        s.send(packet);
        System.out.println("sent " + getSequence());
    }

    public void send (DatagramSocket s, Endpoint endpoint) throws IOException {
        send(s, endpoint.ip(), endpoint.port());
    }

    public static Token receive(DatagramSocket s) throws IOException {
        byte[] buf = new byte[max_buffer_size];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        s.receive(packet);
        String rc_json = new String(packet.getData(),0,packet.getLength(), StandardCharsets.UTF_8);
        // System.out.printf("Received %s from %s:%d\n", rc_json, packet.getAddress().getHostAddress(), packet.getPort());
        return fromJSON(rc_json);
    }

    @JsonProperty
    private final LinkedList<Endpoint> ring = new LinkedList<>();

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
