package infrastructure.verificationcode;

import infrastructure.verificationcode.generator.SimpleCodeGenerator;
import infrastructure.verificationcode.generator.VerificationCodeGenerator;
import infrastructure.verificationcode.sender.VerificationCodeSender;
import infrastructure.verificationcode.storage.RedisBackedVerificationCodeStorage;
import infrastructure.verificationcode.storage.VerificationCodeStorage;
import infrastructure.verificationcode.verifier.DefaultVerificationCodeService;
import infrastructure.verificationcode.verifier.VerificationCodeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class VerificationCodeConfig {
    @Bean
    public VerificationCodeService verificationCodeService(
            StringRedisTemplate stringRedisTemplate, VerificationCodeSender sender) {
        VerificationCodeGenerator generator = new SimpleCodeGenerator();
        VerificationCodeStorage storage = new RedisBackedVerificationCodeStorage(stringRedisTemplate);
        return new DefaultVerificationCodeService(generator, storage, sender);
    }
}
