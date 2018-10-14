package pro.javatar.pipeline.service.orchestration

import pro.javatar.pipeline.service.infra.model.Infra

class DockerSwarmService implements DockerOrchestrationService {

    @Override
    def setup() {
        return null
    }

    @Override
    def dockerDeployContainer(String imageName, String imageVersion, String dockerRepositoryUrl, String environment) {
        return null
    }

    @Override
    def deployInfraContainer(Infra infra) {
        return null
    }
}
