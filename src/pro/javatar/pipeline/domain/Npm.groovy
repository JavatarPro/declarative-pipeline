/*
 * Copyright (c) 2022 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.domain

import com.cloudbees.groovy.cps.NonCPS

/**
 * @author Borys Zora
 * @version 2022-09-24
 */
class Npm implements Serializable {
    String version
    String type
    String distributionFolder = "dist"

    @NonCPS @Override String toString() { return "Npm{" + "version='" + version + '\'' + ", type='" + type + '\'' + ", distributionFolder='" + distributionFolder + '\'' + '}'; }
}
