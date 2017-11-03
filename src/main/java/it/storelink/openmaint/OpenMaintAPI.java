package it.storelink.openmaint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import it.storelink.mango.api.utils.StringUtil;
import it.storelink.openmaintmango.User;
import it.storelink.openmaintmango.openmaint.client.sessions.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class OpenMaintAPI {

    private static Logger logger = LoggerFactory.getLogger(OpenMaintAPI.class);
    private String _basePath = "http://storelink.ns0.it:4321/openmaint/services/rest/v2";

    private Integer statusCode;
    private MultivaluedMap<String, String> responseHeaders;
    private Map<String, String> defaultHeaderMap = new HashMap<String, String>();


    public OpenMaintAPI() {}

    public OpenMaintAPI(String basePath) {
        this._basePath = basePath;
    }

    public Output login(String username, String password) {
        Map<String, String> headerParams = new HashMap<>();
        User user = new User(username, password);

        final String[] accepts = {
                "application/json"
        };
        final String accept = selectHeaderAccept(accepts);

        final String[] contentTypes = {
                "application/json"
        };
        final String contentType = selectHeaderContentType(contentTypes);

        Output response = null;
        try {
            GenericType<Output> returnType = new GenericType<Output>() {};
            response = invokeAPI(_basePath + "/sessions/", "POST", user, headerParams, accept, contentType, returnType);
            logger.info(response.toString());
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return response;
    }

    public it.storelink.openmaintmango.openmaint.client.building.Output attributes(String sessionId) {
        Map<String, String> headerParams = new HashMap<>();
        final String[] accepts = {
                "application/json"
        };
        final String accept = selectHeaderAccept(accepts);

        final String[] contentTypes = {
                "application/json"
        };
        final String contentType = selectHeaderContentType(contentTypes);
        headerParams.put("CMDBuild-Authorization", sessionId);

        it.storelink.openmaintmango.openmaint.client.building.Output response = null;
        try {
            GenericType<it.storelink.openmaintmango.openmaint.client.building.Output> returnType = new GenericType<it.storelink.openmaintmango.openmaint.client.building.Output>() {};
            response = invokeAPI(_basePath + "/classes/Building/attributes/", "GET", null, headerParams, accept, contentType, returnType);
            logger.info(response.toString());
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return response;
    }


    private <T> T  invokeAPI(String path, String method, Object body, Map<String, String> headerParams, String accept, String contentType, GenericType<T> returnType) throws OpenMaintApiException {

        ClientResponse response = getAPIResponse(path, method, body, headerParams, accept, contentType);
        statusCode = response.getStatusInfo().getStatusCode();
        responseHeaders = response.getHeaders();

        if(response.getStatusInfo() == ClientResponse.Status.NO_CONTENT) {
            return null;
        }
        else if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            if (returnType == null) return null;
            else return response.getEntity(returnType);
        }
        else {
            String message = "error";
            String respBody = null;
            if (response.hasEntity()) {
                try {
                    respBody = response.getEntity(String.class);
                    message = respBody;
                }
                catch (RuntimeException e) {
                    logger.error(e.getMessage());
                }
            }
            throw new OpenMaintApiException(
                    response.getStatusInfo().getStatusCode(),
                    message,
                    response.getHeaders(),
                    respBody);
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

    private ClientResponse getAPIResponse(String basePath, String method, /*List<Pair> queryParams, */ Object body, Map<String, String> headerParams, /*Map<String, Object> formParams, */String accept, String contentType/*, String[] authNames*/) throws OpenMaintApiException {

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

        for (String key : headerParams.keySet()) {
            builder = builder.header(key, headerParams.get(key));
        }
        for (String key : defaultHeaderMap.keySet()) {
            if (!headerParams.containsKey(key)) {
                builder = builder.header(key, defaultHeaderMap.get(key));
            }
        }

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
