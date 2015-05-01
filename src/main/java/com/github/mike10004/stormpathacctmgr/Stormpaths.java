/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mchaberski
 */
public class Stormpaths {
    
    private static final Logger log = Logger.getLogger(Stormpaths.class.getName());
    
    public Stormpaths() {
    }
    
    protected URL getResource(String path) throws IOException {
        URL resource = getClass().getResource(path);
        if (resource == null) {
            throw new FileNotFoundException(path);
        }
        return resource;
    }
    
    public Properties loadStormpathProperties() {
        Properties p = new Properties();
        try {
            URL resource = getResource("/application.properties");
            try (InputStream in = resource.openStream()) {
                p.load(in);
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "failed to load stormpath properties", e);
        }
        return p;
    }
    
    public ApiKey toApiKey(Properties stormpathProps) {
        ApiKey apiKey = ApiKeys.builder().setProperties(stormpathProps)
                .setIdPropertyName("stormpath.apiKey.id")
                .setSecretPropertyName("stormpath.apiKey.secret")
                .build();
        return apiKey;
    }

    public Client buildClient(Properties stormpathProps) {
        ApiKey apiKey = toApiKey(stormpathProps);
        Client client = Clients.builder().setApiKey(apiKey).build();
        return client;
    }
    
    public Application buildApplication(Properties stormpathProperties, Client client) {
        String applicationRestUrl = stormpathProperties.getProperty("stormpath.applicationRestUrl");
        Application application = client.getResource(applicationRestUrl, Application.class);
        return application;
    }
    
    public Application buildApplication() {
        Properties p = loadStormpathProperties();
        Client client = buildClient(p);
        Application application = buildApplication(p, client);
        return application;
    }
}
