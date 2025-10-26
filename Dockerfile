# --- Giai đoạn 1: Build bằng Maven ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy toàn bộ code vào container
COPY . .

# Build mà không chạy test
RUN mvn clean package -DskipTests

# --- Giai đoạn 2: Chạy ứng dụng ---
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy file jar từ giai đoạn build
COPY --from=build /app/target/*.jar app.jar

# Mở port 8080
EXPOSE 8080

# Lệnh chạy Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
