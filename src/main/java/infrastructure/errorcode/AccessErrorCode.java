package infrastructure.errorcode;

public enum AccessErrorCode implements ErrorCode {

    UNAUTHORIZED(100401),
    FORBIDDEN(100403),
    ;

    private int code;

    AccessErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
