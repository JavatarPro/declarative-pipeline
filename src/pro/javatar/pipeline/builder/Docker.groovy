package pro.javatar.pipeline.builder

class Docker {

    String credentialsId

    String registry

    List<String> env = new ArrayList<>()

    String orchestrationService

    String getCredentialsId() {
        return credentialsId
    }

    void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId
    }

    String getRegistry() {
        return registry
    }

    void setRegistry(String registry) {
        this.registry = registry
    }

    String getEnv() {
        return env
    }

    void setEnv(String env) {
        this.env = env
    }

    String getOrchestrationService() {
        return orchestrationService
    }

    void setOrchestrationService(String orchestrationService) {
        this.orchestrationService = orchestrationService
    }
}
