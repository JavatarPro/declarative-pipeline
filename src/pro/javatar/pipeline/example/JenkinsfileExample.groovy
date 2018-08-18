/**
 * Copyright Javatar LLC 2018 ©
 * Licensed under the License located in the root of this repository (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://github.com/JavatarPro/pipeline-utils/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pro.javatar.pipeline.example

import pro.javatar.pipeline.Flow
import pro.javatar.pipeline.builder.DockerBuilder
import pro.javatar.pipeline.builder.FlowBuilder
import pro.javatar.pipeline.builder.Maven
import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.builder.RevisionControlBuilder
import pro.javatar.pipeline.builder.YamlFlowBuilder
import pro.javatar.pipeline.stage.Stage

import static pro.javatar.pipeline.model.StageType.*

/**
 * @author Borys Zora
 * @since 2017-10-15
 */
@GrabConfig(systemClassLoader=true)
@GrabResolver(name='atlassian', root='https://maven.atlassian.com/content/groups/public/')
@Grapes([
        @Grab('com.fasterxml.jackson.core:jackson-databind:2.9.6'),
        @Grab('com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.6'),
        @GrabConfig( systemClassLoader=true )
])
class JenkinsfileExample {

    String repo = "test2-service"
    def env = new Env()

    Map<String, String> jenkinsParamsSimulation = new HashMap<>();

    JenkinsfileExample() {
        jenkinsParamsSimulation.put("repoOwner", "bzo")
        jenkinsParamsSimulation.put("buildType", "mvn")
        jenkinsParamsSimulation.put("label", "pipeline")
        jenkinsParamsSimulation.put("flowPrefix", "prefix")
        jenkinsParamsSimulation.put("swaggerDocsEnabled", "false")
        jenkinsParamsSimulation.put("revisionControl", "git")
        jenkinsParamsSimulation.put("commaSeparatedStageTypes", "AUTO_TESTS,DEPLOY_ON_STAGING_ENV")
    }

    static void main(String[] args) {
        Flow flow = new YamlFlowBuilder(this).build()
//        new JenkinsfileExample().execute("some-ui")
//        println(new JenkinsfileExample().domainUrl())
//        println(new JenkinsfileExample().containsBranch())
    }

    def domainUrl() {
        String dockerRepositoryUrl = "registry.javatar.pro:5000/services/dev/"
        String domain = dockerRepositoryUrl.split("/")[0]
        println(domain)
    }

    void execute(String repo) {
        Flow flow = new FlowBuilder(this)
                .withServiceName(repo) // TODO move to appropriate services
                .withBuildType(getProperty('buildType', "maven"))
                .withUseBuildNumberForVersion(false)
                .addPipelineStages(getProperty("pipelineSuit", "service"))
                .addMaven(new Maven(getProperty("maven", "maven_353"))
                .withGroupId("pro.javatar.ui")
                .withRepositoryId("nexus")
                .withRepoUrl("http://nexus/url")
                .withMavenParams(getProperty("mavenParams", ""))
                .withJava(getProperty("java", "JDK8_162")))
                .addModuleRepository("http://nexus/repository/npm-group/")
                .withRevisionControl(new RevisionControlBuilder()
                .withRepo(repo)
                .withRepoOwner(getProperty("vcsRepoOwner", "services"))
                .withCredentialsId(getProperty("vcsCredentialsId", "javatar-gitlab-jenkins"))
                .withDomain(getProperty("vcsDomain", "gitlab.javatar.pro"))
                .withVcsRepositoryType(getProperty("vcsRepositoryType", "gitlab"))
                .withRevisionControlType(getProperty('revisionControl', "git")))
                .withDocker(new DockerBuilder()
                .withRepo(getProperty('dockerRepo', "registry.javatar.pro:5000/services/prod/"))
                .withDockerDevRepo(getProperty('dockerDevRepo', "registry.javatar.pro:5000/services/dev/")))
                .build();

        flow.execute();
    }

    List<String> getBranches() {
        Set<String> result = new HashSet<>()
        getGitBranches().split().findAll { it -> (!it.contains("*")
                && !it.contains("HEAD") && !it.contains("->"))}
            .each{it -> result.add(it.replace("origin/", "")
                .replace("remotes/", ""))}
        return new ArrayList<>(result)
    }

    String getGitBranches() {
        return "* develop\n" +
                "  master\n" +
                "  remotes/origin/HEAD -> origin/master\n" +
                "  remotes/origin/develop\n" +
                "  remotes/origin/master"
    }

    def sh(String command) {
        println("sh: ${command}")
    }

    def sh(String returnStatus, String command) {
        println("sh: returnStatus: ${returnStatus}, command: ${command}")
    }

    def echo(String message) {
        println("echo: ${message}")
    }

    def getProperty(String property, String defaultValue) {
        String result = jenkinsParamsSimulation.get(property)
        if (result == null) {
            return defaultValue
        }
        return result
    }

    def tool(String tool) {
        println("tool: ${tool}")
    }

    def timeout(def time, def unit) {
        println("timeout: time: ${time}, unit: ${unit}")
    }

    def dir() {
        println("dir:")
    }

    def dir(String folder) {
        println("dir: with folder: ${folder}")
    }

    def dir(def folder) {
        println("dir: with folder: ${folder}")
    }

    def dir(def folder, def other) {
        println("dir: with folder: ${folder}")
    }

    def checkout(String... args) {
        println("checkout with args: ${args}")
    }

    def stage(String stageName, def closure) {
        println("stage: with stageName: ${stageName}")
       // closure.stage.execute() // does not work
        // closure.thisObject.stages.get(0).execute() // works fine
        Stage currentStage = closure.thisObject.stages.find{ it -> stageName.equals(it.getName())}
        currentStage.execute()
    }

    def parallel(def closure) {
        println("parallel: ${closure}")
    }

    class Env {
        def M2_HOME
        def JAVA_HOME
        def PATH
    }

}
