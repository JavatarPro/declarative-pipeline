/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.command

import pro.javatar.pipeline.jenkins.api.JenkinsDslService

/**
 * @author Borys Zora
 * @version 2021-01-09
 */
class ShellCommand implements Command {

    JenkinsDslService dslService

    ShellCommand(JenkinsDslService dslService) {
        this.dslService = dslService
    }

    @Override
    String execute(String command) {
        return dslService.getShellExecutionResponse(command)
    }
}
