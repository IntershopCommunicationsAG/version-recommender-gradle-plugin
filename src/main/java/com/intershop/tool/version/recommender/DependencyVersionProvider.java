package com.intershop.tool.version.recommender;

import java.util.Collection;

public class DependencyVersionProvider implements VersionProvider
{
    private Collection<Dependency> dependencies;

    public DependencyVersionProvider(Collection<Dependency> dependencies)
    {
        this.dependencies = dependencies;
    }

    @Override
    public String getVersion(String group, String name)
    {
        return dependencies.stream().filter(t -> t.getGroupID().equals(group) && t.getArtifactID().equals(name))
                        .findAny().map(t -> t.getVersion()).get();
    }

    @Override
    public void setVersion(String group, String name, String version)
    {
        throw new UnsupportedOperationException("Can't store versions at dependencies");
    }

    @Override
    public void storeVersions()
    {
        throw new UnsupportedOperationException("Can't store versions at dependencies");
    }

}
