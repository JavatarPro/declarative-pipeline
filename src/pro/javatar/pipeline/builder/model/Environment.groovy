/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.builder.model;

/**
 * @author Borys Zora
 * @version 2019-02-17
 */
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
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Environment that = (Environment) o

        if (env != that.env) return false

        return true
    }

    @Override
    int hashCode() {
        return (env != null ? env.hashCode() : 0)
    }

}
