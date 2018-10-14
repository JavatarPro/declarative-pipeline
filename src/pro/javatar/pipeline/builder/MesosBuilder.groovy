package pro.javatar.pipeline.builder

import pro.javatar.pipeline.service.vcs.model.VcsRepo

class MesosBuilder {

    VcsRepo dev

    VcsRepo prod

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

    @Override
    public String toString() {
        return "MesosBuilder{" +
                "dev=" + dev +
                ", prod=" + prod +
                '}';
    }
}
