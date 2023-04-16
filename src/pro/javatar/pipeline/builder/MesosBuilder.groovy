package pro.javatar.pipeline.builder

import com.cloudbees.groovy.cps.NonCPS
import pro.javatar.pipeline.service.vcs.model.VcsRepo
import pro.javatar.pipeline.util.Logger

@Deprecated
class MesosBuilder implements Serializable {

    VcsRepo dev

    VcsRepo prod

    MesosBuilder() {
        Logger.debug("MesosBuilder:default constructor")
    }

    VcsRepo getDev() {
        return dev
    }

    void setDev(VcsRepo dev) {
        this.dev = dev
    }

    VcsRepo getProd() {
        return prod
    }

    void setProd(VcsRepo prod) {
        this.prod = prod
    }

    @NonCPS
    @Override
    public String toString() {
        return "MesosBuilder{" +
                "dev=" + dev +
                ", prod=" + prod +
                '}';
    }
}
