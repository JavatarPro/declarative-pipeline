/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.util

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl;

/**
 * @author Borys Zora
 * @version 2019-03-29
 */
class Logger {

    static void info(def message) {
        dsl.echo "${message}"
    }

}
