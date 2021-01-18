/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.release

/**
 * @author Borys Zora
 * @version 2021-01-17
 */
enum ReleaseType {

    ARTIFACT_UPLOAD, // could be ARTIFACT_PROMOTION
    VCS_TAG

    // TODO add exception
    static ReleaseType fromString(String type) {
        if (type == null) {
            throw new RuntimeException("ReleaseType: release type is null")
        }
        if("docker".equalsIgnoreCase(type)) {
            return ARTIFACT_UPLOAD
        }
        if("vcs".equalsIgnoreCase(type)) {
            return VCS_TAG
        }
        throw new RuntimeException("ReleaseType: type " + type + " is not recognized")
    }
}
