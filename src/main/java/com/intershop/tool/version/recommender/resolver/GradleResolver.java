package com.intershop.tool.version.recommender.resolver;

import java.util.Collection;

import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.ConfiguredModuleComponentRepository;
import org.gradle.api.internal.artifacts.repositories.ResolutionAwareRepository;
import org.gradle.internal.component.model.DependencyMetaData;
import org.gradle.internal.resolve.result.BuildableModuleVersionListingResolveResult;
import org.gradle.internal.resolve.result.DefaultBuildableModuleVersionListingResolveResult;
import org.slf4j.LoggerFactory;

public class GradleResolver implements VersionResolver
{
    private final ResolutionAwareRepository repository;

    public GradleResolver(ResolutionAwareRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public Collection<String> getVersions(String group, String artifactID)
    {
        BuildableModuleVersionListingResolveResult builder = new DefaultBuildableModuleVersionListingResolveResult();
        DependencyMetaData dependency = new DependencyMetaDataImpl(group, artifactID);
        ConfiguredModuleComponentRepository resolver = repository.createResolver();
        resolver.getRemoteAccess().listModuleVersions(dependency, builder);
        if (!builder.getVersions().isEmpty())
        {
            System.out.printf("gradleresolver found versions for %s:%s -> %s.\n", group, artifactID, builder.getVersions());
            LoggerFactory.getLogger(getClass()).info("gradleresolver found versions for {}:{} -> {}.", group, artifactID, builder.getVersions());
        }
        return builder.getVersions();
    }

}
