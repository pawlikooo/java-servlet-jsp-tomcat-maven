import models.Client;
import org.junit.Before;
import org.junit.Test;
import services.SimpleAccessService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SimpleAccessServiceTest extends SimpleAccessService {

    SimpleAccessService service;

    @Before
    public void setUp() throws Exception {
        service = new SimpleAccessService();
    }

    @Test
    public void getAccess() throws InterruptedException {
        String ip1 = "0.0.0.0";
        String ip2 = "0.0.0.1";
        Client client1 = service.getAccess(ip1);
        Client client2 = service.getAccess(ip2);
        assertEquals(client1.isBlocked(), false);
        assertEquals(client2.isBlocked(), false);
        assertEquals(client1.getBlockMessage(), null);
        client1 = service.getAccess(ip1);
        assertEquals(client1.isBlocked(), true);
        assertNotEquals(client1.getBlockMessage(), "");
        assertNotEquals(client1.getBlockMessage(), null);
        Thread.sleep(500);
        client1 = service.getAccess(ip1);
        assertEquals(client1.isBlocked(), false);
        assertEquals(client1.getBlockMessage(), null);
    }
}