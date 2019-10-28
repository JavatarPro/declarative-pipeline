/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration.template

import com.cloudbees.groovy.cps.NonCPS

/**
 * @author Borys Zora
 * @version 2019-04-04
 */
class TemplateResolverRequest {

    List<String> templateFileContents = new ArrayList<>()

    Map<String, Object> mergedVariables = new HashMap<>()

    TemplateResolverRequest(List<String> templateFileContents, Map<String, Object> mergedVariables) {
        this.templateFileContents = templateFileContents
        this.mergedVariables = mergedVariables
    }

    List<String> getTemplateFileContents() {
        return templateFileContents
    }

    Map<String, Object> getMergedVariables() {
        return mergedVariables
    }

    @NonCPS
    @Override
    public String toString() {
        return "TemplateResolverRequest{" +
                "templateFileContents=" + templateFileContents.size() +
                ", mergedVariables=" + mergedVariables.size() +
                '}';
    }
}
