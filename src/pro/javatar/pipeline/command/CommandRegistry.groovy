/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.command

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Borys Zora
 * @version 2021-01-10
 */
class CommandRegistry implements Serializable {

    static Map<String, Command> commands = new ConcurrentHashMap<>()

    static void addCommand(String name, Command command) {
        commands.put(name, command)
    }

    static Command getCommand(String name) {
        return commands.get(name)
    }
}
