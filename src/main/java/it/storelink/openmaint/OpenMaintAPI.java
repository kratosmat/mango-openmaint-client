package it.storelink.openmaint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import it.storelink.mango.ApiException;
import it.storelink.mango.api.utils.StringUtil;
import it.storelink.openmaintmango.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenMaintAPI {

    private static Logger logger = LoggerFactory.getLogger(OpenMaintAPI.class);
    private String basePath = "http://storelink.ns0.it:4321/openmaint/services/rest/v2";

    public OpenMaintAPI() {}

    public OpenMaintAPI(String basePath) {
        this.basePath = basePath;
    }

    public void login(String username, String password) {

        User user = new User(username, password);

        final String[] accepts = {
                "application/json"
        };
        final String accept = selectHeaderAccept(accepts);

        final String[] contentTypes = {
                "application/json"
        };
        final String contentType = selectHeaderContentType(contentTypes);

        try {
            ClientResponse response = getAPIResponse(basePath + "/sessions/", "POST", user, accept, contentType);
            logger.info(response.toString());
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public String selectHeaderContentType(String[] contentTypes) {
        if (contentTypes.length == 0) {
            return "application/json";
        }
        for (String contentType : contentTypes) {
            if (isJsonMime(contentType)) {
                return contentType;
            }
        }
        return contentTypes[0];
    }

    public String selectHeaderAccept(String[] accepts) {
        if (accepts.length == 0) {
            return null;
        }
        for (String accept : accepts) {
            if (isJsonMime(accept)) {
                return accept;
            }
        }
        return StringUtil.join(accepts, ",");
    }

    public boolean isJsonMime(String mime) {
        return mime != null && mime.matches("(?i)application\\/json(;.*)?");
    }

    private ClientResponse getAPIResponse(String basePath, String method, /*List<Pair> queryParams, */ Object body, /*Map<String, String> headerParams, Map<String, Object> formParams, */ String accept, String contentType/*, String[] authNames*/) throws OpenMaintApiException {

        /*
        if (body != null && !formParams.isEmpty()){
            throw new OpenMaintApiException(500, "Cannot have body and form params");
        }

        updateParamsForAuth(authNames, queryParams, headerParams);
        */

        Client client = getClient();

        /*
        StringBuilder b = new StringBuilder();
        b.append("?");
        if (queryParams != null){
            for (Pair queryParam : queryParams){
                if (!queryParam.getName().isEmpty()) {
                    b.append(escapeString(queryParam.getName()));
                    b.append("=");
                    b.append(escapeString(queryParam.getValue()));
                    b.append("&");
                }
            }
        }
        String querystring = b.substring(0, b.length() - 1);
           */

        WebResource.Builder builder;
        if (accept == null)
            builder = client.resource(basePath /*+ querystring*/).getRequestBuilder();
        else
            builder = client.resource(basePath /*+ querystring*/).accept(accept);

        /*
        for (String key : headerParams.keySet()) {
            builder = builder.header(key, headerParams.get(key));
        }
        for (String key : defaultHeaderMap.keySet()) {
            if (!headerParams.containsKey(key)) {
                builder = builder.header(key, defaultHeaderMap.get(key));
            }
        }
        */

        ClientResponse response = null;

        if ("GET".equals(method)) {
            response = (ClientResponse) builder.get(ClientResponse.class);
        }
        else if ("POST".equals(method)) {
            //response = builder.type(contentType).post(ClientResponse.class, serialize(body, contentType, formParams));
            response = builder.type(contentType).post(ClientResponse.class, body);
        }
        else if ("PUT".equals(method)) {
            response = builder.type(contentType).put(ClientResponse.class, body);
        }
        else if ("DELETE".equals(method)) {
            response = builder.type(contentType).delete(ClientResponse.class, body);
        }
        else {
            throw new OpenMaintApiException(500, "unknown method type " + method);
        }
        return response;
    }

    private Client getClient() {
        ObjectMapper mapper = new ObjectMapper();
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider(mapper);
        DefaultClientConfig conf = new DefaultClientConfig();
        conf.getSingletons().add(jsonProvider);
        Client client = Client.create(conf);
        client.addFilter(new LoggingFilter());
        return client;
    }
}
