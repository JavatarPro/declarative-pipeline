package pro.javatar.pipeline.service.orchestration.template

import pro.javatar.pipeline.service.orchestration.model.OrchestrationRequest

interface OrchestrationTemplateResolver extends Serializable {

    String createRequestBody(TemplateResolverRequest request)

}