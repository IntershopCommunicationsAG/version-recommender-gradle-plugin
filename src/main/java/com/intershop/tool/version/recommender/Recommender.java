package com.intershop.tool.version.recommender;

import java.util.Collection;
import java.util.Map;

import com.intershop.tool.version.recommender.resolver.VersionResolver;
import com.intershop.tool.version.recommender.semantic.SemanticVersion;
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
            String currentVersion = versionProvider.getVersion(dep.getGroupID(), dep.getArtifactID());
            if (currentVersion == null)
            {
                continue;
            }
            UpdateStrategies allowedChanges = getAllowedChanges(dep.getGroupID(), dep.getArtifactID(), strategy,
                            exceptions);
            if (UpdateStrategies.STICK.equals(allowedChanges))
            {
                continue;
            }
            if (isIncrementable(currentVersion))
            {
                Collection<String> availableVersions = versionResolver.getVersions(dep.getGroupID(),
                                dep.getArtifactID());
                String newestPatchString = SemanticVersions.getNewestVersion(allowedChanges, availableVersions,
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
            else
            {
                System.out.printf("dependency '%s:%s' with version '%s' can't be incremented automatically\r\n",
                                dep.getGroupID(), dep.getArtifactID(),
                                currentVersion == null ? "(none)" : currentVersion);
            }
        }
        System.out.printf("%d dependencies are up to date\r\n", counterUp2Date);
    }

    private static boolean isIncrementable(String currentVersion)
    {
        return SemanticVersion.valueOf(currentVersion).isIncrementable();
    }

    public String getVersion(String groupID, String artifactID, String strategy, Map<String, String> exceptions)
    {
        if (groupID == null || artifactID == null || groupID.isEmpty() || artifactID.isEmpty())
        {
            return null;
        }
        UpdateStrategies allowedChanges = getAllowedChanges(groupID, artifactID, strategy, exceptions);
        String currentVersion = versionProvider.getVersion(groupID, artifactID);
        if (UpdateStrategies.STICK.equals(allowedChanges))
        {
            return currentVersion;
        }
        Collection<String> availableVersions = versionResolver.getVersions(groupID, artifactID);
        try
        {
            String newestVersion = SemanticVersions.getNewestVersion(allowedChanges, availableVersions, currentVersion);
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

    private static UpdateStrategies getAllowedChanges(String groupID, String artifactID, String strategy,
                    Map<String, String> exceptions)
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
        dependencies.forEach(d -> versionProvider.setVersion(d.getGroupID(), d.getArtifactID(), getVersion(d.getGroupID(), d.getArtifactID(), strategy, exceptions)));
        versionProvider.storeVersions();
    }
}
