FROM java:8-jre
MAINTAINER Gabriel Pais <sqshq@sqshq.com>

ADD ./target/eureka.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/eureka.jar"]

EXPOSE 8761