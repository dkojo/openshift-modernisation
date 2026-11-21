# Reusable Dockerfile for any legacy .war: pass the file with --build-arg WAR_FILE=path/to/app.war
#   docker build -f docker/tomcat-war.Dockerfile --build-arg WAR_FILE=app/target/inventory.war -t inventory .
#
# Built for OpenShift's rules, so the same image runs there and on plain Kubernetes:
#   - runs as a non-root user, and also works under any random UID OpenShift assigns (group 0 owns the writable dirs)
#   - no default Tomcat apps (manager, examples), which are a common security finding
#   - the .war is unpacked at build time, so the root filesystem can be read-only at runtime

# Stage 1: unpack the .war (busybox has unzip; the Tomcat image doesn't)
FROM busybox:1.37 AS unpack
ARG WAR_FILE
COPY ${WAR_FILE} /app.war
RUN mkdir /app && unzip -q /app.war -d /app

# Stage 2: the runtime image
FROM tomcat:10.1.62-jre21-temurin-noble
ARG APP_NAME=inventory

RUN apt-get update && apt-get upgrade -y && rm -rf /var/lib/apt/lists/* \
 && rm -rf /usr/local/tomcat/webapps/* /usr/local/tomcat/webapps.dist \
 && mkdir -p /usr/local/tomcat/conf/Catalina/localhost \
 && chgrp -R 0 /usr/local/tomcat/logs /usr/local/tomcat/temp /usr/local/tomcat/work /usr/local/tomcat/conf/Catalina \
 && chmod -R g=u /usr/local/tomcat/logs /usr/local/tomcat/temp /usr/local/tomcat/work /usr/local/tomcat/conf/Catalina

COPY --from=unpack --chown=1001:0 /app /usr/local/tomcat/webapps/${APP_NAME}

USER 1001
EXPOSE 8080
