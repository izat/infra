package infrastructure.verificationcode.verifier;

import com.google.common.base.Strings;
import infrastructure.verificationcode.errorcode.VerificationErrorCode;
import infrastructure.verificationcode.generator.VerificationCodeGenerator;
import infrastructure.verificationcode.identity.Identity;
import infrastructure.verificationcode.sender.VerificationCodeSender;
import infrastructure.verificationcode.storage.VerificationCodeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DefaultVerificationCodeService implements VerificationCodeService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultVerificationCodeService.class);

    private VerificationCodeGenerator verificationCodeGenerator;
    private VerificationCodeStorage verificationCodeStorage;
    private VerificationCodeSender verificationCodeSender;

    public DefaultVerificationCodeService(VerificationCodeGenerator verificationCodeGenerator,
                                          VerificationCodeStorage<?> verificationCodeStorage,
                                          VerificationCodeSender verificationCodeSender) {
        this.verificationCodeGenerator = verificationCodeGenerator;
        this.verificationCodeStorage = verificationCodeStorage;
        this.verificationCodeSender = verificationCodeSender;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String newVerificationCode(Identity identity) {
        Object identityWrapper = verificationCodeStorage.wrap(identity);
        VerificationErrorCode.TOO_MANY_VERIFICATION_CODE_REQUEST.throwIf(
                verificationCodeStorage.isRequestRestricted(identityWrapper));

        String verificationCode = verificationCodeStorage.get(identityWrapper);
        if (Strings.isNullOrEmpty(verificationCode)) {
            verificationCode = verificationCodeGenerator.generate(identity);
        }
        verificationCodeSender.sendVerificationCode(identity, verificationCode);
        verificationCodeStorage.set(identityWrapper, verificationCode);

        logger.debug("verification code: {}", verificationCode);
        return verificationCode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void verify(Identity identity, String verificationCode) {
        VerificationErrorCode.INCORRECT_VERIFICATION_CODE.throwIf(Strings.isNullOrEmpty(verificationCode));

        Object identityWrapper = verificationCodeStorage.wrap(identity);
        VerificationErrorCode.INCORRECT_VERIFICATION_CODE.throwIf(
                !Objects.equals(verificationCode, verificationCodeStorage.get(identityWrapper)));

        verificationCodeStorage.clear(identityWrapper);
    }
}
