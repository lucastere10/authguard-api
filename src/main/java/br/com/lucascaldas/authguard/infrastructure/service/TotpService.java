package br.com.lucascaldas.authguard.infrastructure.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.lucascaldas.authguard.infrastructure.utils.DataUriForImage;

import org.springframework.stereotype.Service;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TotpService {

    @Autowired
    private SecretGenerator secretGenerator;

    @Autowired
    private QrDataFactory qrDataFactory;

    @Autowired
    private QrGenerator qrGenerator;

    private String secret;

    public String setupDevice() throws QrGenerationException {
        // Generate a new secret
        secret = secretGenerator.generate();

        QrData data = qrDataFactory.newBuilder()
                .label("authguard@authguard.com")
                .secret(secret)
                .issuer("Authguard")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        log.info(secret);

        return DataUriForImage.getDataUriForImage(
                qrGenerator.generate(data),
                qrGenerator.getImageMimeType());
    }

    public String verify(@RequestParam String code) {
        log.info("Secret: " + secret);
        log.info("Code: " + code);

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        boolean isValid = verifier.isValidCode(secret, code);

        log.info("Is valid: " + isValid);
        if (isValid) {
            return "CORRECT CODE";
        }
        return "INCORRECT CODE";
    }

    public String verifyMobile() throws CodeGenerationException {
        log.info("Secret: " + secret);

        TimeProvider timeProvider = new SystemTimeProvider();
        long counter = Math.floorDiv(timeProvider.getTime(), 30);

        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        String generatedCode = codeGenerator.generate(secret, counter);
        
        log.info("Generated Code: " + generatedCode);
        log.info("Counter: " + counter);
        
        return generatedCode;
    }

}