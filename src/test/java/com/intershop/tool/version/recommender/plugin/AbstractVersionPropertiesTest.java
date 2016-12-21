package com.intershop.tool.version.recommender.plugin;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.intershop.tool.version.recommender.VersionProvider;

public abstract class AbstractVersionPropertiesTest
{
    private final File versionResourceFile;

    public AbstractVersionPropertiesTest(File versionResourceFile)
    {
        this.versionResourceFile = versionResourceFile;
    }

    abstract protected VersionProvider createTestObject(File propertiesFile);

    @Test
    public void testSimpleVersion()
    {
        VersionProvider underTest = createTestObject(versionResourceFile);
        assertEquals("2.5", underTest.getVersion("commons-io", "commons-io"));
    }

    @Test
    public void testIndirectVersion()
    {
        VersionProvider underTest = createTestObject(versionResourceFile);
        assertEquals("2.4.14", underTest.getVersion("com.typesafe.akka", "akka-testkit_2.11"));
    }

    protected Path createTempProperties() throws IOException
    {
        Path tempDir = Files.createTempDirectory(getClass().getSimpleName());
        Path tempFile = tempDir.resolve("workon.properties");
        Files.copy(versionResourceFile.toPath(), tempFile);
        return tempFile;
    }
}
