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

package pro.javatar.pipeline.model

/**
 * @author Borys Zora
 * @since 2018-03-09
 */
enum StageType implements Serializable{
    BUILD_AND_UNIT_TESTS,
    DEPLOY_ON_DEV_ENV,
    AUTO_TESTS,
    BACKWARD_COMPATIBILITY_TEST,
    BACKWARD_COMPATIBILITY_AUTO_TESTS,
    DEV_SIGN_OFF,
    RELEASE,
    DEPLOY_ON_QA_ENV,
    QA_SIGN_OFF,
    DEPLOY_ON_STAGING_ENV,
    DEVOPS_SIGN_OFF,
    DEPLOY_ON_PROD_ENV
}
