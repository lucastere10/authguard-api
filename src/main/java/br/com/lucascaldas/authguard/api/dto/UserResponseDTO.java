package br.com.lucascaldas.authguard.api.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    
    private Long id;
    private String name;
    private String email;
    
}