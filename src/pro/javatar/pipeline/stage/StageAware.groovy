/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.stage

import pro.javatar.pipeline.exception.PipelineException;
import pro.javatar.pipeline.model.ReleaseInfo;

/**
 * @author Borys Zora
 * @version 2019-11-03
 */
interface StageAware {

    ReleaseInfo releaseInfo();

    void propagateReleaseInfo(ReleaseInfo releaseInfo);

    void execute() throws PipelineException;

    String getName();

    boolean shouldSkip();

    boolean isFiredExitFromPipeline();

}
