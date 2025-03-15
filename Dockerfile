# 使用官方 OpenJDK 运行时作为基础镜像
FROM maven:3.8.6-eclipse-temurin-17 AS build

# 设置工作目录
WORKDIR /app

# 复制 Maven 配置文件（加快构建）
COPY pom.xml .

# 预下载依赖
RUN mvn dependency:go-offline

# 复制项目代码
COPY src ./src

# 编译 Java 应用
RUN mvn clean package -DskipTests

# 运行阶段，使用轻量级 JRE 镜像
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 复制编译后的 JAR 文件
COPY --from=build /app/target/*.jar app.jar

# 运行 Spring Boot 应用
CMD ["java", "-jar", "app.jar"]