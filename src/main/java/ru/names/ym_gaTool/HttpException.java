package ru.names.ym_gaTool;

/**
 * @author kbogdanov 17.03.16
 */
class HttpException extends BaseException {

    private int status;

    public HttpException(int status, String message) {
        super(message);
        this.status = status;
    }

    public HttpException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpException(int status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
