package ua.in.link.rest;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
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
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: b0noI
 * Date: 07.04.13
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public class GrizzliStart {

    private static final String JERSEY_SERVLET_CONTEXT_PATH = "ua.in.link.rest.server";

    protected static HttpServer startServer() throws IOException {
        System.out.println("Starting grizzly...");
        ResourceConfig rc = new PackagesResourceConfig("ua.in.link.rest.server");
        return GrizzlyServerFactory.createHttpServer("http://localhost:8080", rc);
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", "localhost"));
        System.in.read();
        httpServer.stop();
    }

}
