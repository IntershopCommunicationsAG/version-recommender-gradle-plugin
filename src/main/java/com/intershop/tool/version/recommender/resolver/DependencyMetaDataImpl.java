package com.intershop.tool.version.recommender.resolver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.ivy.core.module.descriptor.ExcludeRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ModuleVersionSelector;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradle.internal.component.model.ComponentArtifactMetaData;
import org.gradle.internal.component.model.ConfigurationMetaData;
import org.gradle.internal.component.model.DependencyMetaData;
import org.gradle.internal.component.model.IvyArtifactName;

public class DependencyMetaDataImpl implements DependencyMetaData
{
    private final String group;
    private final String name;
    public DependencyMetaDataImpl(String group, String name)
    {
        this.group = group;
        this.name = name;
    }
    @Override
    public Set<IvyArtifactName> getArtifacts()
    {
        Set<IvyArtifactName> result = new HashSet<>();
        return result;
    }

    @Override
    public Set<ComponentArtifactMetaData> getArtifacts(ConfigurationMetaData arg0, ConfigurationMetaData arg1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getDependencyConfigurations(String arg0, String arg1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDynamicConstraintVersion()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExcludeRule[] getExcludeRules(Collection<String> arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getModuleConfigurations()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleVersionSelector getRequested()
    {
        return new ModuleVersionSelector()
        {
            @Override
            public boolean matchesStrictly(ModuleVersionIdentifier arg0)
            {
                return false;
            }

            @Override
            public String getVersion()
            {
                return "+";
            }

            @Override
            public String getName()
            {
                return name;
            }

            @Override
            public String getGroup()
            {
                return group;
            }
        };
    }

    @Override
    public ComponentSelector getSelector()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isChanging()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isForce()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTransitive()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public DependencyMetaData withChanging()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DependencyMetaData withRequestedVersion(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DependencyMetaData withTarget(ComponentSelector arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
