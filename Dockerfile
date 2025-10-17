#Dockerfile
# --- 1. 빌드 단계 ---
# Java 17 JDK 이미지를 'builder' 라는 이름의 빌드 환경으로 사용합니다.
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app

# 빌드에 필요한 파일들만 먼저 복사하여 Docker의 레이어 캐시를 활용합니다.
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY src src

# gradlew 실행 권한을 부여합니다.
RUN chmod +x ./gradlew
# 테스트를 제외하고 애플리케이션을 빌드하여 JAR 파일을 생성합니다.
RUN ./gradlew bootJar -x test

# --- 2. 실행 단계 ---
# 훨씬 가벼운 JRE 이미지를 최종 실행 환경으로 사용합니다.
FROM openjdk:17-jre-slim
WORKDIR /app

# 빌드 단계('builder')에서 생성된 JAR 파일을 최종 이미지로 복사합니다.
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너의 8080 포트를 외부에 노출시킵니다.
EXPOSE 8080
# 컨테이너가 시작될 때 실행할 명령어를 정의합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]