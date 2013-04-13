package ua.in.link.rest.client;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import ua.in.link.db.URL;
import ua.in.link.rest.RESTSettings;

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

    private static final String POST_URL = RESTSettings.REST_URL + "/generateShort";

    private static final String POST_NEW_URL = RESTSettings.REST_URL + "/rest/postUrl";

    private static final String GET_URL = RESTSettings.REST_URL + "/";

    private static final String GET_STAT = RESTSettings.REST_URL + "/rest/statistic/";

    @Deprecated
    public static String postUrl(String fullUrl){
        return post(POST_URL, fullUrl);
    }

    public static String postUrlToRest(String fullUrl){
        return post(POST_NEW_URL, fullUrl);
    }

    public static String getFullUrl(String shortUrl){

        WebResource webResource = CLIENT
                .resource(GET_URL + shortUrl);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);
        return output;
    }

    public static List<URL.DataStat> getStat(String shortUrl){

        WebResource webResource = CLIENT
                .resource(GET_STAT + shortUrl);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);
        return GSON.fromJson(output, List.class);
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

}
