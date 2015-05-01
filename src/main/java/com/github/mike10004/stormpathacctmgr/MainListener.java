/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author mchaberski
 */
public class MainListener implements ServletContextListener {

    private static final Logger log = Logger.getLogger(MainListener.class.getName());
    
    public static final String ATTR_PROJECT = "project";
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Properties applicationProperties = loadApplicationProperties(context);
        ProjectInfo project = createProjectInfo(applicationProperties);
        context.setAttribute(ATTR_PROJECT, project);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    protected Properties loadApplicationProperties(ServletContext servletContext) {
        Properties p = new Properties();
        try {
            URL resource = getClass().getResource("/application.properties");
            if (resource == null) {
                throw new FileNotFoundException("classpath:/application.properties");
            }
            try (InputStream in = resource.openStream()) {
                p.load(in);
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "failed to load application properties", e);
        }
        return p;
    }
    
    protected ProjectInfo createProjectInfo(Properties p) {
        ProjectInfo project = new ProjectInfo();
        String name = p.getProperty("project.name");
        if ("${stormpath-account-manager.project.name}".equals(name)) {
            name = "Project";
        }
        project.setName(name);
        return project;
    }
    
    public static class ProjectInfo {
        
        private String name;

        public ProjectInfo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "ProjectInfo{" + "name=" + name + '}';
        }
        
    }
}
