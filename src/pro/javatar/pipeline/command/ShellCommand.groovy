/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.command

import pro.javatar.pipeline.jenkins.api.JenkinsDslService
import pro.javatar.pipeline.util.Logger

/**
 * @author Borys Zora
 * @version 2021-01-09
 */
class ShellCommand implements Command {

    JenkinsDslService dslService
    String name
    List<String> commands = new ArrayList<>()
    boolean failOnError

    ShellCommand(JenkinsDslService dslService, String name, boolean failOnError, String... commands) {
        this.dslService = dslService
        this.name = name
        this.commands = commands.toList()
        this.failOnError = failOnError
    }

    @Override
    List<String> execute() throws Exception {
        List<String> result = new ArrayList<>()
        String executionCommand = null
        try {
            for (String command: commands) {
                executionCommand = command
                result.add(dslService.getShellExecutionResponse(command))
            }
            return result
        } catch (Exception e) {
            if (failOnError()) {
                Logger.error("error ocurred while executing: ${executionCommand} \nfailed with a message: "
                        + e.getMessage())
                e.printStackTrace()
                throw e
            } else {
                Logger.warn("error ocurred while executing: ${executionCommand} \nfailed with a message: "
                        + e.getMessage())
                e.printStackTrace()
                return result
            }
        }
    }

    @Override
    boolean failOnError() {
        return failOnError
    }

    @Override
    String name() {
        return name
    }
}
