package com.intershop.tool.version.recommender.resolver;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.plugins.parser.ModuleDescriptorParserRegistry;

import com.intershop.tool.version.recommender.Dependency;

public class IvyDependencyExtractor implements Function<URI, List<Dependency>>
{
    @Override
    public List<Dependency> apply(URI ivyXmlLocation)
    {
        List<Dependency> result = new ArrayList<>();
        try
        {
            Ivy ivy = Ivy.newInstance();
            ModuleDescriptor module = ModuleDescriptorParserRegistry.getInstance().parseDescriptor(ivy.getSettings(),
                            ivyXmlLocation.toURL(), true);

            for (DependencyDescriptor dep : module.getDependencies())
            {
                String groupID = dep.getDependencyId().getOrganisation();
                String artifactID = dep.getDependencyId().getName();
                String version = dep.getDependencyRevisionId().getRevision();

                result.add(new Dependency(groupID, artifactID, version));
            }
        }
        catch(IOException | ParseException e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }
}
