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

package pro.javatar.pipeline.service.orchestration

import pro.javatar.pipeline.model.Env
import pro.javatar.pipeline.service.infra.model.Infra
import pro.javatar.pipeline.service.vcs.VcsHelper
import pro.javatar.pipeline.service.vcs.model.VcsRepo

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 3/22/18 22:21
 */
class MesosService implements DockerOrchestrationService {

    VcsRepo dev
    VcsRepo prod
    Map<String, VcsRepo> vcsRepoMap

    MesosService(){}

    def setup() {
        // TODO prepare vcsRepos on builder stage
        dsl.echo "MesosService: checkout configurations for vcsRepoMap: ${vcsRepoMap}"
        VcsRepo vcsRepo = vcsRepoMap.get(Env.DEV.getValue())
        VcsHelper.checkoutRepo(vcsRepo, getFolder(vcsRepo))
        // TODO checkout prod repo securely on different agent (e.g. pipeline-prod)
//        VcsHelper.checkoutRepo(vcsRepoMap.get(Env.PROD.getValue()))
        dsl.echo "MesosService: configurations checkout completed"
    }

    @Override
    def dockerDeployContainer(String imageName, String imageVersion, String dockerRepositoryUrl, String environment) {
        dsl.echo "dockerDeployContainer(imageName: ${imageName}, imageVersion: ${imageVersion}, " +
                "dockerRepositoryUrl: ${dockerRepositoryUrl}, environment: ${environment})"

        dsl.sh "pwd; ls -l; ls -l .. "

        dsl.withEnv(["SERVICE=${imageName}", "DOCKER_REPOSITORY=${dockerRepositoryUrl}",
                     "RELEASE_VERSION=${imageVersion}", "LABEL_ENVIRONMENT=${environment}"]) {

            // TODO rollback does not work properly (manual involvement is needed when something goes wrong)
            dsl.sh "${getFolder(environment)}/bin/mm-deploy -e ${environment} ${imageName} || " +
                    " (depcon -e ${environment} app rollback /${imageName}-${environment} " +
                    "--wait; echo 'Deploy failed!'; exit 2)"
        }
    }

    @Override
    def deployInfraContainer(Infra infra) {
        return null
    }

    String getFolder(String env) {
        VcsRepo vcsRepo = vcsRepoMap.get(env)
        return "../${vcsRepo.getName()}"
    }

    String getFolder(VcsRepo vcsRepo) {
        return "../${vcsRepo.getName()}"
    }

    MesosService withDev(VcsRepo dev) {
        this.vcsRepoMap.put(Env.DEV.getValue(), dev)
        this.dev = dev
        return this
    }

    MesosService withProd(VcsRepo prod) {prod
        this.vcsRepoMap.put(Env.PROD.getValue(), dev)
        this.prod = prod
        return this
    }

    MesosService withVcsRepoMap(Map<String, VcsRepo> vcsRepoMap) {
        this.vcsRepoMap = vcsRepoMap
        return this
    }

    MesosService withRepo(String repoEnv, VcsRepo vcsRepo) {
        this.vcsRepoMap.put(repoEnv, vcsRepo)
        return this
    }

    @Override
    public String toString() {
        return "MesosService{" +
                "vcsRepoMap=" + vcsRepoMap +
                '}';
    }
}
