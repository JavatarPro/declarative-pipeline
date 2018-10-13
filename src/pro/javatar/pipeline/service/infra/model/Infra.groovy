package pro.javatar.pipeline.service.infra.model

abstract class Infra {

    static final String DEFAULT_BELONGS_TO =  "common-for-all-env"

    protected String belongsToService = DEFAULT_BELONGS_TO

    protected InfraTypes infraType

    protected String version

    protected String image

    protected String customImage

    protected String cpu

    protected String memory

    protected int instances

    protected String user

    protected String password

    protected int hostPort

    protected String hostPath

    String getBelongsToService() {
        return belongsToService
    }

    void setBelongsToService(String belongsToService) {
        this.belongsToService = belongsToService
    }

    Infra withBelongsToService(String belongsToService) {
        this.belongsToService = belongsToService
        return this
    }

    InfraTypes getInfraType() {
        return infraType
    }

    void setInfraType(InfraTypes infraType) {
        this.infraType = infraType
    }

    Infra withInfraType(InfraTypes infraType) {
        this.infraType = infraType
        return this
    }

    String getVersion() {
        return version
    }

    void setVersion(String version) {
        this.version = version
    }

    Infra withVersion(String version) {
        this.version = version
        return this
    }

    String getImage() {
        return image
    }

    void setImage(String image) {
        this.image = image
    }

    String getCustomImage() {
        return customImage
    }

    void setCustomImage(String customImage) {
        this.customImage = customImage
    }

    String getCpu() {
        return cpu
    }

    void setCpu(String cpu) {
        this.cpu = cpu
    }

    String getMemory() {
        return memory
    }

    void setMemory(String memory) {
        this.memory = memory
    }

    int getInstances() {
        return instances
    }

    void setInstances(int instances) {
        this.instances = instances
    }

    String getUser() {
        return user
    }

    void setUser(String user) {
        this.user = user
    }

    String getPassword() {
        return password
    }

    void setPassword(String password) {
        this.password = password
    }

    int getHostPort() {
        return hostPort
    }

    void setHostPort(int hostPort) {
        this.hostPort = hostPort
    }

    String getHostPath() {
        return hostPath
    }

    void setHostPath(String hostPath) {
        this.hostPath = hostPath
    }

    @Override
    public String toString() {
        return "Infra{" +
                "belongsToService='" + belongsToService + '\'' +
                ", infraType=" + infraType +
                ", version='" + version + '\'' +
                ", image='" + image + '\'' +
                ", customImage='" + customImage + '\'' +
                ", cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", instances=" + instances +
                ", user='" + user + '\'' +
                ", password='" + "******" + '\'' +
                ", hostPort=" + hostPort +
                ", hostPath='" + hostPath + '\'' +
                '}';
    }
}
