package com.intershop.tool.version.recommender.resolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.slf4j.LoggerFactory;

import com.intershop.tool.version.recommender.xml.Artifact;
import com.intershop.tool.version.recommender.xml.Versions;
import com.intershop.tool.version.recommender.xml.XmlLoader;

public class CachedVersionResolver implements VersionResolver
{
    private static final XmlLoader loader = new XmlLoader();

    /**
     * Version resolver, which "produces" the versions
     */
    private final VersionResolver delegate;
    private final File file;
    private volatile Versions cache = null;

    public CachedVersionResolver(VersionResolver delegate, File cache)
    {
        this.delegate = delegate;
        this.file = cache;
    }

    @Override
    public Collection<String> getVersions(String group, String artifactID)
    {
        Optional<List<String>> cacheEntry = getCachedVersion(group, artifactID);
        if (cacheEntry.isPresent()) {
            return cacheEntry.get();
        }
        Collection<String> result = delegate.getVersions(group, artifactID);
        try
        {
            storeAtCache(group, artifactID, result);
        }
        catch(IOException|JAXBException e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't store retrieved data", e);
        }
        return result;
    }

    /**
     * Resolves version from cache
     * @param group
     * @param artifactID
     * @return {@link Optional#isPresent()} could be false, in case the artifact is not stored at cache
     */
    private Optional<List<String>> getCachedVersion(String group, String artifactID)
    {
        if (cache == null)
        {
            cache = loadCache();
        }
        return cache.getArtifacts().stream().filter(e -> e.getArtifactId().equals(artifactID) && e.getGroupId().equals(group)).findAny().map(e -> e.getVersions());
    }

    private void storeAtCache(String group, String artifactID, Collection<String> result) throws IOException, JAXBException
    {
        if (cache == null)
        {
            cache = loadCache();
        }
        else
        {
            // load artifacts, may added in between
            cache.getArtifacts().addAll(loadCache().getArtifacts());
        }
        cache.getArtifacts().add(new Artifact(group, artifactID, new Date(), result));
        File dir = file.getParentFile();
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        try(FileWriter writer = new FileWriter(file))
        {
            loader.exportXML(cache, writer);
        }
    }

    private Versions loadCache()
    {
        if(file == null || !file.exists())
        {
            return new Versions();
        }
        try(FileInputStream is = new FileInputStream(file))
        {
            return loader.importXML(is, Versions.class);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Can't load file: " + file.getAbsolutePath(), e);
        }

    }
}
