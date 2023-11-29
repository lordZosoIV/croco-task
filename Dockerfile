FROM gradle:7.5.1-jdk17 as build
COPY . .
RUN gradle clean build

FROM openjdk:17
RUN groupadd oracle && useradd oracle -g oracle
USER oracle
COPY --from=build /home/gradle/build/libs/crocobet-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar","/app.jar"]