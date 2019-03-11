package infrastructure.spring.security.authorization;

import infrastructure.spring.security.authentication.AuthorityUtils;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public interface CachedRoleHierarchy extends RoleHierarchy {
    default Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        Set<String> authorityNames = authorities.stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        Queue<String> parentAuthorityNames = new LinkedList<>(authorityNames);

        while (!parentAuthorityNames.isEmpty()) {
            String parentAuthorityName = parentAuthorityNames.poll();
            authorityNames.add(parentAuthorityName);
            authorityNames.addAll(getAuthorities(parentAuthorityName));
        }

        return AuthorityUtils.createAuthorityList(authorityNames);
    }

    Collection<String> getAuthorities(String parentAuthority);

    Collection<String> getAuthorities(Collection<String> parentAuthorities);

    void setAuthorities(String parentAuthority, Collection<String> authorities);
}
