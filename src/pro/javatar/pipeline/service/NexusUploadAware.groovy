/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service

import pro.javatar.pipeline.model.ReleaseInfo

/**
 * @author Borys Zora
 * @version 2019-11-17
 */
interface NexusUploadAware {

    void uploadMaven2Artifacts(ReleaseInfo releaseInfo);

}
