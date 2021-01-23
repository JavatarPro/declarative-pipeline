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

package pro.javatar.pipeline.service.vcs

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.exception.GitFlowReleaseFinishException
import pro.javatar.pipeline.exception.InvalidBranchException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.vcs.model.VscCheckoutRequest
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @version 2018-03-12
 */
class GitService extends RevisionControlService {

    String credentialsId
    String repo
    String repoOwner
    String flowPrefix = ""
    GitFlowService gitFlowService

    GitService(String repo, String credentialsId,
               String repoOwner, String flowPrefix) {
        this.repo = repo
        this.credentialsId = credentialsId
        this.repoOwner = repoOwner
        this.flowPrefix = flowPrefix
        gitFlowService = new GitFlowService(this)
    }

    @Override
    def setUp() {
        Logger.debug("setUp git started")
        dsl.sh 'git config --global user.email "jenkins@javatar.pro"'
        dsl.sh 'git config --global user.name "jenkins"'
        Logger.debug("setUp git finished")
    }

    @Override
    def checkout(String branch) {
        Logger.info("GitService checkout repo: ${repo}, branch: ${branch}")
        String repoUrl = urlResolver.getRepoUrl()
        // dsl.git branch: 'master', changelog: true, credentialsId: credentialsId, poll: true, url: repoUrl
        Logger.debug("credentialsId: ${credentialsId}, url: ${repoUrl}")
        dsl.git credentialsId: credentialsId, url: repoUrl
        dsl.sh "pwd; ls -la"
        dsl.sh "git checkout ${branch}"
        Logger.info("GitService checkout successfully finished for repo: ${repo}, branch: ${branch}")
    }

    @Override
    def checkoutRepo(String repoOwner, String repo, String branch) {
        Logger.debug("GitService checkoutRepo: repoOwner: ${repoOwner}, repo: ${repo}, branch: ${branch}")
        String repoUrl = urlResolver.getRepoUrl(repoOwner, repo)
        return checkoutRepo(repoUrl, branch)
    }

    @Override
    def checkoutRepo(String repoUrl, String branch) {
        Logger.debug("checkoutRepo with repoUrl: ${repoUrl}, branch: ${branch}")
        VscCheckoutRequest vscCheckoutRequest = new VscCheckoutRequest().withBranch(branch).withRepoUrl(repoUrl)
                                                        .withCredentialsId(credentialsId)
        checkoutRepo(vscCheckoutRequest)
    }

    @Override
    def checkoutRepo(VscCheckoutRequest request) {
        Logger.debug("try to checkoutRepo with request: ${request}")
        // TODO remove hardcode, make configurable
        dsl.timeout(time: 5, unit: 'MINUTES') {
            dsl.git credentialsId: request.getCredentialsId(), url: request.getRepoUrl()
            dsl.sh "pwd; ls -la"
            dsl.sh "git checkout ${request.getBranch()}"
            Logger.debug("GitService#checkoutRepo successfully finished for repoUrl: ${request.getRepoUrl()}, " +
                    "branch: ${request.getBranch()}")
        }
    }

    @Override
    def createReleaseBranchLocally(String releaseVersion) {
        Logger.debug("createReleaseBranchLocally releaseVersion - ${releaseVersion}")
        dsl.sh "git status"
        dsl.sh "git flow release start ${releaseVersion}"
        Logger.debug("createReleaseBranchLocally finished")
    }

    @Override
    def commitChanges(String message) {
        Logger.info("commit changes with message: ${message}")
        dsl.sh "git status"
        dsl.sh "git add ."
        dsl.sh "git status"
        // jenkins does not change the code, nothing to verify by lints rules
        dsl.sh "git commit --no-verify -m \'${message}\'"
        Logger.info("successfully committed")
    }

    @Override
    def release(String releaseVersion) throws GitFlowReleaseFinishException {
        Logger.info("git release: ${releaseVersion} started")
        Logger.debug("creates tag, removes release branch")
        validateReleaseVersion(releaseVersion)
        dsl.sh "git status"
        dsl.sh "git flow release finish ${releaseVersion} -m \"Release ${releaseVersion}\""
        validateReleaseFinish()
        validateTagCreated(releaseVersion)
        Logger.info("git release: ${releaseVersion} finished")
    }

    @Override
    def switchToReleaseBranch(ReleaseInfo releaseInfo) {
        throw new UnsupportedOperationException("switchToReleaseBranch not yet implemented");
    }

    @Override
    def switchToBranch(String branch) {
        Logger.debug("switchToBranch: ${branch} started")
        dsl.sh "git checkout ${branch}"
        Logger.debug("switchToBranch: ${branch} finished")
    }

    @Override
    def createBranch(String branchName) {
        dsl.sh "git checkout -b ${branchName}"
        commitChanges("create new branch: ${branchName}")
    }

    @Override
    def createAndPushBranch(String branchName) {
        Logger.info("GitService:createAndPushBranch: branchName: ${branchName} started")
        dsl.sh "git checkout -b ${branchName}"
        dsl.sshagent([credentialsId]) {
            dsl.sh "git push -u origin ${branchName}"
        }
        Logger.info("GitService:createAndPushBranch: branchName: ${branchName} finished")
    }

    @Override
    def pushNewBranches() {
        Logger.debug("GitService:pushNewBranches started")
        dsl.sshagent([credentialsId]) {
            dsl.sh "git push --all -u"
        }
        Logger.debug("GitService:pushNewBranches finished")
    }

    @Override
    def pushNewBranch(String branchName) {
        Logger.info("GitService:pushNewBranch: ${branchName} started")
        // git branch --set-upstream origin yourbranch
        dsl.sshagent([credentialsId]) {
            dsl.sh "git push -u origin ${branchName}"
        }
        Logger.info("GitService:pushNewBranch: ${branchName} finished")
    }

    @Override
    String getProdBranch() {
        return BRANCH_MASTER
    }

    @Override
    String getRepoName() {
        return repo
    }

    @Override
    String getRepoOwner() {
        return repoOwner
    }

    @Override
    def pushRelease() {
        dsl.sshagent([credentialsId]) {
            dsl.sh "git push"
            dsl.sh "git push --all"
            dsl.sh "git push origin --tags"
        }
        //
//        dsl.withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialsId,
//                              usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
//            dsl.sh("git push https://${userName}:${dsl.env.GIT_PASSWORD}@bitbucket.org/${repoOwner}/${repo}.git")
//            dsl.sh("git push https://${userName}:${dsl.env.GIT_PASSWORD}@bitbucket.org/${repoOwner}/${repo}.git --tags")
//        }
    }

    def updateToDevelopBranch() {
        String branch = getDevBranchWithPrefix(flowPrefix)
        Logger.debug("updateDevelopVersion update to develop branch: ${branch} started")
        dsl.sh "git checkout ${branch}"
        validateBranch(branch)
        Logger.debug("updateDevelopVersion update to develop branch: ${branch} successfully finished")
    }

    def validateReleaseFinish() throws GitFlowReleaseFinishException {
        String currentBranch = getCurrentBranch()
        if(!"develop".equalsIgnoreCase(currentBranch)) {
            String errorMessage = "[git flow release finish] failed! " +
                    "Expected branch develop but was ${currentBranch}"
            Logger.error(errorMessage)
            throw new GitFlowReleaseFinishException(errorMessage)
        }
    }

    def validateBranch(String branch) {
        Logger.debug("validateBranch: ${branch} started")
        String currentBranch = getCurrentBranch()
        if (!branch.equalsIgnoreCase(currentBranch)){
            String errorMessage = "Invalid branch, expected: ${branch}, but was: ${currentBranch}"
            Logger.error(errorMessage)
            throw new InvalidBranchException(errorMessage)
        }
        Logger.debug("validateBranch: ${branch} finished")
    }

    def validateReleaseVersion(String releaseVersion) throws GitFlowReleaseFinishException {
        String currentBranch = getCurrentBranch()
        String expectedBranch = "release/${releaseVersion}"
        if (!expectedBranch.equalsIgnoreCase(currentBranch)) {
            String errorMessage = "[git flow release finish] failed! " +
                    "Expected branch same as releaseVersion: ${expectedBranch} but was ${currentBranch}"
            Logger.error(errorMessage)
            throw new GitFlowReleaseFinishException(errorMessage)
        }
    }

    def validateTagCreated(String tag) {
        Logger.info("validateTagCreated: ${tag} started")
        String actualTag = dsl.sh returnStdout: true, script: "git tag | grep ${tag}"
        actualTag = actualTag.trim()
        if (!tag.equalsIgnoreCase(actualTag)){
            String errorMessage = "Invalid branch, expected: ${tag}, but was: ${actualTag}"
            Logger.error(errorMessage)
            throw new InvalidBranchException(errorMessage)
        }
        Logger.info("validateTagCreated: ${tag} finished")
    }

    @Override
    List<String> getActiveBranches() {
        String output = dsl.sh returnStdout: true, script: 'git branch -a'
        Set<String> result = new HashSet<>()
        output.split().findAll { it -> (!it.contains("*")
                    && !it.contains("HEAD") && !it.contains("->"))}
            .each{it -> result.add(it.replace("origin/", "")
                    .replace("remotes/", ""))}
        return new ArrayList<>(result)
    }

    @Override
    List<String> getTags() {
        String output = dsl.sh returnStdout: true, script: 'git tag'
        return output.split().toList()
    }

    String getCurrentBranch() {
        String output = dsl.sh returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD'
        return output.trim()
    }

    GitService withUserName(String userName) {
        this.userName = userName
        return this
    }

    @Override
    def setUpVcsFlowPreparations() {
        gitFlowService.initFlow()
    }

    def getShowConfigFile() {
        dsl.sh "cat .git/config"
    }

    @NonCPS
    @Override
    public String toString() {
        return "GitService{" +
                "credentialsId='" + credentialsId + '\'' +
                ", repo='" + repo + '\'' +
                ", repoOwner='" + repoOwner + '\'' +
                ", flowPrefix='" + flowPrefix + '\'' +
                "} "
    }
}