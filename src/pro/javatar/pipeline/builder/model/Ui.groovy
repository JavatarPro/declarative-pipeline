package pro.javatar.pipeline.builder.model

class Ui {

    String deploymentType

    String getDeploymentType() {
        return deploymentType
    }

    void setDeploymentType(String deploymentType) {
        this.deploymentType = deploymentType
    }

    Ui withDeploymentType(String deploymentType) {
        this.deploymentType = deploymentType
        return
    }

    @Override
    public String toString() {
        return "Ui{" +
                "deploymentType='" + deploymentType + '\'' +
                '}';
    }

}
