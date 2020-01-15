# declarative-pipeline #

This is shared library for jenkins CD declarative pipeline created by [Javatar LLC](https://javatar.pro/)

### Documentation

Out documentation is in confluence follow this [link](https://javatar.atlassian.net/wiki/spaces/JDP/overview)
    
### What is this repository for? ###

* This is shared library for jenkins CD pipeline
* Jenkinsfile example [continuous-delivery repo](https://github.com/JavatarPro/declarative-pipeline)

### How do I get set up? ###

* You can use import @Library('declarative-pipeline') into Jenkinsfile
* All configuration should be in Jenkinsfile
* All code files must be groovy, Jenkinsfile's restriction
* All jobs (except development/experimental) should use tag version

### Contribution guidelines ###

* All experiments you can do in feature branch, import @Library('declarative-pipeline@branchName')
* After library works property merge into master and create tag version for it

### Who do I talk to? ###

* [borys-zora](mailto:borys.zora@javatar.pro), [spetrychenko](mailto:serhii.petrychenko@javatar.pro)

### Useful links ###

* [pipeline shared library](https://jenkins.io/doc/book/pipeline/shared-libraries/)

### Job examples ###


### RELEASE NOTES ###

* 1.2
    - added yml configuration support
    - ui deployment to s3
    - sonar integration
    - marathon/mesos deployment on dev env with docker images included build number
    - multi docker project (multiple Dockerfiles in sub-folders)
* 1.1
    - added python support
    - added github support
* 1.0
    - tools supported: gitlab, docker, mesos
    - build tools: maven, npm
    - version control types: git, mercurial
    - version control providers: gitlab, bitbucket
    - sonar quality check
