package infrastructure.verificationcode.generator;

import infrastructure.verificationcode.identity.Identity;

public interface VerificationCodeGenerator {
    String generate(Identity identity);
}
