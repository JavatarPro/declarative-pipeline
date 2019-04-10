/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.template

import pro.javatar.pipeline.util.StringUtils

/**
 * @author Borys Zora
 * @version 2019-04-04
 */
class JsonTemplateResolver implements OrchestrationTemplateResolver {

    JsonConfigMerger merger = new JsonConfigMerger()

    @Override
    String createRequestBody(TemplateResolverRequest request) {
        List<String> templates = renderTemplates(request)
        return merger.merge(templates)
    }

    protected List<String> renderTemplates(TemplateResolverRequest request) {
        List<String> templates = new ArrayList<>(4)
        request.getTemplateFileContents().each { String template ->
            templates.add(StringUtils.renderTemplate(template, request.getMergedVariables()))
        }
        return templates
    }

}
