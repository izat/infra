package infrastructure.errorcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * To enable no handler found exception:
 * <pre>
 * spring.mvc.static-path-pattern=/static/** # caution: cause cors failing!
 * spring.mvc.throw-exception-if-no-handler-found=true
 * </pre>
 *
 * @see ResponseEntityExceptionHandler
 * @see org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
 */
@ControllerAdvice
public class ErrorCodeHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorCodeHandler.class);

    private String errorCodeHeader;
    private String errorMessageHeader;

    public ErrorCodeHandler(String errorCodeHeader, String errorMessageHeader) {
        this.errorCodeHeader = errorCodeHeader;
        this.errorMessageHeader = errorMessageHeader;
    }

    public void setError(ErrorCode errorCode, HttpServletResponse response) {
        response.setIntHeader(errorCodeHeader, errorCode.getCode());
        response.setHeader(errorMessageHeader, errorCode.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity<Object> handleErrorCode(ErrorCodeException ex, WebRequest request) {
        return getObjectResponseEntity(ex.getErrorCode());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body, HttpHeaders headers, HttpStatus status, @NonNull WebRequest request) {
        return getObjectResponseEntity(HttpRequestErrorCode.of(HttpStatus.BAD_REQUEST, ex));
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return getObjectResponseEntity(AccessErrorCode.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return getObjectResponseEntity(AccessErrorCode.UNAUTHORIZED);
        }
        return getObjectResponseEntity(AccessErrorCode.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> failSafe(Exception ex, WebRequest request) {
        logger.error("Exception caught", ex);
        return getObjectResponseEntity(HttpRequestErrorCode.of(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    private ResponseEntity<Object> getObjectResponseEntity(ErrorCode errorCode) {
        if (errorCode instanceof HttpRequestErrorCode) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                logger.info("<< {} {}", request.getMethod(), request.getRequestURI());
            }
        }

        int code = errorCode.getCode();
        String message = errorCode.getMessage();
        logger.info(">>> {} {}", code, message);

        HttpHeaders headers = new HttpHeaders();
        headers.add(errorCodeHeader, String.valueOf(code));
        headers.add(errorMessageHeader, message);
        return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
    }
}