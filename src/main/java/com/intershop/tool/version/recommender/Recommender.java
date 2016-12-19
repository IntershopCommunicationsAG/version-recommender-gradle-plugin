package com.intershop.tool.version.recommender;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.intershop.tool.version.recommender.resolver.VersionResolver;
import com.intershop.tool.version.recommender.semantic.SemanticVersions;
import com.intershop.tool.version.recommender.semantic.UpdateStrategies;

public class Recommender
{
    private final VersionResolver versionResolver;
    private final VersionProvider versionProvider;

    public Recommender(VersionResolver versionResolver, VersionProvider versionProvider)
    {
        this.versionResolver = versionResolver;
        this.versionProvider = versionProvider;
    }

    public void recommendVersions(Collection<Dependency> dependencies, String strategy, Map<String, String> exceptions)
    {
        int counterUp2Date = 0;
        for (Dependency dep : dependencies)
        {
            if (dep.getGroupID().toLowerCase().indexOf("intershop") < 0)
            {
                Collection<String> availableVersions = versionResolver.getVersions(dep.getGroupID(),
                                dep.getArtifactID());
                String newestPatchString = SemanticVersions.getNewestVersion(getAllowedChanges(dep.getGroupID(), dep.getArtifactID(), strategy, exceptions), availableVersions,
                                dep.getVersion());
                if (!newestPatchString.equals(dep.getVersion()))
                {
                    System.out.println(dep.getGroupID() + ":" + dep.getArtifactID() + ":" + dep.getVersion() + ":"
                                    + newestPatchString);
                }
                else
                {
                    counterUp2Date++;
                }
            }
        }
        System.out.printf("%d dependencies are up to date", counterUp2Date);
    }

    public String getVersion(String groupID, String artifactID, String strategy, Map<String, String> exceptions)
    {
        if (groupID == null || artifactID == null || groupID.isEmpty() || artifactID.isEmpty())
        {
            return null;
        }
        Collection<String> availableVersions = versionResolver.getVersions(groupID, artifactID);
        String currentVersion = versionProvider.getVersion(groupID, artifactID);
        try
        {
            String newestVersion = SemanticVersions.getNewestVersion(getAllowedChanges(groupID, artifactID, strategy, exceptions),
                            availableVersions, currentVersion);
            if (newestVersion != null && !newestVersion.equals(currentVersion))
            {
                System.out.println(groupID + ":" + artifactID + ":" + currentVersion + ":" + newestVersion);
            }
            return newestVersion == null ? currentVersion : newestVersion;
        }
        catch(Exception e)
        {
            // System.err.printf("lookup for newer version failed for '%s:%s:%s' %s.\n", groupID, artifactID,
            // currentVersion, e.getMessage());
            return currentVersion;
        }
    }

    private static UpdateStrategies getAllowedChanges(String groupID, String artifactID, String strategy, Map<String, String> exceptions)
    {
        if (exceptions.containsKey(groupID + ":" + artifactID))
        {
            return UpdateStrategies.valueOf(exceptions.get(groupID + ":" + artifactID));
        }
        if (exceptions.containsKey(groupID))
        {
            return UpdateStrategies.valueOf(exceptions.get(groupID));
        }
        return UpdateStrategies.valueOf(strategy);
    }

    public void store(Collection<Dependency> dependencies, String strategy, Map<String, String> exceptions)
    {
        Map<String, String> collectedNewVersions = new HashMap<>();
        dependencies.forEach(d -> collectedNewVersions.put(d.getGroupID() + ":" + d.getArtifactID(),
                        getVersion(d.getGroupID(), d.getArtifactID(), strategy, exceptions)));
        versionProvider.storeVersions(collectedNewVersions);
        collectedNewVersions.clear();
    }
}
