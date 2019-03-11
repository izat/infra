package infrastructure.spring.security.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AuthorityUtils extends org.springframework.security.core.authority.AuthorityUtils {

    public static List<GrantedAuthority> createAuthorityList(Collection<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

    public static Collection<GrantedAuthority> createImmutableAuthorityCollection(Collection<String> roles) {
        return Collections.unmodifiableCollection(createAuthorityList(roles));
    }
}
