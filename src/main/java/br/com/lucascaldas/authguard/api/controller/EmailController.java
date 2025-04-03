package br.com.lucascaldas.authguard.api.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucascaldas.authguard.api.dto.EmailRequestDTO;
import br.com.lucascaldas.authguard.api.dto.ValidateTokenRequestDTO;
import br.com.lucascaldas.authguard.infrastructure.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    EmailService resendEmailService;

    @Autowired
    OneTimeTokenService oneTimeTokenService;

    @PostMapping("/send-token")
    public ResponseEntity<Void> sendOneTimeToken(@RequestBody EmailRequestDTO emailRequest) throws IOException {
        resendEmailService.sendEmailWithToken(emailRequest.getDestinatario());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<String> sendTimeToken(@RequestBody ValidateTokenRequestDTO dto) {
        resendEmailService.validateToken(dto.getToken());
        return ResponseEntity.ok("One-Time Token generated and sent via email.");
    }

}
