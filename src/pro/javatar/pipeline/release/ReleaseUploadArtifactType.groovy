/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.release

/**
 * @author Borys Zora
 * @version 2021-01-17
 */
enum ReleaseUploadArtifactType {

    DOCKER,
    S3,
    MAVEN2,
    NPM,
    NO_UPLOAD

    static ReleaseUploadArtifactType fromString(String type) {
        if (type == null) {
            throw new RuntimeException("ReleaseType: release type is null")
        }
        if("docker".equalsIgnoreCase(type)) {
            return DOCKER
        }
        if("s3".equalsIgnoreCase(type)) {
            return S3
        }
        if("maven2".equalsIgnoreCase(type)) {
            return MAVEN2
        }
        if("npm".equalsIgnoreCase(type)) {
            return NPM
        }
        if("vcs".equalsIgnoreCase(type)) {
            return NO_UPLOAD
        }
        throw new RuntimeException("ReleaseType: type " + type + " is not recognized")
    }
}