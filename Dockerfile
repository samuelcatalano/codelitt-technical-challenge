FROM openjdk:17-jdk-slim

MAINTAINER Samuel Catalano <samuel.catalano@gmail.com>

RUN mkdir -p /usr/share/codelitt && \
mkdir /var/run/codelitt && \
mkdir /var/log/codelitt

COPY /target/technical.exercise-0.0.1-SNAPSHOT.jar /usr/share/codelitt/technical.exercise-0.0.1-SNAPSHOT.jar

WORKDIR /usr/share/codelitt/
EXPOSE 8080 8787 5432

ENV TZ=America/New_York
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

CMD ["java","-Djava.security.egd=file:/dev/./urandom", "-Dfile.encoding=UTF-8", "-jar","technical.exercise-0.0.1-SNAPSHOT.jar"]