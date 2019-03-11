package infrastructure.errorcode;

import org.springframework.http.HttpStatus;

public class HttpRequestErrorCode implements ErrorCode {
    private int code;
    private String message;

    public static HttpRequestErrorCode of(HttpStatus httpStatus, Exception ex) {
        return new HttpRequestErrorCode(httpStatus.value(), ex.getClass().getSimpleName() + ": " + ex.getMessage());
    }

    private HttpRequestErrorCode(int code, String message) {
        this.code = code;
        int endIndex = message.indexOf("\n");
        if (endIndex != -1) {
            this.message = message.substring(0, endIndex);
        } else {
            this.message = message;
        }
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
