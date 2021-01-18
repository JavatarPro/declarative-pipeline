/**
 * Copyright Javatar LLC 2018 ©
 * Licensed under the License located in the root of this repository (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://github.com/JavatarPro/declarative-pipeline/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pro.javatar.pipeline.model

import pro.javatar.pipeline.exception.UnrecognizedBuildServiceTypeException
import pro.javatar.pipeline.exception.UnrecognizedRevisionControlTypeException

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
// will be @Deprecated soon in 2.x release
// @see VersioningType, ReleaseType, ReleaseUploadArtifactType as it could be few builds (like maven & docker)
enum BuildServiceType implements Serializable {

    DOCKER,
    MAVEN,
    GRADLE,
    NPM, // e.g. angular + AWS S3
    NPM_DOCKER, // e.g. react + docker
    SENCHA,
    PHP,
    PHP_PYTHON, // python code with composer.json for versioning from php
    PYTHON

    static BuildServiceType fromString(String type) {
        if (type == null) {
            throw new UnrecognizedRevisionControlTypeException("type is null")
        }
        if("maven".equalsIgnoreCase(type) || "mvn".equalsIgnoreCase(type)) {
            return MAVEN
        }
        if("docker".equalsIgnoreCase(type)) {
            return DOCKER
        }
        if("gradle".equalsIgnoreCase(type) || "gradlew".equalsIgnoreCase(type)) {
            return GRADLE
        }
        if ("npm".equalsIgnoreCase(type) || "node".equalsIgnoreCase(type)
                || "nodejs".equalsIgnoreCase(type) || "npm_cdn".equalsIgnoreCase(type)) {
            return NPM
        }
        if ("npm_docker".equalsIgnoreCase(type) || "docker_nodejs".equalsIgnoreCase(type)
                || "nodejs_docker".equalsIgnoreCase(type) || "node_docker".equalsIgnoreCase(type)
                || "docker_npm".equalsIgnoreCase(type) || "react_docker".equalsIgnoreCase(type)
                || "angular_docker".equalsIgnoreCase(type)) {
            return NPM_DOCKER
        }
        if ("sencha".equalsIgnoreCase(type) || "extjs".equalsIgnoreCase(type) || "ext".equalsIgnoreCase(type)) {
            return SENCHA
        }
        if ("php".equalsIgnoreCase(type)) {
            return PHP
        }
        if ("php_python".equalsIgnoreCase(type) || "php-python".equalsIgnoreCase(type)
                || "python-php".equalsIgnoreCase(type) || "python_php".equalsIgnoreCase(type)) {
            return PHP_PYTHON
        }
        if ("python".equalsIgnoreCase(type)) {
            return PYTHON
        }
        throw new UnrecognizedBuildServiceTypeException("type " + type + " is not recognized")
    }

}