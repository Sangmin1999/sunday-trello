FROM openjdk:17-jdk-slim

# ARG는 빌드 시점에 도커 명령어를 통해 전달받을 수 있는 인자를 의미
ARG JAR_FILE=build/libs/*.jar

# 애플리케이션 JAR 파일 복사
COPY ${JAR_FILE} app.jar

# application.yml 파일 복사 (yml 파일이 있는 경로를 확인 후 수정)
COPY src/main/resources/application.yml /app/config/application.yml

# ENTRYPOINT 설정 (spring.config.location에서 application.yml 파일 경로를 지정)
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=classpath:/application.yml,/app/config/application.yml"]