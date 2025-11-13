FROM gtf-system-registry.ap-southeast-5.cr.aliyuncs.com/gopay-systems/cx-java:jdk-21 AS builder
ARG GOPAY_ARTIFACT_REGISTRY_MAVEN_SECRET
WORKDIR /app
COPY . .
RUN make build.binaries
RUN mkdir -p pkg/
RUN cp -ar ./build/libs/* pkg/
RUN mv pkg/pacman-bot*.jar ./pkg/pacman-bot.jar

FROM gtf-system-registry.ap-southeast-5.cr.aliyuncs.com/gopay-systems/app-java:jdk-21
WORKDIR /
COPY --from=builder /app/pkg/ .
