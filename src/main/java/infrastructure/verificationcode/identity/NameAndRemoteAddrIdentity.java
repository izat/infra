package infrastructure.verificationcode.identity;

import com.google.common.base.Strings;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class NameAndRemoteAddrIdentity implements Identity {
    private static final String NAME = "name";
    private static final String ADDR = "addr";

    private String name;
    private Map<String, String> identityMap;

    private static String getRemoteAddr() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return getIpAddress(request);
        }
        return "unknown";
    }

    private static String getIpAddress(HttpServletRequest request) {
        return Stream.of("x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR")
                .map(request::getHeader)
                .filter(ip -> !Strings.isNullOrEmpty(ip) && !Objects.equals(ip, "unknown"))
                .findFirst()
                .orElse(request.getRemoteAddr());
    }

    public NameAndRemoteAddrIdentity(String name) {
        this(name, null);
    }

    private NameAndRemoteAddrIdentity(String name, String remoteAddr) {
        this.name = name;
        identityMap = new HashMap<>(2);
        identityMap.put(NAME, name);
        if (remoteAddr != null) {
            identityMap.put(ADDR, remoteAddr.replace(':', '-'));
        }
        identityMap = Collections.unmodifiableMap(identityMap);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getIdentityMap() {
        return identityMap;
    }
}
