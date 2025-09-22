# ===== Build =====
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /repo
# copy toàn repo để Maven resolve multi-module
COPY . .
# build module anm-auth + các module phụ thuộc (-am)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -DskipTests -pl ms/anm-req-consumer -am package

# ===== Run =====
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /repo/ms/anm-req-consumer/target/*.jar app.jar
EXPOSE 9094
ENTRYPOINT ["java","-jar","/app/app.jar"]