package com.intershop.tool.version.recommender.resolver;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.intershop.tool.version.recommender.resolver.CachedVersionResolver;
import com.intershop.tool.version.recommender.resolver.VersionResolver;
import com.intershop.tool.version.recommender.xml.Versions;
import com.intershop.tool.version.recommender.xml.XmlLoader;

public class CachedVersionResolverTest
{
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private static class Counter
    {
        Integer counter = 0;
    }

    @Test
    public void testCache() throws IOException
    {
        final Counter testCounter = new Counter();
        File cachefile = testFolder.newFile();
        cachefile.delete();
        VersionResolver delegate = (group, artifactID) -> {
            testCounter.counter++;
            if ("first".equals(group))
                return Arrays.asList("2.1.3", "2.2.2", "1.2.3");
            return Arrays.asList("2.2.2", "1.2.3");
        };
        VersionResolver underTest = new CachedVersionResolver(delegate, cachefile);
        underTest.getVersions("first", "abc");
        assertEquals("initial call counted.", Integer.valueOf(1), testCounter.counter);
        underTest.getVersions("first", "abc");
        assertEquals("initial call counted.", Integer.valueOf(1), testCounter.counter);
        underTest.getVersions("second", "abc");
        assertEquals("initial call counted.", Integer.valueOf(2), testCounter.counter);
        underTest.getVersions("second", "abc");
        assertEquals("initial call counted.", Integer.valueOf(2), testCounter.counter);
    }

    private InputStream getInputStream(String resource)
    {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }

    @Test
    public void testProblem() throws IOException, JAXBException
    {
        // copy to temp file
        XmlLoader loader = new XmlLoader();
        Versions versions = loader.importXML(getInputStream("versions.xml"), Versions.class);
        File cachefile = testFolder.newFile();
        cachefile.delete();
        Writer writer = new FileWriter(cachefile);
        loader.exportXML(versions, writer);

        // load file via getVersion without calling the resolver
        final Counter testCounter = new Counter();
        VersionResolver delegate = (group, artifactID) -> {
            testCounter.counter++;
            return Arrays.asList("2.2.2", "1.2.3");
        };
        VersionResolver underTest = new CachedVersionResolver(delegate, cachefile);
        underTest.getVersions("groupid", "artifactID");
        assertEquals("not called", Integer.valueOf(0), testCounter.counter);
    }
}
