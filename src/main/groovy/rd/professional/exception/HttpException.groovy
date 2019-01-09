package rd.professional.exception

class HttpException extends RuntimeException {

    private final int statusCode
    private final String message;

    HttpException(int statusCode) {
        this.statusCode = statusCode
    }

    HttpException(int statusCode, String message) {
        this(statusCode)
        this.message = message
    }

    int getStatusCode() {
        return statusCode
    }

    String getMessage() {
        return message
    }
}
