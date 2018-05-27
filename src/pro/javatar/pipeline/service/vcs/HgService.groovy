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

package pro.javatar.pipeline.service.vcs

import pro.javatar.pipeline.exception.InvalidBranchException
import pro.javatar.pipeline.exception.HgFlowReleaseFinishException
import pro.javatar.pipeline.exception.ReleaseFinishException
import pro.javatar.pipeline.model.ReleaseInfo
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @version 2018-03-12
 */
class HgService extends RevisionControlService {

    String credentialsId
    String repo
    String repoOwner
    String username = "bzo"
    String flowPrefix = ""
    HgFlowService hgFlowService

    HgService(String repo, String credentialsId,
              String repoOwner, String flowPrefix) {
        this.repo = repo
        this.credentialsId = credentialsId
        this.repoOwner = repoOwner
        this.flowPrefix = flowPrefix
        this.hgFlowService = new HgFlowService(this)
    }

    @Override
    def setUp() {
        dsl.echo "setUp mercurial started"
        dsl.sh "hg --version"
        dsl.echo "setUp mercurial finished"
    }

    @Override
    def setUpVcsFlowPreparations() {
        hgFlowService.initFlow()
    }

    @Override
    def checkout(String branch) {
        dsl.echo "mercurial start checkout branch: ${branch}"
        dsl.withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialsId,
                              usernameVariable: 'HG_USERNAME', passwordVariable: 'HG_PASSWORD']]) {
            dsl.sh "hg clone https://${username}:${dsl.env.HG_PASSWORD}@bitbucket.org/${repoOwner}/${repo} ."
        }
        dsl.sh "pwd; ls -la"
        switchToBranch(branch)
        dsl.sh "pwd; ls -la"
        dsl.echo "mercurial finish checkout branch: ${branch}"
    }

    @Override
    def checkoutRepo(String repoOwner, String repo, String branch) {
        String repoUrl = urlResolver.getRepoUrl(repoOwner, repo)
        return checkoutRepo(repoUrl, branch)
    }

    @Override
    def checkoutRepo(String repoUrl, String branch) {
        dsl.timeout(time: 5, unit: 'MINUTES') {
            dsl.checkout([$class: 'MercurialSCM',
                          credentialsId: credentialsId,
                          installation: 'mercurial',
                          revision: "${branch}",
                          source: repoUrl,
                          disableChangeLog: false,
            ])
            dsl.sh "hg pull --rebase"
            dsl.sh "hg update --clean"
        }
    }

    @Override
    def createReleaseBranchLocally(String releaseVersion) {
        dsl.echo "createReleaseBranchLocally releaseVersion - ${releaseVersion}"
        dsl.sh "hg flow release start ${releaseVersion}"
        dsl.echo "release branch created"
    }

    @Override
    def commitChanges(String message) {
        dsl.echo "commit changes with message: ${message}"
        dsl.sh "hg commit -m \'${message}\'"
        dsl.echo "successfully committed"
    }

    @Override
    def release(String releaseVersion) throws ReleaseFinishException {
        dsl.echo "mercurial release: ${releaseVersion} started"
        dsl.echo "creates tag, removes release branch"
        validateReleaseVersion(releaseVersion)
        revertAllUncommitedChanges()
        dsl.sh "hg flow release finish -t ${releaseVersion}"
        // validateReleaseFinish() // TODO validate release branch in status closed instead
        validateTagCreated(releaseVersion)
        dsl.echo "mercurial release: ${releaseVersion} finished"
    }

    def validateReleaseVersion(String releaseVersion) throws HgFlowReleaseFinishException {
        String currentBranch = getCurrentBranch()
        String expectedBranch = "${hgFlowService.getReleaseBranchFolder()}/${releaseVersion}"
        if (!expectedBranch.equalsIgnoreCase(currentBranch)) {
            String errorMessage = "ERROR: [hg flow release finish] failed! " +
                    "Expected branch same as releaseVersion: ${expectedBranch} but was ${currentBranch}"
            dsl.echo errorMessage
            getDebugInfo()
            throw new HgFlowReleaseFinishException(errorMessage)
        }
    }

    def validateTagCreated(String tag) {
        dsl.echo "validateTagCreated: ${tag} started"
        String actualTag = dsl.sh returnStdout: true, script: "hg log -r${tag} | grep tag:"
        actualTag = actualTag.trim()
        if (!actualTag.endsWith(tag)){
            String errorMessage = "Invalid branch, expected: ${tag}, but was: ${actualTag}"
            dsl.echo errorMessage
            getDebugInfo()
            throw new InvalidBranchException(errorMessage)
        }
        dsl.echo "validateTagCreated: ${tag} finished"
    }

    def validateReleaseFinish() throws HgFlowReleaseFinishException {
        // hg flow doesn't raise the error when "hg flow release finish" failed
        // workaround to check if release finished without errors and default branch is active
        String currentBranch = getCurrentBranch()
        if(!"default".equalsIgnoreCase(currentBranch)) {
            String errorMessage = "ERROR: [hg flow release finish] failed! " +
                    "Expected branch default but was ${currentBranch}"
            dsl.echo errorMessage
            getDebugInfo()
            throw new HgFlowReleaseFinishException(errorMessage)
        }
    }

    def revertAllUncommitedChanges() {
        dsl.sh "hg revert --all"
        // dsl.sh "hg purge" // TODO need extension in .hgrc file
    }

    String getCurrentBranch() {
        String output = dsl.sh returnStdout: true, script: 'hg branch'
        return output.trim()
    }

    @Override
    def pushRelease() {
        dsl.withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialsId,
                              usernameVariable: 'HG_USERNAME', passwordVariable: 'HG_PASSWORD']]) {
            dsl.sh("hg push https://${username}:${dsl.env.HG_PASSWORD}@bitbucket.org/${repoOwner}/${repo} --new-branch")
        }
    }

    @Override
    def switchToReleaseBranch(ReleaseInfo releaseInfo) {
        switchToBranch(hgFlowService.getReleaseBranch(releaseInfo))
    }

    def switchToBranch(String branch) {
        dsl.echo "switchToBranch: ${branch} started"
        dsl.sh "hg up ${branch}"
        dsl.echo "switchToBranch: ${branch} finished"
    }

    @Override
    String getFlowPrefix() {
        return flowPrefix
    }

    @Override
    def createBranch(String branchName) {
        dsl.sh "hg branch ${branchName}"
        commitChanges("create branch:${branchName}")
    }

    @Override
    def createAndPushBranch(String branchName) {
        dsl.sh "hg branch ${branchName}"
        dsl.sh "hg push --new-branch"
    }

    def pushNewBranches() {
        dsl.sh "hg push --new-branch"
    }

    @Override
    def pushNewBranch(String branchName) {
        dsl.sh "hg push --new-branch"
    }

    @Override
    List<String> getActiveBranches() {
        String output = dsl.sh returnStdout: true, script: 'hg branches'
        return output.split().findAll {it -> !it.contains(":")}
    }

    @Override
    List<String> getTags() {
        String output = dsl.sh returnStdout: true, script: 'hg tags'
        return output.split().findAll {it -> !it.contains(":")}
    }

    @Override
    String getProdBranch() {
        return BRANCH_DEFAULT
    }

    @Override
    String getRepoName() {
        return null
    }

    @Override
    String getDomain() {
        return null
    }



    def getDebugInfo() {
        dsl.echo "getDebugInfo started"
        dsl.sh "hg branch"
        dsl.sh "hg status"
        dsl.sh "hg diff"
        dsl.echo "getDebugInfo finished"
    }

    public String toString() {
        return "HgService{" +
                "credentialsId='" + credentialsId + '\'' +
                ", repo='" + repo + '\'' +
                ", repoOwner='" + repoOwner + '\'' +
                ", username='" + username + '\'' +
                ", flowPrefix='" + flowPrefix + '\'' +
                ", hgFlowService=" + hgFlowService +
                "} "
    }
}
