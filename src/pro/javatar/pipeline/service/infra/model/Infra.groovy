package pro.javatar.pipeline.service.infra.model

abstract class Infra {

    static final String DEFAULT_BELONGS_TO =  "common-for-all-env"

    protected String belongsToService = DEFAULT_BELONGS_TO

    protected String infraType

    protected String version

}
