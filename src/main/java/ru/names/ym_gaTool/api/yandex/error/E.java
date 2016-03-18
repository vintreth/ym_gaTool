package ru.names.ym_gaTool.api.yandex.error;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Representation of yandex metrics api error response object
 *
 * @author kbogdanov 17.03.16
 */
public class E {

    @JsonProperty("error_type")
    private String errorType;
    private String message;

    public E() {
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{errorType: \"" + errorType + "\", message: \"" + message + "\"}";
    }
}
