FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

COPY target/OrderManagement-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
