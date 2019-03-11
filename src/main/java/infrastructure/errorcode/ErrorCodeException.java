package infrastructure.errorcode;

public class ErrorCodeException extends RuntimeException {
    private ErrorCode errorCode;

    ErrorCodeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    ErrorCodeException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getCode() + " " + errorCode.getMessage();
    }
}