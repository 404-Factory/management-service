# docker build -t management-service:latest .
# docker run -d --name management-service -p 8086:8080 management-service:latest

# Grafana
# 그냥 돌릴 때
# docker run -d -p 3000:3000 --name=grafana   -e "GF_SECURITY_ALLOW_EMBEDDING=true"   -e "GF_AUTH_ANONYMOUS_ENABLED=true"   -e "GF_AUTH_ANONYMOUS_ORG_ROLE=Viewer"   grafana/grafana
# 만든 dashboard 넣어서 돌릴 때
# docker run -d -p 3000:3000   -v "/${PWD}/grafana-backup:/var/lib/grafana"   --name grafana   -e "GF_SECURITY_ALLOW_EMBEDDING=true"   -e "GF_AUTH_ANONYMOUS_ENABLED=true"   -e "GF_AUTH_ANONYMOUS_ORG_ROLE=Viewer"   grafana/grafana

# docker build --build-arg JAR_FILE=path/myapp.jar if you need to specify the path

FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "application.jar"]