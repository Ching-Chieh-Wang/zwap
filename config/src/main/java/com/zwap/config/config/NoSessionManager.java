package com.zwap.config.config;

import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.SessionManager;
import org.springframework.vault.support.VaultToken;

public class NoSessionManager implements SessionManager {

    private final ClientAuthentication clientAuthentication;

    public NoSessionManager(ClientAuthentication clientAuthentication) {
        this.clientAuthentication = clientAuthentication;
    }

    @Override
    public VaultToken getSessionToken() {
        return clientAuthentication.login();
    }
}