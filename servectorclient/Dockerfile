FROM azul/zulu-openjdk:18

ADD target/servectorclient-jar-with-dependencies.jar servectorclient.jar
ENTRYPOINT ["java", "-jar","servectorclient.jar"]
EXPOSE 8080