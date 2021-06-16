import eu.dindoffer.capnp.addressbook.FullDeserialization;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class TimeoutTest {

    @Test
    void timeout() throws IOException {
        InputStream timeoutData = TimeoutTest.class.getResourceAsStream("/timeout-b8be");
        FullDeserialization.fuzzerTestOneInput(timeoutData.readAllBytes());
    }
}
