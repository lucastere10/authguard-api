package br.com.lucascaldas.authguard.infrastructure.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;

import br.com.lucascaldas.authguard.api.dto.LoginRequestDTO;
import br.com.lucascaldas.authguard.api.dto.LoginResponseDTO;
import br.com.lucascaldas.authguard.core.security.JwtUtils;
import br.com.lucascaldas.authguard.domain.enums.UserType;
import br.com.lucascaldas.authguard.domain.exception.BadRequestException;
import br.com.lucascaldas.authguard.domain.exception.InternalServerErrorException;
import br.com.lucascaldas.authguard.domain.exception.NotFoundException;
import br.com.lucascaldas.authguard.domain.models.User;
import br.com.lucascaldas.authguard.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Value("${google.client.id}")
    private String googleClientId;

    public LoginResponseDTO authenticateWithProvider(LoginRequestDTO request) {
        try {
            String provider = request.getProvider();
            String token = request.getToken();

            // Generic payload for user data
            Map<String, Object> payload;

            // Validate the token based on the provider
            switch (provider.toLowerCase()) {
                case "google":
                    payload = validateGoogleToken(token);
                    break;
                case "github":
                    payload = validateGithubToken(token);
                    break;
                default:
                    throw new BadRequestException("Unsupported provider: " + provider) {};
            }

            // Extract the email from the payload
            String email = (String) payload.get("email");
            if (email == null || email.isEmpty()) {
                throw new NotFoundException("Email não encontrado") {};
            }

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setName((String) payload.get("name"));
                        newUser.setUserType(UserType.USER);
                        return userRepository.save(newUser);
                    });

            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(jwtUtils.generateToken(user));

            return response;
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal Server Error: " + e) {};
        }
    }

    private Map<String, Object> validateGoogleToken(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId)).build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                return payload;
            } else {
                throw new NotFoundException("Invalid Google token") {};
            }
        } catch (Exception e) {
            throw new BadRequestException("Failed to validate Google token") {};
        }
    }

    private Map<String, Object> validateGithubToken(String token) {
        String githubEndpoint = "https://api.github.com/user";
        RestTemplate restTemplate = new RestTemplate();

        // Set Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(githubEndpoint, HttpMethod.GET, entity, Map.class);

        // Ensure the response contains necessary fields
        Map<String, Object> user = response.getBody();
        if (user == null || !user.containsKey("email")) {
            throw new NotFoundException("Invalid GitHub token") {};
        }
        return user;
    }
}
