package com.intershop.tool.version.recommender.plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;

import com.intershop.tool.version.recommender.Dependency;
import com.intershop.tool.version.recommender.VersionProvider;

public class ConfigurationCollection
{
    private final Project project;
    private final VersionProvider versionProvider;

    public ConfigurationCollection(Project project, VersionProvider versionProvider)
    {
        this.project = project;
        this.versionProvider = versionProvider;
    }

    public Collection<Dependency> getDependencies()
    {
        Set<Dependency> result = new HashSet<>();
        for (Configuration config : project.getConfigurations().getAsMap().values())
        {
            for (org.gradle.api.artifacts.Dependency dep : config.getAllDependencies())
            {
                if (dep.getGroup() != null && dep.getName() != null)
                {
                    if (dep.getVersion() != null)
                    {
                        result.add(new Dependency(dep.getGroup(), dep.getName(), dep.getVersion()));
                        if (!project.getVersion().equals(dep.getVersion()))
                        {
                            System.out.printf("dependency '%s:%s' has fix version at project '%s' config '%s'\n", dep.getGroup(), dep.getName(), project.getName(), config.getName());
                        }
                    }
                    else if (versionProvider != null)
                    {
                        String version = versionProvider.getVersion(dep.getGroup(), dep.getName());
                        if (version != null)
                        {
                            result.add(new Dependency(dep.getGroup(), dep.getName(), version));
                        }
                        else
                        {
                            System.out.printf("dependency '%s:%s' has no defined version at project '%s' config '%s'\n", dep.getGroup(), dep.getName(), project.getName(), config.getName());
                        }
                    }
                }
            }
        }
        return result;
    }
}
