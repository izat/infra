package infrastructure.errorcode;

public interface ErrorCode {
    int getCode();

    default String getMessage() {
        if (this.getClass().isEnum()) {
            return toString().replace('_', ' ');
        }
        return getCode() + " " + this.getClass().getSimpleName();
    }

    default ErrorCodeException e() {
        return new ErrorCodeException(this);
    }

    default ErrorCodeException e(Throwable cause) {
        return new ErrorCodeException(this, cause);
    }

    default void throwIf(boolean errorCondition) {
        if (errorCondition) {
            throw e();
        }
    }
}
