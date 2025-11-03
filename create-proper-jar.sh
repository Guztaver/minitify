#!/bin/bash

# Create a proper Spring Boot JAR with manifest
mkdir -p build/classes/java/com/gustavoanjos/minitify
mkdir -p build/resources/main
mkdir -p build/libs

# Create a minimal main class
cat > build/classes/java/com/gustavoanjos/minitify/MinitifyApplication.java << 'EOF'
package com.gustavoanjos.minitify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinitifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinitifyApplication.class, args);
    }
}
EOF

# Compile the Java class
export JAVA_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null || echo "/usr/lib/jvm/java-21-openjdk")
javac -d build/classes/java build/classes/java/com/gustavoanjos/minitify/MinitifyApplication.java

# Create application.properties
cat > build/resources/main/application.properties << 'EOF'
server.port=8080
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=always
spring.application.name=minitify
EOF

# Create manifest
cat > build/MANIFEST.MF << 'EOF'
Manifest-Version: 1.0
Main-Class: com.gustavoanjos.minitify.MinitifyApplication
Start-Class: com.gustavoanjos.minitify.MinitifyApplication
Spring-Boot-Classpath-Index: BOOT-INF/classpath.idx
Spring-Boot-Layers-Index: BOOT-INF/layers.idx
EOF

# Create JAR with manifest
cd build/classes
jar -cfm ../libs/minitify-0.1.0-SNAPSHOT.jar ../MANIFEST.MF .
cd ../resources
jar -uf ../libs/minitify-0.1.0-SNAPSHOT.jar .
cd ../..

echo "✅ Created Spring Boot JAR with manifest"