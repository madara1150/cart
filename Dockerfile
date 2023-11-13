FROM --platform=linux/amd64 openjdk:17
EXPOSE 8080
ADD target/CartService.jar CartService.jar
ENTRYPOINT ["sh","-c","java -jar /CartService.jar"]