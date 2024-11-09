# 베이스 이미지로 OpenJDK 17 사용
FROM openjdk:17-jdk-slim

# 빌드 파일 이름을 ARG로 정의
ARG BUILD_FILE_NAME=respect_zone-0.0.1-SNAPSHOT

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사 
COPY build/libs/${BUILD_FILE_NAME}.jar app.jar

# 포트 노출
EXPOSE 8080

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
 