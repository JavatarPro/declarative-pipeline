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

import pro.javatar.pipeline.model.ReleaseInfo
import pro.javatar.pipeline.service.UiBuildService
import pro.javatar.pipeline.util.FileUtils
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class SenchaService extends UiBuildService {

    String applicationFile = "app.json"

    SenchaService() {}

    @Override
    void setUp() {
        String senchaPath = "/opt/Sencha/Cmd/6.2.2.36/"
        dsl.env.PATH="${senchaPath}:${dsl.env.PATH}"
        dsl.sh "env"
        dsl.sh 'sencha help'
        Logger.debug("sencha setup successfully")
    }

    @Override
    void buildAndUnitTests(ReleaseInfo releaseInfo) {
        Logger.info('SenchaService buildAndUnitTests start')
        dsl.sh "pwd; ls -la"
        dsl.sh 'sencha app install --framework=/opt/ext-6.2.0/'
        dsl.sh 'sencha app build production'
        dsl.sh "pwd; ls -la"
        dsl.sh "ln -s build/production/${getApplicationName()} ${distributionFolder}"
        dsl.sh "zip -r ${getArtifact()} ${distributionFolder}"
        dsl.sh "pwd; ls -la; ls -la *"
        Logger.info('SenchaService buildAndUnitTests end')
    }

    @Override
    String getCurrentVersion() {
        def packageJsonFile = dsl.readJSON file: applicationFile
        return packageJsonFile.version
    }

    String getApplicationName() {
        def packageJsonFile = dsl.readJSON file: applicationFile
        return packageJsonFile.name
    }

    @Override
    def setupReleaseVersion(String releaseVersion) {
        Logger.info("setupReleaseVersion: ${releaseVersion} started")
        String currentVersion = getCurrentVersion()
        FileUtils.replace(currentVersion, releaseVersion, applicationFile)
        Logger.info("setupReleaseVersion: ${releaseVersion} finished")
    }

    @Override
    def setupVersion(String version) {
        Logger.info("setupVersion: ${version} started")
        String currentVersion = getCurrentVersion()
        FileUtils.replace(currentVersion, version, applicationFile)
        Logger.info("setupVersion: ${version} finished")
    }

}
