/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.ws.rs.NotAuthorizedException;

/**
 *
 * @author mchaberski
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    
    public static class Utils {
        
        private Utils() {}
        
        public static void checkNotNullAndNotEmpty(String value, String argumentName) {
            checkNotNull(value, argumentName + " must be non-null");
            checkArgument(!value.isEmpty(), argumentName + " must be non-empty");
        }
    
        public static void checkPermissionToSendResetRequest(ServletContext servletContext, String email) 
                throws NotAuthorizedException {
            // TODO: only allow admin to send reset requests for other email addresses
        }
    
        
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.github.mike10004.stormpathacctmgr.PasswordReset.class);
        resources.add(com.github.mike10004.stormpathacctmgr.StormpathResourceExceptionMapper.class);
        resources.add(org.glassfish.jersey.client.filter.HttpDigestAuthFilter.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
    }

}
