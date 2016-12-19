package com.intershop.tool.version.recommender;

public class Dependency
{
    private String groupID;
    private String artifactID;
    private String version;

    public Dependency(String groupID, String artifactID, String version)
    {
        this.groupID = groupID;
        this.artifactID = artifactID;
        this.version = version;
    }

    public String getGroupID()
    {
        return groupID;
    }

    public String getArtifactID()
    {
        return artifactID;
    }

    public String getVersion()
    {
        return version;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactID == null) ? 0 : artifactID.hashCode());
        result = prime * result + ((groupID == null) ? 0 : groupID.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        Dependency other = (Dependency)obj;
        if (artifactID == null)
        {
            if (other.artifactID != null)
                return false;
        }
        else if (!artifactID.equals(other.artifactID))
            return false;
        if (groupID == null)
        {
            if (other.groupID != null)
                return false;
        }
        else if (!groupID.equals(other.groupID))
            return false;
        if (version == null)
        {
            if (other.version != null)
                return false;
        }
        else if (!version.equals(other.version))
            return false;
        return true;
    }

}
