package rd.professional.exception

import org.springframework.http.HttpStatus

class HttpException extends RuntimeException {

    private final int statusCode
    private final String message;

    HttpException(HttpStatus status) {
        this(status.value())
    }

    HttpException(HttpStatus status, String message) {
        this(status.value(), message)
    }

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
