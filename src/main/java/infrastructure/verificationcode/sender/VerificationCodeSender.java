package infrastructure.verificationcode.sender;

import infrastructure.verificationcode.identity.Identity;

public interface VerificationCodeSender {
    void sendVerificationCode(Identity identity, String verificationCode);
}
