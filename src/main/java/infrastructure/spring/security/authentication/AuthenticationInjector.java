package infrastructure.spring.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.SessionRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInjector {

    private SessionAuthenticationStrategy sessionStrategy;
    private SessionRegistry sessionRegistry;
    private SessionRepository sessionRepository;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public AuthenticationInjector(SessionAuthenticationStrategy sessionStrategy,
                                  SessionRegistry sessionRegistry,
                                  SessionRepository sessionRepository,
                                  HttpServletRequest request, HttpServletResponse response) {
        this.sessionStrategy = sessionStrategy;
        this.sessionRepository = sessionRepository;
        this.sessionRegistry = sessionRegistry;
        this.request = request;
        this.response = response;
    }

    public void login(Authentication authentication) {
        sessionStrategy.onAuthentication(authentication, request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // unused for now
//        rememberMeServices.loginSuccess(request, response, authResult);

//        // Fire event
//        if (this.eventPublisher != null) {
//            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
//                    authResult, this.getClass()));
//        }

//        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public void update(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void logout(Object principal) {
        update(principal, null);
    }

    @SuppressWarnings("unchecked")
    public void update(Object principal, Authentication authentication) {
        sessionRegistry.getAllSessions(principal, false)
                .stream()
                .map(SessionInformation::getSessionId)
                .map(id -> sessionRepository.findById(id))
                .forEach(s -> {
                    s.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, authentication);
                    sessionRepository.save(s);
                });
    }

    // impl 2: find by index
//    @SuppressWarnings("unchecked")
//    public void update(String principalName, Authentication authentication) {
//            sessionRepository.findByIndexNameAndIndexValue(
//                FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, principalName)
//                .values().forEach(e -> {
//            Session s = (Session) e;
//            s.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, authentication);
//            sessionRepository.save(s);
//        });
//    }
}
