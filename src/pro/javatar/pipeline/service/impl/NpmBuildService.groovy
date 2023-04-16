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
package pro.javatar.pipeline.service.impl

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.domain.Npm
import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.UiBuildService
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class NpmBuildService extends UiBuildService implements Serializable {

    protected Npm npm

    NpmBuildService(Npm npm) {
        this.npm = npm
    }

    @Override
    void setUp() {
        Logger.debug("NpmBuildService setUp")
        Logger.debug("dsl.tool([name: ${npm.version}, type: ${npm.type}])")
        dslService.addToPath(npm.version, npm.type)
        dslService.executeShell("node --version")
        dslService.executeShell("npm -version")
        dslService.executeShell("npm install --no-save")
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info('npm buildAndUnitTests start')
        dslService.executeShell("pwd; ls -la")
        if (!skipUnitTests) dslService.executeShell('npm run test')
        dslService.executeShell('npm run build')
        dslService.executeShell("zip -r ${getArtifact()} ${npm.distributionFolder}")
        dslService.executeShell("pwd; ls -la")
        Logger.info('npm buildAndUnitTests end')
    }

    @Override
    String getCurrentVersion() {
        def packageJsonFile = dsl.readJSON file: 'package.json'
        return packageJsonFile.version
    }

    @Override
    def setupReleaseVersion(String releaseVersion) { // TODO delete, switch to setupVersion method
        Logger.info("setupReleaseVersion: ${releaseVersion} started")
        Logger.debug("package.json before change version")
        dslService.executeShell("cat package.json | grep version | grep -v flow")
        dslService.executeShell("npm --no-git-tag-version version ${releaseVersion}")
        Logger.debug("package.json after change version")
        dslService.executeShell("cat package.json | grep version | grep -v flow")
        Logger.info("setupReleaseVersion: ${releaseVersion} finished")
    }

    @Override
    def setupVersion(String version) {
        Logger.info("setupVersion: ${version} started")
        Logger.debug("package.json before change version")
        dslService.executeShell("cat package.json | grep version | grep -v flow")
        dslService.executeShell("npm --no-git-tag-version version ${version}")
        Logger.debug("package.json after change version")
        dslService.executeShell("cat package.json | grep version | grep -v flow")
        Logger.info("setupVersion: ${version} finished")
    }

    @NonCPS
    @Override
    String toString() {
        return "NpmBuildService{" +
                "npm='" + npm + '\'' +
                '}'
    }
}
