/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.config

import java.time.Duration

/**
 * @author Borys Zora
 * @version 2019-11-04
 */
interface AutoTestConfig {

    boolean enabled();

    Duration timeout();

}