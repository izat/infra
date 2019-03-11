package infrastructure.verificationcode.storage;

import infrastructure.verificationcode.identity.Identity;

public interface VerificationCodeStorage<IdentityWrapper> {
    IdentityWrapper wrap(Identity identity);

    void set(IdentityWrapper identityWrapper, String verificationCode);

    String get(IdentityWrapper identityWrapper);

    boolean isRequestRestricted(IdentityWrapper identityWrapper);

    void clear(IdentityWrapper identityWrapper);
}
