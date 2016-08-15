FROM java:8

MAINTAINER Yukio, y2.k2mt@gmail.com

WORKDIR /

USER daemon

ADD target/scala-2.11/analogweb-hello-scala-1.jar /app/server.jar

ENTRYPOINT [ "java", "-jar", "/app/server.jar" ]

EXPOSE 8000
