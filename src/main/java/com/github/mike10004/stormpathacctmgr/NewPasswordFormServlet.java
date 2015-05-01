/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import static com.github.mike10004.stormpathacctmgr.ApplicationConfig.Utils.checkNotNullAndNotEmpty;
import com.stormpath.sdk.resource.ResourceException;

/**
 *
 * @author mchaberski
 */
public class NewPasswordFormServlet extends HttpServlet {

    public static final String ATTR_TARGET_EMAIL = "targetEmail";
    public static final String ATTR_TOKEN_VERIFIED = "tokenVerified";

    private static final Logger log = Logger.getLogger(NewPasswordFormServlet.class.getName());
    static final String RESET_ENTER_NEW_PASSWORD_JSP_PATH = "/WEB-INF/views/reset-perform.jsp";
    
    /**
     * Handles the HTTP {@code GET} method. Gets the password reset token 
     * parameter value from the request query string and forwards to 
     * a JSP page for rendering.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ResourceException {
        boolean tokenVerified = false;
        String token = request.getParameter(PasswordReset.PARAM_RESET_TOKEN);
        if (token != null && !token.isEmpty()) {
            checkNotNullAndNotEmpty(token, "token");
            Stormpaths stormpaths = createStormpaths();
            Application application = stormpaths.buildApplication();
            Account account;
            try {
                account = application.verifyPasswordResetToken(token);
                tokenVerified = true;
                log.log(Level.INFO, "verified token {0} and got account {1}", 
                        new Object[]{StringUtils.abbreviate(token, 32), account.getEmail()});
                request.setAttribute(ATTR_TARGET_EMAIL, account.getEmail());
            } catch (ResourceException e) {
                log.log(Level.INFO, "verifyPasswordResetToken failed: {0}", (Object) e);
            }
        } else {
            log.info("'sptoken' query parameter is empty or not present");
        }
        request.setAttribute(ATTR_TOKEN_VERIFIED, tokenVerified);
        RequestDispatcher dispatcher = request.getRequestDispatcher(RESET_ENTER_NEW_PASSWORD_JSP_PATH);
        dispatcher.forward(request, response);
    }

    protected Stormpaths createStormpaths() {
        return new Stormpaths();
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet that provides the new password form";
    }

}
