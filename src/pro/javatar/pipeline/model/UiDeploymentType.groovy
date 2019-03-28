package pro.javatar.pipeline.model

import pro.javatar.pipeline.exception.UnrecognizedUiDeploymentTypeException
import pro.javatar.pipeline.util.StringUtils

/**
 * Author : Borys Zora
 * Date Created: 4/13/18 12:27
 */
enum UiDeploymentType {

    CDN_FOLDER,
    AWS_S3,
    NONE

    static UiDeploymentType fromString(String type) {
        if (StringUtils.isBlank(type)) {
            return NONE
        }
        if("cdn-folder".equalsIgnoreCase(type) || "folder".equalsIgnoreCase(type)) {
            return CDN_FOLDER
        }
        if ("s3".equalsIgnoreCase(type) || "aws_s3".equalsIgnoreCase(type) || "aws".equalsIgnoreCase(type)
                || "aws-s3".equalsIgnoreCase(type)) {
            return AWS_S3
        }
        if ("none".equalsIgnoreCase(type) || "null".equalsIgnoreCase(type) || "no".equalsIgnoreCase(type)) {
            return NONE
        }
        throw new UnrecognizedUiDeploymentTypeException("type ${type} is not recognized")
    }

}
