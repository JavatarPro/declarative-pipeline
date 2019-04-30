package pro.javatar.pipeline.service.cache

import pro.javatar.pipeline.util.Logger

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class CacheService {

    void setup(String service) {
        Logger.debug("CacheService: setup service: ${service} started")
        CacheRequestHolder.getCaches(service).each {String folder -> createRepoFolderWorkspaceSymbolicLink(folder)}
        Logger.debug("CacheService: setup service: ${service} finished")
    }

    void createRepoFolderWorkspaceSymbolicLink(String folderToBeCached) {
        createRepoFolderWorkspaceSymbolicLink(folderToBeCached, folderToBeCached)
    }

    void createRepoFolderWorkspaceSymbolicLink(String folderToBeCached, String alias) {
        Logger.info("CacheService: createFolderWorkspaceSymbolicLink with folderToBeCached: ${folderToBeCached} " +
                "& alias ${alias} started")
        String buildFolder = dsl.env.WORKSPACE
        String cacheFolder = "${buildFolder}/cache/${alias}"
        String repoFolder = "${buildFolder}/repo/${folderToBeCached}"
        createFolderSymbolicLink(cacheFolder, repoFolder)
        Logger.info("CacheService: createFolderWorkspaceSymbolicLink finished")
    }

    void createFolderSymbolicLink(String cacheFolder, String targetFolder) {
        Logger.info("CacheService: createFolderSymbolicLink with cacheFolder: ${cacheFolder} " +
                "& targetFolder ${targetFolder} started")
        if (! dsl.fileExists(cacheFolder)) {
            dsl.sh "mkdir -p ${cacheFolder}"
        }
        Logger.trace("ln -s ${cacheFolder} ${targetFolder}")
        dsl.sh "ln -s ${cacheFolder} ${targetFolder}"
        Logger.info("CacheService: createFolderSymbolicLink finished")
    }

}
