package pro.javatar.pipeline.builder.model

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.util.Logger

@Deprecated
class Python implements Serializable {
    String versionFile = "_version.py"
    String versionParameter = "__version__"
    String projectDirectory = "."

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
        if (versionFile != null && !versionFile.trim().isEmpty()) {
            this.versionFile = versionFile
        }
        return this
    }

    Python withVersionParameter(String versionParameter) {
        if (versionParameter != null && !versionParameter.trim().isEmpty()) {
            this.versionParameter = versionParameter
        }
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
