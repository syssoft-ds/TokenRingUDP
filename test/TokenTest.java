import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenTest {

    @Test
    void testMessage() throws IOException {
        Token m = new Token().append("ip0", 0).append("ip1", 1);
        String json_1 = m.toJSON();
        Token.Endpoint ipr = m.poll();
        m.append(ipr.ip(), ipr.port());
        ipr = m.poll();
        m.append(ipr.ip(), ipr.port());
        String json_2 = m.toJSON();
        assertEquals(json_1, json_2);
    }
}