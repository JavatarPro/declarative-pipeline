package pro.javatar.pipeline.service.infra

import pro.javatar.pipeline.service.infra.model.Infra
import pro.javatar.pipeline.service.orchestration.DockerOrchestrationService

interface InfraService {

    def deployInfra(Infra request, DockerOrchestrationService dockerOrchestrationService)

}