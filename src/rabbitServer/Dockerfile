FROM openjdk:8
COPY build/libs/ /usr/rabbitserver/
WORKDIR /usr/rabbitserver/
CMD ["java", "-jar", "RabbitRpcServer-0.1.0.jar"]