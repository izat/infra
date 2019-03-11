package infrastructure.verificationcode.errorcode;

import infrastructure.errorcode.ErrorCode;

public enum VerificationErrorCode implements ErrorCode {
    TOO_MANY_VERIFICATION_CODE_REQUEST(100601),
    INCORRECT_VERIFICATION_CODE(100602),
    ;

    private int code;

    VerificationErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
