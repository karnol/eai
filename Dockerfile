FROM openjdk:11
WORKDIR /app
ADD ./build/libs/easyimport-backend.war easyimport-backend.war
ENTRYPOINT ["java", "-jar", "-Dserver.port=3333", "/app/easyimport-backend.war"]

