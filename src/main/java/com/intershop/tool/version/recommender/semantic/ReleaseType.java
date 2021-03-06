package com.intershop.tool.version.recommender.semantic;

public enum ReleaseType
{
    /**
     * The order is important for sorting number.
     * In case a RC release is available the DEV releases are behind
     */
    DEV, RC, GA;
}
