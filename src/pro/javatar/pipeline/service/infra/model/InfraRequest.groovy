package pro.javatar.pipeline.service.infra.model

import com.cloudbees.groovy.cps.NonCPS

class InfraRequest {

    List<Infra> serviceInfraDependencies = new ArrayList<>()

    List<Infra> commonInfraDependencies = new ArrayList<>()

    List<Infra> getServiceInfraDependencies() {
        return serviceInfraDependencies
    }

    void setServiceInfraDependencies(List<Infra> serviceInfraDependencies) {
        this.serviceInfraDependencies = serviceInfraDependencies
    }

    InfraRequest withServiceInfraDependencies(List<Infra> serviceInfraDependencies) {
        this.serviceInfraDependencies = serviceInfraDependencies
        return this
    }

    List<Infra> getCommonInfraDependencies() {
        return commonInfraDependencies
    }

    void setCommonInfraDependencies(List<Infra> commonInfraDependencies) {
        this.commonInfraDependencies = commonInfraDependencies
    }

    InfraRequest withCommonInfraDependencies(List<Infra> commonInfraDependencies) {
        this.commonInfraDependencies = commonInfraDependencies
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "InfraRequest{" +
                "serviceInfraDependencies=" + serviceInfraDependencies +
                ", commonInfraDependencies=" + commonInfraDependencies +
                '}';
    }
}
