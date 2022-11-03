FROM openjdk:11-jdk
MAINTAINER dimonium_239.github.com
COPY target/WorkUaAPI-0.0.1-SNAPSHOT.jar WorkUaAPI-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/WorkUaAPI-0.0.1-SNAPSHOT.jar"]