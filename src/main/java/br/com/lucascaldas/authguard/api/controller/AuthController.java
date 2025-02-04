package br.com.lucascaldas.authguard.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucascaldas.authguard.api.dto.LoginRequestDTO;
import br.com.lucascaldas.authguard.api.dto.LoginResponseDTO;
import br.com.lucascaldas.authguard.infrastructure.service.AuthService;
import br.com.lucascaldas.authguard.infrastructure.service.TotpService;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.exceptions.QrGenerationException;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TotpService totpService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateWithProvider(@RequestBody LoginRequestDTO request) {
        authService.authenticateWithProvider(request);
        return ResponseEntity.ok(authService.authenticateWithProvider(request));
    }

    @GetMapping("/totp/setup")
    public String setupTOTP() throws QrGenerationException {
        return totpService.setupDevice();
    }

    @PostMapping("/totp/validate")
    public String verify(@RequestBody String code) {
        return totpService.verify(code);
    }

    @GetMapping("/totp/mobile")
    @CrossOrigin(origins = "http://localhost:3000/")
    public String verifyMobile() throws CodeGenerationException {
        return totpService.verifyMobile();
    }

}
