package br.com.lucascaldas.authguard.api.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucascaldas.authguard.api.dto.EmailRequestDTO;
import br.com.lucascaldas.authguard.infrastructure.service.ResendEmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    ResendEmailService resendEmailService;

    @Autowired
    OneTimeTokenService oneTimeTokenService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) throws IOException {
        resendEmailService.sendEmail(emailRequest.getDestinatario(), "123");
        return ResponseEntity.ok("One-Time Token generated and sent via email.");
    }

    @PostMapping("/send-token")
    public ResponseEntity<String> sendOneTimeToken(@RequestBody EmailRequestDTO emailRequest) throws IOException {

        GenerateOneTimeTokenRequest request = new GenerateOneTimeTokenRequest(emailRequest.getDestinatario());
        OneTimeToken token = oneTimeTokenService.generate(request);

        resendEmailService.sendEmail(emailRequest.getDestinatario(), token.getTokenValue());

        return ResponseEntity.ok("One-Time Token generated and sent via email.");
    }

}
