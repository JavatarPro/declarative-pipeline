/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.domain

/**
 * @author Borys Zora
 * @version 2022-09-24
 */
class Maven implements Serializable {
    String repo_id
    String repo_url
    String jenkins_tool_mvn
    String jenkins_tool_jdk
    String build_cmd = "mvn clean install"
    String integration_test_cmd = "mvn -B verify -DskipITs=false"
    String params
    String layout = "default"
}
