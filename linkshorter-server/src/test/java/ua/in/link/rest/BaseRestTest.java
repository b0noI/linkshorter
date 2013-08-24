package ua.in.link.rest;

import com.sun.jersey.api.container.grizzly2.GrizzlyWebContainerFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: b0noI
 * Date: 02.05.13
 * Time: 14:39
 */
public abstract class BaseRestTest {

    private static final String JERSEY_SERVLET_CONTEXT_PATH = "ua.in.link.rest.server";

    private static final Map<String, String> INIT_PARAMS = new HashMap<>();

    private HttpServer httpServer;

    static {
        INIT_PARAMS.put("com.sun.jersey.config.property.packages",
                JERSEY_SERVLET_CONTEXT_PATH);
    }

    @Before
    public void setUp() throws Exception {
        RESTSettings.setLocaleMode(true);
        startServer();
    }

    @After
    public void tearDown() throws Exception {
        stopSrever();
        RESTSettings.setLocaleMode(false);
    }

    private void startServer() throws IOException {
        httpServer = GrizzlyWebContainerFactory.create(
                RESTSettings.getRestUrl() + "/", INIT_PARAMS);
    }

    private void stopSrever() {
        httpServer.stop();
    }

}
