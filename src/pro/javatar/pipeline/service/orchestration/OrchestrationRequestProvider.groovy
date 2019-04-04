/*
 * Copyright (c) 2019 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.service.orchestration

import pro.javatar.pipeline.service.orchestration.model.OrchestrationRequest

/**
 * @author Borys Zora
 * @version 2019-04-04
 */
interface OrchestrationRequestProvider extends Serializable {

    String createRequest(OrchestrationRequest request)

}