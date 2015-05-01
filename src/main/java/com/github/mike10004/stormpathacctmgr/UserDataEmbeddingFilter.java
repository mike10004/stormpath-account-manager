/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.oauth.profile.google2.Google2Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mchaberski
 */
public class UserDataEmbeddingFilter implements Filter {

    public static final String ATTR_USER_DATA = "userData";
    
    private static final Logger log = LoggerFactory.getLogger(UserDataEmbeddingFilter.class);
    
    private FilterConfig filterConfig;
    
    public UserDataEmbeddingFilter() {
    }    
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        log.debug("UserDataEmbeddingFilter:DoBeforeProcessing");
        Map<String, Object> userDataAttributes = getUserDataAttributes();
        addUserDataAttributes(userDataAttributes, request);
    }    
    
    protected Map<String, Object> getUserDataAttributes() {
        UserData<?> userData = createUserData();
        Map<String, Object> attributes = ImmutableMap.<String, Object>of(ATTR_USER_DATA, userData);
        return attributes;
    }
    
    protected void addUserDataAttributes(Map<String, Object> attributes, ServletRequest request) {
        for (String attributeName : attributes.keySet()) {
            Object attributeValue = attributes.get(attributeName);
            request.setAttribute(attributeName, attributeValue);
        }
    }
    
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        log.debug("UserDataEmbeddingFilter:doFilter()");
        
        doBeforeProcessing(request, response);
        if (chain != null) {
            chain.doFilter(request, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    protected FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    protected void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        log.debug("UserDataEmbeddingFilter:Initializing filter; filterConfig != null: " + (filterConfig != null));
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        FilterConfig filterConfig_ = filterConfig;
        if (filterConfig_ == null) {
            return ("UserDataEmbeddingFilter()");
        }
        StringBuilder sb = new StringBuilder("UserDataEmbeddingFilter(");
        sb.append(filterConfig_);
        sb.append(")");
        return sb.toString();
    }
    
    protected UserData<Google2Profile> createUserData() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("user not authenticated: returning null user data");
            return createNullUserData();
        }
        String typedId = (String) subject.getPrincipal();
        CommonProfile commonProfile = (CommonProfile) SecurityUtils.getSubject()
                .getPrincipals().asList().get(1);
        Google2Profile googleProfile = (Google2Profile) commonProfile;
        UserData<Google2Profile> userData = new UserData<>(typedId, googleProfile);
        log.debug("authenticated user: {}", userData);
        return userData;
    }
    
    static <T extends CommonProfile> UserData<T> createNullUserData() {
        UserData<T> userData = new UserData<>("", null);
        return userData;
    }
    
    public static class UserData<T extends CommonProfile> {
        
        private transient final Object jsonLock = new Object();
        private final String typedId;
        private final T profile;
        private transient String json;
        
        public UserData(String typedId, T profile) {
            this.typedId = typedId;
            this.profile = profile;
        }

        public String getTypedId() {
            return typedId;
        }

        public T getProfile() {
            return profile;
        }
        
        public String getJson() {
            synchronized (jsonLock) {
                if (json == null) {
                    json = new Gson().toJson(this);
                }
            }
            return json;
        }

        @Override
        public String toString() {
            return "UserData{" + "typedId=" + typedId + ", profile=" + profile + '}';
        }
        
    }
}
