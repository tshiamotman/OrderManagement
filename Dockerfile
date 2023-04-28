FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

COPY target/OrderManagement-0.0.1-SNAPSHOT.jar /app

CMD ["java", "-jar", "OrderManagement-0.0.1-SNAPSHOT.jar.jar"]
