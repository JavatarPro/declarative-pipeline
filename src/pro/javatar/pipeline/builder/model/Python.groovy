package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

class Python implements Serializable {
    private String versionFile = "_version.py"
    private String versionParameter = "__version__"
    private String projectDirectory = "."

    Python() {
        Logger.debug("Python:default constructor")
    }

    String getVersionFile() {
        return versionFile
    }

    String getVersionParameter() {
        return versionParameter
    }

    String getProjectDirectory() {
        return projectDirectory
    }

    Python withVersionFile(String versionFile) {
        this.versionFile = versionFile
        return this
    }

    Python withVersionParameter(String versionParameter) {
        this.versionParameter = versionParameter
        return this
    }

    Python withProjectDirectory(String projectDirectory) {
        this.projectDirectory = projectDirectory
        return this
    }

    @NonCPS
    @Override
    public String toString() {
        return "Python{" +
                "versionFile='" + versionFile + '\'' +
                ", versionParameter='" + versionParameter + '\'' +
                ", projectDirectory='" + projectDirectory + '\'' +
                '}';
    }
}
