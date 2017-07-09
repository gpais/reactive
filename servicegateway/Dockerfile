FROM java:8-jre
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/servicegateway.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/servicegateway.jar"]

EXPOSE 8080