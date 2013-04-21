package ua.in.link.rest.server;

import com.google.gson.Gson;
import ua.in.link.db.DBHelper;
import ua.in.link.db.URLData;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: b0noI Date: 06.04.13 Time: 23:10 To change this template use File |
 * Settings | File Templates.
 */
@Path("/")
public class Server {

    private static final Gson GSON = new Gson();

    private static final String URL_FOR_FETCHING_COUNTRY = "http://api.hostip.info/country.php?ip=";

    private static final String NOT_VALID_URL = "URLData not valid";

    private static final String INDEX_URL = "in/index.html";

    private static final String URL_KEY = "###URLData###";

    private static final String REDIRECT_STRING = "<html>" + "<head>"
            + "<meta HTTP-EQUIV=\"REFRESH\" content=\"0; url=" + URL_KEY
            + "\">" + "</head>" + "</html>";

    private static final String SERVER_IP = "89.253.237.43";

    private static final int WAIT_LIMIT = 60000;

    private static final int COUNT_LIMIT = 100000;

    @POST
    @Path("/generateShort")
    @Deprecated
    public Response generateShortLink(@Context HttpServletRequest request,
            String url) {
        return postLongUrl(request, url);
    }

    @GET
    @Path("/")
    public String getIndex() {

        return REDIRECT_STRING.replace(URL_KEY, INDEX_URL);
    }

    @GET
    @Path("/rest/ip")
    public Response persistIPInfo(@Context HttpServletRequest request) {
        try {
            DBHelper.getInstance().checkIP(request.getRemoteAddr());
            return Response.status(201).entity("OK").build();
        } catch (IllegalAccessException e) {
            return Response.status(Status.FORBIDDEN).build();
        }
    }

    @POST
    @Path("/rest/postUrl")
    public Response postLongUrl(@Context HttpServletRequest request , String url) {
        try {
            DBHelper.getInstance().checkIP(request.getRemoteAddr());
        } catch (IllegalAccessException e) {
            return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
        }
        
        if (url == null || url.length() < 4)
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

        URLData url = DBHelper.getInstance().getFullUrl(shortUrl);
        if (url == null)
            return null;
        return GSON.toJson(url.getStatistic());
    }

    @GET
    @Path("/{shortUrl}")
    public String getLongUrl(@PathParam("shortUrl") String shortUrl,
            @Context HttpServletRequest request) {

        if (shortUrl == null || shortUrl.length() < 4
                || shortUrl.equals("index.html"))
            return REDIRECT_STRING.replace(URL_KEY, INDEX_URL);

        URLData url = DBHelper.getInstance().getFullUrl(shortUrl);
        if (url == null)
            return null;
        String IP = request.getRemoteAddr();
        String countryCode = fetchUrl(URL_FOR_FETCHING_COUNTRY + IP);
        if (countryCode == null)
            countryCode = "";
        DBHelper.getInstance().incrementStatForURL(url, countryCode,
                request.getHeader("User-Agent"));
        return REDIRECT_STRING.replace(URL_KEY, url.getOriginalUrl());
    }

    private static String fetchUrl(String strUrl) {
        String output = "";
        String line = null;
        try {

            URL url = new URL(strUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            while ((line = reader.readLine()) != null) {
                output += line;
            }
            reader.close();

        } catch (MalformedURLException e) {
            System.out.println("ERROR CATCHED: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("ERROR CATCHED: " + e.getMessage());
            return null;
        }

        return output;
    }

}
