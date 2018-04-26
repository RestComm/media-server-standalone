FROM openjdk:8-jre-slim

ARG VERSION

# Installs libopus on Debian
RUN apt-get update && apt-get install -y libopus0


CMD [ \
  "/usr/bin/java", \
  "-Dprogram.name=mediaserver", \
  "-Xms3400m", \
  "-Xmx3400m", \
  "-XX:+UseG1GC", \
  "-XX:ParallelGCThreads=8", \
  "-XX:ConcGCThreads=8", \
  "-XX:G1RSetUpdatingPauseTimePercent=10", \
  "-XX:+ParallelRefProcEnabled", \
  "-XX:G1HeapRegionSize=4m", \
  "-XX:G1HeapWastePercent=5", \
  "-XX:InitiatingHeapOccupancyPercent=85", \
  "-XX:+UnlockExperimentalVMOptions", \
  "-XX:G1MixedGCLiveThresholdPercent=85", \
  "-XX:+AlwaysPreTouch", \
  "-XX:+UseCompressedOops", \

# TODO: ask @hrosa -- Force IPv4 on Linux systems since IPv6 doesn't work correctly with jdk5 and lower
  "-Djava.net.preferIPv4Stack=true", \

  "-Dhttp.keepAlive=false", \
  "-Djava.library.path=/usr/lib/x86_64-linux-gnu:/opt/mediaserver/lib/native", \
  "-Drestcomm.opus.library=opus_jni_linux", \
  "-Dspring.output.ansi.enabled=DETECT", \
  "-Dspring.config.location=/opt/mediaserver/conf/", \
  "-Dspring.config.name=media-server,media-plugin-vad-noise-threshold,media-extra", \
  "-Dlogging.config=/opt/mediaserver/conf/log4j2.yml", \
  "-Djava.ext.dirs=/opt/mediaserver/lib:/docker-java-home/jre/lib/ext", \
  "-Dmbrola.base=/opt/mediaserver/mbrola", \
  "-classpath", \
  "/opt/mediaserver/bin/media-server-standalone-bootstrap.jar:/opt/mediaserver/lib/*", \
  "org.restcomm.media.server.standalone.bootstrap.spring.SpringBootstrapper" \
]


# Add Maven dependencies (not shaded into the artifact; Docker-cached)
ADD target/media-server-standalone-${VERSION}/lib           /opt/mediaserver/lib
ADD target/media-server-standalone-${VERSION}/conf           /opt/mediaserver/conf
ADD target/media-server-standalone-${VERSION}/bin           /opt/mediaserver/bin
