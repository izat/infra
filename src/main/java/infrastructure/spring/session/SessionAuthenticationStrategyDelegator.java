package infrastructure.spring.session;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionAuthenticationStrategyDelegator implements SessionAuthenticationStrategy {
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        sessionAuthenticationStrategy.onAuthentication(authentication, request, response);
    }

    public SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
        return sessionAuthenticationStrategy;
    }

    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionAuthenticationStrategy) {
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
    }
}
