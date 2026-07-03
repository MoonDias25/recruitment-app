package com.authserver.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.*;

import java.util.Map;

@Configuration
public class TokenConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(JdbcTemplate jdbcTemplate) {
        return context -> {

            JwsHeader.Builder headers = context.getJwsHeader();
            JwtClaimsSet.Builder claims = context.getClaims();
            Authentication principal = context.getPrincipal();

            if (principal != null && principal.getPrincipal() instanceof UserDetails userDetails) {
                String email = userDetails.getUsername();

                if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {

                    claims.claim("email", email);

                    try {
                        Map<String, Object> userRow = jdbcTemplate.queryForMap(
                                "SELECT id, first_name, last_name, phone_number FROM users WHERE email = ?", email);

                        String userId = userRow.get("id").toString();

                        claims.subject(userId);

                        String firstName = (String) userRow.get("first_name");
                        String lastName = (String) userRow.get("last_name");
                        String phoneNumber = (String) userRow.get("phone_number");

                        claims.claim("given_name", firstName);
                        claims.claim("family_name", lastName);
                        claims.claim("name", firstName + " " + lastName);
                        claims.claim("phone_number", phoneNumber);

                    } catch (Exception e) {
                        claims.claim("name", email.split("@")[0]);
                    }

                    String role = userDetails.getAuthorities().stream()
                            .findFirst()
                            .map(auth -> auth.getAuthority())
                            .orElse("ROLE_USER");
                    claims.claim("role", role);
                }

                else if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                    try {
                        Map<String, Object> userRow = jdbcTemplate.queryForMap(
                                "SELECT id FROM users WHERE email = ?", email);

                        String userId = userRow.get("id").toString();

                        claims.subject(userId);

                        String role = userDetails.getAuthorities().stream()
                                .findFirst()
                                .map(auth -> auth.getAuthority())
                                .orElse("ROLE_USER");
                        claims.claim("role", role);

                    } catch (Exception _) {

                    }
                }
            }
        };
    }
}