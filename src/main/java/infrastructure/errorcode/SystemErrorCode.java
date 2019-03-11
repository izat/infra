package infrastructure.errorcode;

public enum SystemErrorCode implements ErrorCode {

    SYSTEM_ERROR(100000),
    MQ_MSG_ILLEGAL(100001),
    ;

    private int code;

    SystemErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
