package br.com.lucascaldas.authguard.infrastructure.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResendEmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${app.base.url.validation}")
    private String baseUrl;

    private static final String TEMPLATE_PATH = "src/main/resources/templates/shopsession-regular.html";

    public void sendEmail(String to, String token) throws IOException {

        Resend resend = new Resend(apiKey);

        String htmlTemplate = new String(Files.readAllBytes(Paths.get(TEMPLATE_PATH)), "UTF-8");
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
}
