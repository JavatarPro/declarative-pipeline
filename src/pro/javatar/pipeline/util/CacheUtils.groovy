package pro.javatar.pipeline.util

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class CacheUtils {

    static def cacheLibraryFolder(String folderToBeCached, String alias) {
        dsl.echo "CacheUtils: cacheLibraryFolder with folderToBeCached: ${folderToBeCached} & alias ${alias}"
        String buildFolder = dsl.env.WORKSPACE
        String libraryCacheFolder = "${buildFolder}/${alias}"
        if (! dsl.fileExists(libraryCacheFolder)) {
            dsl.sh "mkdir ${libraryCacheFolder}"
        }
        dsl.echo "ln -s ${libraryCacheFolder} ${folderToBeCached}"
        dsl.sh "ln -s ${libraryCacheFolder} ${folderToBeCached}"
    }

}
