/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.config

import java.time.Duration

/**
 * TODO verify where model still good
 * @author Borys Zora
 * @version 2019-11-04
 */
interface AutoTestConfig {

    boolean DEFAULT_AUTO_TESTS_ENABLED = true;

    Duration DEFAULT_TIMEOUT = Duration.parse("PT10M");

    Duration DEFAULT_INITIAL_DELAY = Duration.parse("PT10S");

    String DEFAULT_JOB_NAME = "common/system-tests";

    boolean enabled();

    /**
     * when auto tests duration exceeded timeout pipeline will be failed
     * @return timeout duration
     */
    Duration timeout();

    /**
     * duration to wait before start auto tests
     * the reason could be to wait service availability in orchestration tool or registration into service discovery
     * @return duration to wait
     */
    Duration initialDelay();

    String jobName();

    String command();

    // TODO validate
    boolean staticCodeAnalysisEnabled();

}