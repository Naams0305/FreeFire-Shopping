# Giai đoạn 1: Build project bằng Maven
# Sử dụng phiên bản Java 17 để build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy file .war đã build bằng Tomcat
# Sử dụng Tomcat 10.1 hỗ trợ Jakarta EE 10 và Java 17
FROM tomcat:10.1-jdk17-temurin
# Xóa các ứng dụng mặc định của Tomcat để tiết kiệm dung lượng
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy file .war từ giai đoạn build vào thư mục webapps của Tomcat
# Đổi tên thành ROOT.war để ứng dụng chạy ở đường dẫn gốc (ví dụ: my-app.onrender.com/ thay vì my-app.onrender.com/mProject-1.0-SNAPSHOT/)
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Mở cổng 8080 để bên ngoài có thể truy cập
EXPOSE 8080
# Lệnh để khởi động Tomcat
CMD ["catalina.sh", "run"]