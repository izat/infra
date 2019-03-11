package infrastructure.verificationcode.verifier;

import infrastructure.verificationcode.identity.Identity;

public interface VerificationCodeService {
    String newVerificationCode(Identity identity);

    void verify(Identity identity, String code);
}
