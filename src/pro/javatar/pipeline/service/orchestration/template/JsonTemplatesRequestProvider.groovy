/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.template

import pro.javatar.pipeline.service.orchestration.OrchestrationRequestProvider
import pro.javatar.pipeline.service.orchestration.model.OrchestrationRequest
import pro.javatar.pipeline.util.FileUtils
import pro.javatar.pipeline.util.Logger

/**
 * TODO add responsible team folder in alternative impl
 * @author Borys Zora
 * @version 2019-04-04
 */
class JsonTemplatesRequestProvider implements OrchestrationRequestProvider {

    static final String MAIN_TEMPLATE = "request.json"
    static final String SERVICE_TEMPLATE = "{{service}}-request.json"
    static final String ENV_TEMPLATE = "{{env}}/request.json"
    static final String ENV_SERVICE_TEMPLATE = "{{env}}/{{service}}-request.json"
    static final String MAIN_VARIABLES = "variables.properties"
    static final String SERVICE_VARIABLES = "{{service}}-variables.properties"
    static final String ENV_SERVICE_VARIABLES = "{{env}}/{{service}}-variables.properties"

    OrchestrationTemplateResolver templateResolver = new JsonTemplateResolver()

    @Override
    String createRequest(OrchestrationRequest request) {
        Logger.info("JsonTemplatesRequestProvider:createRequest:started")
        TemplateResolverRequest req = createTemplateResolverRequest(request)
        String result = templateResolver.createRequestBody(req)
        Logger.trace("JsonTemplatesRequestProvider:createRequest:result: \n${result}")
        return result
    }

    private TemplateResolverRequest createTemplateResolverRequest(OrchestrationRequest request) {
        Logger.info("JsonTemplatesRequestProvider:createTemplateResolverRequest:started")
        List<String> templateFileContents = getTemplateFileContents(request)
        Map<String, Object> mergedVariables = getMergedVariables(request)
        TemplateResolverRequest result = new TemplateResolverRequest(templateFileContents, mergedVariables)
        Logger.info("JsonTemplatesRequestProvider:createTemplateResolverRequest:finished")
        return result
    }

    private List<String> getTemplateFileContents(OrchestrationRequest request) {
        Logger.debug("JsonTemplatesRequestProvider:getTemplateFileContents:started")
        List<String> result = new ArrayList<>(3)
        result.add(mainTemplate(request))
        result.add(envTemplate(request))
        result.add(serviceTemplate(request))
        result.add(envServiceTemplate(request))
        Logger.debug("JsonTemplatesRequestProvider:getTemplateFileContents:finished")
        return result
    }

    private Map<String, Object> getMergedVariables(OrchestrationRequest request) {
        Logger.debug("JsonTemplatesRequestProvider:getMergedVariables:started")
        Map<String, Object> result = new HashMap<>()
        result.putAll(readVariables(request, MAIN_VARIABLES))
        result.putAll(readVariables(request, SERVICE_VARIABLES
                .replace("{{service}}", request.getService())))
        result.putAll(readVariables(request, ENV_SERVICE_VARIABLES
                .replace("{{service}}", request.getService())
                .replace("{{env}}", request.getEnv())))
        Logger.debug("JsonTemplatesRequestProvider:getMergedVariables:finished")
        return result
    }

    String mainTemplate(OrchestrationRequest request) {
        return readTemplate(request, MAIN_TEMPLATE)
    }

    String envTemplate(OrchestrationRequest request) {
        return readTemplate(request, ENV_TEMPLATE
                .replace("{{env}}", request.getEnv()))
    }

    String serviceTemplate(OrchestrationRequest request) {
        return readTemplate(request, SERVICE_TEMPLATE.replace("{{service}}", request.getService()))
    }

    String envServiceTemplate(OrchestrationRequest request) {
        return readTemplate(request, ENV_SERVICE_TEMPLATE
                .replace("{{service}}", request.getService())
                .replace("{{env}}", request.getEnv()))
    }

    String readTemplate(OrchestrationRequest request, String path) {
        String fullPath = "${request.getTemplateFolder()}/${path}"
        Logger.debug("JsonTemplatesRequestProvider:readTemplate:started fullPath: ${fullPath}")
        String result = FileUtils.readFile("${request.getTemplateFolder()}/${path}")
        Logger.debug("JsonTemplatesRequestProvider:readTemplate:finished fullPath: ${fullPath}")
        Logger.trace("JsonTemplatesRequestProvider:readTemplate:finished result: \n${result}")
        return result
    }

    Map<String, Object> readVariables(OrchestrationRequest request, String path) {
        return FileUtils.propertyFileToMap("${request.getTemplateFolder()}/${path}")
    }
}
