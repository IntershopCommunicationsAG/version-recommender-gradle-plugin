package com.intershop.tool.version.recommender.plugin;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

import com.intershop.tool.version.recommender.VersionProvider;

public class IntershopVersionProviderTest extends AbstractVersionPropertiesTest
{
    private static final File versionResourceFile = new File(IntershopVersionProviderTest.class.getResource("/intershop_version.properties").getFile());

    public IntershopVersionProviderTest()
    {
        super(versionResourceFile);
    }

    @Override
    protected VersionProvider createTestObject(File propertiesFile)
    {
        return new IntershopVersionProvider(propertiesFile);
    }

    @Test
    public void testStorage() throws IOException
    {
        Path tempFile = createTempProperties();
        VersionProvider underTest = createTestObject(tempFile.toFile());
        underTest.storeVersions();
        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("count of lines", 4, lines.size());
        assertEquals("line 4", "version.commons-io.commons-io=2.5", lines.get(3));
        assertEquals("line 1", "version.com.typesafe.akka.akka-actor_2.11=2.4.14", lines.get(0));
    }

    @Test
    public void testModifyVersion() throws IOException
    {
        Path tempFile = createTempProperties();
        VersionProvider underTest = createTestObject(tempFile.toFile());
        underTest.setVersion("com.typesafe.akka", "akka-actor_2.11", "2.5.1");
        underTest.storeVersions();
        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("count of lines", 4, lines.size());
        assertEquals("line 4", "version.commons-io.commons-io=2.5", lines.get(3));
        assertEquals("line 1", "version.com.typesafe.akka.akka-actor_2.11=2.5.1", lines.get(0));
    }
}
