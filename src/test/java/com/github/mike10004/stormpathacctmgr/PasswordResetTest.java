/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletContext;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mock.web.MockServletContext;

/**
 *
 * @author mchaberski
 */
public class PasswordResetTest {
    
    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    @Test
    public void testSendResetRequest() throws IOException {
        System.out.println("testSendResetRequest");
        ServletContext servletContext = createServletContext();
        String email = "somebody@example.com";
        Application application = createNiceMock(Application.class);
        Account account = createNiceMock(Account.class);
        expect(account.getEmail()).andReturn(email).anyTimes();
        expect(application.sendPasswordResetEmail(email)).andReturn(account).times(1);
        replay(application, account);
        
        final Stormpaths stormpaths = createStormpaths(servletContext, application);
        PasswordReset instance = new PasswordReset() {

            @Override
            protected Stormpaths createStormpaths() {
                return stormpaths;
            }
            
        };
        instance.servletContext = servletContext;
        String response = instance.sendResetRequest(email);
        
        Map<String, Object> responseData = gson.fromJson(response, TreeMap.class);
        System.out.println("response: " + responseData);
        String actualEmail = (String) responseData.get("email");
        assertEquals(email, actualEmail);
    }

    @Test
    public void testPerformReset() throws IOException {
        System.out.println("testPerformReset");
        ServletContext servletContext = createServletContext();
        String emailAddress = "somebody@example.com";
        String token = "981ng9014ng4nh9014h901nh4jhqg8rejg089rjg09zahg49hqg08";
        Application application = createNiceMock(Application.class);
        Account account = createNiceMock(Account.class);
        expect(account.getEmail()).andReturn(emailAddress).anyTimes();
        String newPassword = "shibboleth";
        expect(application.resetPassword(token, newPassword)).andReturn(account).times(1);
        replay(application, account);
        
        final Stormpaths stormpaths = createStormpaths(servletContext, application);
        PasswordReset instance = new PasswordReset() {

            @Override
            protected Stormpaths createStormpaths() {
                return stormpaths;
            }
            
        };
        instance.servletContext = servletContext;
        
        String responseJson = instance.performReset(emailAddress, token, newPassword);
        Map<String, Object> responseData = gson.fromJson(responseJson, TreeMap.class);
        System.out.println("response: " + responseData);
        String actualEmail = (String) responseData.get("email");
        assertEquals(emailAddress, actualEmail);
        
    }
    
    protected MockServletContext createServletContext() {
        MockServletContext context = new MockServletContext();
        return context;
    }
    
    protected Stormpaths createStormpaths(ServletContext servletContext, final Application application) {
        return new Stormpaths() {

            @Override
            public Application buildApplication() {
                return application;
            }
            
        };
    }
}
