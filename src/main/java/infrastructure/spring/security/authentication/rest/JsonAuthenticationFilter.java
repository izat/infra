package infrastructure.spring.security.authentication.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

/**
 * <pre>
 * private SessionAuthenticationStrategy initSessionManagement(HttpSecurity http) throws Exception {
 *     http.sessionManagement()... // session configuration
 *
 *     http.getConfigurer(SessionManagementConfigurer.class).init(http);
 *     return http.getSharedObject(SessionAuthenticationStrategy.class);
 * }
 *
 * private JsonAuthenticationFilter jsonAuthenticationFilter(SessionAuthenticationStrategy sessionAuthenticationStrategy) throws Exception {
 *     JsonAuthenticationFilter filter = new JsonAuthenticationFilter(new AntPathRequestMatcher(loginPath));
 *     filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
 *     filter.setAuthenticationManager(authenticationManager());
 *     filter.setAuthenticationSuccessHandler(new NoRedirectAuthenticationSuccessHandler());
 *     filter.setUsernameProperty(usernameProperty);
 *     filter.setPasswordProperty(passwordProperty);
 *     return filter;
 * }
 *
 * http.addFilterAt(jsonAuthenticationFilter(sessionAuthenticationStrategy), UsernamePasswordAuthenticationFilter.class);
 * </pre>
 */
public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String JSON_USERNAME_PROPERTY = "username";
    private static final String JSON_PASSWORD_PROPERTY = "password";

    private String usernameProperty = JSON_USERNAME_PROPERTY;
    private String passwordProperty = JSON_PASSWORD_PROPERTY;

    public JsonAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Map requestBody;
        try (InputStream is = request.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            requestBody = mapper.readValue(is, Map.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException(
                    "Cannot obtain username and/or password from request body");
        }

        String username = String.valueOf(requestBody.get(usernameProperty));
        String password = String.valueOf(requestBody.get(passwordProperty));
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public String getUsernameProperty() {
        return usernameProperty;
    }

    public JsonAuthenticationFilter setUsernameProperty(String usernameProperty) {
        this.usernameProperty = usernameProperty;
        return this;
    }

    public String getPasswordProperty() {
        return passwordProperty;
    }

    public JsonAuthenticationFilter setPasswordProperty(String passwordProperty) {
        this.passwordProperty = passwordProperty;
        return this;
    }
}
