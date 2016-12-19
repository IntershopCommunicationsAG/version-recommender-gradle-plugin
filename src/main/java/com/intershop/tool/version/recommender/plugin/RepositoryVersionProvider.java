package com.intershop.tool.version.recommender.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.ResolutionAwareRepository;

import com.intershop.tool.version.recommender.resolver.GradleResolver;
import com.intershop.tool.version.recommender.resolver.MavenVersionFetcher;
import com.intershop.tool.version.recommender.resolver.VersionResolver;

public class RepositoryVersionProvider implements VersionResolver
{
    private final List<VersionResolver> resolvers = new ArrayList<>();

    public RepositoryVersionProvider(Project project)
    {
        SortedMap<String, ArtifactRepository> repositories = project.getRepositories().getAsMap();
        for(ArtifactRepository repository : repositories.values())
        {
            resolvers.add(getVersionResolver(repository));
        }
    }

    private static VersionResolver getVersionResolver(ArtifactRepository repository)
    {
        if (repository instanceof ResolutionAwareRepository)
        {
            return new GradleResolver((ResolutionAwareRepository) repository);
        }
        return new MavenVersionFetcher();
    }

    @Override
    public Collection<String> getVersions(String group, String artifactID)
    {
        for(VersionResolver resolver : resolvers)
        {
            Collection<String> versions = resolver.getVersions(group, artifactID);
            if (versions != null && !versions.isEmpty())
            {
                return versions;
            }
        }
        return Collections.emptyList();
    }

}
