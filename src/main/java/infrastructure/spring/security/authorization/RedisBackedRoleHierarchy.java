package infrastructure.spring.security.authorization;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.stream.Collectors;

public class RedisBackedRoleHierarchy implements CachedRoleHierarchy {
    public static final String ROLE_HIERARCHY_NAMESPACE = "infra:roleHierarchy";

    private StringRedisTemplate stringRedisTemplate;

    public RedisBackedRoleHierarchy(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Collection<String> getAuthorities(String parentAuthority) {
        String key = getKeyWithNamespace(parentAuthority);
        return stringRedisTemplate.boundSetOps(key).members();
    }

    @Override
    public Collection<String> getAuthorities(Collection<String> parentAuthorities) {
        return parentAuthorities.stream()
                .map(this::getAuthorities)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void setAuthorities(String parentAuthority, Collection<String> authorities) {
        String key = getKeyWithNamespace(parentAuthority);
        stringRedisTemplate.delete(key);
        stringRedisTemplate.boundSetOps(key).add(authorities.toArray(new String[0]));
    }

    private static String getKeyWithNamespace(String key) {
        return ROLE_HIERARCHY_NAMESPACE + ":" + key;
    }
}
