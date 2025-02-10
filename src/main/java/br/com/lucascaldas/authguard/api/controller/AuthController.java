package br.com.lucascaldas.authguard.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucascaldas.authguard.api.dto.LoginRequestDTO;
import br.com.lucascaldas.authguard.api.dto.LoginResponseDTO;
import br.com.lucascaldas.authguard.api.dto.OTTRequestDTO;
import br.com.lucascaldas.authguard.infrastructure.service.AuthService;
import br.com.lucascaldas.authguard.infrastructure.service.OneTimeTokenGeneratorService;
import br.com.lucascaldas.authguard.infrastructure.service.TimedOneTimePasswordService;
import dev.samstevens.totp.exceptions.QrGenerationException;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TimedOneTimePasswordService totpService;

    @Autowired
    private OneTimeTokenGeneratorService ottService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateWithProvider(@RequestBody LoginRequestDTO request) {
        authService.authenticateWithProvider(request);
        return ResponseEntity.ok(authService.authenticateWithProvider(request));
    }

    @GetMapping("/totp/setup")
    public String setupTOTP() throws QrGenerationException {
        return totpService.setupTwoFactor();
    }

    @PostMapping("/totp/validate")
    public ResponseEntity<Void> verify(@RequestBody String code) {
        return totpService.verify(code);
    }

    @PostMapping("/ott/generate")
    public ResponseEntity<String> generateOTT() {
        String ott = ottService.generateOTT();
        return ResponseEntity.ok(ott);
    }

    @PostMapping("/ott/extract")
    public ResponseEntity<String> extractSecretAndLabel(@RequestBody OTTRequestDTO dto) {
        String result = ottService.getRoomAndLabelFromQrCode(dto.getToken());
        return ResponseEntity.ok(result);
    }

}
