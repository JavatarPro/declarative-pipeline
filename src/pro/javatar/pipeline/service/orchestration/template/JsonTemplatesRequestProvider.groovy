/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.template

import pro.javatar.pipeline.service.orchestration.OrchestrationRequestProvider
import pro.javatar.pipeline.service.orchestration.model.OrchestrationRequest
import pro.javatar.pipeline.util.FileUtils
import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.util.StringUtils.isNotBlank

/**
 * TODO add responsible team folder in alternative impl
 * This provider collects all data from files with specific naming convention
 * and service/environment hierarchy that should be merged in one request
 * in json format
 *
 * @author Borys Zora
 * @version 2019-04-04
 */
class JsonTemplatesRequestProvider implements OrchestrationRequestProvider {

    static final String MAIN_TEMPLATE = "request.json"
    static final String SERVICE_TEMPLATE = "{{service}}-request.json"
    static final String ENV_TEMPLATE = "{{env}}/request.json"
    static final String ENV_SERVICE_TEMPLATE = "{{env}}/{{service}}-request.json"

    static final String MAIN_VARIABLES = "variable.properties"
    static final String SERVICE_VARIABLES = "{{service}}-variable.properties"
    static final String ENV_VARIABLES = "{{env}}/variable.properties"
    static final String ENV_SERVICE_VARIABLES = "{{env}}/{{service}}-variable.properties"

    OrchestrationTemplateResolver templateResolver = new JsonTemplateResolver()

    @Override
    String createRequest(OrchestrationRequest request) {
        Logger.info("JsonTemplatesRequestProvider:createRequest:started")
        TemplateResolverRequest req = createTemplateResolverRequest(request)
        String result = templateResolver.createRequestBody(req)
        Logger.trace("JsonTemplatesRequestProvider:createRequest:result: \n${result}")
        return result
    }

    protected TemplateResolverRequest createTemplateResolverRequest(OrchestrationRequest request) {
        Logger.info("JsonTemplatesRequestProvider:createTemplateResolverRequest:started")
        List<String> templateFileContents = getTemplateFileContents(request)
        Map<String, Object> mergedVariables = getMergedVariables(request)
        TemplateResolverRequest result = new TemplateResolverRequest(templateFileContents, mergedVariables)
        Logger.info("JsonTemplatesRequestProvider:createTemplateResolverRequest:finished")
        return result
    }

    protected List<String> getTemplateFileContents(OrchestrationRequest request) {
        Logger.debug("JsonTemplatesRequestProvider:getTemplateFileContents:started")
        List<String> result = new ArrayList<>(4)
        addTemplate(result, readTemplate(request, MAIN_TEMPLATE))
        addTemplate(result, readTemplate(request, ENV_TEMPLATE
                .replace("{{env}}", request.getEnv())))
        addTemplate(result, readTemplate(request, SERVICE_TEMPLATE
                .replace("{{service}}", request.getService())))
        addTemplate(result, readTemplate(request, ENV_SERVICE_TEMPLATE
                .replace("{{service}}", request.getService())
                .replace("{{env}}", request.getEnv())))
        Logger.debug("JsonTemplatesRequestProvider:getTemplateFileContents:finished")
        return result
    }

    // merge variables could have different strategies
    protected Map<String, Object> getMergedVariables(OrchestrationRequest request) {
        Logger.debug("JsonTemplatesRequestProvider:getMergedVariables:started")
        Map<String, Object> result = new HashMap<>()
        result.putAll(readVariables(request, MAIN_VARIABLES))
        result.putAll(readVariables(request, ENV_VARIABLES
                .replace("{{env}}", request.getEnv())))
        result.putAll(readVariables(request, SERVICE_VARIABLES
                .replace("{{service}}", request.getService())))
        result.putAll(readVariables(request, ENV_SERVICE_VARIABLES
                .replace("{{service}}", request.getService())
                .replace("{{env}}", request.getEnv())))
        Logger.debug("JsonTemplatesRequestProvider:getMergedVariables:finished")
        return result
    }

    protected String readTemplate(OrchestrationRequest request, String path) {
        String fullPath = "${request.getTemplateFolder()}/${path}"
        Logger.debug("JsonTemplatesRequestProvider:readTemplate:started fullPath: ${fullPath}")
        String result = FileUtils.readFile("${request.getTemplateFolder()}/${path}")
        Logger.debug("JsonTemplatesRequestProvider:readTemplate:finished fullPath: ${fullPath}")
        Logger.trace("JsonTemplatesRequestProvider:readTemplate:finished result: \n${result}")
        return result
    }

    protected Map<String, Object> readVariables(OrchestrationRequest request, String path) {
        Map<String, Object> result = FileUtils.propertyFileToMap("${request.getTemplateFolder()}/${path}")
        if (result == null) {
            Logger.debug("no variables found in file: ${path}")
            return new HashMap<>()
        }
        Logger.debug("${result.size()} variables found in file: ${path}")
        return result
    }

    void addTemplate(List<String> templates, String content) {
        if (isNotBlank(content)) {
            templates.add(content)
        }
    }

    JsonTemplatesRequestProvider withTemplateResolver(OrchestrationTemplateResolver templateResolver) {
        this.templateResolver = templateResolver
        return this
    }

}
