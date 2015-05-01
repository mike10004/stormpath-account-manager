/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import com.google.common.annotations.VisibleForTesting;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import static com.github.mike10004.stormpathacctmgr.ApplicationConfig.Utils.checkPermissionToSendResetRequest;
import static com.github.mike10004.stormpathacctmgr.ApplicationConfig.Utils.checkNotNullAndNotEmpty;

/**
 * Password Reset REST endpoints.
 *
 * @author mchaberski
 */
@Path("password-reset")
public class PasswordReset {

    static final String RESET_REQUEST_REDIRECT_DESTINATION = "password-reset/request/done";
    static final String RESET_PERFORM_REDIRECT_DESTINATION = "password-reset/perform/done";
    public static final String PARAM_TARGET_EMAIL = "target";
    public static final String PARAM_RESET_TOKEN = "sptoken";
    
    private static final Logger log = Logger.getLogger(PasswordReset.class.getName());
    
    @Context @VisibleForTesting
    protected UriInfo context;

    @Context @VisibleForTesting
    protected ServletContext servletContext;

    @Context @VisibleForTesting
    protected HttpServletRequest request;
    
    private final transient Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    /**
     * Creates a new instance of PasswordReset
     */
    public PasswordReset() {
    }
    
    /**
     * Retrieves representation of an instance of com.github.mike10004.stormpathacctmgr.PasswordReset
     * @return an instance of java.lang.String
     */
    @POST
    @Path("request/send")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String sendResetRequest(@FormParam("email") String email) throws IOException {
        checkNotNullAndNotEmpty(email, "email");
        checkPermissionToSendResetRequest(servletContext, email);
        Stormpaths stormpaths = createStormpaths();
        Application application = stormpaths.buildApplication();
        Account account = application.sendPasswordResetEmail(email);
        log.log(Level.INFO, "sent reset request for {0} and got back {1}", new Object[]{email, account.getEmail()});
        Object responseData = createResponseObject(account);
        return gson.toJson(responseData);
    }

    /**
     * PUT method for updating or creating an instance of PasswordReset
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Path("perform/send")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String performReset(@FormParam("email") String email, 
            @FormParam(PARAM_RESET_TOKEN) String token, 
            @FormParam("new_shibboleth") String newPassword) throws IOException {
        checkNotNullAndNotEmpty(email, "email");
        checkNotNullAndNotEmpty(token, "token");
        checkNotNull(newPassword);
        // TODO set token -> email mapping in context token-email map
        Stormpaths stormpaths = createStormpaths();
        Application application = stormpaths.buildApplication();
        Account account = application.resetPassword(token, newPassword);
        log.log(Level.INFO, "performed reset for {0} and got {1}", new Object[]{email, account.getEmail()});
        Object responseData = createResponseObject(account);
        return gson.toJson(responseData);
    }

    protected Stormpaths createStormpaths() {
        return new Stormpaths();
    }
    
    protected Object createResponseObject(Account account) {
        Map<String, String> responseData = ImmutableMap.of("email", account.getEmail());    
        return responseData;
    }
}
