FROM ghcr.io/softinstigate/graalvm-maven-docker as build

WORKDIR /app/java

ADD pom.xml .
RUN mvn verify --fail-never

COPY . .

RUN mvn -Pnative -DskipTests package

FROM bitnami/git:latest

WORKDIR /

COPY --from=build /app/java/target/gitAutoMerge /bin
