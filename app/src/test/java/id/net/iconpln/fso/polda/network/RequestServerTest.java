package id.net.iconpln.fso.polda.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import id.net.iconpln.fso.polda.utils.L;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ozcan on 10/02/2017.
 */
public class RequestServerTest {
    String protocol;

    @Before
    public void setupEnvironment() {
        System.out.println("Setup Environment");
        protocol = "http://";
    }

    @After
    public void cleaningUp() {
        System.out.println("Cleaning Environment");
    }

    @Test
    public void request() throws Exception {
        assertEquals(protocol, "http://");
    }

    @Test
    public void execute() throws Exception {
        assertEquals(protocol, "https://");
    }

}