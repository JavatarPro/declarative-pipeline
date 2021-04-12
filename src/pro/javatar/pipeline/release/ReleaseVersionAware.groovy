/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.release

import pro.javatar.pipeline.model.ReleaseInfo

/**
 * @author Borys Zora
 * @version 2021-01-17
 */
interface ReleaseVersionAware {

    String getReleaseVersion(ReleaseInfo releaseInfo);

}