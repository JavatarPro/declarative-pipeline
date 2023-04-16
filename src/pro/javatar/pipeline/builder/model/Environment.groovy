/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS;

/**
 * TODO remove does not work
 * @author Borys Zora
 * @version 2019-02-17
 */
@Deprecated
class Environment implements Serializable {

    // e.g. dev, qa, uat, prod
    private String env;

    Environment(String env) {
        this.env = env;
    }

    String getEnv() {
        return env;
    }

    @Override
    boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Environment that = (Environment) o;

        return env != null ? env.equals(that.env) : that.env == null;
    }

    @Override
    int hashCode() {
        return env != null ? env.hashCode() : 0;
    }

    @NonCPS
    @Override
    public String toString() {
        return "Environment{" +
                "env='" + env + '\'' +
                '}';
    }
}
