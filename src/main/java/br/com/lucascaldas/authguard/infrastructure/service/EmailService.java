package br.com.lucascaldas.authguard.infrastructure.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import java.nio.charset.StandardCharsets;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${app.base.url.validation}")
    private String baseUrl;

    @Autowired
    private OneTimeTokenService oneTimeTokenService;

    private static final String TEMPLATE_CLASSPATH = "templates/shopsession-regular.html";

    public Void sendEmailWithToken(String to) throws IOException {
        GenerateOneTimeTokenRequest request = new GenerateOneTimeTokenRequest(to);
        OneTimeToken token = oneTimeTokenService.generate(request);

        log.info(token.toString());

        sendEmail(to, token.getTokenValue());

        return null;
    }

    public void sendEmail(String to, String token) throws IOException {
        Resend resend = new Resend(apiKey);

        // Load the HTML template from the classpath
        Resource resource = new ClassPathResource(TEMPLATE_CLASSPATH);
        String htmlTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        String tokenLink = baseUrl + "?token=" + token;
        String htmlContent = htmlTemplate.replace("{{validation_token}}", tokenLink);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Lucas Caldas <onboarding@resend.dev>")
                .to(to)
                .subject("Validação de Login")
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            log.info("Email sent successfully with ID: {}", data.getId());
        } catch (ResendException e) {
            log.error("Error sending email", e);
        }
    }

    public void validateToken(OneTimeTokenAuthenticationToken token) {
        oneTimeTokenService.consume(token);
    }
}