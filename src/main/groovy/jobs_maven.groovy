import javaposse.jobdsl.dsl.*

class MavenJobBuilder {
    String jobName
    String description
    String gitUrl
    String branchName
    String credentialsId
    Integer numToKeep
    Integer daysToKeep

    Job build(DslFactory dslFactory){
        dslFactory.mavenJob(jobName){
            description(this.description)
            logRotator{
                numToKeep = this.numToKeep
                daysToKeep = this.daysToKeep
            }
            scm {
                git {
                    remote {
                        name('origin')
                        url(this.gitUrl)
                        credentials(this.credentialsId)
                    }
                    branch(this.branchName)
                }
            }
            goals('package')

            publishers {
                deployPublisher{
                    adapters {
                        tomcat8xAdapter {
                            credentialsId(this.credentialsId)
                            url("http://localhost:8082")
                        }
                        contextPath("target/")
                        onFailure(false)
                        war("mvnsample-1.0-SNAPSHOT.war")
                    }
                }
            }
        }
    }
}

folder("git-maven-folder"){
    description 'This is folder for maven build'
}

def builder = new MavenJobBuilder(  jobName:"Maven Job", description:"This is my first maven job",
                                    gitUrl:"https://github.com/ArtemPervushow/groovy-jenkins-dsl-maven.git",
                                    branchName:"master",
                                    credentialsId:"cacc3e70-7103-4613-b74b-eaa04c825483",
                                    numToKeep:10,
                                    daysToKeep:20)

builder.build(this)
