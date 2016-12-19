package com.intershop.tool.version.recommender.xml;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public class Artifact
{
    public static final String REGEX_SPLIT_VERSIONS = "[|]";

    @XmlAttribute
    private Date latestUpdate;
    @XmlAttribute
    private String groupId;
    @XmlAttribute
    private String artifactId;
    @XmlAttribute
    private String versions;

    public Artifact()
    {
        // default constructor
    }

    public Artifact(String groupId, String artifactId, Date latestUpdate, Collection<String> versions)
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.latestUpdate = latestUpdate;
        StringJoiner joiner = new StringJoiner("|");
        versions.forEach(e -> joiner.add(e));
        this.setVersions(joiner.toString());
        latestUpdate = new Date();
    }

    @XmlTransient
    public Date getLatestUpdate()
    {
        return latestUpdate;
    }

    public void setLatestUpdate(Date latestUpdate)
    {
        this.latestUpdate = latestUpdate;
    }

    @XmlTransient
    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    @XmlTransient
    public String getArtifactId()
    {
        return artifactId;
    }

    public void setArtifactId(String artifactId)
    {
        this.artifactId = artifactId;
    }

    @XmlTransient
    public String getVersionAsString()
    {
        return versions;
    }

    public void setVersions(String versions)
    {
        this.versions = versions;
    }

    public List<String> getVersions()
    {
        return Arrays.asList(versions.split(REGEX_SPLIT_VERSIONS));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Artifact other = (Artifact)obj;
        if (artifactId == null)
        {
            if (other.artifactId != null)
                return false;
        }
        else if (!artifactId.equals(other.artifactId))
            return false;
        if (groupId == null)
        {
            if (other.groupId != null)
                return false;
        }
        else if (!groupId.equals(other.groupId))
            return false;
        return true;
    }

}
