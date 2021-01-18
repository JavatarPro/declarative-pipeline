/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.config

/**
 * @author Borys Zora
 * @version 2021-01-17
 */
class Service implements NameAware, Serializable {

    String name

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }
}
