package br.com.lucascaldas.authguard.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.lucascaldas.authguard.infrastructure.service.EmailService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EmailOneTimeTokenGenerationSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

        private final OneTimeTokenGenerationSuccessHandler redirectHandler = new RedirectOneTimeTokenGenerationSuccessHandler(
                        "/login/ott");

        @Autowired
        private EmailService emailService;

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken)
                        throws ServletException, IOException, java.io.IOException {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                                .replacePath(request.getContextPath())
                                .replaceQuery(null)
                                .fragment(null)
                                .path("/login/ott")
                                .queryParam("token", oneTimeToken.getTokenValue());
                String magicLink = builder.toUriString();
                emailService.sendEmail(oneTimeToken.getUsername(), magicLink);
                this.redirectHandler.handle(request, response, oneTimeToken);
        }

}
