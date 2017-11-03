package it.storelink.openmaint;

import java.util.List;
import java.util.Map;

public class OpenMaintApiException extends Exception {
    private int code = 0;
    private Map<String, List<String>> responseHeaders = null;
    private String responseBody = null;

    public OpenMaintApiException() {}

    public OpenMaintApiException(Throwable throwable) {
        super(throwable);
    }

    public OpenMaintApiException(String message) {
        super(message);
    }

    public OpenMaintApiException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders, String responseBody) {
        super(message, throwable);
        this.code = code;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

    public OpenMaintApiException(String message, int code, Map<String, List<String>> responseHeaders, String responseBody) {
        this(message, (Throwable) null, code, responseHeaders, responseBody);
    }

    public OpenMaintApiException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders) {
        this(message, throwable, code, responseHeaders, null);
    }

    public OpenMaintApiException(int code, Map<String, List<String>> responseHeaders, String responseBody) {
        this((String) null, (Throwable) null, code, responseHeaders, responseBody);
    }

    public OpenMaintApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public OpenMaintApiException(int code, String message, Map<String, List<String>> responseHeaders, String responseBody) {
        this(code, message);
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

    public int getCode() {
        return code;
    }

    /**
     * Get the HTTP response headers.
     */
    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * Get the HTTP response body.
     */
    public String getResponseBody() {
        return responseBody;
    }
}
