package ru.names.ym_gaTool;

/**
 * @author kbogdanov 27.06.16
 */
class ErrorResponseException extends BaseException {

    private String response;

    public ErrorResponseException(String message, String response) {
        super(message);

        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
