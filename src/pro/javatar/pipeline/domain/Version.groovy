/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.domain

/**
 * @author Borys Zora
 * @version 2022-09-19
 */
class Version implements Serializable {
    String major
    String minor
    String patch
    String build

    String toString() {
        return "${major}.${minor}.${patch}.b${build}"
    }

    static Version fromString(String version) {
        Version v = new Version();
        String[] items = release(version).split("\\.")
        v.major = items[0]
        if (items.length > 1) v.minor = items[1]
        if (items.length > 2) v.patch = items[2]
        if (items.length > 3) v.build = build(items[3])
        return v
    }

    private static String release(String rawVersion) {
        return rawVersion.replace("-SNAPSHOT", "")
    }

    private static String build(String buildNumber) {
        if (buildNumber.startsWith("b")) {
            return buildNumber.substring(1)
        }
        return buildNumber;
    }
}

