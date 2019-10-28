package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger
import pro.javatar.pipeline.util.StringUtils

class Pipeline implements Serializable {

    // TODO validate enum PipelineStagesSuit
    String pipelineSuit = "service"

    // TODO validate all String exists in enum if suit custom
    List<String> stages = new ArrayList<>()

    Pipeline() {
        Logger.debug("Pipeline:default constructor")
    }

    String getPipelineSuit() {
        return pipelineSuit
    }

    Pipeline withPipelineSuit(String pipelineSuit) {
        Logger.info("Pipeline:withPipelineSuit: ${pipelineSuit}")
        setPipelineSuit(pipelineSuit)
        return this
    }

    void setPipelineSuit(String pipelineSuit) {
        Logger.info("Pipeline:setPipelineSuit: ${pipelineSuit}")
        if (StringUtils.isBlank(pipelineSuit)) {
            this.pipelineSuit = pipelineSuit
        }
    }

    List<String> getStages() {
        if ("custom".equalsIgnoreCase(pipelineSuit)) {
            return stages
        }
        return new ArrayList<>()
    }

    void setStages(List<String> stages) {
        Logger.info("Pipeline:setStages: ${stages}")
        this.stages = stages
    }

    Pipeline withStages(List<String> stages) {
        Logger.info("Pipeline:withStages: ${stages}")
        setStages(stages)
        return this
    }

    Pipeline addStage(String stage) {
        this.stages.add(stage)
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "Pipeline{" +
                "pipelineSuit='" + pipelineSuit + '\'' +
                ", stages=" + stages +
                '}';
    }
}
