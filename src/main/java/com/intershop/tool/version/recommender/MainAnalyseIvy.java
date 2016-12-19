package com.intershop.tool.version.recommender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.intershop.tool.version.recommender.resolver.CachedVersionResolver;
import com.intershop.tool.version.recommender.resolver.IvyDependencyExtractor;
import com.intershop.tool.version.recommender.resolver.MavenVersionFetcher;
import com.intershop.tool.version.recommender.resolver.VersionResolver;

public class MainAnalyseIvy
{
    private static IvyDependencyExtractor ivyExtractor = new IvyDependencyExtractor();
    private static File versionCache;
    static
    {
        try
        {
            versionCache = Files.createTempFile("version_cache", ".xml").toFile();
            versionCache.deleteOnExit();
        }
        catch(IOException e)
        {
            LoggerFactory.getLogger(MainAnalyseIvy.class).warn("Can't create temporary cache file", e);
        }
    }
    private static VersionResolver versionFetcher = new CachedVersionResolver(new MavenVersionFetcher(), versionCache);

    public static void main(String args[]) throws IOException
    {
        versionUpdate(new File(args[0]), args[1]);
    }

    public static void versionUpdate(File ivyXmlFile, String strategy)
    {
        List<Dependency> dependencies = ivyExtractor.apply(ivyXmlFile.toURI());
        recommendVersions(dependencies, strategy);
    }

    public static void recommendVersions(Collection<Dependency> dependencies, String strategy)
    {
        new Recommender(versionFetcher, new DependencyVersionProvider(dependencies)).recommendVersions(dependencies, strategy, Collections.emptyMap());
    }

    public static String getVersion(Collection<Dependency> dependencies, String strategy, String groupID,
                    String artifactID)
    {
        return new Recommender(versionFetcher, new DependencyVersionProvider(dependencies)).getVersion(groupID, artifactID, strategy, Collections.emptyMap());
    }
}
