FROM openjdk:18

RUN mkdir /app

COPY test/ /app

WORKDIR /app

RUN javac TestClass.java

CMD java TestClass



