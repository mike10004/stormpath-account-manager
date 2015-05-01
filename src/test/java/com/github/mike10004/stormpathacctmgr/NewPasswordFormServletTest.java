/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

/**
 *
 * @author mchaberski
 */
public class NewPasswordFormServletTest {
    
    public NewPasswordFormServletTest() {
    }

    @Test
    public void testDoGet() throws Exception {
        System.out.println("NewPasswordFormServletTest.testDoGet");
        String emailAddress = "somebody@example.com";
        String token = "981ng9014ng4nh9014h901nh4jhqg8rejg089rjg09zahg49hqg08";
        final Application application = createNiceMock(Application.class);
        Account account = createNiceMock(Account.class);
        final RequestDispatcher requestDispatcher = createNiceMock(RequestDispatcher.class);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest() {

            @Override
            public RequestDispatcher getRequestDispatcher(String path) {
                assertEquals(NewPasswordFormServlet.RESET_ENTER_NEW_PASSWORD_JSP_PATH, path);
                return requestDispatcher;
            }
            
        };
        mockRequest.setParameter(PasswordReset.PARAM_RESET_TOKEN, token);
        requestDispatcher.forward(mockRequest, mockResponse);
        EasyMock.expectLastCall().times(1);
        expect(account.getEmail()).andReturn(emailAddress).anyTimes();
        expect(application.verifyPasswordResetToken(token)).andReturn(account).times(1);
        replay(application, account, requestDispatcher);
        
        final Stormpaths stormpaths = new Stormpaths() {

            @Override
            public Application buildApplication() {
                return application;
            }
            
        };
        NewPasswordFormServlet instance = new NewPasswordFormServlet() {

            @Override
            protected Stormpaths createStormpaths() {
                return stormpaths;
            }
            
        };
        MockServletContext servletContext = new MockServletContext();
        instance.init(new MockServletConfig(servletContext));
        instance.doGet(mockRequest, mockResponse);
        String actualTargetEmailAttribute = (String) mockRequest.getAttribute(NewPasswordFormServlet.ATTR_TARGET_EMAIL);
        System.out.println("email attribute: " + actualTargetEmailAttribute);
        assertEquals(emailAddress, actualTargetEmailAttribute);
        System.out.println("status: " + mockResponse.getStatus());
        assertEquals(HttpServletResponse.SC_OK, mockResponse.getStatus());        
    }
    
}
