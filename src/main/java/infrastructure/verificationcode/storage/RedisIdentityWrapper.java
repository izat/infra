package infrastructure.verificationcode.storage;

import infrastructure.verificationcode.identity.Identity;

import java.util.List;
import java.util.stream.Collectors;

public class RedisIdentityWrapper {
    private List<String> restrictKeys;
    private String codeKey;

    public RedisIdentityWrapper(Identity identity, String requestRestrictNamespace, String veriricationCodeNameSpace) {
        restrictKeys = identity.getIdentityMap().entrySet().stream()
                .map(e -> requestRestrictNamespace + ":" + e.getKey() + ":" + e.getValue())
                .collect(Collectors.toList());
        codeKey = veriricationCodeNameSpace + ":" + identity.getName();
    }

    public List<String> getRestrictKeys() {
        return restrictKeys;
    }

    public String getCodeKey() {
        return codeKey;
    }
}
