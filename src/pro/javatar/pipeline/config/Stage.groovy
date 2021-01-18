/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.config

/**
 * @author Borys Zora
 * @version 2021-01-17
 */
class Stage implements Serializable {

    String name
    List<Command> commands = new ArrayList<>()
    List<Command> fallbacks = new ArrayList<>()

}
