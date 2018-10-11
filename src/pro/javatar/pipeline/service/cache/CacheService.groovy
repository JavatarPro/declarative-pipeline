package pro.javatar.pipeline.service.cache

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class CacheService {

    void setup(String service) {
        dsl.echo "CacheService: setup service: ${service} started"
        CacheRequestHolder.getCaches(service).each {String folder -> createRepoFolderWorkspaceSymbolicLink(folder)}
        dsl.echo "CacheService: setup service: ${service} finished"
    }

    void createRepoFolderWorkspaceSymbolicLink(String folderToBeCached) {
        createRepoFolderWorkspaceSymbolicLink(folderToBeCached, folderToBeCached)
    }

    void createRepoFolderWorkspaceSymbolicLink(String folderToBeCached, String alias) {
        dsl.echo "CacheService: createFolderWorkspaceSymbolicLink with folderToBeCached: ${folderToBeCached} " +
                "& alias ${alias} started"
        String buildFolder = dsl.env.WORKSPACE
        String cacheFolder = "${buildFolder}/cache/${alias}"
        String repoFolder = "${buildFolder}/repo/${folderToBeCached}"
        createFolderSymbolicLink(cacheFolder, repoFolder)
        dsl.echo "CacheService: createFolderWorkspaceSymbolicLink finished"
    }

    void createFolderSymbolicLink(String cacheFolder, String targetFolder) {
        dsl.echo "CacheService: createFolderSymbolicLink with cacheFolder: ${targetFolder} " +
                "& targetFolder ${targetFolder} started"
        if (! dsl.fileExists(cacheFolder)) {
            dsl.sh "mkdir -p ${cacheFolder}"
        }
        dsl.echo "ln -s ${cacheFolder} ${targetFolder}"
        dsl.sh "ln -s ${cacheFolder} ${targetFolder}"
        dsl.echo "CacheService: createFolderSymbolicLink finished"
    }

}
