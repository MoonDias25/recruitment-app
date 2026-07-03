package com.authserver.server.controllers;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ConsentController {

    private final RegisteredClientRepository registeredClientRepository;

    public ConsentController(RegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
    }

    @GetMapping("/oauth2/consent")
    public String consent(Principal principal, Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state) {

        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new IllegalArgumentException("Client with ID: " + clientId + " couldn't be found.");
        }

        Set<String> requestedScopes = new HashSet<>();
        Collections.addAll(requestedScopes, scope.split(" "));

        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        model.addAttribute("clientName", registeredClient.getClientName() != null ? registeredClient.getClientName() : clientId);
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("scopes", requestedScopes);

        return "consent";
    }
}