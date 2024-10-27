FROM openjdk:17-slim
VOLUME /tmp
ADD ./build/libs/*.jar group-service.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/parandb
ENV SPRING_DATASOURCE_USERNAME=[USERNAME]
ENV SPRING_DATASOURCE_PASSWORD=[PASSWORD]

ENTRYPOINT ["java","-jar","/group-service.jar"]