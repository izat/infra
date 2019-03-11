package infrastructure.verificationcode.identity;

import java.util.Map;

public interface Identity {

    String getName();

    Map<String, String> getIdentityMap();
}
