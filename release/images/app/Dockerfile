# 编译阶段
FROM maven:3.9.5 AS build
WORKDIR /build

COPY . .

RUN --mount=type=cache,target=/root/.m2 mvn clean package -B -C -Dmaven.test.skip=false
RUN cp start/target/*.jar app.jar && mkdir -p pkg && (cd pkg; java -Djarmode=layertools -jar ../app.jar extract)

FROM openjdk:21
WORKDIR /app
ARG BUILDER_DEPS=/build/pkg

ENV TZ=Asia/Shanghai \
    JAVA_OPTS="-server"

COPY --from=build ${BUILDER_DEPS}/dependencies/ ./classpath/
COPY --from=build ${BUILDER_DEPS}/spring-boot-loader/ ./classpath/
COPY --from=build ${BUILDER_DEPS}/snapshot-dependencies/ ./classpath/
COPY --from=build ${BUILDER_DEPS}/application/ ./classpath/

EXPOSE 8080
VOLUME [ "/app/data", "/app/logs", "/app/config" ]
RUN mkdir config data logs

ENTRYPOINT ["sh", "-c", "exec java -cp ./classpath  $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher ${0} ${@}"]
CMD [""]