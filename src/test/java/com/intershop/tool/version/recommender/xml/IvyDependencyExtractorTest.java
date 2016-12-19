package com.intershop.tool.version.recommender.xml;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import com.intershop.tool.version.recommender.Dependency;
import com.intershop.tool.version.recommender.resolver.IvyDependencyExtractor;

public class IvyDependencyExtractorTest
{

    @Test
    public void test() throws URISyntaxException
    {
        IvyDependencyExtractor underTest = new IvyDependencyExtractor();
        URL resourceUri = getClass().getClassLoader().getResource("ivy.xml");
        List<Dependency> dependencies = underTest.apply(resourceUri.toURI());
        assertEquals("count of dependencies", 5, dependencies.size());
        for (Dependency dep : dependencies)
        {
            if (dep.getGroupID().equals("ch.qos.logback") && dep.getArtifactID().equals("logback-classic"))
            {
                assertEquals(dep.getArtifactID() + " is correct", "1.0.13", dep.getVersion());
            }
            else if (dep.getGroupID().equals("com.amazonaws") && dep.getArtifactID().equals("aws-java-sdk-core"))
            {
                assertEquals(dep.getArtifactID() + " is correct", "1.10.47", dep.getVersion());
            }
        }
    }
}
