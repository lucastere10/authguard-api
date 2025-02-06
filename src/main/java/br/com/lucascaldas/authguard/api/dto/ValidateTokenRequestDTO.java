package br.com.lucascaldas.authguard.api.dto;

import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequestDTO {
    private OneTimeTokenAuthenticationToken token;
}
