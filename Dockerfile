FROM openjdk:17-jdk-slim

# ARG는 빌드 시점에 도커 명령어를 통해 전달받을 수 있는 인자를 의미
ARG JAR_FILE=build/libs/*.jar

# 애플리케이션 JAR 파일 복사
COPY ${JAR_FILE} app.jar

# ENTRYPOINT 설정 (외부 yaml 설정 파일을 지정)
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=classpath:/application-docker.yml,/app/config/application-docker.yml"]
