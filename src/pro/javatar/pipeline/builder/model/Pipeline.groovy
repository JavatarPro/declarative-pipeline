package pro.javatar.pipeline.builder.model

class Pipeline {

    // TODO validate enum PipelineStagesSuit
    String pipelineSuit = "service"

    // TODO validate all String exists in enum if suit custom
    List<String> stages = new ArrayList<>()

    String getPipelineSuit() {
        return pipelineSuit
    }

    Pipeline withPipelineSuit(String pipelineSuit) {
        this.pipelineSuit = pipelineSuit
        return this
    }

    void setPipelineSuit(String pipelineSuit) {
        this.pipelineSuit = pipelineSuit
    }

    List<String> getStages() {
        return stages
    }

    void setStages(List<String> stages) {
        this.stages = stages
    }

    Pipeline withStages(List<String> stages) {
        this.stages = stages
        return this
    }

    Pipeline addStage(String stage) {
        this.stages.add(stage)
        return this
    }

    @Override
    public String toString() {
        return "Pipeline{" +
                "pipelineSuit='" + pipelineSuit + '\'' +
                ", stages=" + stages +
                '}';
    }
}
