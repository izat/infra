package infrastructure.spring.session;

import infrastructure.errorcode.AccessErrorCode;
import infrastructure.errorcode.ErrorCodeHandler;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorCodeSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    private ErrorCodeHandler errorCodeHandler;

    public ErrorCodeSessionInformationExpiredStrategy(ErrorCodeHandler errorCodeHandler) {
        this.errorCodeHandler = errorCodeHandler;
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletResponse response = event.getResponse();
        errorCodeHandler.setError(AccessErrorCode.UNAUTHORIZED, response);
        response.flushBuffer();
    }
}
