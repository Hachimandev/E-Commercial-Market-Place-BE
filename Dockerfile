# Dùng base image của OpenJDK
FROM openjdk:21-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Copy file jar vào container
COPY target/*.jar app.jar

# Cổng mà Spring Boot chạy
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
