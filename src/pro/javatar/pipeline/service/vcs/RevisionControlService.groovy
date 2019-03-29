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

import pro.javatar.pipeline.exception.ReleaseFinishException
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.vcs.model.VcsRepo
import pro.javatar.pipeline.service.vcs.model.VscCheckoutRequest

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @version 2018-03-12
 */
abstract class RevisionControlService implements Serializable {

    public static final String BRANCH_MASTER = "master"
    public static final String BRANCH_DEFAULT = "default"
    public static final String BRANCH_DEVELOP = "develop"

    static final String DEFAULT_CHECKOUT_FOLDER = "repo"
    String folder = DEFAULT_CHECKOUT_FOLDER

    protected VcsRepositoryUrlResolver urlResolver

    protected String domain

    protected String userName

    void cleanUp() {
        dsl.echo "start cleanup"
        dsl.sh "pwd; ls -la"
        dsl.sh "rm -rf ${folder}"
        dsl.sh "rm -rf ${folder}@tmp"
        dsl.sh "mkdir ${folder}"
        dsl.sh "pwd; ls -la"
        dsl.echo "end cleanup"
    }

    String getBranchWithPrefix(String prefix, String branch) {
        if (hasFlowPrefix()) {
            return "${prefix}-${branch}"
        }
        return branch
    }

    boolean isTagExists(String tag) {
        return getTags().contains(tag)
    }

    boolean isBranchExists(String branch) {
        return getActiveBranches().contains(branch)
    }

    abstract def setUp()

    abstract def setUpVcsFlowPreparations()

    abstract def checkout(String branch)

    abstract def checkoutRepo(String repoOwner, String repo, String branch)

    abstract def checkoutRepo(String repoUrl, String branch)

    abstract def checkoutRepo(VscCheckoutRequest request)

    def checkoutProdBranch() {
        checkout(getProdBranch())
    }

    def checkoutDevelopBranch() {
        checkout(getDevBranchWithPrefix(getFlowPrefix()))
    }

    abstract def createReleaseBranchLocally(String releaseVersion)

    abstract def commitChanges(String message)

    abstract def release(String releaseVersion) throws ReleaseFinishException

    abstract def pushRelease()

    def switchToDevelopBranch() {
        switchToBranch(getDevBranchWithPrefix(getFlowPrefix()))
    }

    abstract def switchToReleaseBranch(ReleaseInfo releaseInfo)

    abstract def switchToBranch(String branch)

    abstract String getFlowPrefix()

    abstract def createBranch(String branchName)

    abstract def createAndPushBranch(String branchName)

    abstract def pushNewBranches()

    abstract def pushNewBranch(String branchName)

    abstract String getProdBranch()

    abstract String getRepoName()

    abstract String getRepoOwner()

    abstract List<String> getActiveBranches()

    abstract List<String> getTags()

    boolean hasFlowPrefix() {
        return getFlowPrefix() != null && ! "".equals(getFlowPrefix().trim())
    }

    String getPrefixedDevelopBranch() {
        return getBranchWithPrefix(getDevBranch())
    }

    String getDevBranch() {
        return BRANCH_DEVELOP
    }

    String getProdBranchWithPrefix(String prefix) {
        return getBranchWithPrefix(prefix, getProdBranch())
    }

    String getDevBranchWithPrefix(String prefix) {
        return getBranchWithPrefix(prefix, getDevBranch())
    }

    void setUrlResolver(VcsRepositoryUrlResolver urlResolver) {
        this.urlResolver = urlResolver
    }

    void setDomain(String domain) {
        this.domain = domain
    }

    void setUserName(String userName) {
        this.userName = userName
    }

    String getDomain() {
        return domain
    }

    String getUserName() {
        return userName
    }
}