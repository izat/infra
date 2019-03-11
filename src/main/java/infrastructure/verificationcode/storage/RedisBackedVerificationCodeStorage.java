package infrastructure.verificationcode.storage;

import infrastructure.verificationcode.identity.Identity;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisBackedVerificationCodeStorage implements VerificationCodeStorage<RedisIdentityWrapper> {
    private static final String VERIFICATION_CODE_NAMESPACE = "infra:verificationCode";
    private static final long CODE_EXPIRE_TIMEOUT = 5;
    private static final TimeUnit CODE_EXPIRE_UNIT = TimeUnit.MINUTES;

    private static final String REQUEST_RESTRICT_NAMESPACE = "infra:verificationCode:restrict";
    private static final long REQUEST_RESTRICT_TIMEOUT = 1;
    private static final TimeUnit REQUEST_RESTRICT_UNIT = TimeUnit.MINUTES;

    private StringRedisTemplate stringRedisTemplate;

    public RedisBackedVerificationCodeStorage(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public RedisIdentityWrapper wrap(Identity identity) {
        return new RedisIdentityWrapper(identity, REQUEST_RESTRICT_NAMESPACE, VERIFICATION_CODE_NAMESPACE);
    }

    @Override
    public void set(RedisIdentityWrapper identityWrapper, String verificationCode) {
        List<String> restrictKeys = identityWrapper.getRestrictKeys();
        String codeKey = identityWrapper.getCodeKey();
        restrictKeys.forEach(key ->
                stringRedisTemplate.opsForValue().set(key, codeKey, REQUEST_RESTRICT_TIMEOUT, REQUEST_RESTRICT_UNIT));
        stringRedisTemplate.opsForValue().set(codeKey, verificationCode, CODE_EXPIRE_TIMEOUT, CODE_EXPIRE_UNIT);
    }

    @Override
    public String get(RedisIdentityWrapper identityWrapper) {
        return stringRedisTemplate.opsForValue().get(identityWrapper.getCodeKey());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isRequestRestricted(RedisIdentityWrapper identityWrapper) {
        return identityWrapper.getRestrictKeys().stream().anyMatch(stringRedisTemplate::hasKey);
    }

    @Override
    public void clear(RedisIdentityWrapper identityWrapper) {
        stringRedisTemplate.delete(identityWrapper.getCodeKey());
        identityWrapper.getRestrictKeys().forEach(key -> stringRedisTemplate.delete(key));
    }
}
