/*
 * Copyright (c) 2018 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2018-12-04
 */
class Sonar implements Serializable {

    String serverUrl

    boolean enabled = false

    boolean qualityGateEnabled

    int qualityGateSleepInSeconds

    String jenkinsSettingsName

    String params

    Sonar() {
        Logger.debug("Sonar:default constructor")
    }

    String getServerUrl() {
        return serverUrl
    }

    void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl
    }

    Sonar withServerUrl(String serverUrl) {
        this.serverUrl = serverUrl
        return this
    }

    boolean getEnabled() {
        return enabled
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled
    }

    Sonar withEnabled(boolean enabled) {
        this.enabled = enabled
        return this
    }

    boolean getQualityGateEnabled() {
        return qualityGateEnabled
    }

    void setQualityGateEnabled(boolean qualityGateEnabled) {
        this.qualityGateEnabled = qualityGateEnabled
    }

    Sonar withQualityGateEnabled(boolean qualityGateEnabled) {
        this.qualityGateEnabled = qualityGateEnabled
        return this
    }

    int getQualityGateSleepInSeconds() {
        return qualityGateSleepInSeconds
    }

    void setQualityGateSleepInSeconds(int qualityGateSleepInSeconds) {
        this.qualityGateSleepInSeconds = qualityGateSleepInSeconds
    }

    Sonar withQualityGateSleepInSeconds(int qualityGateSleepInSeconds) {
        this.qualityGateSleepInSeconds = qualityGateSleepInSeconds
        return this
    }

    String getJenkinsSettingsName() {
        return jenkinsSettingsName
    }

    void setJenkinsSettingsName(String jenkinsSettingsName) {
        this.jenkinsSettingsName = jenkinsSettingsName
    }

    Sonar withJenkinsSettingsName(String jenkinsSettingsName) {
        this.jenkinsSettingsName = jenkinsSettingsName
        return this
    }

    String getParams() {
        return params
    }

    void setParams(String params) {
        this.params = params
    }

    Sonar withParams(String params) {
        this.params = params
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "Sonar{" +
                "serverUrl='" + serverUrl + '\'' +
                ", enabled=" + enabled +
                ", qualityGateEnabled=" + qualityGateEnabled +
                ", qualityGateSleepInSeconds=" + qualityGateSleepInSeconds +
                ", jenkinsSettingsName='" + jenkinsSettingsName + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
