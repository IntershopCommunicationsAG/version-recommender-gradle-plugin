package com.intershop.tool.version.recommender.semantic;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class SemanticVersions
{
    private static final Comparator<SemanticVersion> COMPARATOR_NEWEST_FIRST = (o1, o2) -> {
        int majorDiff = o2.getMajor() - o1.getMajor();
        if (majorDiff != 0)
        {
            return majorDiff;
        }
        int minorDiff = o2.getMinor() - o1.getMinor();
        if (minorDiff != 0)
        {
            return minorDiff;
        }
        int patchDiff = o2.getPatch() - o1.getPatch();
        if (patchDiff != 0)
        {
            return patchDiff;
        }
        if (o2.getIncrementState() == null || o1.getIncrementState() == null)
        {
            if (o1.getIncrementState() != null)
            {
                return - o1.getIncrementState().ordinal();
            }
            if (o2.getIncrementState() != null)
            {
                return o2.getIncrementState().ordinal();
            }
        }
        else
        {
            int patchState = o2.getIncrementState().compareTo(o1.getIncrementState());
            if (patchState != 0)
            {
                return patchState;
            }
        }
        return o2.getIncrement() - o1.getIncrement();
    };

    public static String getNewestVersion(UpdateStrategies allowedChanges, Collection<String> versions, String current)
    {
        return getNewestVersion(allowedChanges, versions.stream().map(SemanticVersion::valueOf).collect(Collectors.toList()), SemanticVersion.valueOf(current)).getVersion();
    }

    private static SemanticVersion getNewestVersion(UpdateStrategies allowedChanges, Collection<SemanticVersion> versions, SemanticVersion current)
    {
        SemanticVersion result;
        switch(allowedChanges)
        {
            case MAJOR:
                result = getNewestMajorVersion(versions, current);
                break;
            case MINOR:
                result = getNewestMinorVersion(versions, current);
                break;
            case PATCH:
                result = getNewestPatchVersion(versions, current);
                break;
            case INC:
                result = getNewestAvailableVersion(versions, current);
                break;
            case STICK:
                result = current;
                break;
            default:
                throw new IllegalArgumentException("Unknown meaning provided");
        }
        return result;
    }
    /**
     * Find release, where only the patch version is updated. (used for stabilization branches)
     *
     * @param source
     *            version
     * @param versions
     *            available version
     * @return null in case the source version in not included in the versions list
     */
    private static SemanticVersion getNewestPatchVersion(Collection<SemanticVersion> versions, SemanticVersion current)
    {
        Optional<SemanticVersion> firstElement = versions.stream()
                        .filter(v -> v.isIncrementable())
                        .filter(v -> v.getMajor() == current.getMajor())
                        .filter(v -> v.getMinor() == current.getMinor())
                        .filter(v -> ReleaseType.GA.equals(v.getIncrementState()))
                        .sorted(COMPARATOR_NEWEST_FIRST).findFirst();
        return firstElement.isPresent() ? firstElement.get() : current;
    }

    /**
     * Find release, where only the patch version is updated. (used for stabilization branches)
     *
     * @param source
     *            version
     * @param versions
     *            available version
     * @return null in case the source version in not included in the versions list
     */
    private static SemanticVersion getNewestMinorVersion(Collection<SemanticVersion> versions, SemanticVersion current)
    {
        Optional<SemanticVersion> firstElement = versions.stream()
                        .filter(v -> v.isIncrementable())
                        .filter(v -> v.getMajor() == current.getMajor())
                        .filter(v -> ReleaseType.GA.equals(v.getIncrementState()))
                        .sorted(COMPARATOR_NEWEST_FIRST).findFirst();
        return firstElement.isPresent() ? firstElement.get() : current;
    }

    /**
     * Find release, where major,minor,patch version can be updated. (used external dependencies for trunk/master)
     *
     * @param source
     *            version
     * @param versions
     *            available version
     * @return null in case the source version in not included in the versions list
     */
    private static SemanticVersion getNewestMajorVersion(Collection<SemanticVersion> versions, SemanticVersion current)
    {
        Optional<SemanticVersion> firstElement = versions.stream()
                        .filter(v -> v.isIncrementable())
                        .filter(v -> v.getIncrement() == 0)
                        .filter(v -> ReleaseType.GA.equals(v.getIncrementState()))
                        .sorted(COMPARATOR_NEWEST_FIRST).findFirst();
        return firstElement.isPresent() ? firstElement.get() : current;
    }

    /**
     * Find any increment, where major,minor,patch,increment can be updated. (used internal dependencies for
     * trunk/master)
     *
     * @param source
     *            version
     * @param versions
     *            available version
     * @return null in case the source version in not included in the versions list
     */
    private static SemanticVersion getNewestAvailableVersion(Collection<SemanticVersion> versions, SemanticVersion current)
    {
        Optional<SemanticVersion> firstElement = versions.stream()
                        .filter(v -> v.isIncrementable())
                        .sorted(COMPARATOR_NEWEST_FIRST).findFirst();
        return firstElement.isPresent() ? firstElement.get() : current;
    }
}
