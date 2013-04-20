package ua.in.link.rest;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.container.grizzly2.GrizzlyWebContainerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Grizzly http server start.
 * User: b0noI
 * Date: 07.04.13
 * Time: 17:26
 */
public class GrizzlYStart {

    private static final String JERSEY_SERVLET_CONTEXT_PATH = "ua.in.link.rest.server";

    public static void main(String[] args) throws IOException {
        final String baseUri = "http://localhost:8080/";
        final Map<String, String> initParams = new HashMap<>();
        initParams.put("com.sun.jersey.config.property.packages",
                JERSEY_SERVLET_CONTEXT_PATH);

        System.out.println("Starting grizzly...");
        GrizzlyWebContainerFactory.create(
                baseUri, initParams);
        System.out.println(String.format(
            "Jersey app started with WADL available at %sapplication.wadl\n" +
            "Try out %shelloworld\nHit enter to stop it...", baseUri, baseUri));
        System.in.read();
        System.exit(0);
    }

}
