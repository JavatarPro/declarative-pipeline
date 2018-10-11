package pro.javatar.pipeline.service.cache

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class CacheService {

    void setup(String service) {
        CacheRequestHolder.getCaches(service).each {String folder -> createRepoFolderWorkspaceSymbolicLink(folder)}
    }

    void createRepoFolderWorkspaceSymbolicLink(String folderToBeCached) {
        createRepoFolderWorkspaceSymbolicLink(folderToBeCached, folderToBeCached)
    }

    void createRepoFolderWorkspaceSymbolicLink(String folderToBeCached, String alias) {
        dsl.echo "CacheService: createFolderWorkspaceSymbolicLink with folderToBeCached: ${folderToBeCached} " +
                "& alias ${alias} started"
        String buildFolder = dsl.env.WORKSPACE
        String cacheFolder = "${buildFolder}/${alias}"
        String repoFolder = "${buildFolder}/repo/${folderToBeCached}"
        createFolderSymbolicLink(cacheFolder, repoFolder)
        dsl.echo "CacheService: createFolderWorkspaceSymbolicLink finished"
    }

    void createFolderSymbolicLink(String cacheFolder, String targetFolder) {
        dsl.echo "CacheService: createFolderSymbolicLink with cacheFolder: ${targetFolder} " +
                "& targetFolder ${targetFolder} started"
        if (! dsl.fileExists(cacheFolder)) {
            dsl.sh "mkdir ${cacheFolder}"
        }
        dsl.echo "ln -s ${cacheFolder} ${targetFolder}"
        dsl.sh "ln -s ${cacheFolder} ${targetFolder}"
        dsl.echo "CacheService: createFolderSymbolicLink finished"
    }

}
