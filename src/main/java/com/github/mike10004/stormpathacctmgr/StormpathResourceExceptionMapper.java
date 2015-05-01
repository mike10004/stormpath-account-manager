/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stormpath.sdk.resource.ResourceException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author mchaberski
 */
@Provider
public class StormpathResourceExceptionMapper implements ExceptionMapper<ResourceException> {

    private static final Logger log = Logger.getLogger(StormpathResourceExceptionMapper.class.getName());
    
    private final transient Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    protected final Set<Integer> statusesNotWorthyOfStackTraces;
    
    public StormpathResourceExceptionMapper() {
        statusesNotWorthyOfStackTraces = ImmutableSet.of(400, 404);
    }
    
    @Override
    public Response toResponse(ResourceException e) {
        if (statusesNotWorthyOfStackTraces.contains(e.getStatus())) {
            log.log(Level.INFO, "rolling exception into json: {0}", (Object) e);
        } else {
            log.log(Level.INFO, "rolling exception into json", e);
        }
        Map<String, Object> data = new TreeMap<>();
        data.put("stormpathCode", e.getCode());
        data.put("status", e.getStatus());
        data.put("message", e.getMessage());
        String json = gson.toJson(data);
        Response response = Response.status(e.getStatus())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(json)
                .build();
        return response;
    }
    
}
