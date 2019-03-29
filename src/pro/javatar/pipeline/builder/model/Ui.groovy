package pro.javatar.pipeline.builder.model

import pro.javatar.pipeline.util.Logger

class Ui implements Serializable {

    String deploymentType

    Ui() {
        Logger.debug("Ui:default constructor")
    }

    Ui(String deploymentType) {
        Logger.info("Ui constructor with deploymentType: ${deploymentType}")
        this.deploymentType = deploymentType
    }

    String getDeploymentType() {
        return deploymentType
    }

    void setDeploymentType(String deploymentType) {
        this.deploymentType = deploymentType
    }

    Ui withDeploymentType(String deploymentType) {
        setDeploymentType(deploymentType)
        return this
    }

    @Override
    public String toString() {
        return "Ui{" +
                "deploymentType='" + deploymentType + '\'' +
                '}';
    }

}
