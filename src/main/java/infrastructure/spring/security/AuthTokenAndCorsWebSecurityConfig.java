package infrastructure.spring.security;

import infrastructure.bean.JsonUtils;
import infrastructure.errorcode.ErrorCodeHandler;
import infrastructure.errorcode.SpringSecurityExceptionForwarder;
import infrastructure.mq.MessageQueueConfig;
import infrastructure.spring.security.authentication.AuthenticationInjector;
import infrastructure.spring.security.authentication.SerializablePrincipalResolver;
import infrastructure.spring.session.HttpSessionConfig;
import infrastructure.spring.session.SessionAuthenticationStrategyDelegator;
import infrastructure.verificationcode.VerificationCodeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.SessionRepository;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.TimeZone;

//EnableRedisHttpSession will disable auto configuration for session, such as server.sevlet.session.timeout
//see https://github.com/spring-projects/spring-boot/issues/13721#issuecomment-403056809
//@EnableRedisHttpSession
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Import({HttpSessionConfig.class, VerificationCodeConfig.class, MessageQueueConfig.class})
public class AuthTokenAndCorsWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${summerwind.cors.path-pattern:/**}")
    private String corsPathPattern;
    @Value("${summerwind.errorcode.error-code-header:X-Error-Code}")
    private String errorCodeHeader;
    @Value("${summerwind.error-message-header:X-Error-Message}")
    private String errorMessageHeader;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
    @Autowired
    private SessionAuthenticationStrategyDelegator sessionAuthenticationStrategyDelegator;
    @Autowired
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SpringSecurityExceptionForwarder springSecurityExceptionForwarder =
                new SpringSecurityExceptionForwarder(handlerExceptionResolver);
        http.exceptionHandling()
                .authenticationEntryPoint(springSecurityExceptionForwarder)
                .accessDeniedHandler(springSecurityExceptionForwarder);
        http.csrf().disable();
        http.cors();

        SessionAuthenticationStrategy sessionAuthenticationStrategy = initSessionManagement(http);
        sessionAuthenticationStrategyDelegator.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);

//        http.addFilterAt(jsonAuthenticationFilter(sessionAuthenticationStrategy), UsernamePasswordAuthenticationFilter.class);
    }

    @SuppressWarnings("unchecked")
    private SessionAuthenticationStrategy initSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .sessionRegistry(sessionRegistry);

        http.getConfigurer(SessionManagementConfigurer.class).init(http);
        return http.getSharedObject(SessionAuthenticationStrategy.class);
    }

    @Bean
    public AuthenticationInjector authenticationInjector(SessionRegistry sessionRegistry, SessionRepository sessionRepository, HttpServletRequest request, HttpServletResponse response) {
        return new AuthenticationInjector(sessionAuthenticationStrategyDelegator, sessionRegistry, sessionRepository, request, response);
    }

    @Bean
    public ErrorCodeHandler errorCodeHandler() {
        return new ErrorCodeHandler(errorCodeHeader, errorMessageHeader);
    }

//    // set default password encoder for match
//    // otherwise id prefix is needed in encoded password: {bcrypt}encodedPassword
//    auth.passwordEncoder(new BCryptPasswordEncoder());
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(authenticatedUserService).passwordEncoder(new BCryptPasswordEncoder());
//    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(corsPathPattern)
                        .allowedMethods("*")
                        .exposedHeaders(errorCodeHeader, errorMessageHeader, "X-Auth-Token");
            }

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new SerializablePrincipalResolver());
            }
        };
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        JsonUtils.setObjectMapper(mappingJackson2HttpMessageConverter.getObjectMapper());
    }
}