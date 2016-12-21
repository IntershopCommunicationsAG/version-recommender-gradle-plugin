package com.intershop.tool.version.recommender;

public interface VersionProvider
{
    String getVersion(String group, String name);

    void setVersion(String group, String name, String version);

    void storeVersions();
}
