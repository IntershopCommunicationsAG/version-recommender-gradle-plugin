package com.intershop.tool.version.recommender.xml;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Versions
{
    private Set<Artifact> artifact = new HashSet<>();

    public Set<Artifact> getArtifacts()
    {
        return artifact;
    }

    public void setArtifacts(Set<Artifact> artifacts)
    {
        this.artifact = artifacts;
    }
}
