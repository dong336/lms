# Build stage
FROM maven:amazoncorretto AS build
WORKDIR /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package

# Run stage
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 5000
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]