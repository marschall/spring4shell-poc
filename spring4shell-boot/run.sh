#!/bin/sh
java -Xmx128m -Xss256k \
    -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager \
    -jar target/spring4shell-boot-0.1.0-SNAPSHOT.jar