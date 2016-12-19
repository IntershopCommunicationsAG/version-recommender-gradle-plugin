package com.intershop.tool.version.recommender;

import java.util.Collections;
import java.util.Map;

public interface VersionProvider
{
    String getVersion(String group, String name);

    Map<String, String> getVersions();

    void setVersion(String group, String name, String version);

    void storeVersions(Map<String, String> newVersions);

    default void storeVersions()
    {
        storeVersions(Collections.emptyMap());
    }}
