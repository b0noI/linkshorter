package ua.in.link.rest.server;

import com.google.gson.Gson;
import com.sun.jersey.api.core.HttpContext;
import ua.in.link.db.DBHelper;
import ua.in.link.db.URL;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: b0noI
 * Date: 06.04.13
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
@Path("/")
public class Server {

    private static final Gson GSON = new Gson();

    private static final String NOT_VALID_URL = "URL not valid";

    private static final String INDEX_URL = "in/index.html";

    private static final String URL_KEY = "###URL###";

    private static final String REDIRECT_STRING = "<html>" + "<head>" +
            "<meta HTTP-EQUIV=\"REFRESH\" content=\"0; url="+URL_KEY+"\">" +
            "</head>" +
            "</html>";

    private static final String SERVER_IP = "89.253.237.43";

    private static final int WAIT_LIMIT = 60000;

    private static final int COUNT_LIMIT = 100000;

    @POST
    @Path("/generateShort")
    @Deprecated
    public Response generateShortLink(@Context HttpServletRequest request , String url) {
        return postLongUrl(request, url);
    }

    @GET
    @Path("/")
    public String getIndex() {

        return REDIRECT_STRING.replace(URL_KEY, INDEX_URL);
    }

    @POST
    @Path("/rest/postUrl")
    public Response postLongUrl(@Context HttpServletRequest request , String url) {
        if (url == null && url.length() < 4)
            return null;
        try {
            new java.net.URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return Response.status(500).entity(NOT_VALID_URL).build();
        }

        if (!url.contains("http"))
            url = "http://" + url;

        if (url.substring(0, 16).toLowerCase().equals("http://l.co.ua/"))
            return Response.status(201).entity(url).build();

        String shortUrl = DBHelper.getInstance().getShortUrl(url).getShortUrl();
        return Response.status(201).entity(shortUrl).build();
    }

    @GET
    @Path("/rest/statistic/{shortUrl}")
    public String getStatistic(@PathParam("shortUrl") String shortUrl) {

        if (shortUrl == null || shortUrl.length() < 4
                || shortUrl.equals("index.html"))
            return REDIRECT_STRING.replace(URL_KEY, INDEX_URL);

        URL url = DBHelper.getInstance().getFullUrl(shortUrl);
        if (url == null)
            return null;
        return GSON.toJson(url.getStatistic());
    }

    @GET
    @Path("/{shortUrl}")
    public String getLongUrl(@PathParam("shortUrl") String shortUrl, @HeaderParam("user-agent") String userAgent) {

        if (shortUrl == null || shortUrl.length() < 4
                || shortUrl.equals("index.html"))
            return REDIRECT_STRING.replace(URL_KEY, INDEX_URL);

        URL url = DBHelper.getInstance().getFullUrl(shortUrl);
        if (url == null)
            return null;
        DBHelper.getInstance().incrementStatForURL(url, userAgent);
        return REDIRECT_STRING.replace(URL_KEY, url.getOriginalUrl());
    }

}
