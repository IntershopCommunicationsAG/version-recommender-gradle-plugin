package com.intershop.tool.version.recommender.resolver;

import java.util.Collection;

public interface VersionResolver
{
    /**
     * Resolves a list of versions for a given artifact
     *
     * @param group
     * @param artifactID
     * @return list of versions
     */
    Collection<String> getVersions(String group, String artifactID);
}
