package pro.javatar.pipeline.service.orchestration

import pro.javatar.pipeline.service.orchestration.model.OrchestrationRequest

interface OrchestrationTemplateResolver {

    String createRequestBody(OrchestrationRequest request)

}