package infrastructure.verificationcode.generator;

import infrastructure.verificationcode.identity.Identity;

import java.util.Random;

public class SimpleCodeGenerator implements VerificationCodeGenerator {
    private static final int MAXIMUM_CODE_EXCLUDE = 10000;
    private static final String CODE_FORMAT = "%04d";

    @Override
    public String generate(Identity identity) {
        return String.format(CODE_FORMAT, new Random().nextInt(MAXIMUM_CODE_EXCLUDE));
    }
}
