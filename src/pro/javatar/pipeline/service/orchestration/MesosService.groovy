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

    MesosService(){}

    def setup() {
        // TODO prepare vcsRepos on builder stage
        dsl.echo "MesosService: checkout configurations for dev: ${dev.toString()}, and prod: ${prod.toString()}"
        VcsHelper.checkoutRepo(dev)
        VcsHelper.checkoutRepo(prod)
        dsl.echo "MesosService: configurations checkout completed"
    }

    @Override
    def dockerDeployContainer(String imageName, String imageVersion, String dockerRepositoryUrl, String environment) {
        dsl.echo "dockerDeployContainer(imageName: ${imageName}, imageVersion: ${imageVersion}, " +
                "dockerRepositoryUrl: ${dockerRepositoryUrl}, environment: ${environment})"

        dsl.sh "pwd; ls -l; ls -l .. "

        dsl.withEnv(["SERVICE=${imageName}", "DOCKER_REPOSITORY=${dockerRepositoryUrl}",
                     "RELEASE_VERSION=${imageVersion}", "LABEL_ENVIRONMENT=${environment}"]) {

            dsl.sh "${getFolder(environment)}/bin/mm-deploy -e ${environment} ${imageName} || " +
                    " (depcon -e ${environment} app rollback /${imageName}-${environment} --wait; echo 'Deploy failed!'; exit 2)"
        }
    }

    String getFolder(String env) {
        if (Env.fromString(env) == Env.PROD) {
            return "../${prod.getName()}"
        }
        return "../${dev.getName()}"
    }

    MesosService withDev(VcsRepo dev) {
        this.dev = dev
        return this
    }

    MesosService withProd(VcsRepo prod) {
        this.prod = prod
        return this
    }

    @Override
    public String toString() {
        return "MesosService{" +
                "dev=" + dev +
                ", prod=" + prod +
                '}';
    }
}
