package infrastructure.spring.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class SimpleAuthentication implements Authentication {
    protected SerializablePrincipalObject principal;
    protected Collection<GrantedAuthority> authorities;

    public SimpleAuthentication(SerializablePrincipalObject principal, Collection<String> authorities) {
        this.principal = principal;

        if (authorities == null) {
            this.authorities = AuthorityUtils.NO_AUTHORITIES;
        } else {
            this.authorities = AuthorityUtils.createImmutableAuthorityCollection(authorities);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return principal.getName();
    }
}
