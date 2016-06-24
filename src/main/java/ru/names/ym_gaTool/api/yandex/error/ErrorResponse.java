package ru.names.ym_gaTool.api.yandex.error;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Representation of yandex metrics api error response object
 *
 * @author kbogdanov 17.03.16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private E[] errors;
    private int code;
    private String message;

    public ErrorResponse() {
    }

    public E[] getErrors() {
        return errors;
    }

    public void setErrors(E[] errors) {
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
