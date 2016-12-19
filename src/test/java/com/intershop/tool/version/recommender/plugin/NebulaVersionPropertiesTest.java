package com.intershop.tool.version.recommender.plugin;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

import com.intershop.tool.version.recommender.VersionProvider;

public class NebulaVersionPropertiesTest
{
    private final File versionResourceFile = new File(getClass().getResource("/version.properties").getFile());
    private VersionProvider versionProvider = new NebulaVersionProperties(versionResourceFile);

    @Test
    public void testSimpleVersion()
    {
        assertEquals("2.5", versionProvider.getVersion("commons-io", "commons-io"));
    }

    @Test
    public void testIndirectVersion()
    {
        assertEquals("2.4.14", versionProvider.getVersion("com.typesafe.akka", "akka-testkit_2.11"));
    }

    @Test
    public void testStorage() throws IOException
    {
        Path tempDir = Files.createTempDirectory(getClass().getSimpleName());
        Path tempFile = tempDir.resolve("workon.properties");
        Files.copy(versionResourceFile.toPath(), tempFile);
        NebulaVersionProperties underTest = new NebulaVersionProperties(tempFile.toFile());
        underTest.storeVersions();
        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("count of lines", 5, lines.size());
        assertEquals("line 1", "com.typesafe.akka=2.4.14", lines.get(0));
        assertEquals("line 2", "commons-io:commons-io=2.5", lines.get(1));
        assertEquals("line 3", "com.typesafe.akka:akka-actor_2.11=$com.typesafe.akka", lines.get(2));
    }

    @Test
    public void testModifyVersion() throws IOException
    {
        Path tempDir = Files.createTempDirectory(getClass().getSimpleName());
        Path tempFile = tempDir.resolve("workon.properties");
        Files.copy(versionResourceFile.toPath(), tempFile);
        NebulaVersionProperties underTest = new NebulaVersionProperties(tempFile.toFile());
        underTest.setVersion("com.typesafe.akka", "akka-actor_2.11", "2.5.1");
        underTest.storeVersions();
        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("count of lines", 5, lines.size());
        assertEquals("line 1", "com.typesafe.akka=2.5.1", lines.get(0));
        assertEquals("line 2", "commons-io:commons-io=2.5", lines.get(1));
        assertEquals("line 3", "com.typesafe.akka:akka-actor_2.11=$com.typesafe.akka", lines.get(2));
    }
}
