package ua.in.link.rest.client;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import ua.in.link.db.URLData;
import ua.in.link.rest.RESTSettings;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: b0noI
 * Date: 07.04.13
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public class URLClient {

    protected static final Client CLIENT = Client.create();

    private static final Gson GSON = new Gson();

    private static final String POST_URL = "/generateShort";

    private static final String POST_NEW_URL =  "/rest/postUrl";

    private static final String POST_PRIVATE_URL =  "/rest/postPrivateUrl";

    private static final String GET_URL = "/";

    private static final String GET_STAT = "/rest/statistic/";

    private static final String GET_PRIVATE_URL = "/%s/%s";

    @Deprecated
    public static String postUrl(String fullUrl){
        return post(getServerURI(POST_URL), fullUrl);
    }

    public static String postUrlToRest(String fullUrl){
        return post(getServerURI(POST_NEW_URL), fullUrl);
    }

    public static String postPrivateUrlToRest(String fullUrl, String password) {
        String postContent = String.format("{ \"url\" : \"%s\", \"password\" : \"%s\" }",
                fullUrl, password);


        WebResource webResource = CLIENT
                .resource(getServerURI(POST_PRIVATE_URL));

        //webResource.type(MediaType.APPLICATION_JSON);

        ClientResponse response = webResource
                .post(ClientResponse.class, postContent);

        String output = response.getEntity(String.class);

        return output;
    }

    public static String getFullUrl(String shortUrl){

        WebResource webResource = CLIENT
                .resource(getServerURI(GET_URL + shortUrl));

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);
        return output;
    }

    public static List<URLData.DataStat> getStat(String shortUrl){

        WebResource webResource = CLIENT
                .resource(getServerURI(GET_STAT + shortUrl));

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);
        return Arrays.asList((URLData.DataStat[])GSON.fromJson(output, URLData.DataStat[].class));
    }

    private static String post(String url, String fullUrl){
        WebResource webResource = CLIENT
                .resource(url);

        ClientResponse response = webResource
                .post(ClientResponse.class, fullUrl);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);
        return output;
    }

    private static String getServerURI(String restObject) {
        return RESTSettings.getRestUrl() + restObject;
    }

    public static String getPrivateUrl(String shortUrl, String password) {
        WebResource webResource = CLIENT
                .resource(getPrivateUrlResourceURI(shortUrl, password));


        ClientResponse response = webResource
                .get(ClientResponse.class);

        String output = response.getEntity(String.class);

        return output;
    }

    private static String getPrivateUrlResourceURI(String shortUrl, String password) {
        String resourcePath = String.format(GET_PRIVATE_URL, shortUrl, password);
        return getServerURI(resourcePath);
    }


}
