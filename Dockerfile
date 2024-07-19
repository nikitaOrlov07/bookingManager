# based image for our new docker image is jdk 17
FROM openjdk:17-jdk
#we set containers working directory , where CMD will execute  (without this command when we run container -> will be error)
WORKDIR /app
# /app - is a folder inside the container
COPY target/BookingProject-0.0.1-SNAPSHOT.jar /app/demo2-0.0.1-SNAPSHOT.jar
#only for information which port on the container side
EXPOSE 8080
# will execute jar file , when container starts(execute containers jar file , not host(from which we copied)),must be broken down into parts
CMD ["sh", "-c", "sleep 1 && java -jar demo2-0.0.1-SNAPSHOT.jar"]
