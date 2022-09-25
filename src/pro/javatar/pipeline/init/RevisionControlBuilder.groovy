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
package pro.javatar.pipeline.init

import pro.javatar.pipeline.domain.Vcs
import pro.javatar.pipeline.service.vcs.HgService
import pro.javatar.pipeline.service.vcs.GitService
import pro.javatar.pipeline.service.vcs.RevisionControlService
import pro.javatar.pipeline.util.Logger
import pro.javatar.pipeline.util.StringUtils

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
class RevisionControlBuilder implements Serializable {

    RevisionControlService build(Vcs vcs) {
        Logger.info("RevisionControlService.build() started")
        if (vcs == null || vcs.getUrl() == null
                || StringUtils.isBlank(vcs.getUrl())) {
            return null
        }
        RevisionControlService result
        if (vcs.getUrl().endsWith(".git")) {
            result = new GitService(vcs)
        } else if (vcs.getUrl().endsWith(".hg")) {
            result = new HgService(vcs)
        } else {
            throw new UnsupportedOperationException("Supported only .git and .hg repos");
        }
        Logger.info("RevisionControlService.build() finished")
        return result
    }
}
