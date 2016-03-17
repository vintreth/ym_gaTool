package ru.names.ym_gaTool.api.yandex.error;

/**
 * Representation of yandex metrika api error response object
 *
 * @author kbogdanov 17.03.16
 */
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
