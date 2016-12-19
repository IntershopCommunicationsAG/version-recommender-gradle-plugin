package com.intershop.tool.version.recommender.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.intershop.tool.version.recommender.VersionProvider;

public class NebulaVersionProperties implements VersionProvider
{
    private final Map<String, String> properties;
    private final File propertiesFile;

    public NebulaVersionProperties(File propertiesFile)
    {
        this.propertiesFile = propertiesFile;
        this.properties = loadProperties(propertiesFile);
    }

    @Override
    public String getVersion(String group, String name)
    {
        return getVersion(group + ":" + name);
    }
    public String getVersion(String key)
    {
        String value = properties.get(key);
        while(value != null && value.startsWith("$"))
        {
            value = properties.get(value.substring(1));
        }
        return value;
    }

    private static Map<String, String> loadProperties(File file)
    {
        Map<String, String> result = new HashMap<>();
        if (!file.exists())
        {
            LoggerFactory.getLogger(NebulaVersionProperties.class).error("version properties file '{}' doesn't exist", file.getAbsolutePath());
        }
        try
        {
            for(String line : Files.readAllLines(file.toPath()))
            {
                String[] parts = line.split("=");
                if (parts.length == 2)
                {
                    result.put(parts[0].trim(), parts[1].trim());
                } // ignore wrong lines
            }
        }
        catch(IOException e)
        {
            LoggerFactory.getLogger(NebulaVersionProperties.class).error("Can't load version properties file", e);
        }
        return result;
    }

    @Override
    public void storeVersions(Map<String, String> newVersions)
    {
        newVersions.entrySet().forEach(e -> setVersion(e.getKey(), e.getValue()));
        storeVersions();
    }

    private static void storeProperties(File file, Map<String, String> properties)
    {
        try(FileWriter writer = new FileWriter(file))
        {
            properties.entrySet().stream().filter(e -> e.getValue() != null).sorted(NebulaComparator.INSTANCE).forEach(e -> {
                try
                {
                    writer.write(e.getKey() + "=" + e.getValue() + "\n");
                }
                catch(IOException e1)
                {
                    LoggerFactory.getLogger(NebulaVersionProperties.class).error("Can't write version", e);
                }
            });
        }
        catch(IOException e1)
        {
            LoggerFactory.getLogger(NebulaVersionProperties.class).error("Can't write version", e1);
        }
    }

    @Override
    public void setVersion(String group, String name, String version)
    {
        setVersion(group + ":" + name, version);
    }

    private void setVersion(String key, String version)
    {
        String existingValue = properties.get(key);
        while (existingValue != null && existingValue.startsWith("$"))
        {
            key = existingValue.substring(1);
            existingValue = properties.get(key);
        }
        properties.put(key, version);
    }

    @Override
    public Map<String, String> getVersions()
    {
        Map<String, String> result = new HashMap<>();
        for(String key : properties.keySet())
        {
            result.put(key, getVersion(key));
        }
        return result;
    }

    @Override
    public void storeVersions()
    {
        storeProperties(propertiesFile, properties);
    }
}
