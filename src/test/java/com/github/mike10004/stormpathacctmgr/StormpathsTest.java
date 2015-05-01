/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import com.stormpath.sdk.api.ApiKey;
import java.io.IOException;
import java.util.Properties;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author mchaberski
 */
public class StormpathsTest {
    
    public StormpathsTest() {
    }

    @Test
    public void testLoadStormpathProperties() throws IOException {
        System.out.println("testLoadStormpathProperties");
        Stormpaths instance = new Stormpaths();
        Properties properties = instance.loadStormpathProperties();
        System.out.println();
        properties.store(System.out, null);
        System.out.println();
        assertFalse("expect nonempty properties object", properties.isEmpty());
    }

    @Test
    public void testToApiKey() {
        System.out.println("testToApiKey");
        Stormpaths instance = new Stormpaths();
        Properties properties = instance.loadStormpathProperties();
        ApiKey apiKey = instance.toApiKey(properties);
        System.out.println("apiKey = " + apiKey); // passes if no exception thrown
    }

}
