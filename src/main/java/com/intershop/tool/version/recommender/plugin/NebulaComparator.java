package com.intershop.tool.version.recommender.plugin;

import java.util.Comparator;
import java.util.Map.Entry;

public class NebulaComparator implements Comparator<Entry<String, String>>
{
    public static final Comparator<Entry<String, String>> INSTANCE = new NebulaComparator();

    @Override
    public int compare(Entry<String, String> o1, Entry<String, String> o2)
    {
        if (o1.getValue().startsWith("$") && !o2.getValue().startsWith("$"))
        {
            return 1;
        }
        if (!o1.getValue().startsWith("$") && o2.getValue().startsWith("$"))
        {
            return -1;
        }
        return o1.getKey().compareTo(o2.getKey());
    }
}
