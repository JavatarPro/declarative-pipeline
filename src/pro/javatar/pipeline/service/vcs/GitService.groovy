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

import pro.javatar.pipeline.exception.GitFlowReleaseFinishException
import pro.javatar.pipeline.exception.InvalidBranchException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.vcs.model.VscCheckoutRequest

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
        dsl.echo "setUp git started"
        dsl.sh 'git config --global user.email "jenkins@javatar.pro"'
        dsl.sh 'git config --global user.name "jenkins"'
        dsl.echo "setUp git finished"
    }

    @Override
    def checkout(String branch) {
        dsl.echo "GitService checkout repo: ${repo}, branch: ${branch}"
        String repoUrl = urlResolver.getRepoUrl()
        // dsl.git branch: 'master', changelog: true, credentialsId: credentialsId, poll: true, url: repoUrl
        dsl.echo "credentialsId: ${credentialsId}, url: ${repoUrl}"
        dsl.git credentialsId: credentialsId, url: repoUrl
        dsl.sh "pwd; ls -la"
        dsl.sh "git checkout ${branch}"
        dsl.echo "GitService checkout successfully finished for repo: ${repo}, branch: ${branch}"
    }

    @Override
    def checkoutRepo(String repoOwner, String repo, String branch) {
        dsl.echo "GitService checkoutRepo: repoOwner: ${repoOwner}, repo: ${repo}, branch: ${branch}"
        String repoUrl = urlResolver.getRepoUrl(repoOwner, repo)
        return checkoutRepo(repoUrl, branch)
    }

    @Override
    def checkoutRepo(String repoUrl, String branch) {
        dsl.echo "checkoutRepo with repoUrl: ${repoUrl}, branch: ${branch}"
        VscCheckoutRequest vscCheckoutRequest = new VscCheckoutRequest().withBranch(branch).withRepoUrl(repoUrl)
                                                        .withCredentialsId(credentialsId)
        checkoutRepo(vscCheckoutRequest)
    }

    @Override
    def checkoutRepo(VscCheckoutRequest request) {
        dsl.echo "try to checkoutRepo with request: ${request}"
        // TODO remove hardcode, make configurable
        dsl.timeout(time: 5, unit: 'MINUTES') {
            dsl.git credentialsId: request.getCredentialsId(), url: request.getRepoUrl()
            dsl.sh "pwd; ls -la"
            dsl.sh "git checkout ${request.getBranch()}"
            dsl.echo "GitService#checkoutRepo successfully finished for repoUrl: ${request.getRepoUrl()}, " +
                    "branch: ${request.getBranch()}"
        }
    }

    @Override
    def createReleaseBranchLocally(String releaseVersion) {
        dsl.echo "createReleaseBranchLocally releaseVersion - ${releaseVersion}"
        dsl.sh "git status"
        dsl.sh "git flow release start ${releaseVersion}"
        dsl.echo "createReleaseBranchLocally finished"
    }

    @Override
    def commitChanges(String message) {
        dsl.echo "commit changes with message: ${message}"
        dsl.sh "git status"
        dsl.sh "git add ."
        dsl.sh "git status"
        dsl.sh "git commit -m \'${message}\'"
        dsl.echo "successfully committed"
    }

    @Override
    def release(String releaseVersion) throws GitFlowReleaseFinishException {
        dsl.echo "git release: ${releaseVersion} started"
        dsl.echo "creates tag, removes release branch"
        validateReleaseVersion(releaseVersion)
        dsl.sh "git status"
        dsl.sh "git flow release finish ${releaseVersion} -m \"Release ${releaseVersion}\""
        validateReleaseFinish()
        validateTagCreated(releaseVersion)
        dsl.echo "git release: ${releaseVersion} finished"
    }

    @Override
    def switchToReleaseBranch(ReleaseInfo releaseInfo) {
        throw UnsupportedOperationException("switchToReleaseBranch not yet implemented")
    }

    @Override
    def switchToBranch(String branch) {
        dsl.echo "switchToBranch: ${branch} started"
        dsl.sh "git checkout ${branch}"
        dsl.echo "switchToBranch: ${branch} finished"
    }

    @Override
    def createBranch(String branchName) {
        dsl.sh "git checkout -b ${branchName}"
        commitChanges("create new branch: ${branchName}")
    }

    @Override
    def createAndPushBranch(String branchName) {
        dsl.echo "started createAndPushBranch: ${branchName}"
        dsl.sh "git checkout -b ${branchName}"
        dsl.sshagent([credentialsId]) {
            dsl.sh "git push -u origin ${branchName}"
        }
        dsl.echo "finished createAndPushBranch: ${branchName}"
    }

    @Override
    def pushNewBranches() {
        dsl.echo "started pushNewBranches"
        dsl.sshagent([credentialsId]) {
            dsl.sh "git push --all -u"
        }
        dsl.echo "finished pushNewBranches"
    }

    @Override
    def pushNewBranch(String branchName) {
        dsl.echo "started pushNewBranch: ${branchName}"
        // git branch --set-upstream origin yourbranch
        dsl.sshagent([credentialsId]) {
            dsl.sh "git push -u origin ${branchName}"
        }
        dsl.echo "finished pushNewBranch: ${branchName}"
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
        dsl.echo "updateDevelopVersion update to develop branch: ${branch} started"
        dsl.sh "git checkout ${branch}"
        validateBranch(branch)
        dsl.echo "updateDevelopVersion update to develop branch: ${branch} successfully finished"
    }

    def validateReleaseFinish() throws GitFlowReleaseFinishException {
        String currentBranch = getCurrentBranch()
        if(!"develop".equalsIgnoreCase(currentBranch)) {
            String errorMessage = "ERROR: [git flow release finish] failed! " +
                    "Expected branch develop but was ${currentBranch}"
            dsl.echo errorMessage
            throw new GitFlowReleaseFinishException(errorMessage)
        }
    }

    def validateBranch(String branch) {
        dsl.echo "validateBranch: ${branch} started"
        String currentBranch = getCurrentBranch()
        if (!branch.equalsIgnoreCase(currentBranch)){
            String errorMessage = "Invalid branch, expected: ${branch}, but was: ${currentBranch}"
            dsl.echo errorMessage
            throw new InvalidBranchException(errorMessage)
        }
        dsl.echo "validateBranch: ${branch} finished"
    }

    def validateReleaseVersion(String releaseVersion) throws GitFlowReleaseFinishException {
        String currentBranch = getCurrentBranch()
        String expectedBranch = "release/${releaseVersion}"
        if (!expectedBranch.equalsIgnoreCase(currentBranch)) {
            String errorMessage = "ERROR: [git flow release finish] failed! " +
                    "Expected branch same as releaseVersion: ${expectedBranch} but was ${currentBranch}"
            dsl.echo errorMessage
            throw new GitFlowReleaseFinishException(errorMessage)
        }
    }

    def validateTagCreated(String tag) {
        dsl.echo "validateTagCreated: ${tag} started"
        String actualTag = dsl.sh returnStdout: true, script: "git tag | grep ${tag}"
        actualTag = actualTag.trim()
        if (!tag.equalsIgnoreCase(actualTag)){
            String errorMessage = "Invalid branch, expected: ${tag}, but was: ${actualTag}"
            dsl.echo errorMessage
            throw new InvalidBranchException(errorMessage)
        }
        dsl.echo "validateTagCreated: ${tag} finished"
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

    public String toString() {
        return "GitService{" +
                "credentialsId='" + credentialsId + '\'' +
                ", repo='" + repo + '\'' +
                ", repoOwner='" + repoOwner + '\'' +
                ", userName='" + userName + '\'' +
                ", flowPrefix='" + flowPrefix + '\'' +
                "} " + super.toString();
    }
}