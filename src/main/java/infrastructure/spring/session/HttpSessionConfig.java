package infrastructure.spring.session;

import infrastructure.errorcode.ErrorCodeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration
public class HttpSessionConfig {

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry(RedisOperationsSessionRepository sessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Bean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy(ErrorCodeHandler errorCodeHandler) {
        return new ErrorCodeSessionInformationExpiredStrategy(errorCodeHandler);
    }

    @Bean
    public SessionAuthenticationStrategyDelegator sessionAuthenticationStrategyDelegator() {
        return new SessionAuthenticationStrategyDelegator();
    }
}