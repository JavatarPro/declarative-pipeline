/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.config

/**
 * @author Borys Zora
 * @version 2021-01-17
 */
class Pipeline implements Serializable {

    List<Suit> suits = new ArrayList<>()
    List<Stage> stages = new ArrayList<>()
    List<Command> commands = new ArrayList<>()
    List<Service> services = new ArrayList<>()

}
