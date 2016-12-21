package com.intershop.tool.version.recommender.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class XMLVersionLoaderTest
{
    public String getString(String resource) throws IOException
    {
        StringWriter writer = new StringWriter();
        IOUtils.copy(getInputStream(resource), writer, "UTF-8");
        return writer.toString().replace("\r", "");
    }

    private InputStream getInputStream(String resource)
    {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }

    @Test
    public void testExport() throws JAXBException, IOException
    {
        XmlLoader loader = new XmlLoader();
        Versions versions = new Versions();
        versions.getArtifacts().add(new Artifact("groupid", "artifactID", Arrays.asList("2.3.4", "2.4.5-RC3")));
        StringWriter writer = new StringWriter();
        loader.exportXML(versions, writer);
        assertEquals("content correct", getString("versions.xml"), writer.toString().replace("\r", ""));
    }

    @Test
    public void testImport() throws IOException
    {
        XmlLoader loader = new XmlLoader();
        Versions loaded = loader.importXML(getInputStream("versions.xml"), Versions.class);
        Collection<Artifact> arts = loaded.getArtifacts();
        assertEquals(1, arts.size());
        Artifact artifact = arts.stream().filter(a -> a.getGroupId().equals("groupid")).findAny().get();
        assertEquals("groupid", artifact.getGroupId());
        assertEquals("two versions", 2, artifact.getVersions().size());
    }
}
