package com.intershop.tool.version.recommender.resolver;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.intershop.tool.version.recommender.resolver.MavenVersionFetcher;
import com.intershop.tool.version.recommender.resolver.VersionResolver;

public class MavenFetcherTest
{
    private VersionResolver resolver = new MavenVersionFetcher();

    @Test
    public void test()
    {
        Collection<String> versions = resolver.getVersions("org.eclipse.persistence","eclipselink");
        assertTrue("number of versions", versions.size() > 28);
        assertTrue("contains version 2.6.4", versions.contains("2.6.4"));
    }

    @Test
    public void testMockito()
    {
        Collection<String> versions = resolver.getVersions("org.mockito","mockito-core");
        assertTrue("contains very old version", versions.contains("1.10.19"));
    }
}
