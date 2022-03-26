package com.codesoom.assignment.enums;

/**
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7231#section-6.1">RFC 7231 section 6.1</a>
 */
public enum HttpStatusCode {

    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content"),

    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found");

    private final int code;

    private final String message;

    HttpStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
